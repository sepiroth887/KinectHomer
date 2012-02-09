using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.GPU;
using Emgu.CV.Structure;
using Coding4Fun.Kinect.WinForm;
using Emgu.CV.CvEnum;
using System.Drawing;
using System.Threading;
using System.ComponentModel;
using System.Collections;
using KinectCOM;
using Microsoft.Kinect;
using System.Windows.Media.Imaging;
using System.Windows.Media;
using System.IO;

namespace Kinect
{
    class FaceProcessor
    {

        private KinectData kinect; // reference to Kinect
        private bool isProcessing = false; // indicates state of this processor
        private HaarCascade haar; // haarCascade used for CPU face detection
        private GpuCascadeClassifier haarGPU; // haarCascade used for GPU face detection
        private bool hasCuda; // indicator of GPU capabilities
        private bool forceCPU = false; // forces CPU face detection
        private IKinect kinectHandler; // reference to GUI
        private List<Image<Gray, byte>> faceList = new List<Image<Gray, byte>>(); // list of faces maintained during recognition / detection
        private volatile bool isNew = false; // indicates whether a detected face is new or known
        private BackgroundWorker bw; // background worker to initialize this processor
        private BackgroundWorker recognizerInitWorker; // backgroundworker to initialize the recognizer
        private BackgroundWorker recognizerWorker; // backgroundworker to recognize a user's face
        private Boolean isBWDone = true; // indicates whether the loading 
        private FeatureProcessor featureProcessor; // reference to the FeatureProcessor
        private EigenObjectRecognizer recognizer; // recognizer used for face recognition.
        private RecognitionProcessor recProcessor; // reference to the RecognitionProcessor

        private Boolean isDatabaseLoaded = false; // indicates whether the recognizer needs to be initialized properly.

        private bool faceRecognitionActive = false; // indicates whether recognition has been requested.

        private ArrayList recFaces = null; // stores faces to recognize if the recognizer is not yet loaded.

        // indicated whether the recognizer is currently loading
        private bool isRecognizerLoading = false;
        // indicates whether the recognized is cleanly loaded.
        private bool isRecognizerLoaded = false;

        Image<Gray, byte>[] faceDatabase; // array of all images in the face recognition database
        String[] labelDatabase; // array of all names in the face recognition database, aligned with the faceDatabase array


        private long rgbFrameTime = 0; // timestamp of the last rgbFrame
        private long depthFrameTime = 0; // timestamp of the last depthFrame
        private DepthImageFrame depth2D; // array to store depth data for easier processing



        /**
         *  The faceprocessor does all required processing to extract and recognize faces
         *  from a kinect image frame
         * **/
        public FaceProcessor(KinectData kinect, FeatureProcessor featureProcessor, RecognitionProcessor recProcessor, IKinect p)
        {
            // assign all passed variables to local fields.
            this.kinect = kinect;
            this.kinectHandler = p;
            this.featureProcessor = featureProcessor;
            this.recProcessor = recProcessor;

            try
            {
                // check if the GPU can work with CUDA support
                hasCuda = GpuInvoke.HasCuda;

                // choose the appropriate haarCascade for face detection
                if (hasCuda)
                {
                    haarGPU = new GpuCascadeClassifier(FileLoader.DEFAULT_PATH+"haarcascade_frontalface_default.xml");
                }
                else
                {
                    haar = new HaarCascade(FileLoader.DEFAULT_PATH + "haarcascade_frontalface_default.xml");
                }
            }
            catch (Exception ex) {
                Console.Out.WriteLine(ex.StackTrace);
            }
        }

