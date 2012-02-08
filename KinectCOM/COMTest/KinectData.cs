using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Kinect;

namespace KinectCOM
{
    /*
     * creates a runtime object that initializes the kinect to use color, depth data and player index generation and skeletal tracking.
     * Methods to start/stop streams and attaching EventHandlers to them are provided
     */
    class KinectData
    {
        KinectSensor kinect = KinectSensor.KinectSensors[0];
        private bool isAudioAvailable = true;

        public KinectData()
        {
            
            try
            {
                TransformSmoothParameters parameters = new TransformSmoothParameters();
                parameters.Smoothing = 0.7f;
                parameters.Correction = 0.3f;
                parameters.Prediction = 0.4f;
                parameters.JitterRadius = 1.0f;
                parameters.MaxDeviationRadius = 0.5f;


                kinect.SkeletonStream.Enable(parameters);
                kinect.ColorStream.Enable(ColorImageFormat.RgbResolution640x480Fps30);
                kinect.DepthStream.Enable(DepthImageFormat.Resolution640x480Fps30);
                kinect.Start();

            }
            catch (Exception e)
            {
                Console.Out.WriteLine("[KinectData]Constructor : " + e.Message);
            }
        }

        // returns an KinectAudioSource object and relinquish control to another object.
        // to ensure only one object has an audiosource it can only be returned if it is available
        public KinectAudioSource fetchAudioRuntime() {
            if (isAudioAvailable)
            {
                isAudioAvailable = false;
                return kinect.AudioSource;
            }
            else {
                return null;
            }
        }

        /*
         * Attaches an external method that is called whenever a new VideoFrame is ready from an active RGB stream         
         * 
         */
        public void attachRGBHandler(EventHandler<ColorImageFrameReadyEventArgs> handler)
        {
            kinect.ColorFrameReady += new EventHandler<ColorImageFrameReadyEventArgs>(handler);
        }

        public KinectSensor getSensor()
        {
            return kinect;
        }

        /**
         * Attaches an external method that is called whenever a new DepthFrame is ready from an active DepthStream
         * **/
        public void attachDepthHandler(EventHandler<DepthImageFrameReadyEventArgs> handler)
        {
            kinect.DepthFrameReady += new EventHandler<DepthImageFrameReadyEventArgs>(handler);
        }

        /**
         * Attaches an external method that is called whenever a new SkeletonFrame is ready from an active DepthStream
         * **/
        public void attachSkeletonHandler(EventHandler<SkeletonFrameReadyEventArgs> handler)
        {
            kinect.SkeletonFrameReady += new EventHandler<SkeletonFrameReadyEventArgs>(handler);
        }

        public bool isDepthOpen()
        {
            return kinect.DepthStream.IsEnabled;
        }

        public bool isRGBOpen()
        {
            return kinect.ColorStream.IsEnabled;
        }


        public void incAngle()
        {
            try
            {
                kinect.ElevationAngle += 5;
            }
            catch (Exception ex)
            {
                Console.Out.WriteLine("[KinectData]incAngle : " + ex.Message);
            }

        }

        public void decAngle() 
        {
            try
            {
                kinect.ElevationAngle -= 5;
            }
            catch (Exception ex) {
                Console.Out.WriteLine("[KinectData]decAngle : " + ex.Message);
            }
        }


        public Boolean setAngle(int angle)
        {
            try
            {
                kinect.ElevationAngle = angle;
                return true;
            }
            catch
            {
                return false;
            }

        }
    }
}
