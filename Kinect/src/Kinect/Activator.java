/*
 * Created on Sat Jan 28 12:12:10 GMT 2012
 */
package Kinect;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import uk.ac.stir.cs.homer.homerFrameworkAPI.componentUtils.ComponentGateway;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Activator implements BundleActivator {

	
	private KinectSensorComponent kinect;
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		 
		kinect = new KinectSensorComponent();
		
		
		
		JFrame frame = new JFrame("Kinect tools");
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setPreferredSize(new Dimension(200,200));
		
		frame.setLayout(new GridLayout(6,1));
		
		JTextField gName = new JTextField();
		gName.setMinimumSize(new Dimension(80,20));
		
		JTextField contextN = new JTextField();
		contextN.setMinimumSize(new Dimension(80,20));
		
		final JTextField gF = gName;
		final JTextField cF = contextN;
		
		frame.add(gName);
		frame.add(contextN);
		
		JButton recordGesture = new JButton("Record Gesture");
		recordGesture.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0){
				
				kinect.recordGesture(gF.getText(),cF.getText());
			}
		});
		
		JButton recognizeGesture = new JButton("Recognize Gesture");
		recognizeGesture.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent args0){
				kinect.recognizeGesture(cF.getText());
			}
		});
		
		JTextField skeleton = new JTextField();
		skeleton.setMinimumSize(new Dimension(80,20));
		
		final JTextField skeletonFin = skeleton;
	
		
		JButton setTracking = new JButton("Track user");
		setTracking.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg){
				int skelID = Integer.parseInt(skeletonFin.getText());
				kinect.setTracking(skelID);
			}
		});
		 
		frame.add(skeleton);
		frame.add(setTracking);
		frame.add(recordGesture);
		frame.add(recognizeGesture);
		
		frame.pack();
		frame.setVisible(true);
		
		ComponentGateway.Singleton.get().registerComponent(kinect);
		
		
	}

	

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		
	}
}