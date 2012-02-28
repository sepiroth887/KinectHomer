using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;
using System.Linq;
using Emgu.CV;
using Emgu.CV.CvEnum;
using Emgu.CV.Structure;
using Microsoft.Kinect;
using log4net;

namespace KinectCOM
{
    internal class FaceProcessor
    {
        
        private readonly KinectData _kinect; // reference to Kinect

        private readonly FeatureProcessor _featureProcessor; // reference to the FeatureProcessor

        private readonly RecognitionProcessor _recProcessor; // reference to the RecognitionProcessor
        private bool _isProcessing; // indicates state of this processor
        private BackgroundWorker _bw; // background worker to initialize this processor
        private Image<Gray, byte>[] _faceDatabase; // array of all images in the face recognition database
        private bool _faceRecognitionActive; // indicates whether recognition has been requested.
        private Boolean _isBwDone = true; // indicates whether the loading 

        private Boolean _isDatabaseLoaded; // indicates whether the recognizer needs to be initialized properly.
        private volatile bool _isNew; // indicates whether a detected face is new or known

        // indicates whether the recognized is cleanly loaded.
        private bool _isRecognizerLoaded;
        private bool _isRecognizerLoading;

        private string[] _labelDatabase;
        // array of all names in the face recognition database, aligned with the faceDatabase array

        private ArrayList _recFaces; // stores faces to recognize if the recognizer is not yet loaded.
        private EigenObjectRecognizer _recognizer; // recognizer used for face recognition.
        private BackgroundWorker _recognizerInitWorker; // backgroundworker to initialize the recognizer
        private BackgroundWorker _recognizerWorker; // backgroundworker to recognize a user's face

        private long _rgbFrameTime; // timestamp of the last rgbFrame

        private ArrayList _users;

        private FaceDetector _faceDetector;

        private static readonly ILog Log = LogManager.GetLogger(typeof(FaceProcessor));

        /**
         *  The faceprocessor does all required processing to extract and recognize faces
         *  from a kinect image frame
         * **/
            
        public FaceProcessor(KinectData kinect, FeatureProcessor featureProcessor, RecognitionProcessor recProcessor)
        {
            // assign all passed variables to local fields.
            _kinect = kinect;
            _featureProcessor = featureProcessor;
            _recProcessor = recProcessor;
            _users = new ArrayList();
            _faceDetector = new FaceDetector();
            
        }

        // init method is called during the prototype startup.
        public void Init()
        {
            try
            {
                // opend required data streams and attach event handlers
                if (_kinect != null)
                {
                    _kinect.attachRGBHandler(rgbHandler);
                }

                // initialize the Background workers required for face detection, face recognition, and recognition initialization.
                _bw = new BackgroundWorker();
                _bw.DoWork += BwDoWork;
                _bw.RunWorkerCompleted += BwRunWorkerCompleted;

                _recognizerWorker = new BackgroundWorker();

                _recognizerWorker.DoWork += RecognizerWorkerDoWork;
                _recognizerWorker.RunWorkerCompleted += RecognizerWorkerRunWorkerCompleted;

                _recognizerInitWorker = new BackgroundWorker();

                _recognizerInitWorker.DoWork += RecognizerInitWorkerDoWork;
                _recognizerInitWorker.RunWorkerCompleted += RecognizerInitWorkerRunWorkerCompleted;
            }
            catch (Exception ex)
            {
                Log.Error("Couldn't initialize FaceProcessor", ex);
            }
        }

        // set/get latest detected face.

        // passes the image from the camera to either the detectFaceCPU or detectFaceGPU method 
        // to detect a face in it. If one is found, that face is passed to the RecognitionProcessor instance.
        private void PassImage(Image<Bgr, byte> camImage)
        {

        }

        /// <summary>
        /// This handler is only active if the class is processing. It stores the latest camera image and 
        /// the time it was created. It then retrieves the latest headlocation from the FeatureProcessor and
        /// passes all required information for face detection on to a backgroundworker that is then started 
        /// asynchronously.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void rgbHandler(object sender, ColorImageFrameReadyEventArgs e)
        {
            if (!_isProcessing || !_isBwDone || e == null)
            {
                return;
            }
            
            var frame = e.OpenColorImageFrame();
            if (frame == null) return;
            _rgbFrameTime = frame.Timestamp;

            // check wether the latest frames are still fresh enough.
            if (_featureProcessor == null ||
                 Math.Abs(_featureProcessor.GetFrameNumber() - _rgbFrameTime) >= 500) return;

            _isBwDone = false;
            var args = new Dictionary<int, object>
                           {{0, frame}, {1, _featureProcessor.GetUserHeadPos()}, {2, _rgbFrameTime}};

            // pass all required arguments to the background worker.
            if (_bw != null) _bw.RunWorkerAsync(args);
        }

