﻿using System;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using Kinect.Toolbox.Voice;
using Microsoft.Kinect;
using log4net;

namespace KinectCOM
{
    class KinectHandler : IKinect
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
        private static readonly ILog Log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        private TrackingEngine _trackingEngine;
        private DebugWindow _debugWindow;

        public KinectHandler(KinectData kinect, Device comInterface)
        {
            
            Log.Info("Initializing Framework");
            
            _comInterface = comInterface;
            _skeletons = new ArrayList();
            _kinect = kinect;

            if(_kinect != null && _kinect.GetSensor() == null)
            {
                Log.Error("Kinect not initialized, Cannot continue.");
                return;
            }

            _commands = FileLoader.LoadVoiceCommands();
            //Console.Out.WriteLine("VC loaded: " + commands.Length);
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
            //initialize the FeatureProcessor
            //_featureProcessor = new FeatureProcessor(_kinect, this);
            //Log.Info("Feature processor created");
            //_featureProcessor.Init();

            // initialize RecognitionProcessor
            //_recognitionProcessor = new RecognitionProcessor(this);
            //Console.Out.WriteLine("Feature processor init complete");
            //Log.Info("Recognition processor created");
            //initialize the FaceProcessor
            //_faceProcessor = new FaceProcessor(_kinect, _featureProcessor, _recognitionProcessor);
            //Log.Info("Face processor created");
            //_faceProcessor.Init();

            // pass face and feature processor references to the recongition processor.
            //_recognitionProcessor.SetFaceProcessor(_faceProcessor);
            //_recognitionProcessor.SetFeatureProcessor(_featureProcessor);

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
            _trackingEngine.Train(name, skeletonID);
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
            if ( _gestureProcessor != null)
            {
                //_featureProcessor.SetActiveUser(skeletonID);
                _gestureProcessor.SetActiveUser(skeletonID);
                
                //_faceProcessor.DoProcess();
                //_recognitionProcessor.StartRecognition(skeletonID);
// ReSharper restore PossibleNullReferenceException
                //Console.Out.WriteLine("Tracking user success");
                return true;
            }

            return false;
        }

        public void StopTracking(int skeletonID)
        {
            
// ReSharper disable PossibleNullReferenceException
            //_featureProcessor.SetActiveUser(-1); 
            _gestureProcessor.SetActiveUser(-1);
// ReSharper restore PossibleNullReferenceException
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


        void IKinect.UserDetected(User user)
        {
            if (user != null && !"".Equals(user.Name))
            {
                //Console.Out.WriteLine("User detected:" + user.Name +" Confidence: "+user.Confidence);
                if (_comInterface != null && _featureProcessor != null)
                    _comInterface.UserFound(user.Name, user.Confidence, _featureProcessor.GetActiveUser());
            }
        }

        void IKinect.UserLost(User user)
        {
            if (_comInterface != null && user != null) _comInterface.UserLost(user.Name);
        }

        public void SetDefaultHand(bool def)
        {
            _gestureProcessor.SetDefaultHand(def);
        }

        public string LoadUsers()
        {
            return _trackingEngine.GetUsers();
        }

        public void AddUser(string user)
        {
            _trackingEngine.AddUser(user);
        }

        public void DelUser(string user)
        {
            _trackingEngine.DelUser(user);
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

        private void VoiceCommandDetected(string command)
        {
            //Console.Out.WriteLine("Voice command detected: " + command);
            if (_comInterface != null) _comInterface.VoiceCommandDetected(command);
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
            _comInterface.UserFound(user.Name,user.FaceConfidence,user.TrackingID);
        }

        public void UserLost(User user)
        {
            _comInterface.UserLost(user.Name);
        }
    }
}