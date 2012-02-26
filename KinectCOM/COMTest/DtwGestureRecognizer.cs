//-----------------------------------------------------------------------
// <copyright file="DtwGestureRecognizer.cs" company="Rhemyst and Rymix">
//     Open Source. Do with this as you will. Include this statement or 
//     don't - whatever you like.
//
//     No warranty or support given. No guarantees this will work or meet
//     your needs. Some elements of this project have been tailored to
//     the authors' needs and therefore don't necessarily follow best
//     practice. Subsequent releases of this project will (probably) not
//     be compatible with different versions, so whatever you do, don't
//     overwrite your implementation with any new releases of this
//     project!
//
//     Enjoy working with Kinect!
// </copyright>
//-----------------------------------------------------------------------

using System;
using System.Collections;
using System.Collections.Generic;
using log4net;

namespace KinectCOM
{
    /// <summary>
    /// Dynamic Time Warping nearest neighbour sequence comparison class.
    /// Called 'Gesture Recognizer' but really it can work with any vectors
    /// </summary>
    internal class DtwGestureRecognizer
    {
        /*
         * By Rhemyst. Dude's a freakin' genius. Also he can do the Rubik's Cube. I mean REALLY do the Rubik's Cube.
         * 
         * http://social.msdn.microsoft.com/Forums/en-US/kinectsdknuiapi/thread/4a428391-82df-445a-a867-557f284bd4b1
         * http://www.youtube.com/watch?v=XsIoN96yF3E
         */

        private static readonly ILog Log = LogManager.GetLogger(typeof(DtwGestureRecognizer));

        /// <summary>
        /// Size of obeservations vectors.
        /// </summary>
        private readonly int _dimension;

        /// <summary>
        /// Maximum distance between the last observations of each sequence.
        /// </summary>
        private readonly double _firstThreshold;

        /// <summary>
        /// Maximum DTW distance between an example and a sequence being classified.
        /// </summary>
        private readonly double _globalThreshold;

        /// <summary>
        /// Maximum vertical or horizontal steps in a row.
        /// </summary>
        private readonly int _maxSlope;

        /// <summary>
        /// Minimum length of a gesture before it can be recognised
        /// </summary>
        private readonly double _minimumLength;


        private ArrayList _gestures;

        /// <summary>
        /// Initializes a new instance of the DtwGestureRecognizer class
        /// First DTW constructor
        /// </summary>
        /// <param name="dim">Vector size</param>
        /// <param name="threshold">Maximum distance between the last observations of each sequence</param>
        /// <param name="firstThreshold">Minimum threshold</param>
        public DtwGestureRecognizer(int dim, double threshold, double firstThreshold, double minLen)
        {
            _dimension = dim;
            _gestures = new ArrayList();
            _globalThreshold = threshold;
            _firstThreshold = firstThreshold;
            _maxSlope = int.MaxValue;
            _minimumLength = minLen;
        }

        /// <summary>
        /// Initializes a new instance of the DtwGestureRecognizer class
        /// Second DTW constructor
        /// </summary>
        /// <param name="dim">Vector size</param>
        /// <param name="threshold">Maximum distance between the last observations of each sequence</param>
        /// <param name="firstThreshold">Minimum threshold</param>
        /// <param name="ms">Maximum vertical or horizontal steps in a row</param>
        public DtwGestureRecognizer(int dim, double threshold, double firstThreshold, int ms, double minLen)
        {
            _dimension = dim;
            _gestures = new ArrayList();
            _globalThreshold = threshold;
            _firstThreshold = firstThreshold;
            _maxSlope = ms;
            _minimumLength = minLen;
        }

