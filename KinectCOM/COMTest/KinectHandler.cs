using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using Kinect.Toolbox.Voice;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;
using log4net;

namespace KinectCOM
{
    class KinectHandler : IKinect
    {
        private readonly Device _comInterface;
        private readonly string[] _commands;
        private readonly KinectData _kinect;
        private readonly VoiceCommander _vocCom;
        private GestureProcessor _gestureProcessor;
        private static readonly ILog Log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private TrackingEngine _trackingEngine;

        public KinectHandler(KinectData kinect, Device comInterface)
        {
            if (Log == null) return;
            Log.Info("Initializing Framework");
            
            _comInterface = comInterface;
            _kinect = kinect;

            if(_kinect != null && _kinect.GetSensor() == null)
            {
                Log.Error("Kinect not initialized, Cannot continue.");
                return;
            }

            _commands = FileLoader.LoadVoiceCommands();
            if (_commands == null) return;
            _vocCom = new VoiceCommander(_commands);
            _vocCom.OrderDetected += VoiceCommandDetected;
        }

        #region IKinect Members

        void IKinect.Init()
        {
            // initialize the KinectData object
            if(_kinect != null && _kinect.GetSensor() == null)
            {
                Log.Error("Init cannot be completed. Kinect not active");
                return;
            }
            
            _gestureProcessor = new GestureProcessor(this, _kinect);
            Log.Info("Gesture processor created");

            _trackingEngine = new TrackingEngine(_kinect,this);

            _trackingEngine.Strategy = TrackingEngine.RECOGNIZED_FIRST;
            //_featureProcessor.StartProcess();
            if (_vocCom != null)
                _vocCom.Start(_kinect.GetSensor());
            else
                Log.Error("Could not load voice commands.");
            Log.Info("Voice Commands loaded.");
        }

        void IKinect.Uninit()
        {
            _gestureProcessor = null;
            if (_kinect == null) return;
            var kinectSensor = _kinect.GetSensor();
            if (kinectSensor != null) kinectSensor.Stop();
        }


        void IKinect.SetTrackingStrategy(int strat)
        {
            if (_trackingEngine != null) _trackingEngine.Strategy = strat;
        }

        void IKinect.RecordGesture(string gestureName, string ctxt)
        {
            if (_gestureProcessor != null) _gestureProcessor.RecordGesture(gestureName, ctxt);
        }

        void IKinect.RecognizeGesture(string ctxt)
        {
            if (_gestureProcessor != null) _gestureProcessor.RecognizeGesture(ctxt);
        }

        void IKinect.LearnUser(String name,int skeletonID)
        {
            if (_trackingEngine != null) _trackingEngine.Train(name, skeletonID);
        }

        public bool StartTracking(int skeletonID)
        {
            
            if ( _gestureProcessor != null)
            {
                _gestureProcessor.SetActiveUser(skeletonID);
                return true;
            }

            return false;
        }

        public void StopTracking(int skeletonID)
        {
            if (_gestureProcessor != null) _gestureProcessor.SetActiveUser(-1);
        }


        public void PresenceDetected(int skeletonID)
        {
            if (_comInterface != null) _comInterface.PresenceDetected(skeletonID);
        }

        public void PresenceLost(int skeletonID)
        {
            if (skeletonID <= 0) return;
            //Console.WriteLine("User lost: " + skeletonID);
            if (_comInterface != null) _comInterface.PresenceLost(skeletonID);
            StopTracking(skeletonID);
        }

        void IKinect.UserLost(User user)
        {
            if (_comInterface != null && user != null) _comInterface.UserLost(user.Name);
        }

        public void SetDefaultHand(bool def)
        {
            if (_gestureProcessor != null) _gestureProcessor.SetDefaultHand(def);
        }

        public string LoadUsers()
        {
            if (_trackingEngine != null) return _trackingEngine.GetUsers();
            return "";
        }

        public void AddUser(string user)
        {
            if (_trackingEngine != null) _trackingEngine.AddUser(user);
        }

        public void DelUser(string user)
        {
            if (_trackingEngine != null) _trackingEngine.DelUser(user);
        }

        public void SetNewObjectContext(string context)
        {
            if(!"".Equals(context))
            {
                objectContext = context;
                _points = new Vector3[2];
                Log.Info("Context for new object set: "+context);
            }
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
            if (_comInterface != null) _comInterface.ContextSelected(ctxt);
        }


        void IKinect.OnAddOnGestureValueChange(float value)
        {
            if (_comInterface != null) _comInterface.AddonGestureValueChange(value);
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


        private Vector3[] _points;
        private string objectContext;
        private void VoiceCommandDetected(string command)
        {

            //Console.Out.WriteLine("Voice command detected: " + command);
            if (_comInterface != null) _comInterface.VoiceCommandDetected(command);
            
            if(command.Equals("mark one") && _points != null)
            {
                _points = new Vector3[2];
                _points[1] = _trackingEngine.GetHandLocation(true);
            }else if(command.Equals("mark two") && _points!=null)
            {
                _points[1] = _trackingEngine.GetHandLocation(true);

                if(!"".Equals(objectContext))
                {
                    _gestureProcessor.CreateObject(_points,objectContext);
                    objectContext = "";
                    _points = null;
                }
            }
        }

   
        public void TrackingStopped(int matchingSkeleton)
        {
            if (_comInterface != null && matchingSkeleton != -1) _comInterface.TrackingStopped(matchingSkeleton);
        }

        public void TrackingStarted(int matchingSkeleton)
        {
            if (_comInterface != null && matchingSkeleton != -1) _comInterface.TrackingStarted(matchingSkeleton);
        }

        string IKinect.GetObjects()
        {
            return _gestureProcessor != null ? _gestureProcessor.GetObjects() : "";
        }

        public void UserDetected(User user)
        {
            if (_comInterface != null && user != null) _comInterface.UserFound(user.Name,user.FaceConfidence,user.TrackingID);
        }

    }
}