using Emgu.CV;
using Emgu.CV.Structure;

namespace Kinect
{
    public class UserFeature
    {
        public UserFeature()
        {
            HipHeight = 0;
            ShoulderWidth = 0;
            ArmLength = 0;
        }

        public UserFeature(double hHeight, double sWidth, double aLength)
        {
            HipHeight = hHeight;
            ShoulderWidth = sWidth;
            ArmLength = aLength;
        }

        public Image<Gray, byte> curImage { get; set; }

        public double HipHeight { get; set; }

        public double ShoulderWidth { get; set; }

        public double ArmLength { get; set; }

        public string Name { get; set; }

        public float Confidence { get; set; }

        public float FaceConfidence { get; set; }
    }
}