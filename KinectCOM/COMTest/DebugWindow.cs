using System.Drawing;
using System.Windows.Forms;

namespace KinectCOM
{
    public partial class DebugWindow : Form
    {
        public DebugWindow()
        {
            InitializeComponent();
        }

        public void drawImage(Bitmap img)
        {
            if(img != null)
                pictureBox1.Image = img;
        }

        public void drawFace(Bitmap img)
        {
            if(img != null)
                pictureBox2.Image = img;
        }
    }
}
