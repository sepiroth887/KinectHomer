package uk.ac.stir.cs.homer.userInterfaceJavaClient.Image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class ImageIconUtils {
	public static final String IMAGES_LOCATION = System.getProperty("uk.ac.stir.cs.homer.resources.images");
	
	public static ImageIcon scaleImageIcon(int width, String imageName) 
	{
		if (imageName!=null)
		{
			ImageIcon imageIcon = new ImageIcon(IMAGES_LOCATION +  imageName + ".png");
			
			BufferedImage dst = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2 = dst.createGraphics();
	        g2.drawImage(imageIcon.getImage(), 0, 0, width, width, null);
	        g2.dispose();
	        return new ImageIcon(dst);
		}
		return null;
    }
	
	public static ImageIcon scaleImageIconForTrigger(int width, String beforeImageName, String afterImageName) 
	{
		if (beforeImageName!=null && !beforeImageName.isEmpty())
		{
			if (afterImageName!=null && !afterImageName.isEmpty())
			{
				ImageIcon beforeImageIcon = new ImageIcon(IMAGES_LOCATION + beforeImageName + ".png");
				ImageIcon afterImageIcon = new ImageIcon(IMAGES_LOCATION +  afterImageName + ".png");
				
				BufferedImage bufferedImage = new BufferedImage(15 + width*2 , width, BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g2 = bufferedImage.createGraphics();
		        g2.drawImage(beforeImageIcon.getImage(), 0, 0, width, width, null);
		        g2.setColor(Color.black);
		        g2.drawString("\u2192", width+3, 3 + width/2);
		        g2.drawImage(afterImageIcon.getImage(), width +15, 0, width, width, null);
		        g2.dispose();
		        return new ImageIcon(bufferedImage);
			}
			else
			{
				return scaleImageIcon(width, beforeImageName);
			}
			
		}
		else if (afterImageName!=null && !afterImageName.isEmpty())
		{
			return scaleImageIcon(width, afterImageName);
		}
		return null;
    }
}
