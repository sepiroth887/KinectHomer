using System.Collections;
using System.Collections.Generic;
using Kinect;
using Microsoft.Kinect;

namespace KinectCOM
{
    public interface IKinect
    {
        void Init();
        void Uninit();
        void UpdateFace();
        void UpdateSkeletons(Dictionary<JointType, ColorImagePoint> points, ArrayList users);
        void LearnUser(int skeletonID);
        bool StartTracking(int skeletonID);
        void StopTracking(int skeletonID);
        void UserDetected(UserFeature user);
        void PresenceDetected(int skeletonID);
        void UserLost(UserFeature user);
        void PresenceLost(int skeletonID);

        void GestureRecordCompleted(string gestureName, string ctxt);

        void RecordingCountdownEvent(int p);

        void GestureRecognitionCompleted(string gesture);

        void RecordGesture(string gestureName, string ctxt);

        void RecognizeGesture(string ctxt);

        void StopRecGesture();

        void OnAddOnGestureValueChange(float value);

        void ContextSelected(string ctxt);

        void StoreGestures();

        IEnumerable<string> LoadGestures();
    }
}