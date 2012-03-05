using System;
using System.Collections;
using System.Diagnostics;
using System.Linq;
using System.Threading;
using DTWGestureRecognition;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;
using log4net;

namespace KinectCOM
{
    public class GestureProcessor
    {
        /// <summary>
        /// How many skeleton frames to ignore (_flipFlop)
        /// 1 = capture every frame, 2 = capture every second frame etc.
        /// </summary>
        private const int IGNORE = 2;

        /// <summary>
        /// How many skeleton frames to store in the _video buffer
        /// </summary>
        private const int BUFFER_SIZE = 32;

        /// <summary>
        /// The minumum number of frames in the _video buffer before we attempt to start matching gestures
        /// </summary>
        private const int MINIMUM_FRAMES = 10;

        private const string Z_ADDON = "z";
        private const string X_ADDON = "x";
        private const string Y_ADDON = "y";

        private readonly DtwGestureRecognizer _dtw;
        private readonly KinectData _kinect;
        private readonly IKinect _kinectHandler;

        private readonly ObjectPointer _pointer;
        private readonly Stopwatch _recTimer = new Stopwatch();
        private ArrayList _seqCoords;
        private readonly Stopwatch _startoffset = new Stopwatch();
        private readonly Stopwatch _stopTimer = new Stopwatch();
        private readonly Stopwatch _timer = new Stopwatch();
        private int _activeUser = -1;
        private bool _activityRunning;

        private bool _addOnGesture;
        private string _addOnGestureType;
        private string _ctxt = "__NOCONTEXT";
        private Gesture _currentGesture;
        private int _flipFlop;
        private bool _hasStartPoint;
        private bool _isRecognizing;
        private bool _isRecording;
        private SkeletonPoint _lastPoint;
        private float _rateOfChange;
        private SkeletonPoint _startPoint;
        private bool _defaultHand = true;
        private static readonly ILog Log = LogManager.GetLogger(typeof(GestureProcessor));

        public GestureProcessor(IKinect kinectHandler, KinectData kinect)
        {
            
            _pointer = new ObjectPointer();
            Log.Debug("objectPointer loaded");
            _pointer.SetObjects(FileLoader.LoadObj("livingRoom.obj", FileLoader.Units.cm));

            Log.Debug("objects loaded");

            _kinectHandler = kinectHandler;
            _kinect = kinect;
            _seqCoords = new ArrayList();

            if (kinect != null) kinect.attachSkeletonHandler(SkeletonDataReadyHandler);

            _dtw = new DtwGestureRecognizer(12, 0.6, 2, 2, 10);
            Skeleton2DDataExtract.Skeleton2DdataCoordReady += NuiSkeleton2DdataCoordReady;
            _pointer.ContextSelected += PointerContextSelected;
        }

        public String GetObjects()
        {
            return _pointer != null ? _pointer.GetObjects() : "";
        }

        private void PointerContextSelected(string ctxt)
        {
            if (_kinectHandler != null) _kinectHandler.ContextSelected(ctxt);
        }

        private void SkeletonDataReadyHandler(object sender, SkeletonFrameReadyEventArgs e)
        {
            // no active user, ignore events
            if (_activeUser == -1 || e == null || _kinect == null) return;
            // not recording and no context, ignore events.
            var sFrame = e.OpenSkeletonFrame();
            if (sFrame == null) return;
            var skeletons = new Skeleton[_kinect.GetSensor().SkeletonStream.FrameSkeletonArrayLength];
            sFrame.CopySkeletonDataTo(skeletons);
            sFrame.Dispose();
            foreach (var skeleton in skeletons.Where(skeleton => skeleton != null && skeleton.TrackingId == _activeUser))
            {
                Skeleton2DDataExtract.ProcessData(skeleton, _defaultHand);
                if (!_isRecording)
                    if (_pointer != null) _pointer.FindContext(skeleton);
                if (_addOnGesture)
                {
                    AddOnGesture(_addOnGestureType, skeleton);
                }
            }
        }


        public Vector3? GetHandPos()
        {
            if (_pointer == null) return null;
                
            return _pointer.GetHandPos();
        }


        public void RecognizeGesture(string ctxt)
        {
            _isRecording = false;
            _ctxt = ctxt;
            if (_seqCoords != null) _seqCoords.Clear();
            _isRecognizing = true;
            if (_recTimer != null)
            {
                _recTimer.Reset();
                _recTimer.Start();
            }
        }

        public void StopRecognition()
        {
            _isRecognizing = false;
        }

        public void StoreGestures()
        {
            if (_dtw != null) FileLoader.SaveGestures(_dtw.RetrieveText());
        }

        public string[] LoadGestures()
        {
            return FileLoader.LoadGestures(_dtw);
        }

        public void EnableAddOnGesture(string type)
        {
            _addOnGesture = true;
            _addOnGestureType = type;
        }


        private void AddOnGesture(string type, Skeleton skel)
        {
            if (skel != null && skel.Joints[JointType.HandLeft].TrackingState == JointTrackingState.NotTracked)
            {
                return;
            }

            if (X_ADDON.Equals(type))
            {
                StartAddonGesture(skel, "X");
            }
            else if (Y_ADDON.Equals(type))
            {
                StartAddonGesture(skel, "Y");
            }
            else if (Z_ADDON.Equals(type))
            {
                StartAddonGesture(skel, "Z");
            }
        }


