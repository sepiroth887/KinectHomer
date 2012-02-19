using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Collections;
using Kinect;
using Microsoft.Kinect;

namespace KinectCOM
{
    interface IKinect
    {
        void init();
        void uninit();
        void updateFace();
        void updateSkeletons(Dictionary<JointType, ColorImagePoint> points, ArrayList users);
        void learnUser(int skeletonID);
        bool startTracking(int skeletonID);
        void stopTracking(int skeletonID);
        void userDetected(UserFeature user);
        void presenceDetected(int skeletonID);
        void userLost(UserFeature user);
        void presenceLost(int skeletonID);

        void gestureRecordCompleted(string gestureName, string ctxt);

        void recordingCountdownEvent(int p);

        void gestureRecognitionCompleted(string gesture);

        void recordGesture(string gestureName, string ctxt);

        void recognizeGesture(string ctxt);

        void stopRecGesture();

        void onAddOnGestureValueChange(float value);

        void contextSelected(string ctxt);

        void storeGestures();

        string[] loadGestures();
    }
}
