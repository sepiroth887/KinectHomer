package uk.ac.stir.cs.homer.userInterfaceJavaClient.Image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;

public class ImageFileViewer extends FileView {

	  private Icon fileIcon = UIManager.getIcon("FileView.fileIcon");

	  private Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");

	  private Component observer;

	  public ImageFileViewer(Component c) {
	    // We need a component around to create our icon's image
	    observer = c;
	  }

	  public String getDescription(File f) {
	    // We won't store individual descriptions, so just return the
	    // type description.
	    return getTypeDescription(f);
	  }

	  public Icon getIcon(File f) {
	    // Is it a folder?
	    if (f.isDirectory()) {
	      return folderIcon;
	    }

	    // Ok, it's a file, so return a custom icon if it's an image file
	    String name = f.getName().toLowerCase();
	    if (name.endsWith(".png") || name.endsWith(".PNG")) {
	      return new Icon16(f.getAbsolutePath());
	    }

	    // Return the generic file icon if it's not
	    return fileIcon;
	  }

	  public String getName(File f) {
	    String name = f.getName();
	    return name.equals("") ? f.getPath() : name;
	  }

	  public String getTypeDescription(File f) {
	    String name = f.getName().toLowerCase();
	    if (f.isDirectory()) {
	      return "Folder";
	    }
	    if (name.endsWith(".jpg")) {
	      return "JPEG Image";
	    }
	    if (name.endsWith(".gif")) {
	      return "GIF Image";
	    }
	    return "Generic File";
	  }

	  public Boolean isTraversable(File f) {
	    // We'll mark all directories as traversable
	    return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
	  }

	  public class Icon16 extends ImageIcon {
	    public Icon16(String f) {
	      super(f);
	      Image i = observer.createImage(50, 50);
	      i.getGraphics().drawImage(getImage(), 0, 0, 50, 50, observer);
	      setImage(i);
	    }

	    public int getIconHeight() {
	      return 50;
	    }

	    public int getIconWidth() {
	      return 50;
	    }

	    public void paintIcon(Component c, Graphics g, int x, int y) {
	      g.drawImage(getImage(), x, y, c);
	    }
	  }
	}
