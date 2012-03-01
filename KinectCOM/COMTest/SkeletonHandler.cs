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
        private Skeleton[] _currentSkeletons;
        private Skeleton[] _previousSkeletons;
        private ArrayList _lostSkeletons;
        private ArrayList _newSkeletons;

        private readonly object lockObj = new object();
        public SkeletonHandler()
        {
            _lostSkeletons = new ArrayList();
            _newSkeletons = new ArrayList();
        }

        public void UpdateSkeletons(Skeleton[] skeletons)
        {
            if (skeletons == null) return;
            lock(lockObj)
            {
                if (_previousSkeletons == null)
                {
                    _lostSkeletons.Clear();
                    _newSkeletons.Clear();
                    _previousSkeletons = skeletons.Clone() as Skeleton[];
                    foreach(var skeleton in skeletons)
                    {
                        _newSkeletons.Add(skeleton);
                    }
                    return;
                }else if(skeletons.Length < _previousSkeletons.Length)
                {
                    // some skeletons have been dropped.
                    _lostSkeletons.Clear();
                    _newSkeletons.Clear();
                    foreach(var skeleton in _previousSkeletons)
                    {
                        if(skeletons.Contains(skeleton)) continue;
                        _lostSkeletons.Add(skeleton);
                    }
                }else if(skeletons.Length > _previousSkeletons.Length)
                {
                    // new skeletons have been found.
                    _lostSkeletons.Clear();
                    _newSkeletons.Clear();

                    foreach (var skeleton in skeletons)
                    {
                        if(_previousSkeletons.Contains(skeleton))continue;
                        _newSkeletons.Add(skeleton);
                    }

                }

            }
        }

        public ArrayList LostSkeletons()
        {
            lock(lockObj)
            {
                return _lostSkeletons;
            }
        }

        public ArrayList NewSkeletons()
        {
            lock(lockObj)
            {
                return _newSkeletons;
            }
        }
    }
}
