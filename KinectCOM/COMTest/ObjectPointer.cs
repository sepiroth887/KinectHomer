using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Media;
using Microsoft.Kinect;
using Microsoft.Xna.Framework;

namespace KinectCOM
{
    internal class ObjectPointer
    {
        #region Delegates

        public delegate void ContextSelectedEventHandler(string ctxt);

        #endregion

        private readonly SoundPlayer activeSound;

        private readonly Dictionary<string, BoundingBox> bounds;
        private readonly SoundPlayer selectSound;

        private readonly Stopwatch selectionCooldown = new Stopwatch();
        private readonly Stopwatch selectionTimer = new Stopwatch();
        private readonly SoundPlayer unselectSound;
        private bool contextConfirmed;
        private string currentContext = "__NOCONTEXT";
        private Vector3 handPos;
        private long lastPickEvent;


        private bool playSound = true;
        private bool pointedLastEvent;
        private Boolean pointing;
        private bool selectionCoolingDown;

        public ObjectPointer()
        {
            bounds = new Dictionary<string, BoundingBox>();
            selectSound =
                new SoundPlayer(
                    FileLoader.DefaultPath+"Whit.wav");
            unselectSound =
                new SoundPlayer(
                    FileLoader.DefaultPath+"WhitR.wav");
            activeSound =
                new SoundPlayer(
                    FileLoader.DefaultPath + "Voltage.wav");
        }

        public event ContextSelectedEventHandler ContextSelected;

        public int returnLastContext()
        {
            return -1;
        }

        public void setObjects(Dictionary<string, Vector3[]> objects)
        {
            bounds.Clear();

            foreach (var obj in objects)
            {
                if (obj.Key.Contains("Room"))
                {
                    continue;
                }
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
            foreach (var box in bounds)
            {
                if (ray.Intersects(box.Value) != null)
                {
                    context = box.Key;
                    ////Console.Out.WriteLine("Context picked " + context);
                    return context;
                }
            }
            return context;
        }

        public Vector3 getHandPos()
        {
            return handPos;
        }

        public void findContext(Skeleton skeleton)
        {
            if (selectionCoolingDown && selectionCooldown.IsRunning && selectionCooldown.ElapsedMilliseconds < 4000)
                return;

            if (selectionCooldown.ElapsedMilliseconds > 2000 && selectionCoolingDown)
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

            float distanceHandHip = (hipL.X - handL.X)*(hipL.X - handL.X) + (hipL.Y - handL.Y)*(hipL.Y - handL.Y) +
                                    (hipL.Z - handL.Z)*(hipL.Z - handL.Z);

            ////Console.Out.WriteLine(Math.Sqrt(distanceHandHip));
            handPos = new Vector3(handV.X, handV.Y, handV.Z);

            if (distanceHandHip > 0.22)
            {
                return;
            }

            ////Console.Out.WriteLine("Hand pos: " + handV.X+ ","+handV.Y + "," +handV.Z);

            var rayOrig = new Vector3(elbowV.X, elbowV.Y, elbowV.Z);
            var rayDir = new Vector3(handV.X - rayOrig.X, handV.Y - rayOrig.Y, handV.Z - rayOrig.Z);


            rayDir.Normalize();

            var pick = new Ray(rayOrig, rayDir);

            currentContext = intersects(pick);

            if (!currentContext.Equals("__NOCONTEXT"))
            {
                pointing = true;
            }

            if (pointing)
            {
                ////Console.Out.WriteLine("Pointing at : " + currentContext);
                if (playSound)
                {
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
            else
            {
                if (pointedLastEvent)
                {
                    unselectSound.Play();
                    pointedLastEvent = false;
                }
                lastPickEvent++;
                selectionTimer.Stop();
            }
        }
    }
}