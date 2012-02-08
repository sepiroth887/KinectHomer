package uk.ac.stir.cs.homer.userInterfaceJavaClient.deviceManagement;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import uk.ac.stir.cs.homer.serviceDatabase.objects.Location;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDevice;
import uk.ac.stir.cs.homer.serviceDatabase.objects.UserDeviceType;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageFileViewer;
import uk.ac.stir.cs.homer.userInterfaceJavaClient.Image.ImageIconUtils;

public class ChooseImageButton extends Observable {

	private JPanel masterPanel;
	private final UserDevice userDevice;
	private final Location location;
	private final UserDeviceType userDeviceType;
	private File imagePath;
	private boolean imageNeedsSet = true;
	private JButton imageButton;
	private int imageWidth = 100;
	
	public ChooseImageButton(UserDevice userDevice,UserDeviceType userDeviceType, Location location) {
				this.userDevice = userDevice;
				this.userDeviceType = userDeviceType;
				this.location = location;
				createPanel();
	}

	public ChooseImageButton(UserDeviceType userDeviceType, Location location) {
		this.userDevice = null;
		this.userDeviceType = userDeviceType;
		this.location = location;
		createPanel();
	}

	public ChooseImageButton(Location location) {
		this.userDevice = null;
		this.userDeviceType = null;
		this.location = location;
		createPanel();
	}

	public ChooseImageButton(String currentImagePath) {
		this.userDevice = currentImagePath==null || currentImagePath.isEmpty()? null : new UserDevice(null, null, null, null, currentImagePath, null, null,null);
		this.userDeviceType = null;
		this.location = null;
		createPanel();
	}
	public void setIconWidth(int width) {
		imageWidth = width;
		if (imageButton.getIcon() != null)
		{
			updateImage();
		}
	}
	
	private void createPanel()
	{
		imageButton = new JButton();
		String fileName = (userDevice!=null? userDevice.getImage() : userDeviceType!=null? userDeviceType.getImage() : location!=null? location.getImage() : null);
		
		if (fileName == null)
		{
			imageButton.setText("Choose Image");
			imagePath = new File(ImageIconUtils.IMAGES_LOCATION);
		}
		else
		{
			imagePath = createFile(fileName);
			updateImage();
		}
		masterPanel = new JPanel();
		masterPanel.add(imageButton);
		imageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser(imagePath);
				fc.setFileView(new ImageFileViewer(masterPanel));
				fc.setFileFilter(new FileFilter() {

					@Override
					public boolean accept(File f) {
						return f.isDirectory() || 
							f.isFile() && f.getName().toLowerCase().endsWith(".png");
					}

					@Override
					public String getDescription() {
						return "PNG";
					}
				});
				int returnVal = fc.showOpenDialog(masterPanel);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            imagePath = fc.getSelectedFile();
		            updateImage();
		            updateObservers();
		        } 
			}
		});
	}
	private File createFile(String fileName) {
		return new File(ImageIconUtils.IMAGES_LOCATION + File.separator + fileName + ".png");
	}

	private void updateObservers()
	{
		this.setChanged();
        this.notifyObservers(getImageName());
	}
	private void updateImage() {
		imageButton.setIcon(ImageIconUtils.scaleImageIcon(imageWidth, getImageName()));
		if (imageNeedsSet)
		{
			imageButton.setText("");
			imageButton.setBorderPainted( false );
			imageButton.setBackground(new Color(0,0,0,0)); 
			imageButton.setOpaque(false);
			imageNeedsSet = false;
		}
	}

	public String getImageName() {
		String wholeFilePath = imagePath.getAbsolutePath();
		return wholeFilePath.substring(wholeFilePath.lastIndexOf(File.separator) + File.separator.length(), wholeFilePath.lastIndexOf(".png"));
	}
	public boolean isImageNeedsSet() {
		return imageNeedsSet;
	}
	 public JPanel getMasterPanel() {
		return masterPanel;
	}

	public void setImageName(String string) {
		this.imagePath= createFile(string);
		updateImage();
	}
	
}
