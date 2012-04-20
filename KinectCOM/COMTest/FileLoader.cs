using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Xml;
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

        private static readonly string UserXmlPath = DefaultPath + "users.xml";
        private static readonly string VoiceCommandsPath = DefaultPath + "voiceCommands.cfg";

        /// <summary>
        /// static method to load all images of users from the hard disk.
        /// </summary>
        /// <returns></returns>
        public static Dictionary<Image<Gray, byte>[], string[]> LoadFaceDB()
        {
            
            // get the directory info of the passed path
            var dir = new DirectoryInfo(DefaultPath+"FaceDB");


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
            Log.Info("loading objects in room");
            var dir = new DirectoryInfo(DefaultPath);

            if (!File.Exists(DefaultPath+file))
            {
                Log.Error("Directory doesn't exist: "+DefaultPath);
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

        public static void StoreUserData(Dictionary<FeatureType, string> values)
        {
            // create a new XMLDocument and try to load the content of the users.xml file
            var xml = new XmlDocument();
            xml.Load(UserXmlPath);

            // find the Nodes that contain the "user" tag
            XmlNodeList names = xml.GetElementsByTagName("user");

            bool userExists = false;
            int index = 0;

            // loop through the nodes and search for the users name to check whether it exists already
            for (int i = 0; i < names.Count; i++)
            {
                if (names[i].LastChild.InnerText.Equals(values[FeatureType.Face]))
                {
                    userExists = true;
                    index = i;
                    break;
                }
            }

            // if a user exists in the XML file just update the features.
            if (userExists)
            {
                XmlNode s = names[index].FirstChild;
                s.InnerText = values[FeatureType.ShoulderWidth];
                XmlNode a = s.NextSibling;
                a.InnerText = values[FeatureType.ArmLength];
                XmlNode h = a.NextSibling;
                h.InnerText = values[FeatureType.HipHeadHeight];
            }
            else
            {
                // a new users need a new user tag and all the feature tags as well.
                XmlNode root = xml.SelectSingleNode("/users");
                XmlElement newUser = xml.CreateElement("user");

                // go through each entry in the Feature dictionary and create new tags for the users.xml file
                foreach (var entry in values)
                {
                    XmlElement newFeature = xml.CreateElement(entry.Key.ToString());
                    newFeature.InnerText = "" + entry.Value;
                    newUser.AppendChild(newFeature);
                }

                // append the new user to the XMLDocument
                root.AppendChild(newUser);
            }

            // save the changes to the users.xml file
            xml.Save(UserXmlPath);
        }

        public static void RemoveUser(string user)
        {
            // create a new XMLDocument and try to load the content of the users.xml file
            var xml = new XmlDocument();
            xml.Load(UserXmlPath);

            // find the Nodes that contain the "user" tag
            XmlNodeList names = xml.GetElementsByTagName("user");

            bool userExists = false;
            int index = 0;

            // loop through the nodes and search for the users name to check whether it exists already
            for (int i = 0; i < names.Count; i++)
            {
                if (names[i].LastChild.InnerText.Equals(user))
                {
                    userExists = true;
                    index = i;
                    break;
                }
            }

            if(userExists)
            {
                var delNode = names[index];
                XmlNode root = xml.SelectSingleNode("/users");
                root.RemoveChild(delNode);
            }

            xml.Save(UserXmlPath);
        }

        /**
         * loads all users from the users.xml file into a Dictionary of Dictionaries.
         * 
         **/

        public static Dictionary<string, Dictionary<FeatureType, string>> LoadAllUsers()
        {
            // this dictionary contains the user name as a key to the dictionary with the individual features of that user.
            var users = new Dictionary<string, Dictionary<FeatureType, string>>();

            // create and load an XmlDocument containing all the user info
            var xml = new XmlDocument();
            xml.Load(UserXmlPath);
            
            // locate all tags "user"
            XmlNodeList userNodes = xml.GetElementsByTagName("user");

            // loop through all nodes and retrieve info stored.
            for (int i = 0; i < userNodes.Count; i++)
            {
                var userFeatures = new Dictionary<FeatureType, string>();

                XmlNode s = userNodes[i].FirstChild;
                string ShoulderWidth = s.InnerText;

                XmlNode a = s.NextSibling;
                string ArmLength = a.InnerText;

                XmlNode h = a.NextSibling;
                string HipHeight = h.InnerText;

                XmlNode f = h.NextSibling;
                string Face = f.InnerText;

                userFeatures.Add(FeatureType.ShoulderWidth, ShoulderWidth);
                userFeatures.Add(FeatureType.ArmLength, ArmLength);
                userFeatures.Add(FeatureType.HipHeadHeight, HipHeight);
                userFeatures.Add(FeatureType.Face, Face);

                //Console.Out.WriteLine("User in DB:"+Face);

                users.Add(Face, userFeatures);
            }

            // return the data.
            return users;
        }

        public static string[] LoadVoiceCommands()
        {
            string[] commands = null;

            if (!File.Exists(VoiceCommandsPath))
            {
                Log.Error("Voice command config file does not exists");
                return null;
            }

            var reader = new StreamReader(VoiceCommandsPath);

            string line;
            int index = 0;

            while ((line = reader.ReadLine()) != null)
            {
                if (line.StartsWith("//"))
                {
                    string[] split = line.Split('=');
                    int numCommands = 0;
                    try
                    {
                        numCommands = int.Parse(split[1]);
                    }
                    catch (Exception ex)
                    {
                        Console.Out.WriteLine(ex.Message);
                        return null;
                    }

                    commands = new string[numCommands];
                    
                }
                else
                {
                    commands[index++] = line;
                    Log.Info("Command added: " + line);
                }
            }

            return commands;
        }

        public static void StoreVoiceCommand(string command)
        {
            StreamWriter writer = File.AppendText(VoiceCommandsPath);

            writer.WriteLine(command);

            writer.Flush();
            writer.Close();
        }


        public static void SaveObject(String file, Vector3[] getCorners, string contextName)
        {
            var writer = File.AppendText(DefaultPath+""+file);

            var outStr ="\n\r";

            outStr += "#\n\r# object " + contextName + "\n\r\n\r";

            foreach(var point in getCorners)
            {
                outStr += "v " + point.X*(float) Units.cm + " " + point.Y*(float) Units.cm + " " +
                          point.Z*(float) Units.cm + "\n\r";
            }
            
            outStr+="# "+getCorners.Length+" vertices";

            writer.WriteLine(outStr);

            writer.Flush();

            writer.Close();
           

        }
    }
}