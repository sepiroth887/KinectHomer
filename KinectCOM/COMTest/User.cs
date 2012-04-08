using Microsoft.Kinect;

namespace KinectCOM
{
    public class User
    {
        public const int MAX_ATTEMPTS = 10;

        public User()
        {
            HipHeight = 0;
            ShoulderWidth = 0;
            ArmLength = 0;
            IsActive = false;
            Attempts = 0;
        }

        public double HipHeight { get; set; }

        public double ShoulderWidth { get; set; }

        public double ArmLength { get; set; }

        public string Name { get; set; }

        public float Confidence { get; set; }

        public float FaceConfidence { get; set; }

        public int TrackingID { get; set; }

        public bool IsActive { get; set; }

        public int Attempts { get; set; }

        public Skeleton Skeleton { get; set; }

    }
}