package image_editor;

/*   
 * The idea for this assignment and some of the initial code is from "Teaching 
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R. 
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.*;
import javax.imageio.*;
import becker.util.IModel;
import becker.util.IView;
import java.util.*;
import java.io.*;
import java.net.URL;


/**
 * A representation of an greyscale image. Pixels are stored in a 2D array of integers.
 * A single pixel is given a value between 0 and 255, with 0 being completely white and 255
 * being completely black.
 *
 * @author    Michael DiBernardo
 * @version   1.1
 */

public class GSImage extends Object implements ITransformations, IModel
{
	private ITransformations trans;  
	private ArrayList<IView> views = new ArrayList<IView>();
	
	/**
	 * Reads the image in from the specified file and creates
	 * a 2D array representation of that image. Files should be
	 * of GIF or JPEG format. Any color image will be converted
	 * to greyscale.
	 *
	 * @param fileName   The path to the source image file
	 */
	public GSImage(ITransformations trans)
	{	this.trans = trans;

		// URL resource = ITransformations.class.getResource("NY_Skyline.jpg");
		// this.readImage(resource);
		try {
			BufferedImage preload = ImageIO.read(getClass().getResource("Williamsburg.jpg"));
			this.makeImage(preload);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.updateAllViews();
	}

	/**
	 * Sets the pixelmap for this image.
	 *
	 * @param newPixels   the pixelmap to set to this image
	 */
	public void setPixels(int[][] newPixels)
	{	this.trans.setPixels(newPixels);
	}

	/**
	 * Gets the pixelmap for this image.
	 *
	 * @return   the pixelmap for this image.
	 */
	public int[][] getPixels()
	{	return this.trans.getPixels();
	}

	public void performTransformation(String transformationName)
	{	this.trans.performTransformation(transformationName);
		this.updateAllViews();
	}
	
	public void performTransformation(String transformationName,int amount)
	{	this.trans.performTransformation(transformationName,amount);
		this.updateAllViews();
	}

	public String[] getTransformationNames()
	{	return this.trans.getTransformationNames();
	}

	/**
	 * Draws this image on the surface specified by the
	 * given graphics object at the coordinates (x,y).
	 *
	 * You do not need to modify this method.
	 *
	 * @param g2   The surface on which to paint this image
	 * @param x    The x coordinate at which to draw this image
	 * @param y    The y coordinate at which to draw this image
	 */
	public void draw(Graphics2D g2, int x, int y)
	{	BufferedImage img = this.createBufferedImage();

		g2.drawImage(img, x, y, null);
	}

	/**
	 * Reads in the image file that contains the source for this image and
	 * converts it into a greyscale 2D integer representation.
	 *
	 * You do not need to modify this method.
	 *
	 * I would like to acknowledge Kevin R. Burger from Rockhurt University for
	 * the idea of reading in the array samples using the <code>WriteableRaster</code>
	 * class. Some of the code in this method was taken from his article in the SIGCSE
	 * 2003 compilation.
	 */
	public void readImage(String sourceFile)
	{	// Read the image from an ImageIcon since it's guaranteed to be fully loaded
		// once the constructor exits: no foofy MediaTracker stuff.
		Image img = new ImageIcon(sourceFile).getImage();
		this.makeImage(img);
	}

	public void readImage(URL source)
	{	Image img = new ImageIcon(source).getImage();
		this.makeImage(img);
	}

	private void makeImage(Image img)
	{  	// Determine the width and height of the image, but forget about the ImageObserver
		int width = img.getWidth(null);
		int height = img.getHeight(null);

		if (width <= 0 || height <= 0)
		{	throw new Error("Could not obtain the image.");
		}

		// Create a blank greyscale BufferedImage of the same width and height as ours
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		// Draw our image onto the BufferedImage
		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(img, 0, 0, null);

		// Allocate the samples array
		int[][] pixels = new int[height][width];

		// Grab the raster from the BufferedImage and use it to grab the pixel values.
		// This is the code I got from the SIGCSE compilation.
		WritableRaster raster = bi.getRaster();

		for (int row = 0; row < height; row++)
		{	for (int col = 0; col < width; col++)
			{	pixels[row][col] = raster.getSample(col, row, 0);
			}
		}
		this.trans.setPixels(pixels);
	}

	/**
	 * Converts the 2D array representation of our image into a BufferedImage
	 * object so that it can be drawn to a surface.
	 *
	 * You do not need to modify this method.
	 *
	 * This is basically the inverse of what is done in the last portion of the
	 * <code>readImage()</code> method. Once again, I would like to credit
	 * Kevin R. Burger for the WritableRaster stuff.
	 *
	 * @return   A BufferedImage representation of this image
	 */
	//This is now public so that it can be used in the TransMenuBar so that we can write files with the save function.
	public BufferedImage createBufferedImage()
	{	int[][] pixels = this.trans.getPixels();
		int height = pixels.length;
		int width = pixels[0].length;

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		WritableRaster raster = bi.getRaster();

		for (int row = 0; row < height; row++)
		{	for (int col = 0; col < width; col++)
			{	raster.setSample(col, row, 0, pixels[row][col]);
			}
		}

		return bi;
	}

	public void addView(IView aView)
	{	this.views.add(aView);
	}

	public void removeView(IView aView)
	{	this.views.remove(aView);
	}

	public void updateAllViews()
	{	Iterator<IView> it = this.views.iterator();
		while (it.hasNext())
		{	IView aView = (IView) it.next();
			aView.updateView();
		}
	}
}
