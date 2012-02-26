using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using DTWGestureRecognition;
using Emgu.CV;
using Emgu.CV.Structure;
using Microsoft.Xna.Framework;
using log4net;

namespace KinectCOM
{
    internal static class FileLoader
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(FileLoader));
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
            if (path == null) return null;
            // get the directory info of the passed path
            var dir = new DirectoryInfo(path);


            var database = new Dictionary<Image<Gray, byte>[], string[]>();

            // go through each sub directory and retrieve all PNG files.
            foreach (var dirs in dir.GetDirectories())
            {
                if (dirs == null) continue;
                var images = new Image<Gray, byte>[dirs.GetFiles("*.png").Count()];
                var label = new string[images.Length];

                //loop though all files in the subfolder 
                int counter = 0;
                foreach (var file in dirs.GetFiles("*.png"))
                {
                    // and store each png file as an image in the images array.
                    if (file == null) continue;
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
                    var parts = line.Split(' ');
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
            var folderName = label;
            var numFiles = 0;
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
                Log.Error("DtwGestureRecognizer is null");
                return null;
            }

            Log.Debug("Loading gestures");
            string[] result = null;
            var itemCount = 0;
            string line;
            var gestureName = "";
            var contextName = "";
            // TODO I'm defaulting this to 12 here for now as it meets my current need but I need to cater for variable lengths in the future
            var frames = new ArrayList();
            var items = new double[12];

            // Read the file and display it line by line.
            if (!File.Exists(DefaultPath + "gestures.sav"))
            {
                Log.Error("Gesture file not found");
                return null;
            }

            var file = new StreamReader(DefaultPath + "gestures.sav");

            var counter = 0;

            while ((line = file.ReadLine()) != null)
            {
                if (line.StartsWith("//"))
                {
                    var numGestures = Int32.Parse(line.Split('=')[1]);
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
                    if (result != null) result[counter] = gestureName;
                    continue;
                }

                if (line.StartsWith("$"))
                {
                    contextName = line.Substring(1);
                    if (result != null) result[counter] += ";" + contextName;
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
                    Log.Debug("Gesture frames loaded. Saving...");
                    dtw.AddOrUpdate(frames, gestureName, contextName, false);
                    frames = new ArrayList();
                    gestureName = String.Empty;
                    contextName = String.Empty;
                    counter++;
                    itemCount = 0;
                }
            }

            Log.Debug(counter + " Gestures loaded");
            file.Close();
            return result;
        }

        public static void SaveGestures(string gestureData)
        {
            File.WriteAllText(DefaultPath + "gestures.sav", gestureData);
            Log.Debug("Gestures saved");
        }
    }
}