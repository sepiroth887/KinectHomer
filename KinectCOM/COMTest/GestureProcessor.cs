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
        private const int BufferSize = 24;

        /// <summary>
        /// The minumum number of frames in the _video buffer before we attempt to start matching gestures
        /// </summary>
        private const int MinimumFrames = 12;

        /// <summary>
        /// The minumum number of frames in the _video buffer before we attempt to start matching gestures
        /// </summary>
        private const int CaptureCountdownSeconds = 3;

        private IKinect kinectHandler;
        private KinectData kinect;
        private DtwGestureRecognizer dtw;

        private int activeUser = -1;
        private String ctxt = "__NOCONTEXT";
        private bool isRecording = false;
        private bool isRecognizing = false;
        private DateTime _lastFrameTime=DateTime.MaxValue;
        private int flipFlop;

        private ArrayList seqCoords;

        private DateTime captureCountDown = DateTime.Now;

        private String gestureName;

        private ObjectPointer pointer;

        private bool addOnGesture = false;
        private String addOnGestureType;

        public static readonly String Z_ADDON = "z";
        public static readonly String X_ADDON = "x";
        public static readonly String Y_ADDON = "y";
 

        public GestureProcessor(IKinect kinectHandler, KinectData kinect) {

            Dictionary<int, Object> contextBoxes = new Dictionary<int, Object>();
            Dictionary<int, Object> contextSpheres = new Dictionary<int, Object>();

            pointer = new ObjectPointer();

            pointer.setObjects(FileLoader.loadObj("livingRoom.obj",FileLoader.Units.cm));

            this.kinectHandler = kinectHandler;
            this.kinect = kinect;
            this.seqCoords = new ArrayList();

            kinect.attachSkeletonHandler(SkeletonDataReadyHandler);

            dtw = new DtwGestureRecognizer(12, 0.6, 2, 2, 12);
            Skeleton2DDataExtract.Skeleton2DdataCoordReady += NuiSkeleton2DdataCoordReady;
            pointer.ContextSelected+=new ObjectPointer.ContextSelectedEventHandler(pointer_ContextSelected);
        }

        private void pointer_ContextSelected(String ctxt) {
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
                    Skeleton2DDataExtract.ProcessData(skeleton);
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

        public void recognizeGesture(String ctxt) {
            isRecording = false;
            this.ctxt = ctxt;
            isRecognizing = true;
            recTimer.Reset();
            recTimer.Start();
        }

        public void stopRecognition() {
            this.isRecognizing = false;
        }

        public void storeGesture() { 
            
        }

        public void enableAddOnGesture(String type) {
            addOnGesture = true;
            addOnGestureType = type;
        }

        
        private void AddOnGesture(String type,Skeleton skel) {
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

        private void startAddonGesture(Skeleton skeleton,String axis) {
            if (!hasStartPoint)
            {
                startPoint = skeleton.Joints[JointType.HandLeft].Position;
                lastPoint = skeleton.Joints[JointType.HandLeft].Position;
                timer.Start();
                startoffset.Start();
                Console.Out.WriteLine("Timer started");
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

                //Console.Out.WriteLine(rateOfChange);
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
                        Console.Out.WriteLine("Activity started");
                    }

                    stopTimer.Reset();
                }
                else
                {
                    if (stopTimer.ElapsedMilliseconds > 1000)
                    {
                        if (activityRunning)
                        {
                            Console.Out.WriteLine("Activity stopped");
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
                    //Console.Out.WriteLine("Active change: " + value);
                }
            }
        }

        public void recordGesture(String gestureName, String ctxt) {
            //Console.Out.WriteLine("Recording gesture, starting timer");
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
            seqCoords = new ArrayList();
            isRecording = true;
            
        }

        private void NuiSkeleton2DdataCoordReady(object sender, Skeleton2DdataCoordEventArgs a){
            if (seqCoords.Count > MinimumFrames && !isRecording && isRecognizing) {

                String gesture = dtw.Recognize(seqCoords,ctxt);


                if(!gesture.Contains("__UNKNOWN") || recTimer.ElapsedMilliseconds > 3000){
                    seqCoords = new ArrayList();

                    kinectHandler.gestureRecognitionCompleted(gesture);

                    isRecognizing = false;
                }
            }

            if (isRecording) {
                kinectHandler.recordingCountdownEvent(seqCoords.Count);
            }

            if(seqCoords.Count > BufferSize){
                if(isRecording){
                   
                    dtw.AddOrUpdate(seqCoords,gestureName,ctxt);
                    seqCoords = new ArrayList();
                    kinectHandler.gestureRecordCompleted(gestureName,ctxt);
                    isRecording = false;
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
