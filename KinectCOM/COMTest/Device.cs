using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;
using Kinect;
using System.Collections;

namespace KinectCOM
{

    public delegate void OnPresenceDetectedDel(int skeletonID);
    public delegate void OnPresenceLostDel(int skeletonID);
    public delegate void OnGestureRecordCompletedDel(string gestureName,string ctxt);
    public delegate void OnGestureRecognitionCompletedDel(string gestureName);
    public delegate void OnRecordingCountDownEventDel(int time); 
    public delegate void OnContextSelectedDel(string ctxt);
    public delegate void OnVoiceCommandDetectedDel(string command);
    public delegate void OnAddonGestureValueChangeDel(float value);
    public delegate void OnUserFoundDel(string user,float confidence,int skeletonID);
    public delegate void OnUserLostDel(string user);
    // Declare COM Event interfaces for the Processors (Recognition, Speech, Gesture)
    [InterfaceTypeAttribute(ComInterfaceType.InterfaceIsIDispatch)]
    public interface IUserEvents {
        [DispId(1)]
        void OnPresenceDetected(int skeletonID);
        [DispId(2)]
        void OnUserFound(string user,float confidence,int skeletonID);
        [DispId(3)]
        void OnUserLost(string user);
        [DispId(4)]
        void OnPresenceLost(int skeletonID);
        [DispId(6)]
        void OnGestureRecognitionCompleted(string gestureName);
        [DispId(7)]
        void OnGestureRecordCompleted(string gestureName, string ctxt);
        [DispId(8)]
        void OnRecordingCountDownEvent(int time);
        [DispId(9)]
        void OnContextSelected(string ctxt);
        [DispId(10)]
        void OnVoiceCommandDetected(string command);
        [DispId(11)]
        void OnAddonGestureValueChange(float value);
    }

    //Declare COM method interface for instruction methods
    [InterfaceType(ComInterfaceType.InterfaceIsIDispatch)]
    public interface _Device {
        [DispId(8)]
        void userRecognition(bool on);
        [DispId(9)]
        void setContext(string contextID);
        [DispId(10)]
        void speechRecognition(bool on);
        [DispId(11)]
        Boolean learnUser(string name, int skeletonID);
        [DispId(12)]
        Boolean updateUser(string name, int skeletonID);
        [DispId(13)]
        Boolean setUserToSkeleton(string name, int skeletonID);
        [DispId(14)]
        void incAngle();
        [DispId(15)]
        void decAngle();
        [DispId(16)]
        bool startTracking(int skeletonID);
        [DispId(17)]
        void recordGesture(string gestureName, string ctxt);
        [DispId(18)]
        void recognizeGesture(string ctxt);
        [DispId(19)]
        void stopGestureRecognition();
        [DispId(20)]
        void storeGestures();
        [DispId(21)]
        string loadGestures();
        [DispId(42)]
        Boolean init();
        [DispId(43)]
        void uninit();
    }

    [ClassInterface(ClassInterfaceType.None)]
    [ProgId("KinectCOM.Device")]
    [ComSourceInterfaces(typeof(IUserEvents))]
    public class Device : _Device
    {

        private IKinect kHandler;
        private KinectData kinect;
        public event OnPresenceDetectedDel OnPresenceDetected;
        public event OnPresenceLostDel OnPresenceLost;
        public event OnGestureRecordCompletedDel OnGestureRecordCompleted;
        public event OnGestureRecognitionCompletedDel OnGestureRecognitionCompleted;
        public event OnRecordingCountDownEventDel OnRecordingCountDownEvent;
        public event OnContextSelectedDel OnContextSelected;
        public event OnVoiceCommandDetectedDel OnVoiceCommandDetected;
        public event OnAddonGestureValueChangeDel OnAddonGestureValueChange;
        public event OnUserLostDel OnUserLost;
        public event OnUserFoundDel OnUserFound;
  
        public Device(){
            
        }


        public Boolean init() {
            kinect = new KinectData();
            kHandler = new KinectHandler(kinect,this);

            kHandler.init();
            return true;
        }

        public void uninit() {
            kHandler.uninit();
        }

        public bool setUserToSkeleton(string name, int skelID) { return true; }

        public bool updateUser(string name, int skelID) { return false; }

        public bool learnUser(string name, int skelID) { return false; }

        public void speechRecognition(bool on) { }

        public void setContext(string ctxt) { 
        
        }

        public void userRecognition(bool on) { }

        public void incAngle()
        {
            kinect.incAngle();
        }

        public void decAngle()
        {
            kinect.decAngle();
        }

        public bool startTracking(int skeletonID) {
            return kHandler.startTracking(skeletonID);
        }

        public void presenceDetected(int newUser)
        {
            OnPresenceDetected(newUser);
        }


        public void presenceLost(int skeletonID)
        {
            OnPresenceLost(skeletonID);
        }

        public void gestureRecordCompleted(string gestureName, string ctxt)
        {
            OnGestureRecordCompleted(gestureName, ctxt);
        }

        public void recordingCountDownEvent(int p)
        {
            OnRecordingCountDownEvent(p);
        }

        public void gestureRecognitionCompleted(string gesture)
        {
            OnGestureRecognitionCompleted(gesture);
        }


        public void recordGesture(string gestureName, string ctxt)
        {
            kHandler.recordGesture(gestureName, ctxt);
        }

        public void recognizeGesture(string ctxt)
        {
            kHandler.recognizeGesture(ctxt);
        }


        public void stopGestureRecognition()
        {
            kHandler.stopRecGesture();
        }

        public void onContextSelected(string p)
        {
            OnContextSelected(p);
        }

        public void onVoiceCommandDetected(string command) {
            OnVoiceCommandDetected(command);
        }

        public void onAddonGestureValueChange(float value) {
            OnAddonGestureValueChange(value);
        }

        public void userFound(string name, float confidence,int skeletonID) {
            OnUserFound(name, confidence,skeletonID);
        }

        public void userLost(string name) {
            OnUserLost(name);
        }


        public void storeGestures()
        {
            kHandler.storeGestures();
        }

        public string loadGestures()
        {
            string outStr = string.Empty;

            string[] arr = kHandler.loadGestures();

            if (arr == null) return "";

            foreach (string gesture in arr) {
                outStr += gesture+"\n";
            }

            return outStr;
        }
    }
}
