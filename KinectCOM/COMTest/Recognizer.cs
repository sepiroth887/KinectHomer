using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Text;
using log4net;

namespace FaceComTest
{
    public class Recognizer
    {
        public delegate void RecognitionCompletedHandler( RunWorkerCompletedEventArgs e);
        public delegate void TrainingCompletedHandler( RunWorkerCompletedEventArgs e);

        private readonly FaceRestAPI _faceRest;
        private readonly List<string> _namespace = new List<string>{"homerKinect.com"};
        private readonly List<string> _users = new List<string>();

        private readonly BackgroundWorker _trainBW;
        private readonly BackgroundWorker _recognizeBW;

        private bool _isTraining;
        private bool _isRecognizing;

        public Recognizer()
        {
            _faceRest = new FaceRestAPI("2460a8f47f65651ea1fb039f7782a55a", "90dfe4135e408911cd9acf72fb7b1f6b", "desaster", false, "json", "", "");

            var authResult = _faceRest.account_authenticate();

            var users = _faceRest.account_users(_namespace);

            foreach(var user in users.users[_namespace[0]])
            {
                _users.Add(user);
                Console.Out.WriteLine(user);
            }

            _trainBW = new BackgroundWorker();
            _trainBW.DoWork += _trainBW_DoWork;
            _trainBW.RunWorkerCompleted += _trainBW_RunWorkerCompleted;

            _recognizeBW = new BackgroundWorker();
            _recognizeBW.DoWork += _recognizeBW_DoWork;
            _recognizeBW.RunWorkerCompleted += _recognizeBW_RunWorkerCompleted;

        }

        public event RecognitionCompletedHandler RecognitionCompletedEvent;
        public event TrainingCompletedHandler TrainingCompletedEvent;

        private static readonly ILog Log = LogManager.GetLogger(typeof(Recognizer));

        void _recognizeBW_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if(e.Cancelled)
            {
                //Log.Error("Recognition canceled");
            }else
            {
                //Log.Info("Recognition completed");
            }
            _isRecognizing = false;
            
            
            OnRecCompleted(e);

        }

        protected virtual void OnRecCompleted(RunWorkerCompletedEventArgs e)
        {
            if (RecognitionCompletedEvent != null)
                RecognitionCompletedEvent(e);
        }

        void _recognizeBW_DoWork(object sender, DoWorkEventArgs e)
        {
            if (e == null) return;

            var image = e.Argument as Bitmap;

            var imgName = Guid.NewGuid() + "kin.jpg";

            var recResult = _faceRest.faces_recognize(null, _users, null, "", imgName, null, null, image);

          

            if(!recResult.status.Equals("success"))
            {
                e.Cancel = true;
                return;
            }

            var users = new Dictionary<string, float>();
            if (recResult.photos.Count == 0 || recResult.photos[0].tags.Count == 0)
            {
                e.Cancel = true;
                return;
            }

            foreach(var uid in recResult.photos[0].tags[0].uids)
            {
                users.Add(uid.uid,uid.confidence);
                if(uid.confidence > 90)
                {
                    _faceRest.tags_save(new List<string>{recResult.photos[0].tags[0].tid}, uid.uid, "", "kinectHome");
                }
            }

            e.Result = users;
        }

        void _trainBW_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if(e.Cancelled)
            {
                Log.Error("Training was canceled");
            }
            _isTraining = false;
            if (TrainingCompletedEvent != null) TrainingCompletedEvent(e);

            Log.Info("Training completed");
        }

        void _trainBW_DoWork(object sender, DoWorkEventArgs e)
        {
            if (e == null) return;

            var arg = e.Argument as Object[];

            var name = arg[0] as String;

            var image = arg[1] as Bitmap;

            var imgName = Guid.NewGuid() + "kin.jpg";


           

            var detectionResult = _faceRest.faces_detect(null, imgName, null, "", image);
                
            var tags = detectionResult.photos[0].tags;

            var tagList = new List<string>();



            if(tags == null || tags.Count != 1)
            {
                e.Cancel = true;
                return;
            }

            var tag = tags[0];

            if (tag.attributes["face"].confidence < 80)
            {
                e.Cancel = true;
                return;
            }

            tagList.Add(tag.tid);
            if (!_users.Contains(name + "@" + _namespace[0]))
            {
                _users.Add(name + "@"+_namespace[0]);
            }

            _faceRest.tags_save(tagList, name + "@"+_namespace[0], "", "kinectHome");
            var trainRes = _faceRest.faces_train(new List<string> { name },_namespace[0], string.Empty);

            var result = new object[2];

            result[0] = image;
            result[1] = detectionResult;
            e.Result = result;

            var trainStatus = _faceRest.faces_status(_users, _namespace[0]);

        }

        public void Train(string user, Bitmap image)
        {
            if (_isTraining) return;

            var args = new object[2];

            args[0] = user;
            args[1] = image;
            _isTraining = true;
            _trainBW.RunWorkerAsync(args);
        }

        public void Recognize(Bitmap image)
        {
            if (_isRecognizing) return;
            //Log.Info("Recognition started.");
            _isRecognizing = true;
            _recognizeBW.RunWorkerAsync(image);
        }

    }
}