        private void StartAddonGesture(Skeleton skeleton, string axis)
        {
            if (skeleton == null) return;
            if (!_hasStartPoint)
            {
                _startPoint = skeleton.Joints[JointType.HandLeft].Position;
                _lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                if (_timer != null) _timer.Start();
                if (_startoffset != null) _startoffset.Start();
                //Console.Out.WriteLine("Timer started");
                _hasStartPoint = true;
            }
            if (_timer != null && _timer.ElapsedMilliseconds > 200)
            {
                // get the amount of change between the previous point and the current one in cm.

                float delta = 0;
                if ("X".Equals(axis))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.X - _lastPoint.X)*1000;
                }
                else if ("Y".Equals(axis))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Y - _lastPoint.Y)*1000;
                }
                else if ("Z".Equals(axis))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Z - _lastPoint.Z)*1000;
                }

                _lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                // get the time elapsed between the last event in msec.
                var time = (_timer.ElapsedMilliseconds);

                // calculate the change in cm / mssec
                _rateOfChange = delta/time;

                ////Console.Out.WriteLine(rateOfChange);
                _timer.Restart();
            }


            if (_startoffset != null && _startoffset.ElapsedMilliseconds > 1000)
            {
                if (_stopTimer != null)
                {
                    _stopTimer.Start();
                    if (Math.Abs(_rateOfChange) > 0.15)
                    {
                        if (!_activityRunning)
                        {
                            _startPoint = skeleton.Joints[JointType.HandLeft].Position;
                            _activityRunning = true;
                            //Console.Out.WriteLine("Activity started");
                        }

                        _stopTimer.Reset();
                    }
                    else
                    {
                        if (_stopTimer.ElapsedMilliseconds > 1000)
                        {
                            if (_activityRunning)
                            {
                                //Console.Out.WriteLine("Activity stopped");
                                _activityRunning = false;
                                _hasStartPoint = false;
                                _addOnGesture = false;
                            }
                        }
                    }
                }

                if (_activityRunning)
                {
                    float cmChange = 0;
                    if ("X".Equals(axis))
                    {
                        cmChange = (_lastPoint.X - _startPoint.X)*100;
                    }
                    else if ("Y".Equals(axis))
                    {
                        cmChange = (_lastPoint.Y - _startPoint.Y)*100;
                    }
                    else if ("Z".Equals(axis))
                    {
                        cmChange = (_lastPoint.Z - _startPoint.Z)*100;
                    }


                    const float maxVal = 20;
                    const float minVal = -20;
                    var value = Math.Min(Math.Max(cmChange, minVal), maxVal)/maxVal;
                    if (_kinectHandler != null) _kinectHandler.OnAddOnGestureValueChange(value);
                    
                }
            }
        }

        public void RecordGesture(string gestureName, string ctxt)
        {
            if (_kinectHandler == null) return;

            _isRecognizing = false;
            _currentGesture = new Gesture(gestureName, ctxt);
            _kinectHandler.RecordingCountdownEvent(3);
            Thread.Sleep(1000);
            _kinectHandler.RecordingCountdownEvent(2);
            Thread.Sleep(1000);
            _kinectHandler.RecordingCountdownEvent(1);
            Thread.Sleep(1000);
            _kinectHandler.RecordingCountdownEvent(0);
            Thread.Sleep(1000);
            if (_seqCoords != null)
            {
                _seqCoords.Clear();
            }
            else
            {
                _seqCoords = new ArrayList();
            }
            _isRecording = true;
        }


        private void NuiSkeleton2DdataCoordReady(object sender, Skeleton2DdataCoordEventArgs a)
        {
          
                if (_kinectHandler == null || _seqCoords == null || _dtw == null) return;

                if (_seqCoords.Count > MINIMUM_FRAMES && !_isRecording && _isRecognizing)
                {
                    ////Console.Out.WriteLine("No of frames: " + seqCoords.Count);
                    if (_dtw != null)
                    {
                        var g = _dtw.Recognize(_seqCoords, _ctxt);


                        if (_recTimer != null && (g != null || _recTimer.ElapsedMilliseconds > 3000))
                        {
                            _isRecognizing = false;
                            _seqCoords.Clear();

                            
                                if (g != null)
                                    _kinectHandler.GestureRecognitionCompleted(g.Name);

                        }
                    }
                }

                if (_isRecording)
                {
                    _kinectHandler.RecordingCountdownEvent(_seqCoords.Count);
                }

                if (_seqCoords.Count > BUFFER_SIZE)
                {
                    lock (this) { 
                        if (_isRecording && _currentGesture != null)
                        {
                            _isRecording = false;

                            if (_dtw != null) _dtw.AddOrUpdate(_seqCoords, _currentGesture);
                            _seqCoords.Clear();
                            _kinectHandler.GestureRecordCompleted(_currentGesture.Name, _currentGesture.Context);
                            _currentGesture = null;
                        }
                        else
                        {
                            _seqCoords.RemoveAt(0);
                        }
                    }
                }

            if (a == null || Double.IsNaN(a.GetPoint(0).X)) return;
            // Optionally register only 1 frame out of every n
            _flipFlop = (_flipFlop + 1)%IGNORE;
            if (_flipFlop == 0)
            {
                _seqCoords.Add(a.GetCoords());
            }
        }

        internal void SetActiveUser(int skeletonID)
        {
            _activeUser = skeletonID;
        }

        internal void UpdateContextObjects()
        {
            if (_pointer != null) _pointer.SetObjects(FileLoader.LoadObj("livingRoom.obj", FileLoader.Units.cm));
        }

        public int ReturnLastContext()
        {
            if (_pointer != null) 
                return ObjectPointer.ReturnLastContext();
            return -1;
        }

        public void SetDefaultHand(bool def)
        {
            _defaultHand = def;
        }
    }
}