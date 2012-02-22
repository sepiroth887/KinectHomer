using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading;
using DTWGestureRecognition;
using Kinect;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;

namespace KinectCOM
{
    internal class GestureProcessor
    {
        /// <summary>
        /// How many skeleton frames to ignore (_flipFlop)
        /// 1 = capture every frame, 2 = capture every second frame etc.
        /// </summary>
        private const int Ignore = 2;

        /// <summary>
        /// How many skeleton frames to store in the _video buffer
        /// </summary>
        private const int BufferSize = 32;

        /// <summary>
        /// The minumum number of frames in the _video buffer before we attempt to start matching gestures
        /// </summary>
        private const int MinimumFrames = 10;

        /// <summary>
        /// The minumum number of frames in the _video buffer before we attempt to start matching gestures
        /// </summary>
        private const int CaptureCountdownSeconds = 3;

        public static readonly string Z_ADDON = "z";
        public static readonly string X_ADDON = "x";
        public static readonly string Y_ADDON = "y";

        private readonly DtwGestureRecognizer dtw;
        private readonly KinectData kinect;
        private readonly IKinect kinectHandler;

        private readonly ObjectPointer pointer;
        private readonly Stopwatch recTimer = new Stopwatch();
        private readonly ArrayList seqCoords;
        private readonly Stopwatch startoffset = new Stopwatch();
        private readonly Stopwatch stopTimer = new Stopwatch();
        private readonly Stopwatch timer = new Stopwatch();
        private DateTime _lastFrameTime = DateTime.MaxValue;
        private int activeUser = -1;
        private bool activityRunning;

        private bool addOnGesture;
        private string addOnGestureType;
        private DateTime captureCountDown = DateTime.Now;
        private string ctxt = "__NOCONTEXT";
        private Gesture currentGesture;
        private int flipFlop;
        private bool hasStartPoint;
        private bool isRecognizing;
        private bool isRecording;
        private SkeletonPoint lastPoint;
        private float rateOfChange;
        private SkeletonPoint startPoint;


        public GestureProcessor(IKinect kinectHandler, KinectData kinect)
        {
            var contextBoxes = new Dictionary<int, Object>();
            var contextSpheres = new Dictionary<int, Object>();

            pointer = new ObjectPointer();

            pointer.setObjects(FileLoader.loadObj("livingRoom.obj", FileLoader.Units.cm));

            this.kinectHandler = kinectHandler;
            this.kinect = kinect;
            seqCoords = new ArrayList();

            kinect.attachSkeletonHandler(SkeletonDataReadyHandler);

            dtw = new DtwGestureRecognizer(12, 0.6, 2, 2, 10);
            Skeleton2DDataExtract.Skeleton2DdataCoordReady += NuiSkeleton2DdataCoordReady;
            pointer.ContextSelected += pointer_ContextSelected;
        }

        private void pointer_ContextSelected(string ctxt)
        {
            kinectHandler.contextSelected(ctxt);
        }

        private void SkeletonDataReadyHandler(object sender, SkeletonFrameReadyEventArgs e)
        {
            // no active user, ignore events
            if (activeUser == -1) return;
            // not recording and no context, ignore events.
            SkeletonFrame sFrame = e.OpenSkeletonFrame();
            if (sFrame == null) return;
            var skeletons = new Skeleton[kinect.GetSensor().SkeletonStream.FrameSkeletonArrayLength];
            sFrame.CopySkeletonDataTo(skeletons);
            sFrame.Dispose();
            foreach (Skeleton skeleton in skeletons)
            {
                if (skeleton.TrackingId == activeUser)
                {
                    Skeleton2DDataExtract.ProcessData(skeleton);
                    if (!isRecording)
                        pointer.findContext(skeleton);
                    if (addOnGesture)
                    {
                        AddOnGesture(addOnGestureType, skeleton);
                    }
                }
            }
        }


        public Vector3 getHandPos()
        {
            return pointer.getHandPos();
        }


        public void recognizeGesture(string ctxt)
        {
            isRecording = false;
            this.ctxt = ctxt;
            seqCoords.Clear();
            isRecognizing = true;
            recTimer.Reset();
            recTimer.Start();
        }

        public void stopRecognition()
        {
            isRecognizing = false;
        }

        public void storeGestures()
        {
            FileLoader.saveGestures(dtw.RetrieveText());
        }

        public string[] loadGestures()
        {
            return FileLoader.loadGestures(dtw);
        }

        public void enableAddOnGesture(string type)
        {
            addOnGesture = true;
            addOnGestureType = type;
        }


        private void AddOnGesture(string type, Skeleton skel)
        {
            if (skel.Joints[JointType.HandLeft].TrackingState == JointTrackingState.NotTracked)
            {
                return;
            }

            if (type.Equals(X_ADDON))
            {
                startAddonGesture(skel, "X");
            }
            else if (type.Equals(Y_ADDON))
            {
                startAddonGesture(skel, "Y");
            }
            else if (type.Equals(Z_ADDON))
            {
                startAddonGesture(skel, "Z");
            }
        }


