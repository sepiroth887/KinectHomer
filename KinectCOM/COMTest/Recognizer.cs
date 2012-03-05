using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;

namespace KinectCOM
{
    class Recognizer
    {
        private EigenObjectRecognizer _recognizer; // recognizer used for face recognition.
        private Image<Gray, byte>[] _images;
        private String[] _labels;

        public Recognizer()
        {

            var db = FileLoader.LoadFaceDB("FaceDB");

            if (db == null) return;

            foreach(var item in db)
            {
               if(_images == null && _labels == null)
               {
                   _images = item.Key;
                   _labels = item.Value;
               }else
               {
                   if (_images != null) _images = _images.Concat(item.Key).ToArray();
                   if (_labels != null) _labels = _labels.Concat(item.Value).ToArray();
               }
            }
            var termCrit = new MCvTermCriteria(32, 0.001);
            _recognizer = new EigenObjectRecognizer(_images,_labels,2000, ref termCrit);
        }

        public String Recognize(Image<Gray,byte> face)
        {
            if(_recognizer != null)
            {
                var label = _recognizer.Recognize(face);

                return label;
            }

            return null;
        }

        public void TrainFace(Image<Gray,byte>face,String name)
        {
            if (face == null || name == null) return;
            _images = _images.Concat(new[] { face }).ToArray();
            _labels = _labels.Concat(new[] { name }).ToArray();

            var termCrit = new MCvTermCriteria(32, 0.001);
            _recognizer = new EigenObjectRecognizer(_images, _labels, 2000, ref termCrit);
        }

        public void SaveDatabase()
        {
            var database = new Dictionary<Image<Gray, byte>[], string[]> { { _images, _labels } };
            FileLoader.SaveFaceDB(database);
        }
    }
}
