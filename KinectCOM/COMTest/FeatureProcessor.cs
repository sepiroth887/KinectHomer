using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using Microsoft.Kinect;

namespace KinectCOM
{
    internal class FeatureProcessor
    {
        private readonly ArrayList _armVals = new ArrayList(); // list of last 20 arm length values.
        private readonly IKinect _handler;
        private readonly ArrayList _headVals = new ArrayList(); // list of last 20 hip to head height values.

        private readonly Dictionary<JointType, ColorImagePoint> _jointPoints =
            new Dictionary<JointType, ColorImagePoint>(); // Dictionary of joints for the actively tracked user.

        private readonly KinectData _kinect; // refernce to Kinect manager
        private readonly ArrayList _shoulderVals = new ArrayList(); // list of last 20 shoulder length values.
        private readonly ArrayList _users = new ArrayList(); // list of user ids that are in the FOV
        private int _activeUser = -1; // current actively tracked user

        private SkeletonPoint _activeUserHeadPos; // location of the active users head
        private bool _isProcessing; // indicates whether the processor is active

        /// <summary>
        /// Event handler activated when a skeletonFrame is ready. Returns immediately if the processor is
        /// no started. The frame is retrieved and for each skeleton in the frame the user id is stored. 
        /// If an active user has been selected, its skeleton is parsed for joints and the  is updated.
        /// </summary>
        private Skeleton[] _skeletonData;

        private SkeletonFrame _skeletonFrame;

        private long _skeletonFrameTime; // time of the last frame.


        /// <summary>
        /// Constructor. Assigns parameters to local fields
        /// </summary>
        /// <param name="kinect">Kinect Data object</param>
        /// <param name="kinectHandler">Handler interface </param>
        public FeatureProcessor(KinectData kinect, IKinect kinectHandler)
        {
            _kinect = kinect;
            _handler = kinectHandler;
        }

        /// <summary>
        /// init opens the depth stream and attaches a skeletonFrame handler.
        /// </summary>
        public void Init()
        {
            if (_kinect != null) _kinect.attachSkeletonHandler(SkeletonImageReady);
        }

        /// <summary>
        /// starts feature tracking
        /// </summary>
        public void StartProcess()
        {
            _isProcessing = true;
        }

        /// <summary>
        /// stops feature tracking
        /// </summary>
        public void StopProcess()
        {
            _isProcessing = false;
        }

        /// <summary>
        /// sets the active user to be tracked.
        /// </summary>
        /// <param name="uid"></param>
        public void SetActiveUser(int uid)
        {
            _activeUser = uid;
        }

        /// <summary>
        /// returns the timestamp of the last skeleton frame
        /// </summary>
        /// <returns></returns>
        public long GetFrameNumber()
        {
            return _skeletonFrameTime;
        }

        /// <summary>
        /// gets the active users head position as a SkeletonPoint.
        /// </summary>
        /// <returns></returns>
        public SkeletonPoint GetUserHeadPos()
        {
            return _activeUserHeadPos;
        }

