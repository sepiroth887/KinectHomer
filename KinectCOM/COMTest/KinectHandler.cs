using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Collections;
using Kinect;
using Microsoft.Kinect;
using Kinect.Toolbox.Voice;
using Microsoft.Xna.Framework;

namespace KinectCOM
{
    class KinectHandler: IKinect
    {
        private KinectData kinect;
        private FeatureProcessor featureProcessor;
        private FaceProcessor faceProcessor;
        private RecognitionProcessor recognitionProcessor;
        private GestureProcessor gestureProcessor;
        private ArrayList skeletons;
        private Device COMInterface;
        private string[] commands;
        VoiceCommander vocCom;

        public KinectHandler(KinectData kinect, Device comInterface) {
            this.COMInterface = comInterface;
            skeletons = new ArrayList();
            this.kinect = kinect;

            commands = DataStore.loadVoiceCommands();
            Console.Out.WriteLine("VC loaded: " + commands.Length);
            vocCom = new VoiceCommander(commands);
            vocCom.OrderDetected += new Action<string>(voiceCommandDetected);
           
        }

        public void init()
        {
            // initialize the KinectData object
            
            //initialize the FeatureProcessor
            featureProcessor = new FeatureProcessor(kinect, this);

            featureProcessor.init();

            // initialize RecognitionProcessor
            recognitionProcessor = new RecognitionProcessor(this);
            Console.Out.WriteLine("Feature processor init complete");

            //initialize the FaceProcessor
            faceProcessor = new FaceProcessor(kinect, featureProcessor, recognitionProcessor, this);
            
            faceProcessor.init();
            
            // pass face and feature processor references to the recongition processor.
            recognitionProcessor.setFaceProcessor(faceProcessor);
            recognitionProcessor.setFeatureProcessor(featureProcessor);

            gestureProcessor = new GestureProcessor(this, kinect);

            featureProcessor.startProcess();
            vocCom.Start(kinect.getSensor());
        }

        public void uninit() {
            featureProcessor.stopProcess();
            recognitionProcessor = null;
            featureProcessor = null;
            faceProcessor = null;
            kinect.getSensor().Stop();
        }

        public void updateFace()
        {
            throw new NotImplementedException();
        }

        public void updateSkeletons(Dictionary<JointType,ColorImagePoint> points, ArrayList users)
        {
            //Console.Out.WriteLine(users.Count+""+skeletons.Count);

            if (users.Count != skeletons.Count) {
                if (users.Count > skeletons.Count)
                {
                    // new user fire presence event

                    foreach (int newUser in users)
                    {
                        if (!skeletons.Contains(newUser))
                        {
                            skeletons.Add(newUser);
                            this.presenceDetected(newUser);
                            break;
                        }
                    }


                }
                else {
                    foreach (int lostUser in skeletons)
                    {
                        if (!users.Contains(lostUser))
                        {
                            skeletons.Remove(lostUser);
                            this.presenceLost(lostUser);
                            break;
                        }
                    }
                }
            }

        }

        public void recordGesture(string gestureName, String ctxt) {
            gestureProcessor.recordGesture(gestureName, ctxt);
        }

        public void recognizeGesture(String ctxt) {
            gestureProcessor.recognizeGesture(ctxt);
        }

        public void learnUser(int skeletonID)
        {
            throw new NotImplementedException();
        }

        public bool startTracking(int skeletonID)
        {
            //Console.Out.WriteLine("Trying to start tracking!");
            if(skeletons.Contains(skeletonID)){
               featureProcessor.setActiveUser(skeletonID);
               gestureProcessor.setActiveUser(skeletonID);
               faceProcessor.doProcess();
               recognitionProcessor.startRecognition(skeletonID);
               Console.Out.WriteLine("Tracking user success");
               return true;
            }else{
                //Console.Out.WriteLine("Tracking user failed");
                return false;
            }
        }

        public void stopTracking(int skeletonID)
        {
            featureProcessor.setActiveUser(-1);
            gestureProcessor.setActiveUser(-1);
        }



        public void presenceDetected(int skeletonID)
        {
            COMInterface.presenceDetected(skeletonID);
        }

        public void presenceLost(int skeletonID)
        {
            //Console.WriteLine("User lost: " + skeletonID);
            COMInterface.presenceLost(skeletonID);
            this.stopTracking(skeletonID);
        }


        public void userDetected(UserFeature user)
        {
            if (!"".Equals(user.Name))
            {
                Console.Out.WriteLine("User detected:" + user.Name +" Confidence: "+user.Confidence);
                COMInterface.userFound(user.Name, user.Confidence);
            }
            else {
                Console.Out.WriteLine("No User detected:" + user.Name);
            }
        }

        public void userLost(UserFeature user)
        {
            COMInterface.userLost(user.Name);
        }


        public void gestureRecordCompleted(string gestureName, String ctxt)
        {
            COMInterface.gestureRecordCompleted(gestureName,ctxt);
        }

        public void recordingCountdownEvent(int p)
        {
            COMInterface.recordingCountDownEvent(p);
        }


        public void gestureRecognitionCompleted(string gesture)
        {
            COMInterface.gestureRecognitionCompleted(gesture);
        }


        public void stopRecGesture()
        {
            gestureProcessor.stopRecognition();
        }


        public void contextSelected(String ctxt)
        {
            COMInterface.onContextSelected(ctxt);
        }

        private bool marking = false;
        private Vector3 pointA;
        private Vector3 pointB;

        public void voiceCommandDetected(string command) {
            Console.Out.WriteLine("Voice command detected: " + command);

            if(command.Equals("mark one")){
                marking = true;
                pointA = gestureProcessor.getHandPos();
            }

            if (command.Equals("mark two") && marking) {
                pointB = gestureProcessor.getHandPos();

                Vector3[] vectors = new Vector3[2];

                vectors[0] = pointA;
                vectors[1] = pointB;

                int context = gestureProcessor.returnLastContext()+1;

                FileLoader.saveObject(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData)+"\\KinectHomer",context,vectors);
                marking = false;

                gestureProcessor.updateContextObjects();
            }

            COMInterface.onVoiceCommandDetected(command);
        }


        public void onAddOnGestureValueChange(float value)
        {
            COMInterface.onAddonGestureValueChange(value);
        }
    }
}