        /// <summary>
        /// Add a seqence with a label to the known sequences library.
        /// The gesture MUST start on the first observation of the sequence and end on the last one.
        /// Sequences may have different lengths.
        /// </summary>
        /// <param name="seq">The sequence</param>
        /// <param name="lab">Sequence name</param>
        /// <param name="ctxt"> </param>
        /// <param name="saveToFile"> </param>
        public void AddOrUpdate(ArrayList seq, string lab, string ctxt, bool saveToFile)
        {
                if (seq.Count == 0) return;
                // First we check whether there is already a recording for this label. If so overwrite it, otherwise add a new entry
                var existingIndex = -1;
                
                if(_gestures == null)
                {
                    _gestures = new ArrayList();
                }
                
            for (var i = 0; i < _gestures.Count; i++)
                {
                    var gesture = (Gesture) _gestures[i];

                    if (gesture != null && lab.Equals(gesture.Name) && ctxt.Equals(gesture.Context))
                    {
                        existingIndex = i;
                    }
                }

                Gesture g;
                // If we have a match then remove the entries at the existing index to avoid duplicates. We will add the new entries later anyway
                if (existingIndex >= 0)
                {
                    Log.Info("Found existing gesture, updating sequence.");
                    g = ((Gesture)_gestures[existingIndex]);
                    g.Sequence = seq;
                }
                else
                {
                    Log.Info("This is a new gesture, saving.");
                    g = new Gesture(lab, ctxt, seq);
                    _gestures.Add(g);
                }
                

            if(saveToFile)
                FileLoader.SaveGestures(RetrieveText());
            
        }

        public void AddOrUpdate(ArrayList seq, Gesture g)
        {
            AddOrUpdate(seq, g.Name, g.Context, true);
        }

        /// <summary>
        /// Recognize gesture in the given sequence.
        /// It will always assume that the gesture ends on the last observation of that sequence.
        /// If the distance between the last observations of each sequence is too great, or if the overall DTW distance between the two sequence is too great, no gesture will be recognized.
        /// </summary>
        /// <param name="seq">The sequence to recognise</param>
        /// <returns>The recognised gesture name</returns>
        public Gesture Recognize(ArrayList seq, string ctxt)
        {
            double minDist = double.PositiveInfinity;
            for (int i = 0; i < _gestures.Count; i++)
            {
                var gesture = (Gesture) _gestures[i];
                var example = gesture.Sequence;
                ////Debug.WriteLine(Dist2((double[]) seq[seq.Count - 1], (double[]) example[example.Count - 1]));
                if (Dist2((double[]) seq[seq.Count - 1], (double[]) example[example.Count - 1]) < _firstThreshold)
                {
                    double d = Dtw(seq, example)/example.Count;
                    if (d < minDist)
                    {
                        minDist = d;
                        if (gesture.Context == ctxt)
                        {
                            return gesture;
                        }
                    }
                }
            }

            return null;
        }

        /// <summary>
        /// Retrieves a text represeantation of the _label and its associated _sequence
        /// For use in dispaying debug information and for saving to file
        /// </summary>
        /// <returns>A string containing all recorded gestures and their names</returns>
        public string RetrieveText()
        {
            string retStr = string.Empty;

            int numGestures = _gestures.Count;

            retStr += "//numGestures=" + numGestures + "\r\n";

            if (_gestures != null)
            {
                // Iterate through each gesture
                for (int gestureNum = 0; gestureNum < numGestures; gestureNum++)
                {
                    // Echo the label
                    var gesture = (Gesture) _gestures[gestureNum];

                    retStr += "@" + gesture.Name + "\r\n";
                    retStr += "$" + gesture.Context + "\r\n";
                    int frameNum = 0;

                    //Iterate through each frame of this gesture
                    foreach (double[] frame in (gesture.Sequence))
                    {
                        // Extract each double
                        foreach (double dub in frame)
                        {
                            //Console.Out.WriteLine(dub);
                            retStr += dub + "\r\n";
                        }

                        // Signifies end of this double
                        retStr += "~\r\n";

                        frameNum++;
                    }

                    // Signifies end of this gesture
                    retStr += "----";
                    if (gestureNum < numGestures - 1)
                    {
                        retStr += "\r\n";
                    }
                }
            }

            return retStr;
        }

