using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Media.Imaging;
using Emgu.CV;
using Emgu.CV.CvEnum;
using Emgu.CV.GPU;
using Emgu.CV.Structure;
using log4net;

namespace KinectCOM
{
    class FaceDetector
    {
        private readonly GpuCascadeClassifier _haarGpu;

        private readonly HaarCascade _haarCpu;
                         
        private readonly bool _hasCuda;

        private static readonly ILog Log = LogManager.GetLogger(typeof(FaceProcessor));

        public FaceDetector()
        {
            if(GpuInvoke.HasCuda)
            {
                _haarGpu = new GpuCascadeClassifier(FileLoader.DefaultPath + "haarcascade_frontalface_default.xml");
                _hasCuda = true;
            }else
            {
                _haarCpu = new HaarCascade(FileLoader.DefaultPath + "haarcascade_frontalface_default.xml");
            }

        }

        public Image<Gray,byte>[]  DetectFaces(Image<Gray,byte> img)
        {
            Image<Gray, byte>[] faces = null;
            if (img != null)
            {
                

                Rectangle[] faceRegions = null;

                try
                {
                    if(_haarGpu != null && _hasCuda)
                    {
                        var grayGPUImage = new GpuImage<Gray, byte>(img);
                        faceRegions = _haarGpu.DetectMultiScale(grayGPUImage, 1.2, 4, new Size(img.Width/12, img.Height/12));
                        if (faceRegions != null)
                        {
                            faces = new Image<Gray, byte>[faceRegions.Length];
                            for (var i = 0; i < faces.Length; i++)
                            {
                                faces[i] = grayGPUImage.GetSubRect(faceRegions[i]).ToImage();
                            }
                        }
                    }else if(_haarCpu != null)
                    {
                        var facesComp = _haarCpu.Detect(img, 1.2, 4, HAAR_DETECTION_TYPE.DO_CANNY_PRUNING,
                                                             new Size(img.Width/12, img.Height/12));

                        if (facesComp != null) faceRegions = new Rectangle[facesComp.Length];
                        else return null;
                        for(var i = 0; i<facesComp.Length;i++)
                        {
                            faceRegions[i] = facesComp[i].rect;
                        }

                        if (faceRegions != null)
                        {
                            faces = new Image<Gray, byte>[faceRegions.Length];
                            for (var i = 0; i < faces.Length; i++)
                            {
                                img.ROI = faceRegions[i];
                                faces[i] = img.Copy();
                                faces[i] = faces[i].Resize(100, 100, INTER.CV_INTER_CUBIC);
                            }
                        }
                    }
                }catch (Exception ex)
                {
                    Log.Error("Face detection failed",ex);
                }

               

            }

            return faces;

        }
    }
}
