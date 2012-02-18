using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Emgu.CV;
using Emgu.CV.Structure;
using System.IO;
using Microsoft.Xna.Framework;
using System.Collections;

namespace Kinect
{

    static class FileLoader
    {
        /// <summary>
        /// static method to load all images of users from the hard disk.
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        public static Dictionary<Image<Gray, byte>[],String[]> loadFaceDB(string path) { 
            // get the directory info of the passed path
            DirectoryInfo dir = new DirectoryInfo(path);

           
            Dictionary<Image<Gray, byte>[], String[]> database = new Dictionary<Image<Gray, byte>[], string[]>();
           
            // go through each sub directory and retrieve all PNG files.
            foreach (DirectoryInfo dirs in dir.GetDirectories()) {
                Image<Gray,byte>[] images = new Image<Gray,byte>[dirs.GetFiles("*.png").Count()];
                String[] label = new String[images.Length];

                //loop though all files in the subfolder 
                int counter = 0;
                foreach (FileInfo file in dirs.GetFiles("*.png")) {
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

        public static readonly String DEFAULT_PATH = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData) + "\\KinectHomer\\";
        /// <summary>
        /// static method to load all images of users from the hard disk.
        /// </summary>
        /// <param name="path"></param>
        /// <returns></returns>
        /// 
        public enum Units { mm = 1000, cm = 100, m = 1 };

        public static Dictionary<String, Vector3[]> loadObj(String file, Units unit)
        {

            DirectoryInfo dir = new DirectoryInfo(FileLoader.DEFAULT_PATH);

            if (!dir.Exists)
            {
                return null;
            }

            StreamReader reader = new StreamReader(FileLoader.DEFAULT_PATH + "" + file);

            String line;
            bool objectFound = false;
            String objectName = "";
            ArrayList points = new ArrayList();
            Dictionary<String, Vector3[]> objects = new Dictionary<String, Vector3[]>();

            while ((line = reader.ReadLine()) != null)
            {


                if (line.StartsWith("#"))
                {
                    Console.Out.WriteLine(line);
                    if (line.Contains("object"))
                    {
                        objectFound = true;

                        objectName = line.Split(' ')[2];
                    }

                    if (line.Contains("vertices") && objectFound)
                    {
                        objectFound = false;

                        Vector3[] pointV = new Vector3[points.Count];

                        points.CopyTo(pointV);

                        objects.Add(objectName, pointV);
                        points.Clear();
                    }

                }
                else if (objectFound && line.StartsWith("v"))
                {
                    String[] parts = line.Split(' ');

                    Vector3 point = new Vector3();

                    point.X = (float.Parse(parts[2])) / (float)unit;
                    point.Y = float.Parse(parts[3]) / (float)unit;
                    point.Z = (float.Parse(parts[4]) / (float)unit);

                    points.Add(point);
                }


            }

            return objects;
        }

        public static void saveObject(String path,int context, Vector3[] points) {
            DirectoryInfo dir = new DirectoryInfo(path);

            if (!dir.Exists)
            {
                throw new Exception("Cannot load objects. Invalid path:" + path);
            }

            System.IO.StreamWriter file = File.AppendText(dir.FullName + "\\objects.hos");

            file.Write(context + ":");
            for (int i = 0; i < points.Length; i++) {
                if (i + 1 == points.Length)
                {
                    file.Write(points[i].X + ";" + points[i].Y + ";" + points[i].Z);
                }
                else {
                    file.Write(points[i].X + ";" + points[i].Y + ";" + points[i].Z + ",");
                }
                
            }
            file.WriteLine();
            file.Flush();
            file.Close();
        }

        public static Dictionary<int, Vector3[]> loadObjects(String path)
        {
            
            DirectoryInfo dir = new DirectoryInfo(path);

            if (!dir.Exists) {
                throw new Exception("Cannot load objects. Invalid path:" + path);
            }

            Dictionary<int, Vector3[]> objects = new Dictionary<int, Vector3[]>();
            Vector3[] vectors;

            System.IO.StreamReader file = new StreamReader(dir.FullName+"\\objects.hos");
            String line;
            while ((line = file.ReadLine()) != null) {
                if (line.StartsWith("//"))
                {
                    Console.Out.WriteLine(line);

                }
                else {
                        
                    String[] split1 = line.Split(':');

                    int context = Int32.Parse(split1[0]);

                    String[] vStrings = split1[1].Split(',');

                    vectors = new Vector3[vStrings.Length];

                    for (int i = 0; i < vStrings.Length; i++) {

                        String[] components = vStrings[i].Split(';');

                        float x = float.Parse(components[0]);
                        float y = float.Parse(components[1]);
                        float z = float.Parse(components[2]);

                        vectors[i] = new Vector3(x, y, z);

                    }

                    objects.Add(context, vectors);


                }


            }

            file.Close();
            return objects;
        }

        public static Dictionary<Image<Rgb, byte>[], String[]> loadFaceDBC(string path)
        {
            // get the directory info of the passed path
            DirectoryInfo dir = new DirectoryInfo(path);


            Dictionary<Image<Rgb, byte>[], String[]> database = new Dictionary<Image<Rgb, byte>[], string[]>();

            // go through each sub directory and retrieve all PNG files.
            foreach (DirectoryInfo dirs in dir.GetDirectories())
            {
                Image<Rgb, byte>[] images = new Image<Rgb, byte>[dirs.GetFiles("*.pgm").Count()];
                String[] label = new String[images.Length];

                //loop though all files in the subfolder 
                int counter = 0;
                foreach (FileInfo file in dirs.GetFiles("*.pgm"))
                {
                    // and store each png file as an image in the images array.
                    images[counter] = new Image<Rgb, byte>(file.FullName);
                    // and the folder name as the label for that file.
                    label[counter++] = dirs.Name;
                }

                // add the arrays to the database.
                database.Add(images, label);

            }

            // return the loaded database.
            return database;

        }

        /// <summary>
        /// Saves a set of images for one user and its name to the file path
        /// </summary>
        /// <param name="database"></param>
        /// <param name="path">file path to store the images</param>
        public static void saveFaceDB(Dictionary<Image<Gray, byte>[], String[]> database, string path)
        {
            
            // loop though each entry in the dictionary 
            foreach (KeyValuePair<Image<Gray, byte>[], String[]> entry in database) {
              
                // and get the name of the user as the folder name
                String folderName = entry.Value[0];

                DirectoryInfo dir = new DirectoryInfo(path+"/"+folderName);

                int numFiles = 0;

                // check whether the folder already exists or create it if not.
                if (!dir.Exists)
                {
                    dir.Create();
                }
                else {
                    // retrieve the number of images already stored 
                    numFiles = dir.GetFiles("*.png").Count();
                }

                // loop through all images and save them with a number
                for (int i = 0; i < entry.Key.Length; i++) { 
                    entry.Key[i].Save(dir.FullName+"/"+(numFiles++)%200+".png");
                }

            }
            

        }


        /// <summary>
        /// Adds an individual image to the local userDB folder.
        /// </summary>
        /// <param name="label"></param>
        /// <param name="image"></param>
        /// <param name="path"></param>
        public static void saveImage(String label, Image<Gray, byte> image, String path) {
            String folderName = label;
            int numFiles = 0;
            DirectoryInfo dir = new DirectoryInfo(path + "/" + folderName);

            if (!dir.Exists)
            {
                dir.Create();
            }
            else
            {
                numFiles = dir.GetFiles("*.png").Count();
            }

           
            image.Save(dir.FullName + "/" + (numFiles++)%200 + ".png");
          
            
        }
    }
}
