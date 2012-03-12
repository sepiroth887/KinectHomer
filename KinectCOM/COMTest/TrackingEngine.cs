using System;
using System.Collections;
using System.Linq;
using System.Media;
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
        private int _activeTID;
        private Skeleton _activeSkeleton;
        private User _activeUser;
        private byte[] _pixelData;

        private readonly object _lockObj = new object();

        private readonly ArrayList _users = new ArrayList(); 

        private readonly ArrayList _presenceIDs = new ArrayList();

        private bool _recSoundPlayed;

        private readonly SoundPlayer  _recStart =
                new SoundPlayer(
                    FileLoader.DefaultPath + "recStart.wav");

        private readonly SoundPlayer _recDone =
        new SoundPlayer(
            FileLoader.DefaultPath + "recStop.wav");

        public TrackingEngine(KinectData kinect, KinectHandler kinectHandler)
        {
            if (Log == null) return;
            Log.Info("Starting TrackingEngine");
            _kinect = kinect;
            _kinectHandler = kinectHandler;
            Strategy = RECOGNIZED_FIRST;
            if (_kinect != null)
            {
                Log.Info("Attaching to Kinect");
                var kinectSensor = _kinect.GetSensor();
                if (kinectSensor != null)
                    kinectSensor.AllFramesReady+=TrackingEngineAllFramesReady;
                _recognitionEngine = new RecognitionEngine(kinect,this);
            }
            _activeTID = -1;
            Log.Info("Tracking Engine Started");

            LoadUsers();

            Log.Info("Users loaded");
        }

        public string GetUsers()
        {
            if (_users == null) return "";
            var result = _users.Cast<User>().Aggregate("", (current, user) => user != null ? current + (user.Name + ";") : null);

            if (result != null)
            {
                return result.Substring(0, result.Length - 1);
            }

            return "";
        }

        private void LoadUsers()
        {
            var userData = FileLoader.LoadAllUsers();

            if (userData == null) return;

            foreach (var userInfo in userData)
            {
                var user = new User { Name = userInfo.Key, TrackingID = -1, IsActive = false };

                if (userInfo.Value != null)
                    foreach (var feature in userInfo.Value)
                    {
                        if (feature.Key.Equals(FeatureType.ArmLength))
                        {
                            user.ArmLength = float.Parse(feature.Value);
                        }
                        else if (feature.Key.Equals(FeatureType.HipHeadHeight))
                        {
                            user.HipHeight = float.Parse(feature.Value);

                        }
                        else if (feature.Key.Equals(FeatureType.ShoulderWidth))
                        {
                            user.ShoulderWidth = float.Parse(feature.Value);

                        }
                    }  
                _users.Add(user);
            }
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
                
                //_skeletonHandler.UpdateSkeletons(_skeletons);

                var tempIds = new ArrayList();

                foreach(var skel in _skeletons)
                {
                    if(skel.TrackingId != 0)
                    {
                        tempIds.Add(skel.TrackingId);

                    }

                    if(!_presenceIDs.Contains(skel.TrackingId) && skel.TrackingState != SkeletonTrackingState.NotTracked)
                    {
                        _presenceIDs.Add(skel.TrackingId);
                        _kinectHandler.PresenceDetected(skel.TrackingId);
                    }

                    
                }

                var removeIds = new ArrayList();

                foreach(int pId in _presenceIDs)
                {
                    if(!tempIds.Contains(pId))
                    {
                        removeIds.Add(pId);
                    }
                }

                foreach(int id in removeIds)
                {
                    _kinectHandler.PresenceLost(id);
                    _presenceIDs.Remove(id);
                    if(_activeTID == id)
                    {
                        _activeTID = -1;
                        _activeSkeleton = null;
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

        private int FindCurrentUser(bool forceRecognition)
        {
            if (_recognitionEngine == null) return -1;
            if (_skeletons == null) return -1;

            foreach (var user in _users.Cast<User>().Where(user => user.IsActive))
            {
                foreach(var skel in _skeletons)
                {
                    if(skel.TrackingId == user.TrackingID && skel.TrackingState == SkeletonTrackingState.Tracked)
                    {
                        return user.TrackingID;
                    }
                }

                user.IsActive = false;
                user.TrackingID = -1;

                return -1;
            }

            foreach(var skel in _skeletons)
            {
                if(skel.TrackingId == _activeTID && skel.TrackingState != SkeletonTrackingState.Tracked)
                { 
                    return -1;
                }
            }

            if(_activeTID != -1 && _activeSkeleton != null && !_recognitionEngine.IsRecognizing)
            {
                if(_activeUser == null)
                {
                    _activeUser = new User();
                    _activeUser.TrackingID = _activeTID;
                    _activeUser.Skeleton = _activeSkeleton;
                }else if(_activeUser.TrackingID != _activeTID || _activeSkeleton.TrackingId != _activeUser.Skeleton.TrackingId)
                {
                    _activeUser.TrackingID = _activeTID;
                    _activeUser.Skeleton = _activeSkeleton;
                    _activeUser.Attempts = 0;
                }

                if(_activeUser.Attempts >= User.MAX_ATTEMPTS && _activeUser.Attempts <= User.MAX_ATTEMPTS+1)
                {
                    Log.Info("Gave up on recognition for user with id: " + _activeUser.TrackingID);
                    _activeUser.Attempts++;
                    return -1;
                }

                if(!_recSoundPlayed)
                {
                    _recStart.Play();
                    _recSoundPlayed = true;
                }


                if(_recognitionEngine.Recognize(_activeSkeleton,Coding4Fun.Kinect.WinForm.BitmapExtensions.ToBitmap(_pixelData,640,480)))
                {
                    _activeUser.Attempts++;
                }

                return _activeTID;

            }else if(_recognitionEngine.IsRecognizing){
                return _activeTID;
            }


            return FindClosestSkeleton();
        }

        private void SetTrackedSkeleton(int matchingSkeleton)
        {
           

            if (matchingSkeleton == _activeTID) return;
            if(matchingSkeleton != -1 && _kinect != null && _kinect.GetSensor() != null){
                
                _kinect.GetSensor().SkeletonStream.ChooseSkeletons(matchingSkeleton);
                
                if(Strategy.Equals(TrackingEngine.CLOSEST_SKELETON)|| (_activeUser != null && _activeUser.Attempts >= User.MAX_ATTEMPTS))
                {
                    Log.Info("Closest skeleton found. Starting tracking of id : " + matchingSkeleton);
                    _kinectHandler.StartTracking(matchingSkeleton);
                    _kinectHandler.TrackingStarted(matchingSkeleton);
                }

                _activeTID = matchingSkeleton;
            }else 
            {
                Log.Info("No suitable skeleton found for tracking. Tracking stopped");
                _kinectHandler.StopTracking(_activeTID);
                _kinectHandler.TrackingStopped(matchingSkeleton);
                _activeTID = -1;
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
            Skeleton tempSkel = null;
            foreach(var skeleton in _skeletons)
            {
                if (skeleton == null || skeleton.TrackingState == SkeletonTrackingState.NotTracked) continue;

                var distance = getDistanceFromOrigin(skeleton.Position);
                if (minDistance <= distance) continue;
                minDistance = distance;
                trackingID = skeleton.TrackingId;
                tempSkel = skeleton;
            }

            if(tempSkel != null)
            {
                _activeSkeleton = tempSkel;
            }

            return trackingID;
        }

        public int Strategy { get; set; }

        public void Train(string name, int skelID)
        {
            foreach(var skeleton in _skeletons)
            {
                if(skeleton.TrackingId == skelID && skeleton.TrackingState == SkeletonTrackingState.Tracked && skeleton.TrackingId == _activeTID)
                {
                    var exists = _recognitionEngine.AddUser(name,skeleton); 
                    _recognitionEngine.Train(name, skeleton, Coding4Fun.Kinect.WinForm.BitmapExtensions.ToBitmap(_pixelData, 640, 480));
                    
                    if(!exists)
                    {
                        var u = new User {Name = name, IsActive = false, TrackingID = skeleton.TrackingId};
                        _users.Add(u);
                    }
                }
            }
            
        }

        public void RecognitionResult(User user)
        {
            if (user == null) return;
            foreach(var skeleton in _skeletons)
            {
                if (skeleton.TrackingId != user.TrackingID ||
                    skeleton.TrackingState == SkeletonTrackingState.NotTracked) continue;
                _kinectHandler.UserDetected(user);

                foreach (User userL in _users)
                {
                    if (!userL.Name.Equals(user.Name)) continue;
                    userL.TrackingID = user.TrackingID;
                    userL.Confidence = user.Confidence;
                    userL.FaceConfidence = user.FaceConfidence;
                    userL.IsActive = true;
                    userL.ShoulderWidth = user.ShoulderWidth;
                    userL.HipHeight = user.HipHeight;
                    userL.ArmLength = user.ArmLength;
                    _activeSkeleton = skeleton;
                    _recDone.Play();
                    _recSoundPlayed = false;
                    //zLog.Info("User skeleton found. Starting tracking of id : " + userL.TrackingID);
                    _activeTID = userL.TrackingID;
                    _kinectHandler.StartTracking(userL.TrackingID);
                    _kinectHandler.TrackingStarted(userL.TrackingID);
                    _activeUser = null;
                }
            }
        }

        public void AddUser(string user)
        {
            if (_activeTID == -1) return;

            Train(user,_activeTID);
        }

        public void DelUser(string user)
        {
            foreach(User u in _users)
            {
                if(u.Name.Equals(user))
                {
                    _users.Remove(u);
                }
            }

            _recognitionEngine.DelUser(user);
        }
    }
}