        /// <summary>
        /// Processes an rgb image, depth image and headLocation to retrieve a face from the rgb image.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void BwDoWork(object sender, DoWorkEventArgs e)
        {
            if (!_isProcessing || e == null) return;
            // retrieve all passed arguments and store them locally.
            var args = e.Argument as Dictionary<int, object>;

            if (args == null || args.Count < 3) return;
                
            var frame = args[0] as ColorImageFrame;

            var activeHeadPos = (SkeletonPoint)args[1];

            // get the depth pixel location of the head.

            if (_kinect == null) return;
            var sensor = _kinect.GetSensor();
            if (sensor == null) return;

            var headDepth = sensor.MapSkeletonPointToColor(activeHeadPos,
                                                           ColorImageFormat.
                                                               RgbResolution1280x960Fps12);

            if (sensor.ColorStream == null) return;
            var pixelData = new byte[sensor.ColorStream.FramePixelDataLength];
            if (frame != null)
            {
                frame.CopyPixelDataTo(pixelData);
                frame.Dispose();
            }

            var mystream = new MemoryStream(pixelData);
            var p = Image.FromStream(mystream);
            var img = new Bitmap(p);

            var camImage = new Image<Bgr, byte>(img);

            // resize the 640x480 rgb image to match the depth image size of 320x240
            var reImg = camImage.Resize(1280, 240, INTER.CV_INTER_CUBIC);

            // set the region of interest which currently is a 40x40 pixel region where
            // the face is supposed to be. ( being to close might raise some issues )
            const int roi = 120;

            if (reImg != null)
            {
                reImg.ROI = new Rectangle((headDepth.X) - (roi/2), (headDepth.Y), roi, roi);

                // pass the image and the timestamp on to the faceDetection methods
                PassImage(reImg);
            }
        }

        /// <summary>
        /// resets the isBWDone switch to indicate the next image from the camera can be processed now.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void BwRunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            _isBwDone = true;
        }

        /// <summary>
        /// Starts face detection.
        /// </summary>
        public void DoProcess()
        {
            _isProcessing = true;
        }

        /// <summary>
        /// Stops face detection
        /// </summary>
        public void StopProcess()
        {
            _isProcessing = false;
        }

        /// <summary>
        /// trains the recognizer on a new face (or list of faces with the same name label)
        /// </summary>
        /// <param name="faces"></param>
        /// <param name="label"></param>
        public void LearnFace(ArrayList faces, string label)
        {
            // create a new image array to hold the faces
            if (faces == null) return;

            var images = new Image<Gray, byte>[faces.Count];
            // and an array to hold the name of the users aligned to the face array.
            var labels = new string[faces.Count];

            // loop though the faces and convert, store and equalize the contrast of each image.
            var counter = 0;
            foreach (var image in faces)
            {
                var img = image as Image<Gray, byte>;

                if (img != null)
                {
                    var resizedImg = img.Resize(100, 100, INTER.CV_INTER_CUBIC, true);

                    resizedImg._EqualizeHist();
                    images[counter] = resizedImg;
                }
                labels[counter++] = label;
            }

            // append the faces and labels to the labeldatabase if it exists 
            if (_faceDatabase != null && _labelDatabase != null)
            {
                _faceDatabase = _faceDatabase.Concat(images).ToArray();
                _labelDatabase = _labelDatabase.Concat(labels).ToArray();
            }
            else
            {
                // or create them if they dont
                _faceDatabase = images;
                _labelDatabase = labels;
            }

            // flag the recognizer as changed
            _isRecognizerLoaded = false;
            // and reload it
            InitRecognizer();

            // create a dictionary to hold both arrays and pass store it on the HDD.
            var database = new Dictionary<Image<Gray, byte>[], string[]> {{images, labels}};

            FileLoader.SaveFaceDB(database);
        }


        private void InitRecognizer()
        {
            if (_isRecognizerLoading) return;
            // if something changed in the recognizer or its not loaded start the 
            // initialization process.
            _isRecognizerLoading = true;

            if (_recognizerInitWorker != null) _recognizerInitWorker.RunWorkerAsync(_isDatabaseLoaded);
        }

