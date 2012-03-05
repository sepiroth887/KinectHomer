using System.Drawing;
using Emgu.CV;
using Emgu.CV.CvEnum;
using Emgu.CV.Structure;
using Microsoft.Kinect;

namespace KinectCOM
{
    class RecognitionEngine
    {
        private readonly FaceDetector _faceDetector;

        private readonly BodyFeatureDetector _featureDetector;

        private readonly Recognizer _recognizer;

        private readonly KinectData _kinect;

        public RecognitionEngine(KinectData kinect)
        {
            _faceDetector = new FaceDetector();
            _featureDetector = new BodyFeatureDetector();
            _recognizer = new Recognizer();
            _kinect = kinect;
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

        public User Recognize(Skeleton skel, Bitmap image, User user)
        {
            var faces = FindFace(skel, image);

            if(faces == null)
            {
                return null;
            }

            var userName = _recognizer.Recognize(faces[0]);

            if(userName != null)
            {
                user.Name = userName;

                if (_featureDetector != null) user = _featureDetector.ValidateUser(user, skel);
                user.Attempts++;
                return user;
            }
            var alternateUser = _featureDetector.ValidateUser(user, skel);
            alternateUser.TrackingID = skel.TrackingId;
            user.Attempts++;
            return alternateUser;
        }

        private Image<Gray,byte>[] FindFace(Skeleton skel, Bitmap image)
        {
            var headPos = _kinect.GetSensor().MapSkeletonPointToColor(skel.Joints[JointType.Head].Position,
                                                         ColorImageFormat.RgbResolution640x480Fps30);

            const int recSize = 80;

            var img = new Image<Bgr, byte>(image) { ROI = new Rectangle(headPos.X - recSize / 2, headPos.Y - recSize / 2, recSize, recSize) };

            var processedImg = PreProcess(img);

            var faces = _faceDetector.DetectFaces(processedImg);

            return faces;
        }

        public void Train(string name, Skeleton matchingSkeleton,Bitmap img)
        {
            _featureDetector.AddUser(name,matchingSkeleton);
            _recognizer.TrainFace(FindFace(matchingSkeleton,img)[0],name);
        }
    }
}
