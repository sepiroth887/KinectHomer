using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;
using System.Collections;
using KinectCOM;

namespace Kinect
{
    class RecognitionProcessor
    {
        private IKinect kinectHandler;
        private FeatureProcessor featureProcessor;
        private FaceProcessor faceProcessor;

        private Image<Gray, byte> latestFace = null;
        private ArrayList faces;
        
        public RecognitionProcessor(IKinect handler) {
            kinectHandler = handler;
            faces = new ArrayList();
        }

        public void setFeatureProcessor(FeatureProcessor feat) {
            this.featureProcessor = feat;
        }

        public void setFaceProcessor(FaceProcessor face) {
            this.faceProcessor = face;
        }

        public void learnUser(string name, int skeletonID) {

            faceProcessor.learnFace(faces,name);

            Dictionary<FeatureType, string> features = featureProcessor.getFeatures(skeletonID);
            features.Add(FeatureType.Face,name);
            new DataStore().storeUserData(features);

        }

        public void startRecognition(int skeletonID) {
            trackedUser = skeletonID;
            faceProcessor.recognizeFace(faces);
        }

        public Image<Gray, byte> LatestFace
        {
            get { return latestFace; }
            set
            {
                latestFace = value;

                if (faces.Count < 10)
                {
                    faces.Add(latestFace);
                }
                else
                {
                    faces.RemoveAt(0);
                    faces.Add(latestFace);
                }

           }
        }

        private int trackedUser;
        public void userRecognized(UserFeature user) { 
           
            Dictionary<string, Dictionary<FeatureType,string>> users = DataStore.loadAllUsers();

            if (user != null && users != null) {
                
                Dictionary<FeatureType,string> actualFeatures = featureProcessor.getFeatures(trackedUser);
                if (actualFeatures == null) { kinectHandler.userDetected(null); return; }                
                
                string userLabel = "";
                string bestMatch = "";
                float sumConfidence = 0;
                float bestConfidence = 0;

                foreach (KeyValuePair<string, Dictionary<FeatureType, string>> storedFeatures in users)
                {
                    Dictionary<FeatureType, float> featureConfidence = new Dictionary<FeatureType, float>(); 

                    foreach (KeyValuePair<FeatureType, string> feature in storedFeatures.Value)
                    {

                        
                        if (feature.Key == FeatureType.Face)
                        {
                            userLabel = feature.Value;
                        }
                        else
                        {
                            float actual = float.Parse(actualFeatures[feature.Key]);
                            float stored = float.Parse(feature.Value);
                            //float diff = Math.Abs(actual - stored);
                            float perDiff = Math.Abs(1 - (actual/stored));

                            float confidence = 1 - perDiff;

                            featureConfidence.Add(feature.Key, confidence);
                        }

                       
                    }

                    sumConfidence = 0;

                    foreach (KeyValuePair<FeatureType, float> feature2 in featureConfidence)
                    {
                        sumConfidence += feature2.Value;
                    }

                    sumConfidence = sumConfidence / 3;

                    if (bestConfidence < sumConfidence)
                    {
                        bestConfidence = sumConfidence;
                        bestMatch = userLabel;
                    }

                }
                if (bestMatch != user.Name)
                {
                    user.Confidence = (user.FaceConfidence - bestConfidence/2) /2;
                }
                else {
                    user.Confidence = (user.FaceConfidence + bestConfidence/2) /2;
                }

                if (user.Confidence < 0)
                {
                    UserFeature newUser = new UserFeature();
                    newUser.Confidence = bestConfidence / 2;
                    newUser.FaceConfidence = 0;
                    newUser.Name = bestMatch;
                    kinectHandler.userDetected(newUser);
                }
                else {
                    kinectHandler.userDetected(user);    
                }

            }

            
        }

        

    }
        

}