        /// <summary>
        /// Initializes the recognizer, either with the database of images from HDD or, if
        /// those are already loaded, with the newly trained users.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecognizerInitWorkerDoWork(object sender, DoWorkEventArgs e)
        {
            if (e == null) return;
            if (!_isRecognizerLoaded)
            {
                // check if the db has been loaded already.
                var dbLoaded = e.Argument != null && (bool) e.Argument;

                if (!dbLoaded)
                {
                    // db needs to be loaded so retrieve the faces and labels from HDD

                    var database = FileLoader.LoadFaceDB("FaceDB");

                    // empty database??? cant be right... no need to load the recognizer then.
                    if (database == null)
                    {
                       Log.Error("Database could not be loaded");
                        return;
                    }

                    // for each entry in the database retrieve the faces and labels and append them to the local
                    // faceDatabase and labelDatabase arrays.
                    foreach (var entry in database)
                    {
                        var images = entry.Key;
                        var label = entry.Value;

                        if (_faceDatabase != null && _labelDatabase != null)
                        {
                            //Console.Out.WriteLine(label[0]);
                            if (images != null) _faceDatabase = _faceDatabase.Concat(images).ToArray();
                            if (label != null) _labelDatabase = _labelDatabase.Concat(label).ToArray();
                        }
                        else
                        {
                            _faceDatabase = images;
                            _labelDatabase = label;
                        }
                    }
                }

                // initialize the recognizer and train it wth the database data.
                var termCrit = new MCvTermCriteria(32, 0.001);
                _recognizer = new EigenObjectRecognizer(_faceDatabase, _labelDatabase, 1700, ref termCrit);
            }
        }

        /// <summary>
        /// Resets switches once  the recognizer is initialized. If recognition has been requested by
        /// the RecognitionProcessor this will also activate the recognizeFace method.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecognizerInitWorkerRunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            _isDatabaseLoaded = true;
            _isRecognizerLoading = false;
            _isRecognizerLoaded = true;

            if (_faceRecognitionActive)
            {
                RecognizeFace(_recFaces);
                _faceRecognitionActive = false;
            }
        }

        /// <summary>
        /// Works through the images passed for recognition and finds the closest match from the 
        /// EigenObjectRecognizer. 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecognizerWorkerDoWork(object sender, DoWorkEventArgs e)
        {
            if (e == null) return;
            // retrieve the arraylist from the arguments
            var faces = e.Argument as ArrayList;

            // copy them into an image array
            if (faces != null)
            {
                var facesCopy = new Image<Gray, byte>[faces.Count];

                faces.CopyTo(facesCopy);

                var bestLabel = ""; // and the closest matching label as well.
                var bestDistance = float.MaxValue; // keeps track of the best match distance
                Image<Gray, byte> optiImage = null;

                // loop though the faces and find the most similar object in the EigenRecognizer.
                foreach (var face in facesCopy)
                {
                    int index;
                    if (face != null) optiImage = face.Resize(100, 100, INTER.CV_INTER_CUBIC, false);
                    if (optiImage != null) optiImage._EqualizeHist();

                    float eigenDistance = 0;
                    string tempLabel = null;
                    if (_recognizer != null)
                        _recognizer.FindMostSimilarObject(optiImage, out index, out eigenDistance, out tempLabel);

                    // if the new eigendistance is better than the current best one, replace the values for 
                    // best distance, best label and best image.
                    if (bestDistance > eigenDistance)
                    {
                        bestDistance = eigenDistance;
                        bestLabel = tempLabel;
                    }
                }

                // recognition is done so create a new User object to store the results.
                var user = new User();

                // if a label has been returned
                if (!"".Equals(bestLabel))
                {
                    // calculate the confidence as a percentage value.
                    user.FaceConfidence = Math.Max(0, (1 - bestDistance/3000));
                    // store the username
                    user.Name = bestLabel;
                    // and the closest matching image.
                    user.Image = optiImage;
                }
                //Console.Out.WriteLine("User label: " + user.Name);
                e.Result = user;
            }
        }

        /// <summary>
        /// Invoked when recognition worker is finished. Passes the results to the recognitionProcessor to
        /// finish the recognition process.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RecognizerWorkerRunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if (e == null) return;
            var user = e.Result as User;
            if (_recProcessor != null) _recProcessor.UserRecognized(user);
        }

        /// <summary>
        /// Invoked by the recognitionProcessor to start recognition of faces passed to the method.
        /// </summary>
        /// <param name="faces"></param>
        public void RecognizeFace(ArrayList faces)
        {
            // if the recognizer needs to be loaded do so.
            if (!_isRecognizerLoaded)
            {
                _faceRecognitionActive = true;
                _recFaces = faces;
                InitRecognizer();
            }
            else
            {
                // otherwise start recognition in the background.
                if (_recognizerWorker != null) _recognizerWorker.RunWorkerAsync(faces);
            }
        }
    }
}