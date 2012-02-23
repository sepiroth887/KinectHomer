using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Emgu.CV;
using Emgu.CV.Structure;
using Kinect;
using log4net;

namespace KinectCOM
{
    internal class RecognitionProcessor
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof (RecognitionProcessor));
        private readonly ArrayList _faces;
        private readonly IKinect _kinectHandler;
        private FaceProcessor _faceProcessor;
        private FeatureProcessor _featureProcessor;

        private Image<Gray, byte> _latestFace;
        private int _trackedUser;

        public RecognitionProcessor(IKinect handler)
        {
            _kinectHandler = handler;
            _faces = new ArrayList();
            Log.Info("RecognitionProcessor created");
        }

        public Image<Gray, byte> LatestFace
        {
            get { return _latestFace; }
            set
            {
                _latestFace = value;


                if (_faces != null && _faces.Count < 10)
                {
                    if (_latestFace != null) _faces.Add(_latestFace);
                }
                else
                {
                    if (_faces != null)
                    {
                        _faces.RemoveAt(0);
                        if (_latestFace != null) _faces.Add(_latestFace);
                    }
                }
            }
        }

        public void SetFeatureProcessor(FeatureProcessor feat)
        {
            _featureProcessor = feat;
        }

        public void SetFaceProcessor(FaceProcessor face)
        {
            _faceProcessor = face;
        }

        public void LearnUser(string name, int skeletonID)
        {
            if (_faceProcessor == null)
            {
                Log.Warn("Learning failed, no faceProcessor present");
                return;
            }

            _faceProcessor.LearnFace(_faces, name);

            if (_featureProcessor == null)
            {
                Log.Warn("Learning failed, no featureProcessor present");
                return;
            }

            var features = _featureProcessor.GetFeatures(skeletonID);
            if (features == null)
            {
                Log.Warn("Learning failed, could not extract features from skeletonID: " + skeletonID);
                return;
            }

            features.Add(FeatureType.Face, name);
            new DataStore().storeUserData(features);
            Log.Info("User trained");
        }

        public void StartRecognition(int skeletonID)
        {
            _trackedUser = skeletonID;
            if (_faceProcessor != null) _faceProcessor.RecognizeFace(_faces);
        }

        public void UserRecognized(UserFeature user)
        {
            var users = DataStore.loadAllUsers();

            if (user == null || users == null)
            {
                Log.Warn("Recognition failed, no user found");
                return;
            }

            if (_featureProcessor == null)
            {
                Log.Warn("Recognition failed, no featureProcessor present");
                return;
            }

            var actualFeatures = _featureProcessor.GetFeatures(_trackedUser);
            if (actualFeatures != null)
            {
                string userLabel = "";
                string bestMatch = "";
                float bestConfidence = 0;

                foreach (var storedFeatures in users)
                {
                    var featureConfidence = new Dictionary<FeatureType, float>();

                    if (storedFeatures.Value != null)
                        foreach (var feature in storedFeatures.Value)
                        {
                            if (feature.Key == FeatureType.Face)
                            {
                                userLabel = feature.Value;
                            }
                            else
                            {
                                var actual = float.Parse(actualFeatures[feature.Key]);
                                var stored = float.Parse(feature.Value);
                                //float diff = Math.Abs(actual - stored);
                                var perDiff = Math.Abs(1 - (actual/stored));

                                var confidence = 1 - perDiff;

                                featureConfidence.Add(feature.Key, confidence);
                            }
                        }

                    var sumConfidence = featureConfidence.Sum(feature2 => feature2.Value);

                    sumConfidence = sumConfidence/3;

                    if (bestConfidence >= sumConfidence) continue;
                    bestConfidence = sumConfidence;
                    bestMatch = userLabel;
                }

                if (bestMatch != user.Name)
                {
                    user.Confidence = (user.FaceConfidence - bestConfidence/2)/2;
                }
                else
                {
                    user.Confidence = (user.FaceConfidence + bestConfidence/2)/2;
                }

                if (user.Confidence < 0)
                {
                    var newUser = new UserFeature {Confidence = bestConfidence/2, FaceConfidence = 0, Name = bestMatch};
                    if (_kinectHandler != null) _kinectHandler.UserDetected(newUser);
                }
                else
                {
                    if (_kinectHandler != null) _kinectHandler.UserDetected(user);
                }
            }
            else
            {
                if (_kinectHandler != null) _kinectHandler.UserDetected(null);
            }
        }
    }
}