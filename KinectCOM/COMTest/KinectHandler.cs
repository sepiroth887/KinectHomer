using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using Kinect;
using Kinect.Toolbox.Voice;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;
using log4net;
using log4net.Appender;

namespace KinectCOM
{
    internal class KinectHandler : IKinect
    {
        private readonly Device COMInterface;
        private readonly string[] commands;
        private readonly KinectData kinect;
        private readonly ArrayList skeletons;
        private readonly VoiceCommander vocCom;
        private FaceProcessor faceProcessor;
        private FeatureProcessor featureProcessor;
        private GestureProcessor gestureProcessor;
        private bool marking;
        private Vector3 pointA;
        private Vector3 pointB;
        private RecognitionProcessor recognitionProcessor;
        private static readonly ILog Log = LogManager.GetLogger(type: typeof(KinectHandler));

        public KinectHandler(KinectData kinect, Device comInterface)
        {
            var rollAppend = new RollingFileAppender
                                 {
                                     File =
                                         Environment.SpecialFolder.LocalApplicationData + "\\KinectHomer\\kinectCOM.log"
                                 };
            log4net.Config.BasicConfigurator.Configure(rollAppend);
            Log.Info("Initializing Framework");
            COMInterface = comInterface;
            skeletons = new ArrayList();
            this.kinect = kinect;

            commands = DataStore.loadVoiceCommands();
            //Console.Out.WriteLine("VC loaded: " + commands.Length);
            vocCom = new VoiceCommander(commands);
            vocCom.OrderDetected += voiceCommandDetected;
        }

        #region IKinect Members

        public void init()
        {
            // initialize the KinectData object

            //initialize the FeatureProcessor
            featureProcessor = new FeatureProcessor(kinect, this);

            featureProcessor.init();

            // initialize RecognitionProcessor
            recognitionProcessor = new RecognitionProcessor(this);
            //Console.Out.WriteLine("Feature processor init complete");

            //initialize the FaceProcessor
            faceProcessor = new FaceProcessor(kinect, featureProcessor, recognitionProcessor);

            faceProcessor.Init();

            // pass face and feature processor references to the recongition processor.
            recognitionProcessor.SetFaceProcessor(faceProcessor);
            recognitionProcessor.SetFeatureProcessor(featureProcessor);

            gestureProcessor = new GestureProcessor(this, kinect);

            featureProcessor.startProcess();
            vocCom.Start(kinect.GetSensor());
        }

        public void uninit()
        {
            featureProcessor.stopProcess();
            recognitionProcessor = null;
            featureProcessor = null;
            faceProcessor = null;
            kinect.GetSensor().Stop();
        }

        public void updateFace()
        {
            throw new NotImplementedException();
        }

        public void updateSkeletons(Dictionary<JointType, ColorImagePoint> points, ArrayList users)
        {
            ////Console.Out.WriteLine(users.Count+""+skeletons.Count);

            if (users.Count != skeletons.Count)
            {
                if (users.Count > skeletons.Count)
                {
                    // new user fire presence event

                    foreach (int newUser in users)
                    {
                        if (!skeletons.Contains(newUser))
                        {
                            skeletons.Add(newUser);
                            presenceDetected(newUser);
                            break;
                        }
                    }
                }
                else
                {
                    foreach (int lostUser in skeletons)
                    {
                        if (!users.Contains(lostUser))
                        {
                            skeletons.Remove(lostUser);
                            presenceLost(lostUser);
                            break;
                        }
                    }
                }
            }
        }

        public void recordGesture(string gestureName, string ctxt)
        {
            gestureProcessor.recordGesture(gestureName, ctxt);
        }

        public void recognizeGesture(string ctxt)
        {
            gestureProcessor.recognizeGesture(ctxt);
        }

        public void learnUser(int skeletonID)
        {
            throw new NotImplementedException();
        }

        public bool startTracking(int skeletonID)
        {
            ////Console.Out.WriteLine("Trying to start tracking!");
            if (skeletons.Contains(skeletonID))
            {
                featureProcessor.setActiveUser(skeletonID);
                gestureProcessor.setActiveUser(skeletonID);
                faceProcessor.DoProcess();
                recognitionProcessor.StartRecognition(skeletonID);
                //Console.Out.WriteLine("Tracking user success");
                return true;
            }
            else
            {
                ////Console.Out.WriteLine("Tracking user failed");
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
            stopTracking(skeletonID);
        }


        public void userDetected(UserFeature user)
        {
            if (!"".Equals(user.Name))
            {
                //Console.Out.WriteLine("User detected:" + user.Name +" Confidence: "+user.Confidence);
                COMInterface.userFound(user.Name, user.Confidence, featureProcessor.getActiveUser());
            }
            else
            {
                //Console.Out.WriteLine("No User detected:" + user.Name);
            }
        }

        public void userLost(UserFeature user)
        {
            COMInterface.userLost(user.Name);
        }


        public void gestureRecordCompleted(string gestureName, string ctxt)
        {
            COMInterface.gestureRecordCompleted(gestureName, ctxt);
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


        public void contextSelected(string ctxt)
        {
            COMInterface.onContextSelected(ctxt);
        }


        public void onAddOnGestureValueChange(float value)
        {
            COMInterface.onAddonGestureValueChange(value);
        }


        public void storeGestures()
        {
            gestureProcessor.storeGestures();
        }

        public string[] loadGestures()
        {
            return gestureProcessor.loadGestures();
        }

        #endregion

        public void voiceCommandDetected(string command)
        {
            //Console.Out.WriteLine("Voice command detected: " + command);

            if (command.Equals("mark one"))
            {
                marking = true;
                pointA = gestureProcessor.getHandPos();
            }

            if (command.Equals("mark two") && marking)
            {
                pointB = gestureProcessor.getHandPos();

                var vectors = new Vector3[2];

                vectors[0] = pointA;
                vectors[1] = pointB;

                int context = gestureProcessor.returnLastContext() + 1;

                FileLoader.saveObject(
                    Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData) + "\\KinectHomer", context,
                    vectors);
                marking = false;

                gestureProcessor.updateContextObjects();
            }

            COMInterface.onVoiceCommandDetected(command);
        }
    }
}