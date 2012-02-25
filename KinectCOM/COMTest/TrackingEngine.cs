using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Kinect.Toolbox;
using Microsoft.Kinect;

namespace KinectCOM
{
    class TrackingEngine
    {
        private int _currentTrackingID;
        private KinectData _kinect;
        private KinectHandler _kinectHandler;

        private const int CLOSEST_SKELETON = 0x0;
        private static readonly int RECOGNIZED_FIRST = 0x1;
        private static readonly int RECOGNIZED_ONLY = 0x2;

        private static ColorStreamManager _colorStreamManager = new ColorStreamManager();

        public TrackingEngine(KinectData kinect, KinectHandler kinectHandler)
        {
            _kinect = kinect;
            _kinectHandler = kinectHandler;
            _currentTrackingID = -1;
            Strategy = CLOSEST_SKELETON;
            if (_kinect != null)
            {
                var kinectSensor = _kinect.GetSensor();
                if (kinectSensor != null)
                    kinectSensor.AllFramesReady+=new EventHandler<AllFramesReadyEventArgs>(TrackingEngineAllFramesReady);
            }
        }

        private byte[] _rgbPixelData;
        private Skeleton[] _skeletons;
        private int _activeSkeleton;

        private void CheckArrayIsSet(int skeletonLenght)
        {
            if(_skeletons == null || _skeletons.Length != skeletonLenght)
            {
                _skeletons = new Skeleton[skeletonLenght];
            }
        }

        private void TrackingEngineAllFramesReady(object sender, AllFramesReadyEventArgs e)
        {
            var rgbFrame = e.OpenColorImageFrame();
            var depthFrame = e.OpenDepthImageFrame();
            var skeletonFrame = e.OpenSkeletonFrame();

            if (rgbFrame == null || depthFrame == null || skeletonFrame == null) return;

            CheckArrayIsSet(skeletonFrame.SkeletonArrayLength);
            skeletonFrame.CopySkeletonDataTo(_skeletons);

// ReSharper disable PossibleNullReferenceException
            _colorStreamManager.Update(rgbFrame);
// ReSharper restore PossibleNullReferenceException

            FindUserToTrack();

            rgbFrame.Dispose();
            skeletonFrame.Dispose();
            depthFrame.Dispose();
        }

        private void FindUserToTrack()
        {
            int matchingSkeleton = -1;
            if(Strategy == CLOSEST_SKELETON)
            {
                matchingSkeleton = FindClosestSkeleton();
            }else if(Strategy == RECOGNIZED_FIRST)
            {
                matchingSkeleton = FindCurrentUser(false);
            }else if(Strategy == RECOGNIZED_ONLY)
            {
                matchingSkeleton = FindCurrentUser(true);
            }

            SetTrackedSkeleton(matchingSkeleton);
        }

        private void SetTrackedSkeleton(int matchingSkeleton)
        {
            _activeSkeleton = matchingSkeleton;
        }

        private int FindCurrentUser(bool userOnly)
        {
            foreach (var skeleton in _skeletons)
            {
                if (skeleton.TrackingId == _activeSkeleton)
                {
                    return _activeSkeleton;
                }
            }

            if (userOnly)
            {
                return FindClosestSkeleton();
            }else
            {
                _kinectHandler.recognizeFace(_colorStreamManager.Bitmap);
            }

            return -1;
        }

        private int FindClosestSkeleton()
        {
            var minDistance = float.MaxValue;
            var trackingID = int.MaxValue;
            foreach(var skeleton in _skeletons)
            {
                var x = skeleton.Position.X;
                var y = skeleton.Position.Y;
                var z = skeleton.Position.Z;

                var distance = x*x + y*y + z*z;

                if (minDistance <= distance) continue;
                minDistance = distance;
                trackingID = skeleton.TrackingId;
            }

            return trackingID;
        }

        public int Strategy { get; set; }
    }
}
