using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using Emgu.CV;
using Emgu.CV.CvEnum;
using Emgu.CV.Structure;
using FaceComTest;
using Microsoft.Kinect;
using log4net;

namespace KinectCOM
{
    class RecognitionEngine
    {
        private readonly FaceDetector _faceDetector;

        private readonly BodyFeatureDetector _featureDetector;

        private readonly Recognizer _recognizer;

        private readonly KinectData _kinect;

        private readonly TrackingEngine _trackingEngine;

        private static readonly ILog Log = LogManager.GetLogger(typeof(RecognitionEngine));

        private Skeleton _skelToMatch;

        public RecognitionEngine(KinectData kinect, TrackingEngine trackingEngine)
        {
            _faceDetector = new FaceDetector();
            _featureDetector = new BodyFeatureDetector();
            _recognizer = new Recognizer();
            _kinect = kinect;
            _trackingEngine = trackingEngine;
            _recognizer.RecognitionCompletedEvent += _recognizer_RecognitionCompletedEvent;
            _recognizer.TrainingCompletedEvent += _recognizer_TrainingCompletedEvent;
        }

        public bool IsRecognizing { set; get; }

        public bool IsTraining { get; set; }

        void _recognizer_TrainingCompletedEvent(System.ComponentModel.RunWorkerCompletedEventArgs e)
        {
            IsTraining = false;
        }

        void _recognizer_RecognitionCompletedEvent(System.ComponentModel.RunWorkerCompletedEventArgs e)
        {

            IsRecognizing = false;
            if (e.Cancelled)
            {
                _trackingEngine.RecognitionResult(null);
                return;
            }

            var users = e.Result as Dictionary<string, float>;

            var bestMatch = "";
            var bestConf = 50f;

            foreach (var user in users)
            {
                if (user.Value > bestConf)
                {
                    bestMatch = user.Key;
                    bestConf = user.Value;
                }
            }

            if ("".Equals(bestMatch))
            {
                _trackingEngine.RecognitionResult(null);
                return;
            }

            bestMatch = bestMatch.Replace("@homerKinect.com", "");

            Log.Info(bestMatch);

            User userF = new User();

            userF.Name = bestMatch;
            userF.FaceConfidence = bestConf;

            userF.TrackingID = _skelToMatch.TrackingId;

            userF = _featureDetector.ValidateUser(userF, _skelToMatch);




            _trackingEngine.RecognitionResult(userF);
            

        }

        private Image<Gray,byte> PreProcess(Image<Bgr,byte> image)
        {
            if (image != null)
            {

                var grey = image.Convert<Gray, byte>();

                var resizedImg = grey.Resize(100, 100, INTER.CV_INTER_CUBIC, true);


                if (resizedImg != null)
                {
                    resizedImg._EqualizeHist();

                    return resizedImg;
                }
            }

            return null;
        }

        public bool Recognize(Skeleton skel, Bitmap image)
        {
            
            _skelToMatch = skel;
            
            var faces = FindFace(skel, image);



            if(faces == null || faces.Length != 1 || faces[0] == null || _skelToMatch == null || IsRecognizing)
            {
                return false;
            }


            IsRecognizing = true;
            _recognizer.Recognize(faces[0].ToBitmap());
            return true;
        }

        private Image<Bgr,byte>[] FindFace(Skeleton skel, Bitmap image)
        {
            var headPos = _kinect.GetSensor().MapSkeletonPointToColor(skel.Joints[JointType.Head].Position,
                                                         ColorImageFormat.RgbResolution640x480Fps30);

            const int recSize = 100;

            var img = new Image<Bgr, byte>(image) { ROI = new Rectangle(headPos.X - recSize / 2, headPos.Y - recSize / 2, recSize, recSize) };

            var processedImg = PreProcess(img);

            var faces = new Image<Bgr,byte>[1];

            faces[0] = new Image<Bgr, byte>(image);

            return faces;
        }

        public bool Train(string name, Skeleton matchingSkeleton, Bitmap img)
        {
            if (img == null || "".Equals(name) || matchingSkeleton == null || IsTraining) return false;
            IsTraining = true;
            _featureDetector.AddUser(name,matchingSkeleton);
            _recognizer.Train(name, FindFace(matchingSkeleton, img)[0].ToBitmap());
            return true;
        }
    }
}
