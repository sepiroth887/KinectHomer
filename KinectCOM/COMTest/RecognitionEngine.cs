using System;
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
        private readonly BodyFeatureDetector _featureDetector;

        private readonly Recognizer _recognizer;

        private readonly KinectData _kinect;

        private readonly TrackingEngine _trackingEngine;

        private static readonly ILog Log = LogManager.GetLogger(typeof(RecognitionEngine));

        private Skeleton _skelToMatch;

        public RecognitionEngine(KinectData kinect, TrackingEngine trackingEngine)
        {
            new FaceDetector();
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
            Log.Info("Training completed");
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

            var shoulderL = _kinect.GetSensor().MapSkeletonPointToColor(skel.Joints[JointType.ShoulderLeft].Position,
                                                                        ColorImageFormat.RgbResolution640x480Fps30);

            var shoulderR = _kinect.GetSensor().MapSkeletonPointToColor(skel.Joints[JointType.ShoulderRight].Position,
                                                                        ColorImageFormat.RgbResolution640x480Fps30);

            var startX = shoulderL.X;
            var startY = Math.Max(headPos.Y - 80,0);

            var width = Math.Sqrt((shoulderR.X - shoulderL.X)*(shoulderR.X - shoulderL.X));
            var height = Math.Sqrt((headPos.Y - shoulderL.Y)*(headPos.Y - shoulderL.Y)) + 100;

            //Log.Info(startX + "," + startY + "," + width + "," + height);


            width = Math.Min(Math.Max(width, 0),640);
            height = Math.Min(Math.Max(height, 0), 480);

            


            if (Double.IsNaN(width) || Double.IsNaN(height) || Math.Abs(width - 0) < 0.0001 || Math.Abs(height - 0) < 0.0001) return null;

            var img = new Image<Bgr, byte>(image) { ROI = new Rectangle(startX,startY,(int)width,(int)height) };

            

            var faces = new Image<Bgr,byte>[1];

            faces[0] = img.Copy();

            img.Save(FileLoader.DefaultPath+"testHeadPos.jpg");

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

        public bool AddUser(string userName,Skeleton skel)
        {
            return _featureDetector.AddUser(userName,skel);
        }

        public void DelUser(string user)
        {
            _featureDetector.DelUser(user);
        }
    }
}
