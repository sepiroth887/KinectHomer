using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Kinect;
using Kinect.Toolbox.Voice;
using Microsoft.Kinect;
using log4net;
using log4net.Appender;

namespace KinectCOM
{
    internal class KinectHandler : IKinect
    {
        private readonly Device _comInterface;
        private readonly string[] _commands;
        private readonly KinectData _kinect;
        private readonly ArrayList _skeletons;
        private readonly VoiceCommander _vocCom;
        private FaceProcessor _faceProcessor;
        private FeatureProcessor _featureProcessor;
        private GestureProcessor _gestureProcessor;
        private RecognitionProcessor _recognitionProcessor;
        private static readonly ILog Log = LogManager.GetLogger(typeof(KinectHandler));

        public KinectHandler(KinectData kinect, Device comInterface)
        {
            var rollAppend = new RollingFileAppender
                                 {
                                     File =
                                         Environment.SpecialFolder.LocalApplicationData + "\\KinectHomer\\kinectCOM.log"
                                 };
            log4net.Config.BasicConfigurator.Configure(rollAppend);
            Log.Info("Initializing Framework");
            _comInterface = comInterface;
            _skeletons = new ArrayList();
            _kinect = kinect;

            _commands = DataStore.loadVoiceCommands();
            //Console.Out.WriteLine("VC loaded: " + commands.Length);
            _vocCom = new VoiceCommander(_commands);
            _vocCom.OrderDetected += VoiceCommandDetected;
        }

        #region IKinect Members

        void IKinect.Init()
        {
            // initialize the KinectData object

            //initialize the FeatureProcessor
            _featureProcessor = new FeatureProcessor(_kinect, this);

            _featureProcessor.Init();

            // initialize RecognitionProcessor
            _recognitionProcessor = new RecognitionProcessor(this);
            //Console.Out.WriteLine("Feature processor init complete");

            //initialize the FaceProcessor
            _faceProcessor = new FaceProcessor(_kinect, _featureProcessor, _recognitionProcessor);

            _faceProcessor.Init();

            // pass face and feature processor references to the recongition processor.
            _recognitionProcessor.SetFaceProcessor(_faceProcessor);
            _recognitionProcessor.SetFeatureProcessor(_featureProcessor);

            _gestureProcessor = new GestureProcessor(this, _kinect);

            _featureProcessor.StartProcess();
            if (_vocCom != null && _kinect != null) _vocCom.Start(_kinect.GetSensor());
        }

        void IKinect.Uninit()
        {
            if (_featureProcessor != null) _featureProcessor.StopProcess();
            _recognitionProcessor = null;
            _featureProcessor = null;
            _faceProcessor = null;
            if (_kinect != null)
            {
                var kinectSensor = _kinect.GetSensor();
                if (kinectSensor != null) kinectSensor.Stop();
            }
        }

        void IKinect.UpdateFace()
        {
            throw new NotImplementedException();
        }

        void IKinect.UpdateSkeletons(IDictionary points, ArrayList users)
        {
            ////Console.Out.WriteLine(users.Count+""+skeletons.Count);
            if (users == null || _skeletons == null || users.Count == _skeletons.Count) return;

            if (users.Count > _skeletons.Count)
            {
                // new user fire presence event

                foreach (var newUser in users.Cast<int>().Where(newUser => !_skeletons.Contains(newUser)))
                {
                    _skeletons.Add(newUser);
                    PresenceDetected(newUser);
                    break;
                }
            }
            else
            {
                foreach (var lostUser in _skeletons.Cast<int>().Where(lostUser => !users.Contains(lostUser)))
                {
                    _skeletons.Remove(lostUser);
                    PresenceLost(lostUser);
                    break;
                }
            }
        }

        void IKinect.RecordGesture(string gestureName, string ctxt)
        {
            if (_gestureProcessor != null) _gestureProcessor.RecordGesture(gestureName, ctxt);
        }

        void IKinect.RecognizeGesture(string ctxt)
        {
            if (_gestureProcessor != null) _gestureProcessor.RecognizeGesture(ctxt);
        }

        void IKinect.LearnUser(int skeletonID)
        {
            throw new NotImplementedException();
        }

        private bool AreProcessorsLoaded()
        {
            return _skeletons != null && _featureProcessor != null && _faceProcessor != null &&
                   _gestureProcessor != null && _recognitionProcessor != null;
        }

        public bool StartTracking(int skeletonID)
        {
            ////Console.Out.WriteLine("Trying to start tracking!");
// ReSharper disable PossibleNullReferenceException
            if ( AreProcessorsLoaded() && _skeletons.Contains(skeletonID))
            {
                _featureProcessor.SetActiveUser(skeletonID);
                _gestureProcessor.SetActiveUser(skeletonID);
                _faceProcessor.DoProcess();
                _recognitionProcessor.StartRecognition(skeletonID);
// ReSharper restore PossibleNullReferenceException
                //Console.Out.WriteLine("Tracking user success");
                return true;
            }

            return false;
        }

        public void StopTracking(int skeletonID)
        {
            if (!AreProcessorsLoaded()) return;

// ReSharper disable PossibleNullReferenceException
            _featureProcessor.SetActiveUser(-1); 
            _gestureProcessor.SetActiveUser(-1);
// ReSharper restore PossibleNullReferenceException
        }


        public void PresenceDetected(int skeletonID)
        {
            if (_comInterface != null) _comInterface.PresenceDetected(skeletonID);
        }

        public void PresenceLost(int skeletonID)
        {
            //Console.WriteLine("User lost: " + skeletonID);
            if (_comInterface != null) _comInterface.PresenceLost(skeletonID);
            StopTracking(skeletonID);
        }


        void IKinect.UserDetected(UserFeature user)
        {
            if (user != null && !"".Equals(user.Name))
            {
                //Console.Out.WriteLine("User detected:" + user.Name +" Confidence: "+user.Confidence);
                if (_comInterface != null && AreProcessorsLoaded())
                    _comInterface.UserFound(user.Name, user.Confidence, _featureProcessor.GetActiveUser());
            }
        }

        void IKinect.UserLost(UserFeature user)
        {
            if (_comInterface != null && user != null) _comInterface.UserLost(user.Name);
        }


        void IKinect.GestureRecordCompleted(string gestureName, string ctxt)
        {
            if (_comInterface != null) _comInterface.GestureRecordCompleted(gestureName, ctxt);
        }

        void IKinect.RecordingCountdownEvent(int p)
        {
            if (_comInterface != null) _comInterface.RecordingCountDownEvent(p);
        }


        void IKinect.GestureRecognitionCompleted(string gesture)
        {
            if (_comInterface != null) _comInterface.GestureRecognitionCompleted(gesture);
        }


        void IKinect.StopRecGesture()
        {
            if (_gestureProcessor != null) _gestureProcessor.StopRecognition();
        }


        void IKinect.ContextSelected(string ctxt)
        {
            if (_comInterface != null) _comInterface.onContextSelected(ctxt);
        }


        void IKinect.OnAddOnGestureValueChange(float value)
        {
            if (_comInterface != null) _comInterface.onAddonGestureValueChange(value);
        }


        void IKinect.StoreGestures()
        {
            if (_gestureProcessor != null) _gestureProcessor.StoreGestures();
        }

        public IEnumerable LoadGestures()
        {
            return _gestureProcessor != null ? _gestureProcessor.LoadGestures() : null;
        }

        #endregion

        private void VoiceCommandDetected(string command)
        {
            //Console.Out.WriteLine("Voice command detected: " + command);
            if (_comInterface != null) _comInterface.onVoiceCommandDetected(command);
        }
    }
}