        /// <summary>
        /// Compute the min DTW distance between seq2 and all possible endings of seq1.
        /// </summary>
        /// <param name="seq1">The first array of sequences to compare</param>
        /// <param name="seq2">The second array of sequences to compare</param>
        /// <returns>The best match</returns>
        private double Dtw(ArrayList seq1, ArrayList seq2)
        {
            // Init
            if (seq1 == null || seq2 == null) return Double.PositiveInfinity;
            var seq1R = new ArrayList(seq1);
            seq1R.Reverse();
            var seq2R = new ArrayList(seq2);
            seq2R.Reverse();
            var tab = new double[seq1R.Count + 1,seq2R.Count + 1];
            var slopeI = new int[seq1R.Count + 1,seq2R.Count + 1];
            var slopeJ = new int[seq1R.Count + 1,seq2R.Count + 1];

            for (int i = 0; i < seq1R.Count + 1; i++)
            {
                for (int j = 0; j < seq2R.Count + 1; j++)
                {
                    tab[i, j] = double.PositiveInfinity;
                    slopeI[i, j] = 0;
                    slopeJ[i, j] = 0;
                }
            }

            tab[0, 0] = 0;

            // Dynamic computation of the DTW matrix.
            for (int i = 1; i < seq1R.Count + 1; i++)
            {
                for (int j = 1; j < seq2R.Count + 1; j++)
                {
                    if (tab[i, j - 1] < tab[i - 1, j - 1] && tab[i, j - 1] < tab[i - 1, j] &&
                        slopeI[i, j - 1] < _maxSlope)
                    {
                        tab[i, j] = Dist2((double[]) seq1R[i - 1], (double[]) seq2R[j - 1]) + tab[i, j - 1];
                        slopeI[i, j] = slopeJ[i, j - 1] + 1;
                        slopeJ[i, j] = 0;
                    }
                    else if (tab[i - 1, j] < tab[i - 1, j - 1] && tab[i - 1, j] < tab[i, j - 1] &&
                             slopeJ[i - 1, j] < _maxSlope)
                    {
                        tab[i, j] = Dist2((double[]) seq1R[i - 1], (double[]) seq2R[j - 1]) + tab[i - 1, j];
                        slopeI[i, j] = 0;
                        slopeJ[i, j] = slopeJ[i - 1, j] + 1;
                    }
                    else
                    {
                        tab[i, j] = Dist2((double[]) seq1R[i - 1], (double[]) seq2R[j - 1]) + tab[i - 1, j - 1];
                        slopeI[i, j] = 0;
                        slopeJ[i, j] = 0;
                    }
                }
            }

            // Find best between seq2 and an ending (postfix) of seq1.
            double bestMatch = double.PositiveInfinity;
            for (int i = 1; i < (seq1R.Count + 1) - _minimumLength; i++)
            {
                if (tab[i, seq2R.Count] < bestMatch)
                {
                    bestMatch = tab[i, seq2R.Count];
                }
            }

            return bestMatch;
        }

        /// <summary>
        /// Computes a 1-distance between two observations. (aka Manhattan distance).
        /// </summary>
        /// <param name="a">Point a (double)</param>
        /// <param name="b">Point b (double)</param>
        /// <returns>Manhattan distance between the two points</returns>
        private double Dist1(IList<double> a, IList<double> b)
        {
            double d = 0;
            for (int i = 0; i < _dimension; i++)
            {
                if (a != null && b != null) d += Math.Abs(a[i] - b[i]);
            }

            return d;
        }

        /// <summary>
        /// Computes a 2-distance between two observations. (aka Euclidian distance).
        /// </summary>
        /// <param name="a">Point a (double)</param>
        /// <param name="b">Point b (double)</param>
        /// <returns>Euclidian distance between the two points</returns>
        private double Dist2(IList<Double> a, IList<Double> b)
        {
            double d = 0;
            for (var i = 0; i < _dimension; i++)
            {
                if (a != null && b != null) d += Math.Pow(a[i] - b[i], 2);
            }

            return Math.Sqrt(d);
        }
    }
}