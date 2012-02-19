using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Kinect;
using System.Diagnostics;
using System.Media;

namespace KinectCOM
{

    class ObjectPointer
    {
        private SoundPlayer selectSound;
        private SoundPlayer unselectSound;
        private SoundPlayer activeSound;
    
        public delegate void ContextSelectedEventHandler(string ctxt);
        public event ContextSelectedEventHandler ContextSelected;

        private Dictionary<string, BoundingBox> bounds;
        public ObjectPointer()
        {
            bounds = new Dictionary<string, BoundingBox>();
            selectSound = new SoundPlayer("C:\\Users\\Admin\\Documents\\Visual Studio 2010\\Projects\\KinectCOM\\COMTest\\Whit.wav");
            unselectSound = new SoundPlayer("C:\\Users\\Admin\\Documents\\Visual Studio 2010\\Projects\\KinectCOM\\COMTest\\WhitR.wav");
            activeSound = new SoundPlayer("C:\\Users\\Admin\\Documents\\Visual Studio 2010\\Projects\\KinectCOM\\COMTest\\Voltage.wav");
        }

        public int returnLastContext() {
            return -1;
        }

        public void setObjects(Dictionary<string, Vector3[]> objects)
        {
            bounds.Clear();
            
            foreach (KeyValuePair<string, Vector3[]> obj in objects)
            {
                if(obj.Key.Contains("Room")){continue;}
                BoundingBox box = BoundingBox.CreateFromPoints(obj.Value);
                bounds.Add(obj.Key, box);
                //Console.Out.WriteLine(obj.Key+": "+box.ToString());

            }
        }

        //check for an intersection of a ray with a bounding box and return the context of the box
        // or -1 if no intersection is found.
        public string intersects(Ray ray)
        {
            
            string context = "__NOCONTEXT";
            foreach (KeyValuePair<string, BoundingBox> box in bounds) {
                
                if (ray.Intersects(box.Value) != null) {
                    context = box.Key;
                    ////Console.Out.WriteLine("Context picked " + context);
                    return context;
                    
                }
            }
            return context;
        }

        private Stopwatch selectionTimer = new Stopwatch();
        private Stopwatch selectionCooldown = new Stopwatch();
        private bool contextConfirmed = false;
        private bool selectionCoolingDown = false;
        private long lastPickEvent = 0;
        private Boolean pointing = false;
        private string currentContext = "__NOCONTEXT";
        private Vector3 handPos;

        public Vector3 getHandPos()
        {
            return handPos;
        }


        private bool playSound = true;
        private bool pointedLastEvent = false;

        public void findContext(Skeleton skeleton)
        {


            if (selectionCoolingDown && selectionCooldown.IsRunning && selectionCooldown.ElapsedMilliseconds < 4000) return;

            if (selectionCooldown.ElapsedMilliseconds > 4000 && selectionCoolingDown)
            {
                selectionCoolingDown = false;
                selectionCooldown.Stop();
                selectionCooldown.Reset();
                contextConfirmed = false;
                selectionTimer.Reset();
                ContextSelected("__NOCONTEXT");
                return;
            }


            if (skeleton.Joints[JointType.HandRight].TrackingState == JointTrackingState.NotTracked ||
                skeleton.Joints[JointType.ElbowRight].TrackingState == JointTrackingState.NotTracked)
            {
                return;
            }


            SkeletonPoint handV = skeleton.Joints[JointType.HandRight].Position;
            SkeletonPoint elbowV = skeleton.Joints[JointType.ElbowRight].Position;
            SkeletonPoint hipL = skeleton.Joints[JointType.HipLeft].Position;
            SkeletonPoint handL = skeleton.Joints[JointType.HandLeft].Position;
            
            float distanceHandHip = (hipL.X - handL.X) * ( hipL.X - handL.X ) + (hipL.Y - handL.Y) *(hipL.Y - handL.Y) + (hipL.Z - handL.Z) * (hipL.Z - handL.Z)  ;

            ////Console.Out.WriteLine(Math.Sqrt(distanceHandHip));
            handPos = new Vector3(handV.X, handV.Y, handV.Z);

            if (distanceHandHip > 0.22) { return; }

            ////Console.Out.WriteLine("Hand pos: " + handV.X+ ","+handV.Y + "," +handV.Z);

            Vector3 rayOrig = new Vector3(elbowV.X, elbowV.Y, elbowV.Z);
            Vector3 rayDir = new Vector3(handV.X - rayOrig.X, handV.Y - rayOrig.Y, handV.Z - rayOrig.Z);

            

            rayDir.Normalize();

            Ray pick = new Ray(rayOrig, rayDir);

            currentContext = this.intersects(pick);

            if (!currentContext.Equals("__NOCONTEXT"))
            {
                pointing = true;
            }

            if (pointing)
            {
                ////Console.Out.WriteLine("Pointing at : " + currentContext);
                if (playSound) {
                    selectSound.Play();
                    ////Console.Out.WriteLine("Sound should have played");
                    playSound = false;
                }
                
                selectionTimer.Start();

                if (!pointedLastEvent)
                {
                    selectionTimer.Reset();
                    lastPickEvent = 0;
                    playSound = true;
                }
                else
                {
                    if (selectionTimer.ElapsedMilliseconds > 1000)
                    {
                        selectionTimer.Stop();
                        contextConfirmed = true;
                        playSound = true;
                    }

                }

                if (contextConfirmed)
                {
                    activeSound.Play();
                    ContextSelected(currentContext);
                    selectionCooldown.Start();
                    selectionTimer.Reset();
                    lastPickEvent = 0;
                    selectionCoolingDown = true;
                    pointedLastEvent = false;
                }

                pointing = false;
                pointedLastEvent = true;
                
            }
            else {
                if (pointedLastEvent) {
                    unselectSound.Play();
                    pointedLastEvent = false;
                }
                lastPickEvent++;
                selectionTimer.Stop();
            }


           



        }
    }
}
