using System;
using System.Collections;
using System.Linq;
using Kinect.Toolbox;
using Microsoft.Kinect;
using log4net;

namespace KinectCOM
{
    class TrackingEngine
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(TrackingEngine));
        private readonly KinectData _kinect;
        private readonly KinectHandler _kinectHandler;
        private readonly RecognitionEngine _recognitionEngine;

        private const int CLOSEST_SKELETON = 0x0;
        private const int RECOGNIZED_FIRST = 0x1;
        private const int RECOGNIZED_ONLY = 0x2;

        private static readonly ColorStreamManager ColorStreamManager = new ColorStreamManager();

        private Skeleton[] _skeletons;
        private int _activeSkeleton;
        private User _currentUser;
        private readonly ArrayList _users;
        private byte[] _pixelData;


        public TrackingEngine(KinectData kinect, KinectHandler kinectHandler)
        {
            Log.Info("Starting TrackingEngine");
            _kinect = kinect;
            _kinectHandler = kinectHandler;
            Strategy = CLOSEST_SKELETON;
            if (_kinect != null)
            {
                Log.Info("Attaching to Kinect");
                var kinectSensor = _kinect.GetSensor();
                if (kinectSensor != null)
                    kinectSensor.AllFramesReady+=TrackingEngineAllFramesReady;
                _recognitionEngine = new RecognitionEngine(kinect);
            }
            _activeSkeleton = -1;
            _users = new ArrayList();
            Log.Info("Tracking Engine Started");
        }

        private void CheckArrayIsSet(int skeletonLenght, int rgbLength)
        {
            if(_skeletons == null || _skeletons.Length != skeletonLenght)
            {
                _skeletons = new Skeleton[skeletonLenght];
            }
            if (_pixelData == null || _pixelData.Length != rgbLength)
            {
                _pixelData = new byte[rgbLength];
            }
        }

        private void TrackingEngineAllFramesReady(object sender, AllFramesReadyEventArgs e)
        {
            if(e == null) return;
            var rgbFrame = e.OpenColorImageFrame();
            var depthFrame = e.OpenDepthImageFrame();
            var skeletonFrame = e.OpenSkeletonFrame();

            if (rgbFrame == null || depthFrame == null || skeletonFrame == null) return;

            CheckArrayIsSet(skeletonFrame.SkeletonArrayLength,rgbFrame.PixelDataLength);
            skeletonFrame.CopySkeletonDataTo(_skeletons);

// ReSharper disable PossibleNullReferenceException
            rgbFrame.CopyPixelDataTo(_pixelData);
// ReSharper restore PossibleNullReferenceException

            FindUserToTrack();

            rgbFrame.Dispose();
            skeletonFrame.Dispose();
            depthFrame.Dispose();
        }

        private void FindUserToTrack()
        {
            int matchingSkeleton;
            switch (Strategy)
            {
                case CLOSEST_SKELETON:
                    matchingSkeleton = FindClosestSkeleton();
                    break;
                case RECOGNIZED_FIRST:
                    matchingSkeleton = FindCurrentUser(false);
                    break;
                case RECOGNIZED_ONLY:
                    matchingSkeleton = FindCurrentUser(true);
                    break;
                default:
                    matchingSkeleton = FindClosestSkeleton();
                    break;
            }

            SetTrackedSkeleton(matchingSkeleton);
        }

        private void SetTrackedSkeleton(int matchingSkeleton)
        {
            if (matchingSkeleton == _activeSkeleton) return;
            if(matchingSkeleton != -1 && _kinect != null && _kinect.GetSensor() != null){
                Log.Info("Closest skeleton found. Starting tracking of id : "+matchingSkeleton);
                _kinect.GetSensor().SkeletonStream.ChooseSkeletons(matchingSkeleton);
                _kinectHandler.StartTracking(matchingSkeleton);
                _kinectHandler.TrackingStarted(matchingSkeleton);
                _activeSkeleton = matchingSkeleton;
            }else 
            {
                Log.Info("No suitable skeleton found for tracking.");
                _kinectHandler.StopTracking(_activeSkeleton);
                _kinectHandler.TrackingStopped(matchingSkeleton);
                _activeSkeleton = -1;
            }
        }

        private int FindCurrentUser(bool userOnly)
        {
            if (_skeletons == null) return -1;
            if (_skeletons.Any(skeleton => skeleton != null && skeleton.TrackingId == _activeSkeleton) && _currentUser.TrackingID == _activeSkeleton)
            {
                return _activeSkeleton;
            }

            if (_kinectHandler != null && ColorStreamManager != null)
            {
                float minDistance = float.MaxValue;
                User closestUser = null;

                DetectUsers();

                if(_users != null)
                {
                    foreach(User user in _users)
                    {
                        foreach(var skeleton in _skeletons)
                        {
                            if (user.TrackingID != skeleton.TrackingId) continue;
                            
                            var distance = getDistanceFromOrigin(skeleton.Position);
                            
                            if (distance >= minDistance) continue;
                            
                            minDistance = getDistanceFromOrigin(skeleton.Position);
                            closestUser = user;
                        }
                    }

                    _currentUser = closestUser;
                    if (_currentUser != null) _activeSkeleton = _currentUser.TrackingID;
                    return _activeSkeleton;
                }


            }

            if (!userOnly)
            {
                return FindClosestSkeleton();
            }
                
            return -1;
        }

        private void DetectUsers()
        {
            foreach(var skel in _skeletons)
            {
                if (_pixelData != null)
                    _recognitionEngine.Recognize(skel, Coding4Fun.Kinect.WinForm.BitmapExtensions.ToBitmap(_pixelData,640,480));
            }
        }

        private float getDistanceFromOrigin(SkeletonPoint skeleton)
        {
            //Log.Info("Getting Distance for skeleton with position: "+skeleton.X+","+skeleton.Z+","+skeleton.Z);
            var x = skeleton.X;
            var y = skeleton.Y;
            var z = skeleton.Z;

            return (float)Math.Sqrt(x * x + y * y + z * z);
        }

        private int FindClosestSkeleton()
        {
            if (_skeletons == null) return -1;
            var minDistance = float.MaxValue;
            var trackingID =  -1;
            foreach(var skeleton in _skeletons)
            {
                if (skeleton == null || skeleton.TrackingState == SkeletonTrackingState.NotTracked) continue;

                var distance = getDistanceFromOrigin(skeleton.Position);
                if (minDistance <= distance) continue;
                minDistance = distance;
                trackingID = skeleton.TrackingId;
            }

            
            return trackingID;
        }

        public int Strategy { get; set; }
    }
}
