using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using DTWGestureRecognition;
using Emgu.CV;
using Emgu.CV.Structure;
using Microsoft.Xna.Framework;

namespace KinectCOM
{
    internal static class FileLoader
    {
        #region Units enum

        public enum Units
        {
            mm = 1000,
            cm = 100,
            m = 1
        };

        #endregion

        public static readonly string DefaultPath =
            Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData) + "\\KinectHomer\\";

        /// <summary>
        /// static method to load all images of users from the hard disk.
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public static Dictionary<Image<Gray, byte>[], string[]> LoadFaceDB(string path)
        {
            // get the directory info of the passed path
            var dir = new DirectoryInfo(path);


            var database = new Dictionary<Image<Gray, byte>[], string[]>();

            // go through each sub directory and retrieve all PNG files.
            foreach (var dirs in dir.GetDirectories())
            {
                var images = new Image<Gray, byte>[dirs.GetFiles("*.png").Count()];
                var label = new string[images.Length];

                //loop though all files in the subfolder 
                int counter = 0;
                foreach (var file in dirs.GetFiles("*.png"))
                {
                    // and store each png file as an image in the images array.
                    images[counter] = new Image<Gray, byte>(file.FullName);
                    // and the folder name as the label for that file.
                    label[counter++] = dirs.Name;
                }

                // add the arrays to the database.
                database.Add(images, label);
            }

            // return the loaded database.
            return database;
        }

        public static Dictionary<string, Vector3[]> LoadObj(string file, Units unit)
        {
            Console.Out.WriteLine("loading objects in room");
            var dir = new DirectoryInfo(DefaultPath);

            if (!dir.Exists)
            {
                Console.Out.WriteLine("Directory doesn't exist: "+DefaultPath);
                return null;
            }

            var reader = new StreamReader(DefaultPath + "" + file);

            string line;
            var objectFound = false;
            var objectName = "";
            var points = new ArrayList();
            var objects = new Dictionary<string, Vector3[]>();

            while ((line = reader.ReadLine()) != null)
            {
                if (line.StartsWith("#"))
                {
                    //Console.Out.WriteLine(line);
                    if (line.Contains("object"))
                    {
                        objectFound = true;

                        objectName = line.Split(' ')[2];
                    }

                    if (line.Contains("vertices") && objectFound)
                    {
                        objectFound = false;

                        var pointV = new Vector3[points.Count];

                        points.CopyTo(pointV);

                        if (objectName != null && !objects.ContainsKey(objectName)) objects.Add(objectName, pointV);
                        points.Clear();
                    }
                }
                else if (objectFound && line.StartsWith("v"))
                {
                    string[] parts = line.Split(' ');

                    var point = new Vector3
                                    {
                                        X = (float.Parse(parts[2]))/(float) unit,
                                        Y = float.Parse(parts[3])/(float) unit,
                                        Z = (float.Parse(parts[4])/(float) unit)
                                    };


                    points.Add(point);
                }
            }

            return objects;
        }

        /// <summary>
        /// Saves a set of images for one user and its name to the file path
        /// </summary>
        /// <param name="database"></param>
        /// <param name="path">file path to store the images</param>
        public static void SaveFaceDB(Dictionary<Image<Gray, byte>[], string[]> database, string path)
        {
            // loop though each entry in the dictionary 
            if (database != null)
                foreach (var entry in database)
                {
                    // and get the name of the user as the folder name
                    if (entry.Value != null)
                    {
                        var folderName = entry.Value[0];

                        var dir = new DirectoryInfo(path + "/" + folderName);

                        int numFiles = 0;

                        // check whether the folder already exists or create it if not.
                        if (!dir.Exists)
                        {
                            dir.Create();
                        }
                        else
                        {
                            // retrieve the number of images already stored 
                            numFiles = dir.GetFiles("*.png").Count();
                        }

                        // loop through all images and save them with a number
                        if (entry.Key != null)
                            foreach (var t in entry.Key.Where(t => t != null))
                            {
                                if (t != null) t.Save(dir.FullName + "/" + (numFiles++)%200 + ".png");
                            }
                    }
                }
        }


        /// <summary>
        /// Adds an individual image to the local userDB folder.
        /// </summary>
        /// <param name="label"></param>
        /// <param name="image"></param>
        /// <param name="path"></param>
        public static void SaveImage(string label, Image<Gray, byte> image, string path)
        {
            string folderName = label;
            int numFiles = 0;
            var dir = new DirectoryInfo(path + "/" + folderName);

            if (!dir.Exists)
            {
                dir.Create();
            }
            else
            {
                numFiles = dir.GetFiles("*.png").Count();
            }


            if (image != null) image.Save(dir.FullName + "/" + (numFiles)%200 + ".png");
        }

        public static string[] LoadGestures(DtwGestureRecognizer dtw)
        {   
            if(dtw == null )
            {
                Console.Out.WriteLine("DtwGestureRecognizer is null");
                return null;
            }

            Console.Out.WriteLine("Loading gestures");
            string[] result = null;
            int itemCount = 0;
            string line;
            var gestureName = "";
            var contextName = "";
            // TODO I'm defaulting this to 12 here for now as it meets my current need but I need to cater for variable lengths in the future
            var frames = new ArrayList();
            var items = new double[12];

            // Read the file and display it line by line.
            if (!File.Exists(DefaultPath + "gestures.sav"))
            {
                Console.Out.WriteLine("Gesture file not found");
                return null;
            }

            var file = new StreamReader(DefaultPath + "gestures.sav");

            int counter = 0;

            while ((line = file.ReadLine()) != null)
            {
                if (line.StartsWith("//"))
                {
                    int numGestures = int.Parse(line.Split('=')[1]);
                    if (numGestures != 0)
                    {
                        result = new string[numGestures];
                    }
                    else
                    {
                        return null;
                    }
                    continue;
                }
                if (line.StartsWith("@"))
                {
                    gestureName = line.Substring(1);
                    result[counter] = gestureName;
                    continue;
                }

                if (line.StartsWith("$"))
                {
                    contextName = line.Substring(1);
                    result[counter] += ";" + contextName;
                    continue;
                }

                if (line.StartsWith("~"))
                {
                    frames.Add(items);
                    itemCount = 0;
                    items = new double[12];
                    continue;
                }

                if (!line.StartsWith("----"))
                {
                    items[itemCount] = Double.Parse(line);
                }

                itemCount++;

                if (line.StartsWith("----"))
                {
                    if (frames.Count == 0) continue;
                    Console.Out.WriteLine("Gesture frames loaded. Saving...");
                    dtw.AddOrUpdate(frames, gestureName, contextName, false);
                    frames = new ArrayList();
                    gestureName = string.Empty;
                    contextName = string.Empty;
                    counter++;
                    itemCount = 0;
                }
            }

            Console.Out.WriteLine(counter + " Gestures loaded");
            file.Close();
            return result;
        }

        public static void SaveGestures(string gestureData)
        {
            File.WriteAllText(DefaultPath + "gestures.sav", gestureData);
            //Console.Out.WriteLine("Gestures saved");
        }
    }
}