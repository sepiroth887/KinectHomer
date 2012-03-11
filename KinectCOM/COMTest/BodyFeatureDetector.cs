using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace KinectCOM
{
    internal class BodyFeatureDetector
    {
        private Dictionary<String, Dictionary<FeatureType, String>> _users;

        public BodyFeatureDetector()
        {
            _users = FileLoader.LoadAllUsers();
        }

        public User ValidateUser(User user, Skeleton skel)
        {
            if (skel == null) return user;
            var actualHeight = PointDistance(skel.Joints[JointType.HipCenter].Position,
                                             skel.Joints[JointType.Head].Position);
            var actualWidth = PointDistance(skel.Joints[JointType.ShoulderLeft].Position,
                                            skel.Joints[JointType.ShoulderRight].Position);
            var actualLength = PointDistance(skel.Joints[JointType.ElbowLeft].Position,
                                             skel.Joints[JointType.ShoulderLeft].Position);

            var bestMatch = FindBestMatch(actualHeight, actualWidth, actualLength);

            if(bestMatch.Name.Equals(user.Name))
            {
                user.Confidence = (user.FaceConfidence/100 + bestMatch.Confidence)/2;
                user.HipHeight = bestMatch.HipHeight;
                user.ShoulderWidth = bestMatch.ShoulderWidth;
                user.ArmLength = bestMatch.ArmLength;

            }else
            {
                user.Confidence = (user.FaceConfidence/100 - bestMatch.Confidence)/2;
                user.HipHeight = bestMatch.HipHeight;
                user.ShoulderWidth = bestMatch.ShoulderWidth;
                user.ArmLength = bestMatch.ArmLength;

            }

            return user;

        }

        public bool AddUser(String name, Skeleton skel)
        {
            var actualHeight = PointDistance(skel.Joints[JointType.HipCenter].Position,
                                            skel.Joints[JointType.Head].Position);
            var actualWidth = PointDistance(skel.Joints[JointType.ShoulderLeft].Position,
                                            skel.Joints[JointType.ShoulderRight].Position);
            var actualLength = PointDistance(skel.Joints[JointType.ElbowLeft].Position,
                                             skel.Joints[JointType.ShoulderLeft].Position);

            var userData = new Dictionary<FeatureType, string>();

            userData.Add(FeatureType.ArmLength,actualLength.ToString(CultureInfo.InvariantCulture));
            userData.Add(FeatureType.ShoulderWidth,actualWidth.ToString(CultureInfo.InvariantCulture));
            userData.Add(FeatureType.HipHeadHeight, actualHeight.ToString(CultureInfo.InvariantCulture));
            userData.Add(FeatureType.Face, name);

            if (!_users.ContainsKey(name))
            {
                _users.Add(name,userData);
                StoreFeatures(userData);
                return false;
            }

            _users[name] = userData;
            return true;
        }

        private User FindBestMatch(float actualHeight, float actualWidth, float actualLength)
        {
            var bestUser = new User();
            var bestConfidence = float.MinValue;

            foreach (var storedFeatures in _users)
            {
                var userLabel = storedFeatures.Key;
                float sumConfidence = 0;
                foreach (var featureType in storedFeatures.Value)
                {
                    switch (featureType.Key)
                    {
                        case FeatureType.HipHeadHeight:
                            {
                                var perDiff = Math.Abs(1 - (actualHeight/float.Parse(featureType.Value)));
                                var confidence = 1 - perDiff;
                                sumConfidence += confidence;
                            }
                            break;
                        case FeatureType.ShoulderWidth:
                            {
                                var perDiff = Math.Abs(1 - (actualWidth/float.Parse(featureType.Value)));
                                var confidence = 1 - perDiff;
                                sumConfidence += confidence;
                            }
                            break;
                        case FeatureType.ArmLength:
                            {
                                var perDiff = Math.Abs(1 - (actualLength/float.Parse(featureType.Value)));
                                var confidence = 1 - perDiff;
                                sumConfidence += confidence;
                            }
                            break;
                    }

                }

                sumConfidence = sumConfidence/3;

                if (bestConfidence >= sumConfidence) continue;
                bestConfidence = sumConfidence;
                bestUser.Name = userLabel;
                bestUser.HipHeight = actualHeight;
                bestUser.ShoulderWidth = actualWidth;
                bestUser.ArmLength = actualLength;
                bestUser.Confidence = bestConfidence;
            }

            return bestUser;
        }

        private static float PointDistance(SkeletonPoint a, SkeletonPoint b)
        {
            var distanceSqr = (a.X - b.X)*(a.X - b.X) + (a.Y - b.Y)*(a.Y - b.Y) + (a.Z - b.Z)*(a.Z - b.Z);

            return (float) Math.Sqrt(distanceSqr);
        }

        private void StoreFeatures(Dictionary<FeatureType,String> features )
        {
            FileLoader.StoreUserData(features);
        }

        public void DelUser(string user)
        {
            FileLoader.RemoveUser(user);
        }
    }
}
