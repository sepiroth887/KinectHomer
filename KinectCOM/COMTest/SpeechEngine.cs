using System;
using System.Collections.Generic;
using System.Linq;
using System.Speech.Recognition;
using System.Text;

namespace KinectCOM
{
    class SpeechEngine
    {
        private SpeechRecognitionEngine _recognition;
        private Grammar _grammar;
        private GrammarBuilder _commands;
        private KinectHandler _kinectHandler;

        public SpeechEngine(KinectHandler handler)
        {
            _kinectHandler = handler;
            _commands = new GrammarBuilder();
            _grammar = new Grammar(_commands);
            _grammar.SpeechRecognized += new EventHandler<SpeechRecognizedEventArgs>(_grammar_SpeechRecognized);
        }

        void _grammar_SpeechRecognized(object sender, SpeechRecognizedEventArgs e)
        {
            throw new NotImplementedException();
        }

        public void SetCommands(String[] commands)
        {
            foreach(var cmd in commands)
            {
                _commands.Append(cmd);
            }

        }
    }
}
