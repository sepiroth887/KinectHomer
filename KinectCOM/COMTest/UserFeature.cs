using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;

namespace Kinect
{
    public class UserFeature
    {
        private double hipHeight;
        private double shoulderWidth;
        private double armLength;
        private string name;
        private float faceConfidence;
        private float overallConfidence;
        private Image<Gray, byte> currentImage;

        public UserFeature() {
            hipHeight = 0;
            shoulderWidth = 0;
            armLength = 0;
        }

        public Image<Gray, byte> curImage {
            get { return currentImage; }
            set { currentImage = value; }
        }

        public UserFeature(double hHeight,double sWidth,double aLength) {
            this.hipHeight = hHeight;
            this.shoulderWidth = sWidth;
            this.armLength = aLength;
        }

        public double HipHeight {
            get { return hipHeight; }
            set { hipHeight = value; }
        }

        public double ShoulderWidth
        {
            get { return shoulderWidth; }
            set { shoulderWidth = value; }
        }

        public double ArmLength
        {
            get { return armLength; }
            set { armLength = value; }
        }

        public string Name {
            get { return name; }
            set { name = value; }
        }

        public float Confidence {
            get { return overallConfidence; }
            set { overallConfidence = value; }
        }

        public float FaceConfidence {
            get { return faceConfidence; }
            set { faceConfidence = value; }
        }
    }
}
