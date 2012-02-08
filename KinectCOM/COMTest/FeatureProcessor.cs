using System;
using System.Collections;
using System.Linq;
using System.Text;
using Microsoft.Kinect;
using System.Drawing;
using System.Collections.Generic;
using System.Globalization;
using System.Windows;

namespace KinectCOM
{
    class FeatureProcessor
    {
        private KinectData kinect; // refernce to Kinect manager
        private bool isProcessing = false; // indicates whether the processor is active
        private int activeUser = -1; // current actively tracked user
        private ArrayList users = new ArrayList(); // list of user ids that are in the FOV
        private Dictionary<JointType, ColorImagePoint> jointPoints = new Dictionary<JointType, ColorImagePoint>(); // Dictionary of joints for the actively tracked user.
        private long skeletonFrameTime = 0; // time of the last frame.
        private SkeletonPoint activeUserHeadPos; // location of the active users head
        private IKinect handler;
        private ArrayList shoulderVals = new ArrayList(); // list of last 20 shoulder length values.
        private ArrayList armVals = new ArrayList(); // list of last 20 arm length values.
        private ArrayList headVals = new ArrayList(); // list of last 20 hip to head height values.


        /// <summary>
        /// Constructor. Assigns parameters to local fields
        /// </summary>
        /// <param name="kinect">Kinect Data object</param>
        /// <param name="p">GUI object</param>
        public FeatureProcessor(KinectData kinect,IKinect kinectHandler)
        {
            this.kinect = kinect;
            this.handler = kinectHandler;

        }

        /// <summary>
        /// init opens the depth stream and attaches a skeletonFrame handler.
        /// </summary>
        public void init()
        {
            kinect.attachSkeletonHandler(SkeletonImageReady);
        }

        /// <summary>
        /// starts feature tracking
        /// </summary>
        public void startProcess()
        {
            isProcessing = true;
        }

        /// <summary>
        /// stops feature tracking
        /// </summary>
        public void stopProcess()
        {
            isProcessing = false;
        }

        /// <summary>
        /// sets the active user to be tracked.
        /// </summary>
        /// <param name="uid"></param>
        public void setActiveUser(int uid)
        {
            activeUser = uid;
        }

        /// <summary>
        /// returns the timestamp of the last skeleton frame
        /// </summary>
        /// <returns></returns>
        public long getFrameNumber()
        {
            return skeletonFrameTime;
        }

        /// <summary>
        /// gets the active users head position as a SkeletonPoint.
        /// </summary>
        /// <returns></returns>
        public SkeletonPoint getUserHeadPos() {
            return activeUserHeadPos;
        }

        /// <summary>
        /// Event handler activated when a skeletonFrame is ready. Returns immediately if the processor is
        /// no started. The frame is retrieved and for each skeleton in the frame the user id is stored. 
        /// If an active user has been selected, its skeleton is parsed for joints and the  is updated.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        /// 
        private Skeleton[] skeletonData;
        public SkeletonFrame skeletonFrame; 
        public void SkeletonImageReady(object sender, SkeletonFrameReadyEventArgs e) {
            this.skeletonData = new Skeleton[kinect.getSensor().SkeletonStream.FrameSkeletonArrayLength];

            if (!isProcessing) { return; }
            // retrieve the frame
            skeletonFrame = e.OpenSkeletonFrame();
            skeletonFrame.CopySkeletonDataTo(skeletonData);

            // and the time it has been created.
            skeletonFrameTime = skeletonFrame.Timestamp;

            //Logger.logIt("[FeatureProcessor]Skeletons in frame: " + skeletons.Length);
    
            // clear both, the last users stored, and the active users joints.
            users.Clear();
            jointPoints.Clear();

            

            // loop through all skeletons and add the TrackingID to the users arraylist.
            foreach (Skeleton skeleton in skeletonData) { 
                
                if(!skeleton.TrackingState.Equals(SkeletonTrackingState.NotTracked)){
                     users.Add(skeleton.TrackingId);
                     //Console.Out.WriteLine("Tracked user found: " + skeleton.TrackingID);
                }

                // if no active user has been selected, skip the remainder.
                if (!users.Contains(activeUser)) { continue; }

                // if an active user is present
                if (activeUser == skeleton.TrackingId) {

                    // loop through each joint and if its tracked 
                    foreach (Joint joint in skeleton.Joints)
                    {
                        if(joint.Equals(JointTrackingState.Tracked) ||
                            joint.TrackingState.Equals(JointTrackingState.Inferred)){
                            // get the location as a point fitting on the GUI picturebox for the skeleton
                            ColorImagePoint p = getDisplayPosition(joint.Position);
                            // and add it to the list of jointpoints
                            jointPoints.Add(joint.JointType,p);

                            //Console.WriteLine("Joint: "+joint.ID+" position: "+p.X+"|"+p.Y);
                        }
                    }

                    // store the current position of the active users head
                    activeUserHeadPos = skeleton.Joints[JointType.Head].Position;

                    // and try to find the features of this skeleton for later processing.
                    this.findFeatures(skeleton);
                }

                
            }

            // update the  with the retrieved information
           
            handler.updateSkeletons(jointPoints, users);
        }

