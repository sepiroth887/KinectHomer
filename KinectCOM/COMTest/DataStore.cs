using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Collections;
using Kinect;
using System.IO;

namespace KinectCOM
{
    class DataStore
    {
        // location of the user info file
        private static string user_xml_path = FileLoader.DEFAULT_PATH+"users.xml";
        private static string voice_commands_path = FileLoader.DEFAULT_PATH + "voiceCommands.cfg";


        /**
            Stores userdata passed to it via a Dictionary containing the user features
         */
        public void storeUserData(System.Collections.Generic.Dictionary<FeatureType,String> values){
        
            // create a new XMLDocument and try to load the content of the users.xml file
            XmlDocument xml = new XmlDocument();
            xml.Load(user_xml_path);

            // find the Nodes that contain the "user" tag
            XmlNodeList names = xml.GetElementsByTagName("user");

            bool userExists = false;
            int index = 0;

            // loop through the nodes and search for the users name to check whether it exists already
            for (int i = 0; i < names.Count; i++) {
                if(names[i].LastChild.InnerText.Equals(values[FeatureType.Face])){
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
            else {
                // a new users need a new user tag and all the feature tags as well.
                XmlNode root = xml.SelectSingleNode("/users");
                XmlElement newUser = xml.CreateElement("user");

                // go through each entry in the Feature dictionary and create new tags for the users.xml file
                foreach (KeyValuePair<FeatureType, String> entry in values) {
                    XmlElement newFeature = xml.CreateElement(entry.Key.ToString());
                    newFeature.InnerText = ""+entry.Value;
                    newUser.AppendChild(newFeature);
                }

                // append the new user to the XMLDocument
                root.AppendChild(newUser);
            }

            // save the changes to the users.xml file
            xml.Save(user_xml_path);
        }

        /**
         * loads all users from the users.xml file into a Dictionary of Dictionaries.
         * 
         **/
        public static Dictionary<String,Dictionary<FeatureType,String>> loadAllUsers(){
            // this dictionary contains the user name as a key to the dictionary with the individual features of that user.
            Dictionary<String, Dictionary<FeatureType, String>> users = new Dictionary<String, Dictionary<FeatureType, String>>();

            // create and load an XmlDocument containing all the user info
            XmlDocument xml = new XmlDocument();
            xml.Load(user_xml_path);

            // locate all tags "user"
            XmlNodeList userNodes = xml.GetElementsByTagName("user");

            // loop through all nodes and retrieve info stored.
            for (int i = 0; i < userNodes.Count; i++) {
                Dictionary<FeatureType, String> userFeatures = new Dictionary<FeatureType, String>();

                XmlNode s = userNodes[i].FirstChild;
                String ShoulderWidth = s.InnerText;

                XmlNode a = s.NextSibling;
                String ArmLength = a.InnerText;

                XmlNode h = a.NextSibling;
                String HipHeight = h.InnerText;

                XmlNode f = h.NextSibling;
                String Face = f.InnerText;

                userFeatures.Add(FeatureType.ShoulderWidth, ShoulderWidth);
                userFeatures.Add(FeatureType.ArmLength, ArmLength);
                userFeatures.Add(FeatureType.HipHeadHeight, HipHeight);
                userFeatures.Add(FeatureType.Face, Face);

                Console.Out.WriteLine("User in DB:"+Face);

                users.Add(Face, userFeatures);
                
            }

            // return the data.
            return users;
        }

        public DataStore()
        {
           //nothing here
        }

        public static string[] loadVoiceCommands()
        {
            string[] commands = null;

            if (!File.Exists(DataStore.voice_commands_path)) {
                Console.Out.WriteLine("Voice command config file does not exists");
                return null;
            }

            StreamReader reader = new StreamReader(DataStore.voice_commands_path);

            String line;
            int index = 0;

            while ((line = reader.ReadLine()) != null) {
                if (line.StartsWith("//"))
                {
                    String[] split = line.Split('=');
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
                else { 
                    commands[index++] = line;
                }
            }

            return commands;
        }

        public void storeVoiceCommand(String command)
        {
            StreamWriter writer = File.AppendText(DataStore.voice_commands_path);

            writer.WriteLine(command);

            writer.Flush();
            writer.Close();
        }
    }

}