        // init method is called during the prototype startup.
        public void init()
        {
            try
            {
                // opend required data streams and attach event handlers
                kinect.attachRGBHandler(rgbHandler);
                kinect.attachDepthHandler(depthHandler);

                // initialize the Background workers required for face detection, face recognition, and recognition initialization.
                bw = new BackgroundWorker();
                bw.DoWork += new DoWorkEventHandler(bw_DoWork);
                bw.RunWorkerCompleted += new RunWorkerCompletedEventHandler(bw_RunWorkerCompleted);

                recognizerWorker = new BackgroundWorker();

                recognizerWorker.DoWork += new DoWorkEventHandler(recognizerWorker_DoWork);
                recognizerWorker.RunWorkerCompleted += new RunWorkerCompletedEventHandler(recognizerWorker_RunWorkerCompleted);

                recognizerInitWorker = new BackgroundWorker();

                recognizerInitWorker.DoWork += new DoWorkEventHandler(recognizerInitWorker_DoWork);
                recognizerInitWorker.RunWorkerCompleted += new RunWorkerCompletedEventHandler(recognizerInitWorker_RunWorkerCompleted);
            }
            catch (Exception ex) {
                Console.Out.WriteLine(ex.StackTrace);
            }

        }

        // set/get latest detected face.
        public Image<Gray, byte> latestFace()
        {
            if (isNew)
            {
                isNew = false;
                return faceList[0];
            }
            else
            {
                return null;
            }
        }

        // passes the image from the camera to either the detectFaceCPU or detectFaceGPU method 
        // to detect a face in it. If one is found, that face is passed to the RecognitionProcessor instance.
        private void passImage(Image<Bgr, byte> camImage, long time)
        {

            isNew = false;

            if (!forceCPU && hasCuda)
            {
                detectFaceGPU(camImage);
            }
            else
            {
                detectFaceCPU(camImage);
            }



            if (faceList != null & faceList.Count != 0 && isNew)
                recProcessor.LatestFace = faceList[0];
        }


        /// <summary>
        /// This handler is only active if the class is processing. It stores the latest depth image and 
        /// the time it was created. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void depthHandler(object sender, DepthImageFrameReadyEventArgs e)
        {
            if (!isProcessing) { return; }

            DepthImageFrame dFrame = e.OpenDepthImageFrame();
            depthFrameTime = dFrame.Timestamp;
            depth2D = dFrame;
        }

        /// <summary>
        /// This handler is only active if the class is processing. It stores the latest camera image and 
        /// the time it was created. It then retrieves the latest headlocation from the FeatureProcessor and
        /// passes all required information for face detection on to a backgroundworker that is then started 
        /// asynchronously.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void rgbHandler(object sender, ColorImageFrameReadyEventArgs e)
        {

            if (!isProcessing || !isBWDone)
            {
                return;
            }
            else
            {
                ColorImageFrame frame = e.OpenColorImageFrame();
                rgbFrameTime = frame.Timestamp;

                // check wether the latest frames are still fresh enough.
                if (Math.Abs(depthFrameTime - rgbFrameTime) < 500
                    && Math.Abs(featureProcessor.getFrameNumber() - rgbFrameTime) < 500)
                {
                    isBWDone = false;
                    Dictionary<int, object> args = new Dictionary<int, object>();

                    args.Add(0, frame);
                    args.Add(1, featureProcessor.getUserHeadPos());
                    args.Add(2, rgbFrameTime);

                    // pass all required arguments to the background worker.
                    bw.RunWorkerAsync(args);
                }

            }
        }

        /// <summary>
        /// Processes an rgb image, depth image and headLocation to retrieve a face from the rgb image.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void bw_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;

            if (isProcessing)
            {
                // retrieve all passed arguments and store them locally.
                Dictionary<int, object> args = e.Argument as Dictionary<int, object>;

                ColorImageFrame frame = args[0] as ColorImageFrame;

                SkeletonPoint activeHeadPos = (SkeletonPoint)args[1];

                // get the depth pixel location of the head.
                ColorImagePoint headDepth = kinect.getSensor().MapSkeletonPointToColor(activeHeadPos,ColorImageFormat.RgbResolution1280x960Fps12);

                byte[] pixelData = new byte[kinect.getSensor().ColorStream.FramePixelDataLength];
                frame.CopyPixelDataTo(pixelData);

                MemoryStream mystream = new MemoryStream(pixelData);
                System.Drawing.Image p = System.Drawing.Image.FromStream(mystream);
                Bitmap Img = new Bitmap(p);    

                Image<Bgr, byte> camImage = new Image<Bgr, byte>(Img);
                
                // resize the 640x480 rgb image to match the depth image size of 320x240
                Image<Bgr, byte> reImg = camImage.Resize(1280, 240, INTER.CV_INTER_CUBIC);

                // set the region of interest which currently is a 40x40 pixel region where
                // the face is supposed to be. ( being to close might raise some issues )
                int roi = 120;

                reImg.ROI = new Rectangle((headDepth.X) - (roi / 2), (headDepth.Y), roi, roi);

                // pass the image and the timestamp on to the faceDetection methods
                passImage(reImg, (long)args[3]);


            }



        }