        /// <summary>
        /// Calculates averages of the stored features for the active user.
        /// </summary>
        /// <param name="skelID"></param>
        /// <returns>Dictionary of the body features</returns>
        public Dictionary<FeatureType, String> getFeatures(int skelID)
        {
            // the passed userID does not match the currently tracked user so stop working.
            if (activeUser != skelID) {
                return null;
            }

            // add all the shoulder values and calculate the average.
            double avgShoulder = 0;

            if (shoulderVals.Count != 0)
            {
                foreach (double value in shoulderVals)
                {
                    avgShoulder += value;
                }

                avgShoulder = avgShoulder / shoulderVals.Count;
            }

            // add all the arm values and calculate the average.
            double avgArm = 0;

            if (armVals.Count != 0)
            {
                foreach (double value in armVals)
                {
                    avgArm += value;
                }

                avgArm = avgArm / armVals.Count;
            }

            // add all the head values and calculate the average.
            double avgHead = 0;

            if (headVals.Count != 0)
            {
                foreach (double value in headVals)
                {
                    avgHead += value;
                }

                avgHead = avgHead / headVals.Count;
            }

            // store all average values in a dictionary of featureTypes.
            Dictionary<FeatureType, String> vals = new Dictionary<FeatureType, String>();

            vals.Add(FeatureType.ShoulderWidth, avgShoulder.ToString(CultureInfo.CreateSpecificCulture("en-GB")));
            vals.Add(FeatureType.ArmLength, avgArm.ToString(CultureInfo.CreateSpecificCulture("en-GB")));
            vals.Add(FeatureType.HipHeadHeight, avgHead.ToString(CultureInfo.CreateSpecificCulture("en-GB")));

            return vals;
        }

        
        /// <summary>
        /// Progresses through the skeletonData and if the joints of interest are tracked, do some 
        /// maths to calculate distances and store the results for future use.
        /// </summary>
        /// <param name="skeleton"></param>
        private void findFeatures(Skeleton skeleton)
        {
            double ShoulderWidth = 0;
            double ArmLength = 0;
            double HipHeadHeight = 0;

            // if left and right shoulder is tracked
            if (skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked)
                 && skeleton.Joints[JointType.ShoulderRight].TrackingState.Equals(JointTrackingState.Tracked))
            {
                
                // get the SkeletonPoint of each 
                SkeletonPoint sL = skeleton.Joints[JointType.ShoulderLeft].Position;
                SkeletonPoint sR = skeleton.Joints[JointType.ShoulderRight].Position;

                // and calculate the Eucledian distance between them.
                double sSqr = (sL.X - sR.X) * (sL.X - sR.X) + (sL.Y - sR.Y) * (sL.Y - sR.Y) + (sL.Z - sR.Z) * (sL.Z - sR.Z);

                ShoulderWidth = Math.Sqrt(sSqr);
            }

