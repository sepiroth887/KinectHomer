using System;
using System.Linq;
using System.Runtime.InteropServices;

namespace KinectCOM
{
    public delegate void OnPresenceDetectedDel(int skeletonID);

    public delegate void OnPresenceLostDel(int skeletonID);

    public delegate void OnGestureRecordCompletedDel(string gestureName, string ctxt);

    public delegate void OnGestureRecognitionCompletedDel(string gestureName);

    public delegate void OnRecordingCountDownEventDel(int time);

    public delegate void OnContextSelectedDel(string ctxt);

    public delegate void OnVoiceCommandDetectedDel(string command);

    public delegate void OnAddonGestureValueChangeDel(float value);

    public delegate void OnUserFoundDel(string user, float confidence, int skeletonID);

    public delegate void OnUserLostDel(string user);

    // Declare COM Event interfaces for the Processors (Recognition, Speech, Gesture)
    [InterfaceType(ComInterfaceType.InterfaceIsIDispatch)]
    public interface IUserEvents
    {
        [DispId(1)]
        void OnPresenceDetected(int skeletonID);

        [DispId(2)]
        void OnUserFound(string user, float confidence, int skeletonID);

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
    public interface IDevice
    {
        [DispId(8)]
        void UserRecognition(bool on);

        [DispId(9)]
        void SetContext(string contextID);

        [DispId(10)]
        void SpeechRecognition(bool on);

        [DispId(11)]
        Boolean LearnUser(string name, int skeletonID);

        [DispId(12)]
        Boolean UpdateUser(string name, int skeletonID);

        [DispId(13)]
        Boolean SetUserToSkeleton(string name, int skeletonID);

        [DispId(14)]
        void IncAngle();

        [DispId(15)]
        void DecAngle();

        [DispId(16)]
        bool StartTracking(int skeletonID);

        [DispId(17)]
        void RecordGesture(string gestureName, string ctxt);

        [DispId(18)]
        void RecognizeGesture(string ctxt);

        [DispId(19)]
        void StopGestureRecognition();

        [DispId(20)]
        void StoreGestures();

        [DispId(21)]
        string LoadGestures();

        [DispId(42)]
        Boolean Init();

        [DispId(43)]
        void Uninit();
    }

    [ClassInterface(ClassInterfaceType.None)]
    [ProgId("KinectCOM.Device")]
    [ComSourceInterfaces(typeof (IUserEvents))]
    public abstract class Device : IDevice
    {
        private IKinect _kHandler;
        private KinectData _kinect;

        #region _Device Members

        Boolean IDevice.Init()
        {
            _kinect = new KinectData(0);
            _kHandler = new KinectHandler(_kinect, this);

            _kHandler.Init();
            return true;
        }

        void IDevice. Uninit()
        {
            if (_kHandler != null) _kHandler.Uninit();
        }

        public bool SetUserToSkeleton(string name, int skelID)
        {
            return true;
        }

        public bool UpdateUser(string name, int skelID)
        {
            return false;
        }

        public bool LearnUser(string name, int skelID)
        {
            return false;
        }

        void IDevice. SpeechRecognition(bool on)
        {
        }

        void IDevice. SetContext(string ctxt)
        {
        }

        void IDevice. UserRecognition(bool on)
        {
        }

        void IDevice. IncAngle()
        {
            if (_kinect != null) _kinect.IncAngle();
        }

        void IDevice. DecAngle()
        {
            if (_kinect != null) _kinect.DecAngle();
        }

        public bool StartTracking(int skeletonID)
        {
            return _kHandler != null && _kHandler.StartTracking(skeletonID);
        }

        void IDevice. RecordGesture(string gestureName, string ctxt)
        {
            if (_kHandler != null) _kHandler.RecordGesture(gestureName, ctxt);
        }

        void IDevice. RecognizeGesture(string ctxt)
        {
            if (_kHandler != null) _kHandler.RecognizeGesture(ctxt);
        }


        void IDevice. StopGestureRecognition()
        {
            if (_kHandler != null) _kHandler.StopRecGesture();
        }

        public void StoreGestures()
        {
            if (_kHandler != null) _kHandler.StoreGestures();
        }

        public string LoadGestures()
        {
            if (_kHandler != null)
            {
                var arr = _kHandler.LoadGestures() as string[];

                if (arr == null) return "";

                return arr.Aggregate("", (current, gesture) => current + (gesture + "\n"));
            }

            return "";
        }

        #endregion

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

        public void PresenceDetected(int newUser)
        {
            OnPresenceDetected(newUser);
        }


        public void PresenceLost(int skeletonID)
        {
            OnPresenceLost(skeletonID);
        }

        public void GestureRecordCompleted(string gestureName, string ctxt)
        {
            OnGestureRecordCompleted(gestureName, ctxt);
        }

        public void RecordingCountDownEvent(int p)
        {
            OnRecordingCountDownEvent(p);
        }

        public void GestureRecognitionCompleted(string gesture)
        {
            OnGestureRecognitionCompleted(gesture);
        }


        public void onContextSelected(string p)
        {
            OnContextSelected(p);
        }

        public void onVoiceCommandDetected(string command)
        {
            OnVoiceCommandDetected(command);
        }

        public void onAddonGestureValueChange(float value)
        {
            OnAddonGestureValueChange(value);
        }

        public void UserFound(string name, float confidence, int skeletonID)
        {
            OnUserFound(name, confidence, skeletonID);
        }

        public void UserLost(string name)
        {
            OnUserLost(name);
        }
    }
}