package uk.ac.stir.cs.homer.userInterfaceJavaClient.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import uk.ac.stir.cs.homer.serviceDatabase.HomerDatabase;

public class ImageUploader {

	private final HomerDatabase database;
	public ImageUploader(HomerDatabase database) {
		this.database = database;
		initialiseScreen();
	}
	private void initialiseScreen() {
		final JFrame imageUploaderFrame = new JFrame();
		
		FileFilter pngOrDirectoryFilter = new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || 
					f.isFile() && f.getName().toLowerCase().endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "PNG";
			}
		};
		
		final JFileChooser fc = new JFileChooser(ImageIconUtils.IMAGES_LOCATION);
		fc.setMultiSelectionEnabled(true);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setFileFilter(pngOrDirectoryFilter);
		fc.setFileView(new ImageFileViewer(imageUploaderFrame));
		
		final JLabel resultMessage = new JLabel("Press the button to upload an image!");
		final JButton uploadButton = new JButton("Choose Images!");
		uploadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				int returnVal = fc.showOpenDialog(imageUploaderFrame);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					int numSaved = saveSubImages(fc.getSelectedFiles());
					resultMessage.setText(Integer.toString(numSaved) + " images uploaded successfully!");
					uploadButton.setText("Upload More!");
		        } 
				else
				{
					resultMessage.setText("Sorry, you didn't choose any images!");
					uploadButton.setText("Try Again!");
				}
			}
			
		});
		
		JPanel mainPanel = new JPanel();
		mainPanel.add(uploadButton);
		mainPanel.add(resultMessage);
		
		imageUploaderFrame.add(mainPanel);
		imageUploaderFrame.setTitle("Upload Images");
		imageUploaderFrame.setSize(300,300);
		imageUploaderFrame.setVisible(true);
	}
	
	
	private int saveSubImages(File[] files)
	{
		int totalSavedMessages = 0;
		
		for (File f : files)
		{
			if (f.isDirectory())
			{
				totalSavedMessages += saveSubImages(f.listFiles());
			}
			else
			{
				if (f.getName().toLowerCase().endsWith("png"))
				{
					database.addImage(f);
					totalSavedMessages++;
				}
			}
		}
		return totalSavedMessages;
	}
}