        private void SkeletonImageReady(object sender, SkeletonFrameReadyEventArgs e)
        {

// ReSharper disable PossibleNullReferenceException
            _skeletonData = new Skeleton[_kinect.GetSensor().SkeletonStream.FrameSkeletonArrayLength];


            if (!_isProcessing)
            {
                return;
            }
            // retrieve the frame
            _skeletonFrame = e.OpenSkeletonFrame();
// ReSharper restore PossibleNullReferenceException
            if (_skeletonFrame == null) return;
            _skeletonFrame.CopySkeletonDataTo(_skeletonData);

            // and the time it has been created.
            _skeletonFrameTime = _skeletonFrame.Timestamp;

            //Logger.logIt("[FeatureProcessor]Skeletons in frame: " + skeletons.Length);

            // clear both, the last users stored, and the active users joints.
            if(_users == null || _jointPoints == null) return;

            _users.Clear();
            _jointPoints.Clear();


            // loop through all skeletons and add the TrackingID to the users arraylist.
            foreach (var skeleton in _skeletonData)
            {
                if (skeleton != null && !skeleton.TrackingState.Equals(SkeletonTrackingState.NotTracked))
                {
                    _users.Add(skeleton.TrackingId);
                    ////Console.Out.WriteLine("Tracked user found: " + skeleton.TrackingID);
                }

                // if no active user has been selected, skip the remainder.
                if (!_users.Contains(_activeUser))
                {
                    continue;
                }

                // if an active user is present
                if (skeleton != null && _activeUser == skeleton.TrackingId)
                {
                    // loop through each joint and if its tracked 
                    Debug.Assert(skeleton.Joints != null, "skeleton.Joints != null");
                    foreach (Joint joint in skeleton.Joints)
                    {
                        if (!joint.Equals(JointTrackingState.Tracked) &&
                            !joint.TrackingState.Equals(JointTrackingState.Inferred)) continue;
                        // get the location as a point fitting on the GUI picturebox for the skeleton
                        var p = GetDisplayPosition(joint.Position);
                        // and add it to the list of jointpoints
                        _jointPoints.Add(joint.JointType, p);

                        //Console.WriteLine("Joint: "+joint.ID+" position: "+p.X+"|"+p.Y);
                    }

                    // store the current position of the active users head
                    _activeUserHeadPos = skeleton.Joints[JointType.Head].Position;

                    // and try to find the features of this skeleton for later processing.
                    FindFeatures(skeleton);
                }
            }

            // update the  with the retrieved information

            if (_handler != null) _handler.UpdateSkeletons(_jointPoints, _users);
        }

        /// <summary>
        /// Calculates averages of the stored features for the active user.
        /// </summary>
        /// <param name="skelID"></param>
        /// <returns>Dictionary of the body features</returns>
        public Dictionary<FeatureType, string> GetFeatures(int skelID)
        {
            // the passed userID does not match the currently tracked user so stop working.
            if (_activeUser != skelID)
            {
                return null;
            }

            // add all the shoulder values and calculate the average.
            double avgShoulder = 0;

            if (_shoulderVals != null && _shoulderVals.Count != 0)
            {
                avgShoulder += _shoulderVals.Cast<double>().Sum();

                avgShoulder = avgShoulder/_shoulderVals.Count;
            }

            // add all the arm values and calculate the average.
            double avgArm = 0;

            if (_armVals != null && _armVals.Count != 0)
            {
                avgArm += _armVals.Cast<double>().Sum();

                avgArm = avgArm/_armVals.Count;
            }

            // add all the head values and calculate the average.
            double avgHead = 0;

            if (_headVals != null && _headVals.Count != 0)
            {
                avgHead += _headVals.Cast<double>().Sum();

                avgHead = avgHead/_headVals.Count;
            }

            // store all average values in a dictionary of featureTypes.
            var vals = new Dictionary<FeatureType, string>
                           {
                               {
                                   FeatureType.ShoulderWidth,
                                   avgShoulder.ToString(CultureInfo.CreateSpecificCulture("en-GB"))
                                   },
                               {FeatureType.ArmLength, avgArm.ToString(CultureInfo.CreateSpecificCulture("en-GB"))},
                               {FeatureType.HipHeadHeight, avgHead.ToString(CultureInfo.CreateSpecificCulture("en-GB"))}
                           };

            return vals;
        }


        /// <summary>
        /// Progresses through the skeletonData and if the joints of interest are tracked, do some 
        /// maths to calculate distances and store the results for future use.
        /// </summary>
        /// <param name="skeleton"></param>
        private void FindFeatures(Skeleton skeleton)
        {
            double shoulderWidth = 0;
            double armLength = 0;
            double hipHeadHeight = 0;

            // if left and right shoulder is tracked
            if (skeleton != null && (skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked)
                                     && skeleton.Joints[JointType.ShoulderRight].TrackingState.Equals(JointTrackingState.Tracked)))
            {
                // get the SkeletonPoint of each 
                var sL = skeleton.Joints[JointType.ShoulderLeft].Position;
                var sR = skeleton.Joints[JointType.ShoulderRight].Position;

                // and calculate the Eucledian distance between them.
                var sSqr = (sL.X - sR.X)*(sL.X - sR.X) + (sL.Y - sR.Y)*(sL.Y - sR.Y) + (sL.Z - sR.Z)*(sL.Z - sR.Z);

                shoulderWidth = Math.Sqrt(sSqr);
            }