        private void startAddonGesture(Skeleton skeleton, string axis)
        {
            if (!hasStartPoint)
            {
                startPoint = skeleton.Joints[JointType.HandLeft].Position;
                lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                timer.Start();
                startoffset.Start();
                //Console.Out.WriteLine("Timer started");
                hasStartPoint = true;
            }
            if (timer.ElapsedMilliseconds > 200)
            {
                // get the amount of change between the previous point and the current one in cm.

                float delta = 0;
                if (axis.Contains("X"))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.X - lastPoint.X)*1000;
                }
                else if (axis.Contains("Y"))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Y - lastPoint.Y)*1000;
                }
                else if (axis.Contains("Z"))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Z - lastPoint.Z)*1000;
                }

                lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                // get the time elapsed between the last event in msec.
                long time = (timer.ElapsedMilliseconds);

                // calculate the change in cm / mssec
                rateOfChange = delta/time;

                ////Console.Out.WriteLine(rateOfChange);
                timer.Restart();
            }


            if (startoffset.ElapsedMilliseconds > 1000)
            {
                stopTimer.Start();
                if (Math.Abs(rateOfChange) > 0.15)
                {
                    if (!activityRunning)
                    {
                        startPoint = skeleton.Joints[JointType.HandLeft].Position;
                        activityRunning = true;
                        //Console.Out.WriteLine("Activity started");
                    }

                    stopTimer.Reset();
                }
                else
                {
                    if (stopTimer.ElapsedMilliseconds > 1000)
                    {
                        if (activityRunning)
                        {
                            //Console.Out.WriteLine("Activity stopped");
                            activityRunning = false;
                            hasStartPoint = false;
                            addOnGesture = false;
                        }
                    }
                }

                if (activityRunning)
                {
                    float cmChange = 0;
                    if (axis.Contains("X"))
                    {
                        cmChange = (lastPoint.X - startPoint.X)*100;
                    }
                    else if (axis.Contains("Y"))
                    {
                        cmChange = (lastPoint.Y - startPoint.Y)*100;
                    }
                    else if (axis.Contains("Z"))
                    {
                        cmChange = (lastPoint.Z - startPoint.Z)*100;
                    }


                    float maxVal = 20;
                    float minVal = -20;
                    float value = Math.Min(Math.Max(cmChange, minVal), maxVal)/maxVal;
                    kinectHandler.onAddOnGestureValueChange(value);
                    ////Console.Out.WriteLine("Active change: " + value);
                }
            }
        }

        public void recordGesture(string gestureName, string ctxt)
        {
            ////Console.Out.WriteLine("Recording gesture, starting timer");
            isRecognizing = false;
            currentGesture = new Gesture(gestureName, ctxt);
            kinectHandler.recordingCountdownEvent(3);
            Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(2);
            Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(1);
            Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(0);
            Thread.Sleep(1000);
            seqCoords.Clear();
            isRecording = true;
        }


        private void NuiSkeleton2DdataCoordReady(object sender, Skeleton2DdataCoordEventArgs a)
        {
            if (seqCoords.Count > MinimumFrames && !isRecording && isRecognizing)
            {
                ////Console.Out.WriteLine("No of frames: " + seqCoords.Count);
                Gesture g = dtw.Recognize(seqCoords, ctxt);


                if (g != null || recTimer.ElapsedMilliseconds > 3000)
                {
                    seqCoords.Clear();

                    kinectHandler.gestureRecognitionCompleted(g.Name);

                    isRecognizing = false;
                }
            }

            if (isRecording)
            {
                kinectHandler.recordingCountdownEvent(seqCoords.Count);
            }

            if (seqCoords.Count > BufferSize)
            {
                if (isRecording && currentGesture != null)
                {
                    isRecording = false;
                    dtw.AddOrUpdate(seqCoords, currentGesture);
                    seqCoords.Clear();
                    kinectHandler.gestureRecordCompleted(currentGesture.Name, currentGesture.Context);
                    currentGesture = null;
                }
                else
                {
                    seqCoords.RemoveAt(0);
                }
            }

            if (!double.IsNaN(a.GetPoint(0).X))
            {
                // Optionally register only 1 frame out of every n
                flipFlop = (flipFlop + 1)%Ignore;
                if (flipFlop == 0)
                {
                    seqCoords.Add(a.GetCoords());
                }
            }
        }

        internal void setActiveUser(int skeletonID)
        {
            activeUser = skeletonID;
        }

        internal void updateContextObjects()
        {
            pointer.setObjects(FileLoader.loadObj("livingRoom.obj", FileLoader.Units.cm));
        }

        public int returnLastContext()
        {
            return pointer.returnLastContext();
        }
    }
}