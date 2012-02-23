using System;
using System.Collections;

namespace KinectCOM
{
    public class Gesture
    {
        private ArrayList _sequence;

        public Gesture(String name, String context)
        {
            Name = name;
            Context = context;
        }

        public Gesture(String name, String context, ArrayList seq)
        {
            Name = name;
            Context = context;
            if (seq != null) _sequence = (ArrayList) seq.Clone();
        }

        public String Name { get; set; }

        public String Context { get; set; }

        public ArrayList Sequence
        {
            get { return _sequence; }
            set { if (value != null) _sequence = (ArrayList) value.Clone(); }
        }
    }
}