            // if either the left shoulder and left elbow, or the right shoulder and right elbow are tracked 
            if (skeleton != null && ((skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked)
                                      && skeleton.Joints[JointType.ElbowLeft].TrackingState.Equals(JointTrackingState.Tracked))
                                     ||
                                     (skeleton.Joints[JointType.ShoulderRight].TrackingState.Equals(JointTrackingState.Tracked)
                                      && skeleton.Joints[JointType.ElbowRight].TrackingState.Equals(JointTrackingState.Tracked))))
            {
                // get the SkeletonPoints of the tracked joints and determine the distance between both joints
                if (skeleton.Joints[JointType.ShoulderLeft].TrackingState.Equals(JointTrackingState.Tracked))
                {
                    var sL = skeleton.Joints[JointType.ShoulderLeft].Position;
                    var eL = skeleton.Joints[JointType.ElbowLeft].Position;

                    double aSqr = (sL.X - eL.X)*(sL.X - eL.X) + (sL.Y - eL.Y)*(sL.Y - eL.Y) +
                                  (sL.Z - eL.Z)*(sL.Z - eL.Z);

                    armLength = Math.Sqrt(aSqr);
                }
                else
                {
                    var sR = skeleton.Joints[JointType.ShoulderRight].Position;
                    var eR = skeleton.Joints[JointType.ElbowRight].Position;

                    double aSqr = (sR.X - eR.X)*(sR.X - eR.X) + (sR.Y - eR.Y)*(sR.Y - eR.Y) +
                                  (sR.Z - eR.Z)*(sR.Z - eR.Z);

                    armLength = Math.Sqrt(aSqr);
                }
            }

            // finally if the hip and the center shoulder joint is tracked
            if (skeleton != null && (skeleton.Joints[JointType.ShoulderCenter].TrackingState.Equals(JointTrackingState.Tracked)
                                     && skeleton.Joints[JointType.HipCenter].TrackingState.Equals(JointTrackingState.Tracked)))
            {
                // get their SkeletonPoints and calculate the distance again.
                var sC = skeleton.Joints[JointType.ShoulderLeft].Position;
                SkeletonPoint hC = skeleton.Joints[JointType.HipCenter].Position;

                double sSqr = (sC.X - hC.X)*(sC.X - hC.X) + (sC.Y - hC.Y)*(sC.Y - hC.Y) + (sC.Z - hC.Z)*(sC.Z - hC.Z);

                hipHeadHeight = Math.Sqrt(sSqr);
            }

            // add all the values to the last 20 values 
            if (Math.Abs(armLength - 0) > 0.00001f)
            {
// ReSharper disable PossibleNullReferenceException
                if (_armVals.Count < 20)

                {
                    _armVals.Add(armLength);
                }
                else
                {
                    _armVals.Remove(0);
                    _armVals.Add(armLength);
                }
            }

            if (Math.Abs(hipHeadHeight - 0) > 0.00001f)
            {
                if (_headVals.Count < 20)
                {
                    _headVals.Add(hipHeadHeight);
                }
                else
                {
                    _headVals.Remove(0);
                    _headVals.Add(hipHeadHeight);
                }
            }

            if (Math.Abs(shoulderWidth - 0) > 0.00001f)
            {
                if (_shoulderVals.Count < 20)
                {
                    _shoulderVals.Add(shoulderWidth);
                }
                else
                {
                    _shoulderVals.Remove(0);
                    _shoulderVals.Add(shoulderWidth);
                }
            }
// ReSharper restore PossibleNullReferenceException
        }

        /// <summary>
        /// Calculates the position of a joint to fit onto a specific panel with given width and height.
        /// </summary>
        /// <param name="joint"></param>
        /// <returns>the x and y components of that joints pixel position</returns>
        private ColorImagePoint GetDisplayPosition(SkeletonPoint joint)
        {
            return _kinect.GetSensor().MapSkeletonPointToColor(joint, ColorImageFormat.RgbResolution1280x960Fps12);
        }


        public int GetActiveUser()
        {
            return _activeUser;
        }
    }
}