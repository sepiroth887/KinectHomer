using System;
using System.Collections;

namespace KinectCOM
{
    public class Gesture
    {
        private ArrayList sequence;

        public Gesture(String name, String context)
        {
            Name = name;
            Context = context;
        }

        public Gesture(String name, String context, ArrayList seq)
        {
            Name = name;
            Context = context;
            sequence = (ArrayList) seq.Clone();
        }

        public String Name { get; set; }

        public String Context { get; set; }

        public ArrayList Sequence
        {
            get { return sequence; }
            set { sequence = (ArrayList) value.Clone(); }
        }
    }
}