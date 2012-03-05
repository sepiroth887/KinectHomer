using System;
using System.Collections;
using System.Collections.Generic;
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

        public const int CLOSEST_SKELETON = 0x0;
        public const int RECOGNIZED_FIRST = 0x1;
        public const int RECOGNIZED_ONLY = 0x2;

        private Skeleton[] _skeletons;
        private int _activeSkeleton;
        private byte[] _pixelData;

        private readonly object _lockObj = new object();

        private readonly Dictionary<int, User> _users = new Dictionary<int, User>();

        private readonly SkeletonHandler _skeletonHandler = new SkeletonHandler();

        private volatile bool _isRecRequired;
        private volatile bool _isTraining;


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
            lock (_lockObj)
            {
                if (e == null) return;
                var rgbFrame = e.OpenColorImageFrame();
                var depthFrame = e.OpenDepthImageFrame();
                var skeletonFrame = e.OpenSkeletonFrame();

                if (rgbFrame == null || depthFrame == null || skeletonFrame == null) return;

                CheckArrayIsSet(skeletonFrame.SkeletonArrayLength, rgbFrame.PixelDataLength);
                skeletonFrame.CopySkeletonDataTo(_skeletons);

                _skeletonHandler.UpdateSkeletons(_skeletons);

                if (_skeletonHandler.LostSkeletons().Count != 0)
                {
                    var oldSkels = _skeletonHandler.LostSkeletons();

                    foreach (Skeleton skeleton in oldSkels)
                    {
                        _kinectHandler.PresenceDetected(skeleton.TrackingId);
                        if(_users.ContainsKey(skeleton.TrackingId))
                        {
                            if(_users[skeleton.TrackingId].IsActive)
                            {
                                _isRecRequired = true;
                                _kinectHandler.UserLost(_users[skeleton.TrackingId]);
                            }
                            _users.Remove(skeleton.TrackingId);
                        }
                        

                    }
                }

                if (_skeletonHandler.NewSkeletons().Count != 0)
                {
                    var newSkels = _skeletonHandler.NewSkeletons();

                    foreach (Skeleton skeleton in newSkels)
                    {
                        _kinectHandler.PresenceLost(skeleton.TrackingId);
                    }
                }

                // ReSharper disable PossibleNullReferenceException
                rgbFrame.CopyPixelDataTo(_pixelData);
                // ReSharper restore PossibleNullReferenceException

                FindUserToTrack();

                rgbFrame.Dispose();
                skeletonFrame.Dispose();
                depthFrame.Dispose();
            }
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
            // no skeletons = no users to find so return
            if (_skeletons == null) return -1;
            // skeletons present and one marked as active
            if (_skeletons.Any(skeleton => skeleton != null && skeleton.TrackingId == _activeSkeleton))
            {
                // and is a user marked as active previously
                if(_users[_activeSkeleton] != null && ( _users[_activeSkeleton].IsActive || _isRecRequired ))
                    // no change to the current user
                    return _activeSkeleton;
            }

            // nothing tracked or no user detected yet

            int matchedUser = -1;
            if (_kinectHandler != null )
            {
                if(_isRecRequired && _users.ContainsKey(_activeSkeleton))
                {
                    Skeleton skel = null;
                    User user = _users[_activeSkeleton];
                    foreach(Skeleton skeleton in _skeletons)
                    {
                        if(skeleton.TrackingId == _activeSkeleton && skeleton.TrackingState == SkeletonTrackingState.Tracked)
                        {
                            skel = skeleton;
                        }
                    }


                    if (skel != null){
                       
                        _recognitionEngine.Recognize(skel, Coding4Fun.Kinect.WinForm.BitmapExtensions.ToBitmap(_pixelData,640,480), user);
                    }

                    if(user != null)
                    {
                        if(!"".Equals(user.Name))
                        {
                            user.TrackingID = skel.TrackingId;
                            user.IsActive = true;
                            _isRecRequired = false;
                            _kinectHandler.UserDetected(user);
                            _users[user.TrackingID] = user;
                            return user.TrackingID;
                        }
                    }
                }
                matchedUser = DetectUsers();
            }

            // still no user found, so fallback to closest skeleton tracking if using RECOGNIZED_FIRST strategy
            if (!userOnly && matchedUser == -1)
            {
                return FindClosestSkeleton();
            }
                
            return -1;
        }

        private int DetectUsers()
        {
            var mostAttempts = 0;
            User userToRecognizeNext = null;
           foreach(var user in _users)
           {
               if(user.Value.Attempts > mostAttempts && user.Value.Attempts < User.MAX_ATTEMPTS)
               {
                   mostAttempts = user.Value.Attempts;
                   userToRecognizeNext = user.Value;
               }
           }
            if(userToRecognizeNext != null)
            {
                return userToRecognizeNext.TrackingID;
            }
            return -1;
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

        public void Train(string name, int skeletonID)
        {
            _isTraining = true;
            Skeleton matchingSkeleton = _skeletons.FirstOrDefault(skel => skel.TrackingId == skeletonID);

            if (matchingSkeleton != null)
            {
                _recognitionEngine.Train(name, matchingSkeleton, Coding4Fun.Kinect.WinForm.BitmapExtensions.ToBitmap(_pixelData, 640, 480));
            }

        }
    }
}
