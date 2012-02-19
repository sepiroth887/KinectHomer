using System;
using System.Collections.Generic;
using System.Text;
using DTWGestureRecognition;
using System.Collections;
using System.Windows.Forms;
using System.Diagnostics;
using Microsoft.Xna.Framework;
using Microsoft.Kinect;
using Kinect;
namespace KinectCOM
{
    class GestureProcessor
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

        private IKinect kinectHandler;
        private KinectData kinect;
        private DtwGestureRecognizer dtw;

        private int activeUser = -1;
        private string ctxt = "__NOCONTEXT";
        private bool isRecording = false;
        private bool isRecognizing = false;
        private DateTime _lastFrameTime=DateTime.MaxValue;
        private int flipFlop;

        private ArrayList seqCoords;

        private DateTime captureCountDown = DateTime.Now;

        private string gestureName;

        private ObjectPointer pointer;

        private bool addOnGesture = false;
        private string addOnGestureType;

        public static readonly string Z_ADDON = "z";
        public static readonly string X_ADDON = "x";
        public static readonly string Y_ADDON = "y";
 

        public GestureProcessor(IKinect kinectHandler, KinectData kinect) {

            pointer = new ObjectPointer();

            pointer.setObjects(FileLoader.loadObj("livingRoom.obj",FileLoader.Units.cm));

            this.kinectHandler = kinectHandler;
            this.kinect = kinect;
            this.seqCoords = new ArrayList();

            kinect.attachSkeletonHandler(SkeletonDataReadyHandler);

            dtw = new DtwGestureRecognizer(12, 0.8, 2,2,10);
            Skeleton2DDataExtract.Skeleton2DdataCoordReady += NuiSkeleton2DdataCoordReady;
            pointer.ContextSelected+=new ObjectPointer.ContextSelectedEventHandler(pointer_ContextSelected);
        }

        private void pointer_ContextSelected(string ctxt) {
            kinectHandler.contextSelected(ctxt);
        }

        private void SkeletonDataReadyHandler(object sender, SkeletonFrameReadyEventArgs e){

            // no active user, ignore events
            if (activeUser == -1) return;
            // not recording and no context, ignore events.
            SkeletonFrame sFrame = e.OpenSkeletonFrame();
            Skeleton[] skeletons = new Skeleton[kinect.getSensor().SkeletonStream.FrameSkeletonArrayLength];
            sFrame.CopySkeletonDataTo(skeletons);
            foreach (Skeleton skeleton in skeletons) {
                if (skeleton.TrackingId == activeUser) {
                    if(isRecognizing || isRecording){
                        //Console.Out.WriteLine("Recording or Recognizing");
                        Skeleton2DDataExtract.ProcessData(skeleton);
                    }
                    if(!isRecording)
                        pointer.findContext(skeleton);
                    if (addOnGesture) {
                        AddOnGesture(addOnGestureType, skeleton);
                    }
                }
            }

        }


        public Vector3 getHandPos()
        {
            return pointer.getHandPos();
        }

       

        private Stopwatch recTimer = new Stopwatch(); 

        public void recognizeGesture(string ctxt) {
            isRecording = false;
            this.ctxt = ctxt;
            seqCoords.Clear();
            isRecognizing = true;
            recTimer.Reset();
            recTimer.Start();
        }

        public void stopRecognition() {
            this.isRecognizing = false;
        }

        public void storeGestures() {
            FileLoader.saveGestures(dtw.RetrieveText());
        }

        public string[] loadGestures() {
            return FileLoader.loadGestures(dtw);
        }

        public void enableAddOnGesture(string type) {
            addOnGesture = true;
            addOnGestureType = type;
        }

        
        private void AddOnGesture(string type,Skeleton skel) {
            if (skel.Joints[JointType.HandLeft].TrackingState == JointTrackingState.NotTracked) { return; }

            if (type.Equals(GestureProcessor.X_ADDON)) {
                this.startAddonGesture(skel,"X");
            }
            else if (type.Equals(GestureProcessor.Y_ADDON))
            {
                this.startAddonGesture(skel, "Y");
            }
            else if(type.Equals(GestureProcessor.Z_ADDON)){
                this.startAddonGesture(skel, "Z");
            }
        }


