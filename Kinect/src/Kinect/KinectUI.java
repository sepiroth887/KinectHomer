package Kinect;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Window.Type;

public class KinectUI {
	public KinectUI(KinectSensorComponent kinectComp){
		
		final KinectSensorComponent kinect = kinectComp;
		
		JFrame frame = new JFrame("Kinect tools");
		frame.setType(Type.UTILITY);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JTextField gName = new JTextField();
		gName.setBounds(0, 0, 0, 0);
		gName.setMinimumSize(new Dimension(80,20));
		
		JTextField contextN = new JTextField();
		contextN.setBounds(0, 0, 0, 0);
		contextN.setMinimumSize(new Dimension(80,20));
		
		final JTextField gF = gName;
		final JTextField cF = contextN;
		frame.getContentPane().setLayout(null);
		
		frame.getContentPane().add(gName);
		frame.getContentPane().add(contextN);
		
		JButton recordGesture = new JButton("Record Gesture");
		recordGesture.setBounds(0, 0, 0, 0);
		recordGesture.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0){
				
				kinect.recordGesture(gF.getText(),cF.getText());
			}
		});
		
		JButton recognizeGesture = new JButton("Recognize Gesture");
		recognizeGesture.setBounds(0, 0, 123, 23);
		recognizeGesture.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent args0){
				kinect.recognizeGesture(cF.getText());
			}
		});
		
		JTextField skeleton = new JTextField();
		skeleton.setBounds(0, 0, 0, 0);
		skeleton.setMinimumSize(new Dimension(80,20));
		
		final JTextField skeletonFin = skeleton;
	
		
		JButton setTracking = new JButton("Track user");
		setTracking.setBounds(0, 0, 0, 0);
		setTracking.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg){
				int skelID = Integer.parseInt(skeletonFin.getText());
				kinect.setTracking(skelID);
			}
		});
		 
		frame.getContentPane().add(skeleton);
		frame.getContentPane().add(setTracking);
		frame.getContentPane().add(recordGesture);
		frame.getContentPane().add(recognizeGesture);
		
		frame.pack();
		frame.setVisible(true);
		
	}
}
