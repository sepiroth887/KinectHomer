using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Media;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;
using log4net;

namespace KinectCOM
{
    internal class ObjectPointer
    {
        #region Delegates

        public delegate void ContextSelectedEventHandler(string ctxt);

        #endregion

        private readonly SoundPlayer _activeSound;

        private readonly Dictionary<string, BoundingBox> _bounds;
        private readonly SoundPlayer _selectSound;

        private readonly Stopwatch _selectionCooldown = new Stopwatch();
        private readonly Stopwatch _selectionTimer = new Stopwatch();
        private readonly SoundPlayer _unselectSound;
        private bool _contextConfirmed;
        private string _currentContext = "__NOCONTEXT";
        private Vector3 _handPos;
        private long _lastPickEvent;


        private bool _playSound = true;
        private bool _pointedLastEvent;
        private Boolean _pointing;
        private bool _selectionCoolingDown;
        private static readonly ILog Log = LogManager.GetLogger(typeof(ObjectPointer));

        public ObjectPointer()
        {
            _bounds = new Dictionary<string, BoundingBox>();
            _selectSound =
                new SoundPlayer(
                    FileLoader.DefaultPath+"Whit.wav");
            _unselectSound =
                new SoundPlayer(
                    FileLoader.DefaultPath+"WhitR.wav");
            _activeSound =
                new SoundPlayer(
                    FileLoader.DefaultPath + "Voltage.wav");
        }

        public event ContextSelectedEventHandler ContextSelected;

        public static int ReturnLastContext()
        {
            return -1;
        }

        public void CreateObject(Vector3[] points, string contextName)
        {
            var box = BoundingBox.CreateFromPoints(points);

            _bounds.Add(contextName,box);

            Log.Info("Object added: "+contextName);
        }

        public String GetObjects()
        {
            var ret = "";
            if (_bounds != null)
                ret = _bounds.Aggregate(ret, (current, bound) => current + (bound.Key + ";"));

            return ret != null ? ret.Substring(0, ret.Length - 1) : "";
        }

        public void SetObjects(Dictionary<string, Vector3[]> objects)
        {
            if (_bounds != null)
            {
                _bounds.Clear();

                if (objects == null)
                {
                    Log.Info("No objects loaded, no model stored?");
                    return;
                }
                foreach (var obj in objects)
                    {
                        if (obj.Key != null && obj.Key.Contains("Room"))
                        {
                            continue;
                        }
                        var box = BoundingBox.CreateFromPoints(obj.Value);
                        _bounds.Add(obj.Key, box);
                        //Console.Out.WriteLine(obj.Key+": "+box.ToString());
                    }
            }
        }

        //check for an intersection of a ray with a bounding box and return the context of the box
        // or -1 if no intersection is found.
        private string Intersects(Ray ray)
        {
            var context = "__NOCONTEXT";
            if (_bounds != null)
                foreach (var box in _bounds.Where(box => ray.Intersects(box.Value) != null))
                {
                    context = box.Key;
                    ////Console.Out.WriteLine("Context picked " + context);
                    return context;
                }
            return context;
        }

        public Vector3 GetHandPos()
        {
            return _handPos;
        }

        public void FindContext(Skeleton skeleton)
        {
            if (_selectionCooldown != null && (_selectionCoolingDown && _selectionCooldown.IsRunning && _selectionCooldown.ElapsedMilliseconds < 4000))
                return;

            if (_selectionCooldown != null && (_selectionCooldown.ElapsedMilliseconds > 2000 && _selectionCoolingDown))
            {
                _selectionCoolingDown = false;
                _selectionCooldown.Stop();
                _selectionCooldown.Reset();
                _contextConfirmed = false;
                if (_selectionTimer != null) _selectionTimer.Reset();
                ContextSelected("__NOCONTEXT");
                return;
            }


            if (skeleton != null && (skeleton.Joints[JointType.HandRight].TrackingState == JointTrackingState.NotTracked ||
                                     skeleton.Joints[JointType.ElbowRight].TrackingState == JointTrackingState.NotTracked))
            {
                return;
            }


            var handV = skeleton.Joints[JointType.HandRight].Position;
            var elbowV = skeleton.Joints[JointType.ElbowRight].Position;
            var hipL = skeleton.Joints[JointType.Head].Position;
            var handL = skeleton.Joints[JointType.HandLeft].Position;

            var distanceHandHead = (hipL.X - handL.X)*(hipL.X - handL.X) + (hipL.Y - handL.Y)*(hipL.Y - handL.Y) +
                                    (hipL.Z - handL.Z)*(hipL.Z - handL.Z);

            //Log.Info(distanceHandHead);
            _handPos = new Vector3(handV.X, handV.Y, handV.Z);

            if (Math.Sqrt(distanceHandHead) > 0.45)
            {
                return;
            }

            ////Console.Out.WriteLine("Hand pos: " + handV.X+ ","+handV.Y + "," +handV.Z);

            var rayOrig = new Vector3(elbowV.X, elbowV.Y, elbowV.Z);
            var rayDir = new Vector3(handV.X - rayOrig.X, handV.Y - rayOrig.Y, handV.Z - rayOrig.Z);


            rayDir.Normalize();

            var pick = new Ray(rayOrig, rayDir);

            _currentContext = Intersects(pick);

            if (!"__NOCONTEXT".Equals(_currentContext))
            {
                _pointing = true;
            }

            if (_pointing)
            {
                ////Console.Out.WriteLine("Pointing at : " + currentContext);
                if (_playSound)
                {
                    if (_selectSound != null) _selectSound.Play();
                    ////Console.Out.WriteLine("Sound should have played");
                    _playSound = false;
                }

                _selectionTimer.Start();

                if (!_pointedLastEvent)
                {
                    _selectionTimer.Reset();
                    _lastPickEvent = 0;
                    _playSound = true;
                }
                else
                {
                    if (_selectionTimer.ElapsedMilliseconds > 1000)
                    {
                        _selectionTimer.Stop();
                        _contextConfirmed = true;
                        _playSound = true;
                    }
                }

                if (_contextConfirmed)
                {
                    if (_activeSound != null) _activeSound.Play();
                    ContextSelected(_currentContext);
                    _selectionCooldown.Start();
                    _selectionTimer.Reset();
                    _lastPickEvent = 0;
                    _selectionCoolingDown = true;
                    _pointedLastEvent = false;
                }

                _pointing = false;
                _pointedLastEvent = true;
            }
            else
            {
                if (_pointedLastEvent)
                {
                    if (_unselectSound != null) _unselectSound.Play();
                    _pointedLastEvent = false;
                }
                _lastPickEvent++;
                _selectionTimer.Stop();
            }
        }
    }
}