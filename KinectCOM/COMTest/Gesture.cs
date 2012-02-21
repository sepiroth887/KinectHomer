using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace KinectCOM
{
    public class Gesture
    {
        private String name;
        private String context;
        private ArrayList sequence;
        
        public Gesture(String name, String context) {
            this.name = name;
            this.context = context;
        }

        public Gesture(String name, String context,ArrayList seq)
        {
            this.name = name;
            this.context = context;
            this.sequence = (ArrayList) seq.Clone();
        }

        public String Name {
            get { return name; }
            set { name = value; }
        }

        public String Context
        {
            get { return context; }
            set { context = value; }
        }

        public ArrayList Sequence {
            get { return sequence; }
            set { sequence = (ArrayList)value.Clone(); }
        }
    }
}