            // if either the left shoulder and left elbow, or the right shoulder and right elbow are tracked 
            if ((skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked)
                  && skeleton.Joints[JointType.ElbowLeft].TrackingState.Equals(JointTrackingState.Tracked))
               ||
                (skeleton.Joints[JointType.ShoulderRight].TrackingState.Equals(JointTrackingState.Tracked)
                  && skeleton.Joints[JointType.ElbowRight].TrackingState.Equals(JointTrackingState.Tracked)))
            {

                // get the SkeletonPoints of the tracked joints and determine the distance between both joints
                if (skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked))
                {
                
                    SkeletonPoint sL = skeleton.Joints[JointType.ShoulderLeft].Position;
                    SkeletonPoint eL = skeleton.Joints[JointType.ElbowLeft].Position;

                    double aSqr = (sL.X - eL.X) * (sL.X - eL.X) + (sL.Y - eL.Y) * (sL.Y - eL.Y) + (sL.Z - eL.Z) * (sL.Z - eL.Z);

                    ArmLength = Math.Sqrt(aSqr);

                }
                else
                {
                    SkeletonPoint sR = skeleton.Joints[JointType.ShoulderRight].Position;
                    SkeletonPoint eR = skeleton.Joints[JointType.ElbowRight].Position;

                    double aSqr = (sR.X - eR.X) * (sR.X - eR.X) + (sR.Y - eR.Y) * (sR.Y - eR.Y) + (sR.Z - eR.Z) * (sR.Z - eR.Z);

                    ArmLength = Math.Sqrt(aSqr);
                }
            }

            // finally if the hip and the center shoulder joint is tracked
            if (skeleton.Joints[JointType.ShoulderCenter].TrackingState.Equals(JointTrackingState.Tracked)
                 && skeleton.Joints[JointType.HipCenter].TrackingState.Equals(JointTrackingState.Tracked))
            {
                // get their SkeletonPoints and calculate the distance again.
                SkeletonPoint sC = skeleton.Joints[JointType.ShoulderLeft].Position;
                SkeletonPoint hC = skeleton.Joints[JointType.HipCenter].Position;

                double sSqr = (sC.X - hC.X) * (sC.X - hC.X) + (sC.Y - hC.Y) * (sC.Y - hC.Y) + (sC.Z - hC.Z) * (sC.Z - hC.Z);

                HipHeadHeight = Math.Sqrt(sSqr);
            }

            // add all the values to the last 20 values 
            if (ArmLength != 0)
            {
                if (armVals.Count < 20)
                {
                    armVals.Add(ArmLength);
                }
                else
                {
                    armVals.Remove(0);
                    armVals.Add(ArmLength);
                }
            }

            if (HipHeadHeight != 0)
            {
                if (headVals.Count < 20)
                {
                    headVals.Add(HipHeadHeight);
                }
                else
                {
                    headVals.Remove(0);
                    headVals.Add(HipHeadHeight);
                }
            }

            if (ShoulderWidth != 0)
            {
                if (shoulderVals.Count < 20)
                {
                    shoulderVals.Add(ShoulderWidth);
                }
                else
                {
                    shoulderVals.Remove(0);
                    shoulderVals.Add(ShoulderWidth);
                }
            }


        }

        /// <summary>
        /// Calculates the position of a joint to fit onto a specific panel with given width and height.
        /// </summary>
        /// <param name="joint"></param>
        /// <returns>the x and y components of that joints pixel position</returns>
        private ColorImagePoint getDisplayPosition(SkeletonPoint joint)
        {
            return kinect.getSensor().MapSkeletonPointToColor(joint,ColorImageFormat.RgbResolution1280x960Fps12);
        }


        public int getActiveUser()
        {
            return activeUser;
        }
    }
}