        /// <summary>
        /// resets the isBWDone switch to indicate the next image from the camera can be processed now.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void bw_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            isBWDone = true;
        }

        /// <summary>
        /// Tries to detect a face inside an image using GPU processing. 
        /// This is achieved by converting the image to grayscale and then to 
        /// a GPUImage<>. Once a face is detected it is passed to the faceList.
        /// And marked as new.
        /// </summary>
        /// <param name="image"></param>
        private void detectFaceGPU(Image<Bgr, byte> image)
        {
            Image<Gray, byte> grayImage = image.Convert<Gray, byte>();

            GpuImage<Gray, byte> grayGPUImage = new GpuImage<Gray, byte>(grayImage);

            // image conversion went wrong or image passed was null
            if (grayGPUImage == null) { return; }

            // do the detection and mark the startime for performance tracking.
            DateTime time = DateTime.Now;
            Rectangle[] faces = null;
            try
            {
                faces = haarGPU.DetectMultiScale<Gray>(grayGPUImage, 1.2, 4, new Size(image.Width / 12, image.Height / 12));
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine("[FaceProcessor] detectFaceGPU: " + ex.Message);
            }


            faceList.Clear();
            // if a face has been detected store each in the facelist and indicate there are new values in the list.
            if (faces != null && faces.Count() != 0)
            {
                foreach (Rectangle face in faces)
                {
                    GpuImage<Gray, byte> faceImg = grayGPUImage.GetSubRect(face);
                    faceList.Add(faceImg.ToImage());

                }
                isNew = true;
            }
            // mark the end time of the operation.
            TimeSpan endTime = DateTime.Now - time;

            //Console.WriteLine("Time to find face: " + endTime.Milliseconds + "ms");

        }

        /// <summary>
        /// Same as detectFaceGPU only with CPU processing.
        /// </summary>
        /// <param name="image"></param>
        private void detectFaceCPU(Image<Bgr, byte> image)
        {

            Image<Gray, byte> grayImage = image.Convert<Gray, byte>();

            if (grayImage == null) { return; }

            MCvAvgComp[] faces = null;

            try
            {
                faces = haar.Detect(grayImage, 1.2, 4, HAAR_DETECTION_TYPE.DO_CANNY_PRUNING, new Size(image.Width / 12, image.Height / 12));
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine("[FaceProcessor] detectFaceCPU: " + ex.Message);
            }
            faceList.Clear();

            if (faces.Count() != 0)
            {
                foreach (var face in faces)
                {
                    grayImage.ROI = face.rect;
                    faceList.Add(grayImage.Copy());
                }
            }


        }

        /// <summary>
        /// Starts face detection.
        /// </summary>
        public void doProcess()
        {
            isProcessing = true;
        }

        /// <summary>
        /// Stops face detection
        /// </summary>
        public void stopProcess()
        {
            isProcessing = false;
        }


        /// <summary>
        /// Shuffles both database arrays so that the recognizer is not trained with each user in sequence which reduces
        /// Neural network performance.
        /// </summary>
        /// <param name="images"></param>
        /// <param name="labels"></param>
        public void shuffle(Image<Gray, byte>[] images, String[] labels)
        {
            Random rand = new Random();
            int size = images.Length;

            for (int i = 0; i < size; i++)
            {
                int a = rand.Next(size);
                Image<Gray, byte> tempI = images[i];
                images[i] = images[a];
                images[a] = tempI;

                String tempS = labels[i];
                labels[i] = labels[a];
                labels[a] = tempS;

            }

        }

        /// <summary>
        /// trains the recognizer on a new face (or list of faces with the same name label)
        /// </summary>
        /// <param name="faces"></param>
        /// <param name="label"></param>
        public void learnFace(ArrayList faces, String label)
        {

            // create a new image array to hold the faces
            Image<Gray, byte>[] images = new Image<Gray, byte>[faces.Count];
            // and an array to hold the name of the users aligned to the face array.
            String[] labels = new String[faces.Count];

            // loop though the faces and convert, store and equalize the contrast of each image.
            int counter = 0;
            for (int i = 0; i < faces.Count; i++)
            {

                Image<Gray, byte> Img = faces[i] as Image<Gray, byte>;

                Image<Gray, byte> resizedImg = Img.Resize(100, 100, INTER.CV_INTER_CUBIC, true);

                resizedImg._EqualizeHist();
                images[counter] = resizedImg;
                labels[counter++] = label;

            }

            // append the faces and labels to the labeldatabase if it exists 
            if (faceDatabase != null && labelDatabase != null)
            {
                faceDatabase = faceDatabase.Concat(images).ToArray();
                labelDatabase = labelDatabase.Concat(labels).ToArray();
            }
            else
            {
                // or create them if they dont
                faceDatabase = images;
                labelDatabase = labels;
            }

            // flag the recognizer as changed
            isRecognizerLoaded = false;
            // and reload it
            initRecognizer();

            // create a dictionary to hold both arrays and pass store it on the HDD.
            Dictionary<Image<Gray, byte>[], String[]> database = new Dictionary<Image<Gray, byte>[], String[]>();

            database.Add(images, labels);

            FileLoader.saveFaceDB(database, "../../FaceDB");
        }



        private void initRecognizer()
        {
            if (!isRecognizerLoading)
            {
                // if something changed in the recognizer or its not loaded start the 
                // initialization process.
                isRecognizerLoading = true;

                recognizerInitWorker.RunWorkerAsync(isDatabaseLoaded);
            }

        }

        /// <summary>
        /// Initializes the recognizer, either with the database of images from HDD or, if
        /// those are already loaded, with the newly trained users.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void recognizerInitWorker_DoWork(object sender, DoWorkEventArgs e)
        {

            if (!isRecognizerLoaded)
            {
                // check if the db has been loaded already.
                bool dbLoaded = (bool)e.Argument;

                if (!dbLoaded)
                {
                    // db needs to be loaded so retrieve the faces and labels from HDD
                    Dictionary<Image<Gray, byte>[], String[]> database = new Dictionary<Image<Gray, byte>[], String[]>();

                    database = FileLoader.loadFaceDB("FaceDB");

                    // empty database??? cant be right... no need to load the recognizer then.
                    if (database == null)
                    {
                        Console.Out.WriteLine("Database could not be loaded");
                        return;

                    } 
                    
                    // for each entry in the database retrieve the faces and labels and append them to the local
                    // faceDatabase and labelDatabase arrays.
                    foreach (KeyValuePair<Image<Gray, byte>[], String[]> entry in database)
                    {
                        Image<Gray, byte>[] images = entry.Key;
                        String[] label = entry.Value;

                        if (faceDatabase != null && labelDatabase != null)
                        {
                            Console.Out.WriteLine(label[0]);
                            faceDatabase = faceDatabase.Concat(images).ToArray();
                            labelDatabase = labelDatabase.Concat(label).ToArray();
                        }
                        else
                        {
                            faceDatabase = images;
                            labelDatabase = label;
                        }
                    }

                    // shuffle the data so the neural network works better.
                    this.shuffle(faceDatabase, labelDatabase);
                }

                // initialize the recognizer and train it wth the database data.
                MCvTermCriteria termCrit = new MCvTermCriteria(32, 0.001);
                recognizer = new EigenObjectRecognizer(faceDatabase, labelDatabase, 1700, ref termCrit);
            }
        }

        /// <summary>
        /// Resets switches once  the recognizer is initialized. If recognition has been requested by
        /// the RecognitionProcessor this will also activate the recognizeFace method.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void recognizerInitWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            isDatabaseLoaded = true;
            isRecognizerLoading = false;
            isRecognizerLoaded = true;

            if (faceRecognitionActive)
            {
                this.recognizeFace(recFaces);
                faceRecognitionActive = false;
            }
        }

        /// <summary>
        /// Works through the images passed for recognition and finds the closest match from the 
        /// EigenObjectRecognizer. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void recognizerWorker_DoWork(object sender, DoWorkEventArgs e)
        {

            // retrieve the arraylist from the arguments
            ArrayList faces = e.Argument as ArrayList;

            // copy them into an image array
            Image<Gray, byte>[] facesCopy = new Image<Gray, byte>[faces.Count];

            faces.CopyTo(facesCopy);

            Image<Gray, byte> bestMatch = null; // stores the face that was the closest match.
            String bestLabel = ""; // and the closest matching label as well.
            float bestDistance = float.MaxValue; // keeps track of the best match distance
            float eigenDistance = 0;
            String tempLabel = "";
            Image<Gray, byte> optiImage = null;

            // loop though the faces and find the most similar object in the EigenRecognizer.
            foreach (Image<Gray, byte> face in facesCopy)
            {
                int index = 0;
                optiImage = face.Resize(100, 100, INTER.CV_INTER_CUBIC, false);
                optiImage._EqualizeHist();

                recognizer.FindMostSimilarObject(optiImage, out index, out eigenDistance, out tempLabel);

                // if the new eigendistance is better than the current best one, replace the values for 
                // best distance, best label and best image.
                if (bestDistance > eigenDistance)
                {
                    bestDistance = eigenDistance;
                    bestMatch = optiImage;
                    bestLabel = tempLabel;
                }
            }

            // recognition is done so create a new UserFeature object to store the results.
            UserFeature user = new UserFeature();

            // if a label has been returned
            if (!bestLabel.Equals(""))
            {
                // calculate the confidence as a percentage value.
                user.FaceConfidence = Math.Max(0, (1 - bestDistance / 3000));
                // store the username
                user.Name = bestLabel;
                // and the closest matching image.
                user.curImage = optiImage;

            }
            Console.Out.WriteLine("User label: " + user.Name);
            e.Result = user;
        }

        /// <summary>
        /// Invoked when recognition worker is finished. Passes the results to the recognitionProcessor to
        /// finish the recognition process.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void recognizerWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            UserFeature user = e.Result as UserFeature;
            recProcessor.userRecognized(user);
        }

        /// <summary>
        /// Invoked by the recognitionProcessor to start recognition of faces passed to the method.
        /// </summary>
        /// <param name="faces"></param>
        public void recognizeFace(ArrayList faces)
        {
            // if the recognizer needs to be loaded do so.
            if (!isRecognizerLoaded)
            {
                faceRecognitionActive = true;
                recFaces = faces;
                initRecognizer();
            }
            else
            {
                // otherwise start recognition in the background.
                recognizerWorker.RunWorkerAsync(faces);
            }


        }


        public Image<Gray, byte> detectFaceGPU(Image<Rgb, byte> image, bool p)
        {

            if (image == null)
            {
                return null;
            }
            Image<Gray, byte> grayImage = image.Convert<Gray, byte>();

            GpuImage<Gray, byte> grayGPUImage = new GpuImage<Gray, byte>(grayImage);

            // image conversion went wrong or image passed was null
            if (grayGPUImage == null) { return null; }

            // do the detection and mark the startime for performance tracking.
            DateTime time = DateTime.Now;
            Rectangle[] faces = null;
            try
            {
                faces = haarGPU.DetectMultiScale<Gray>(grayGPUImage, 1.2, 4, new Size(image.Width / 12, image.Height / 12));
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine("[FaceProcessor] detectFaceGPU: " + ex.Message);
            }

            // if a face has been detected store each in the facelist and indicate there are new values in the list.
            if (faces != null && faces.Count() != 0)
            {
                foreach (Rectangle face in faces)
                {
                    Image<Gray, byte> resizedImg = grayGPUImage.GetSubRect(face).ToImage().Resize(100, 100, INTER.CV_INTER_CUBIC, true);
                    return resizedImg;
                }
                isNew = true;
            }
            // mark the end time of the operation.
            TimeSpan endTime = DateTime.Now - time;
            return null;
            //Console.WriteLine("Time to find face: " + endTime.Milliseconds + "ms");

        }
    }
}
