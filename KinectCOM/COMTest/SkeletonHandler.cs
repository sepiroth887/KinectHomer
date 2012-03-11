using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace KinectCOM
{
    class SkeletonHandler
    {
        private readonly Dictionary<int,Skeleton> _previousSkeletons = new Dictionary<int, Skeleton>();
        private readonly Dictionary<int,Skeleton> _currentSkeletons = new Dictionary<int, Skeleton>();
        private readonly ArrayList _lostSkeletons;
        private readonly ArrayList _newSkeletons;
        private bool _isConsumed;

        private readonly object _lockObj = new object();
        public SkeletonHandler()
        {
            _lostSkeletons = new ArrayList();
            _newSkeletons = new ArrayList();
        }

        public void UpdateSkeletons(Skeleton[] skeletons)
        {
            if (skeletons == null || !_isConsumed) return;
            
            lock(_lockObj)
            {
                _isConsumed = false;
                _newSkeletons.Clear();
                _lostSkeletons.Clear();
               
                _previousSkeletons.Clear();
                foreach(var skeleton in _currentSkeletons)
                {
                    _previousSkeletons.Add(skeleton.Key,skeleton.Value);
                }

                _currentSkeletons.Clear();
                foreach(var skeleton in skeletons)
                {
                    if (skeleton.TrackingState != SkeletonTrackingState.NotTracked)
                    {
                        _currentSkeletons.Add(skeleton.TrackingId, skeleton);
                        
                    }
                    
                }

                if(_previousSkeletons.Count > _currentSkeletons.Count)
                {
                    foreach(var skel in _previousSkeletons)
                    {
                        if(!_currentSkeletons.ContainsKey(skel.Key))
                        {
                            _lostSkeletons.Add(skel.Value);
                        }
                    }
                }else
                {
                    foreach(var skel in _currentSkeletons)
                    {
                        if(!_previousSkeletons.ContainsKey(skel.Key))
                        {
                            _newSkeletons.Add(skel.Value);
                        }
                    }
                }

            }
        }

        public ArrayList LostSkeletons()
        {
            lock(_lockObj)
            {
                return _lostSkeletons;
            }
        }

        public void Consume()
        {
            _isConsumed = true;
        }

        public ArrayList NewSkeletons()
        {
            lock(_lockObj)
            {
                return _newSkeletons;
            }
        }
    }
}
