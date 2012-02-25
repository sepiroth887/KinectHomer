using System;
using Microsoft.Kinect;
using log4net;

namespace KinectCOM
{
    /*
     * creates a runtime object that initializes the kinect to use color, depth data and player index generation and skeletal tracking.
     * Methods to start/stop streams and attaching EventHandlers to them are provided
     */

    public class KinectData
    {
        private readonly KinectSensor _kinect;
        private static readonly ILog Log = LogManager.GetLogger(typeof(KinectData));

        public KinectData(int deviceId)
        {
            try
            {
                 _kinect = KinectSensor.KinectSensors[deviceId];
                var parameters = new TransformSmoothParameters
                                     {
                                         Smoothing = 0.7f,
                                         Correction = 0.3f,
                                         Prediction = 0.4f,
                                         JitterRadius = 1.0f,
                                         MaxDeviationRadius = 0.5f
                                     };


                if (_kinect != null)
                {
                    if (_kinect.SkeletonStream != null) _kinect.SkeletonStream.Enable(parameters);
                    if (_kinect.ColorStream != null)
                        _kinect.ColorStream.Enable(ColorImageFormat.RgbResolution640x480Fps30);
                    if (_kinect.DepthStream != null)
                        _kinect.DepthStream.Enable(DepthImageFormat.Resolution640x480Fps30);
                    _kinect.Start();
                    Log.Info("Kinect DevID:"+deviceId+" started");
                }
            }
            catch (Exception e)
            {
                Log.Error("Kinect could not be started",e);
            }
        }


        /*
         * Attaches an external method that is called whenever a new VideoFrame is ready from an active RGB stream         
         * 
         */

        public void attachRGBHandler(EventHandler<ColorImageFrameReadyEventArgs> handler)
        {
            if (_kinect != null) _kinect.ColorFrameReady += handler;
        }

        public KinectSensor GetSensor()
        {
            return _kinect;
        }

        /**
         * Attaches an external method that is called whenever a new DepthFrame is ready from an active DepthStream
         * **/

        public void attachDepthHandler(EventHandler<DepthImageFrameReadyEventArgs> handler)
        {
            if (_kinect != null) _kinect.DepthFrameReady += handler;
        }

        /**
         * Attaches an external method that is called whenever a new SkeletonFrame is ready from an active DepthStream
         * **/

        public void attachSkeletonHandler(EventHandler<SkeletonFrameReadyEventArgs> handler)
        {
            if (_kinect != null) _kinect.SkeletonFrameReady += handler;
        }


        public void IncAngle()
        {
            try
            {
                if (_kinect != null) _kinect.ElevationAngle += 5;
            }
            catch (Exception ex)
            {
                Log.Error("Angle could not be changed",ex);
            }
        }

        public void DecAngle()
        {
            try
            {
                if (_kinect != null) _kinect.ElevationAngle -= 5;
            }
            catch (Exception ex)
            {
                Log.Error("Angle could not be changed", ex);
            }
        }
    }
}