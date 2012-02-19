package Kinect;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Window.Type;
import java.awt.Font;

public class KinectUI extends JFrame{
	
	private JTextField nameTF;
	private JTextField imagePathTF;
	private JTextField gNameTF;
	private JTextField gCtxtTF;
	private JTextField gPermTF;
	private final KinectSensorComponent kinect;
	private GestureListModel gestureModel;
	
	public KinectUI(KinectSensorComponent kinectComp) {
		setAlwaysOnTop(true);
		setTitle("Kinect Configuration");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setType(Type.POPUP);
		this.kinect = kinectComp;
		this.setMinimumSize(new Dimension(477,371));
		getContentPane().setLayout(null);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 471, 348);
		getContentPane().add(tabbedPane);
		
		JPanel userTab = new JPanel();
		tabbedPane.addTab("User settings", null, userTab, null);
		
		JPanel userListPanel = new JPanel();
		userListPanel.setBounds(10, 5, 114, 291);
		userListPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBounds(130, 149, 326, 147);
		infoPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		JPanel userPicPanel = new JPanel();
		userPicPanel.setBounds(292, 6, 164, 137);
		infoPanel.setLayout(new GridLayout(4, 2, 0, 0));
		
		JPanel namePanel = new JPanel();
		infoPanel.add(namePanel);
		namePanel.setLayout(null);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setBounds(10, 8, 85, 14);
		namePanel.add(lblName);
		
		nameTF = new JTextField();
		nameTF.setBounds(107, 5, 101, 20);
		nameTF.setHorizontalAlignment(SwingConstants.LEFT);
		nameTF.setAlignmentX(Component.LEFT_ALIGNMENT);
		namePanel.add(nameTF);
		nameTF.setColumns(20);
		nameTF.setMaximumSize(new Dimension(150,20));
		
		JPanel imagePanel = new JPanel();
		infoPanel.add(imagePanel);
		imagePanel.setLayout(null);
		
		JLabel imageLbl = new JLabel("ImageDB: ");
		imageLbl.setBounds(10, 9, 92, 14);
		imagePanel.add(imageLbl);
		
		imagePathTF = new JTextField();
		imagePathTF.setBounds(107, 6, 101, 20);
		imagePathTF.setMaximumSize(new Dimension(100, 20));
		imagePathTF.setHorizontalAlignment(SwingConstants.LEFT);
		imagePathTF.setColumns(20);
		imagePathTF.setAlignmentX(0.0f);
		imagePanel.add(imagePathTF);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setFont(new Font("Dialog", Font.BOLD, 10));
		btnBrowse.setBounds(220, 5, 87, 23);
		imagePanel.add(btnBrowse);
		userListPanel.setLayout(new BorderLayout(5, 5));
		
		JList userList = new JList();
	
		userList.setModel(new AbstractListModel() {
			String[] values = new String[] {"User"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		userTab.setLayout(null);
		userListPanel.add(userList, BorderLayout.CENTER);
		userTab.add(userListPanel);
		userTab.add(infoPanel);
		userTab.add(userPicPanel);
		
		JPanel gestureTab = new JPanel();
		tabbedPane.addTab("Gesture settings", null, gestureTab, null);
		
		JPanel gListPanel = new JPanel();
		gListPanel.setBounds(10, 11, 214, 285);
		gListPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JList<Gesture> list = new JList<Gesture>();
		
		gestureModel = new GestureListModel(kinect.loadGestures());
	
		list.setVisibleRowCount(40);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(gestureModel);
		
		
		
		JPanel gActionPanel = new JPanel();
		gActionPanel.setBounds(234, 11, 222, 285);
		gActionPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		JPanel gNamePanel = new JPanel();
		gNamePanel.setBounds(12, 7, 198, 20);
		
		JPanel gCtxtPanel = new JPanel();
		gCtxtPanel.setBounds(12, 33, 198, 20);
		gCtxtPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblContext = new JLabel("Context: ");
		gCtxtPanel.add(lblContext);
		
		gCtxtTF = new JTextField();
		gCtxtTF.setColumns(10);
		gCtxtPanel.add(gCtxtTF);
		
		JPanel gPermPanel = new JPanel();
		gPermPanel.setBounds(12, 59, 198, 20);
		gPermPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblPermission = new JLabel("Permission: ");
		gPermPanel.add(lblPermission);
		
		gPermTF = new JTextField();
		gPermTF.setColumns(10);
		gPermPanel.add(gPermTF);
		
		JButton btnRecordGesture = new JButton("Record Gesture");
		btnRecordGesture.setBounds(44, 251, 144, 23);
		
	
		gNamePanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblName_1 = new JLabel("Name: ");
		gNamePanel.add(lblName_1);
		
		gNameTF = new JTextField();
		gNamePanel.add(gNameTF);
		gNameTF.setColumns(10);
		gActionPanel.setLayout(null);
		gActionPanel.add(gCtxtPanel);
		gActionPanel.add(gNamePanel);
		gActionPanel.add(gPermPanel);
		gActionPanel.add(btnRecordGesture);
		gestureTab.setLayout(null);
		gListPanel.setLayout(new BorderLayout(0, 0));
		gListPanel.add(list);
		gestureTab.add(gListPanel);
		gestureTab.add(gActionPanel);
		
		JPanel bindingTab = new JPanel();
		tabbedPane.addTab("Gesture binding", null, bindingTab, null);
		bindingTab.setLayout(null);
		
		

		final JTextField gName = gNameTF;
		final JTextField gCtxt = gCtxtTF;
		final JTextField gPerm = gPermTF;
		
		
		list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				@SuppressWarnings("unchecked")
				JList<Gesture> list = (JList<Gesture>)arg0.getSource();
				
				Gesture g = list.getSelectedValue();
				
				gName.setText(g.getName());
				gCtxt.setText(g.getContext());
				gPerm.setText(g.getPermission());
			}
			
		});
		
		btnRecordGesture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = gName.getText();
				String context = gCtxt.getText();
				kinect.recordGesture(name,context);
			}
		});
		
		this.setVisible(true);
	}
	
	
	public void updateGestureModel(){
		
		Gesture g = new Gesture(gNameTF.getText(),gCtxtTF.getText());
		gestureModel.updateModel(g);
	}
	
}