        private bool hasStartPoint = false;
        private SkeletonPoint startPoint = new SkeletonPoint();
        private SkeletonPoint lastPoint = new SkeletonPoint();
        private Stopwatch startoffset = new Stopwatch();
        private Stopwatch stopTimer = new Stopwatch();
        private Stopwatch timer = new Stopwatch();
        private float rateOfChange;
        private bool activityRunning = false;

        private void startAddonGesture(Skeleton skeleton,string axis) {
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
                if(axis.Contains("X")){
                    delta = (skeleton.Joints[JointType.HandLeft].Position.X - lastPoint.X) * 1000;
                }
                else if (axis.Contains("Y"))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Y - lastPoint.Y) * 1000;
                }
                else if (axis.Contains("Z"))
                {
                    delta = (skeleton.Joints[JointType.HandLeft].Position.Z - lastPoint.Z) * 1000;
                }

                lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                // get the time elapsed between the last event in msec.
                long time = (timer.ElapsedMilliseconds);

                // calculate the change in cm / mssec
                rateOfChange = delta / time;

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
                        cmChange = (lastPoint.X - startPoint.X) * 100;
                    }else if (axis.Contains("Y"))
                    {
                        cmChange = (lastPoint.Y - startPoint.Y) * 100;
                    }else if (axis.Contains("Z"))
                    {
                        cmChange = (lastPoint.Z - startPoint.Z) * 100;
                    }
                    
                   
                    float maxVal = 20;
                    float minVal = -20;
                    float value = Math.Min(Math.Max(cmChange, minVal), maxVal) / maxVal;
                    kinectHandler.onAddOnGestureValueChange(value);
                    ////Console.Out.WriteLine("Active change: " + value);
                }
            }
        }

        public void recordGesture(string gestureName, string ctxt) {
            ////Console.Out.WriteLine("Recording gesture, starting timer");
            this.gestureName = gestureName;
            this.ctxt = ctxt;
            this.isRecognizing = false;
            captureCountDown = DateTime.Now.AddSeconds(3);
           
            kinectHandler.recordingCountdownEvent(3);
            System.Threading.Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(2);
            System.Threading.Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(1);
            System.Threading.Thread.Sleep(1000);
            kinectHandler.recordingCountdownEvent(0);
            System.Threading.Thread.Sleep(1000);
            seqCoords.Clear();
            isRecording = true;
            
        }

        private void NuiSkeleton2DdataCoordReady(object sender, Skeleton2DdataCoordEventArgs a){
            
            if (seqCoords.Count > MinimumFrames && !isRecording && isRecognizing) {
                ////Console.Out.WriteLine("No of frames: " + seqCoords.Count);
                string gesture = dtw.Recognize(seqCoords,ctxt);


                if(!gesture.Contains("__UNKNOWN") || recTimer.ElapsedMilliseconds > 3000){
                    seqCoords.Clear();

                    kinectHandler.gestureRecognitionCompleted(gesture);
                    isRecognizing = false;
                }
            }

            if (isRecording) {
                kinectHandler.recordingCountdownEvent(seqCoords.Count);
            }

            if(seqCoords.Count > BufferSize){
                if(isRecording){
                    isRecording = false;
                    dtw.AddOrUpdate(seqCoords,gestureName,ctxt);
                    //seqCoords.Clear();
                    kinectHandler.gestureRecordCompleted(gestureName,ctxt);
                }else{
                    seqCoords.RemoveAt(0);
                }
            }

            if(!double.IsNaN(a.GetPoint(0).X)){
                // Optionally register only 1 frame out of every n
                flipFlop = (flipFlop + 1) % Ignore;
                if (flipFlop == 0)
                {
                    seqCoords.Add(a.GetCoords());
                } 
            }


        }

        internal void setActiveUser(int skeletonID)
        {
            this.activeUser = skeletonID;
        }

        internal void updateContextObjects()
        {
            pointer.setObjects(FileLoader.loadObj("livingRoom.obj",FileLoader.Units.cm));

        }

        public int returnLastContext() {
            return pointer.returnLastContext();
        }
    }
}
