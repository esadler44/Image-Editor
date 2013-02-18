package image_editor;

/*   
 * The idea for this assignment and some of the initial code is from "Teaching 
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R. 
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;


public class TransMenuBar extends JMenuBar
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GSImage model;

	public TransMenuBar(GSImage theModel)
	{	super();
		this.model = theModel;
		this.add(this.makeFileMenu());
		this.add(this.makeTransformMenu());
	}

	private JMenu makeFileMenu()
	{	JMenu m = new JMenu("File");
		m.add(new OpenAction());
		m.add(new SaveAction());
		m.addSeparator();
		m.add(new ExitAction());

		return m;
	}

	private JMenu makeTransformMenu()
	{	JMenu m = new JMenu("Transformations");
		String[] names = this.model.getTransformationNames();
		for (int i = 0; i < names.length; i++)
		{	m.add(new TransformAction(names[i]));
		}
		return m;
	}

	private class OpenAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OpenAction()
		{	super("Open...");
		}

		public void actionPerformed(ActionEvent evt)
		{	JFileChooser d = new JFileChooser();
			d.setCurrentDirectory(new File(".\\"));
			//Making filters so they can only choose a JPEG image.
			FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPEG Images","jpg");
			//Adding the filter to be used.
			d.addChoosableFileFilter(jpegFilter);
			int result = d.showOpenDialog(TransMenuBar.this.getParent());
			if (result == JFileChooser.APPROVE_OPTION)
			{	String fName = d.getSelectedFile().getPath();
				System.out.println();
				System.out.println("Opening file: " + fName);
				TransMenuBar.this.model.readImage(fName);
				model.updateAllViews();
			}		
		}
	}


	private class SaveAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SaveAction()
		{	super("Save...");
		}

		public void actionPerformed(ActionEvent evt)
		{	JFileChooser s = new JFileChooser();
			s.setCurrentDirectory(new File(".\\"));
			//Making filters so they can only save a JPEG image.
			FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPEG Images","jpg");
			//Adding the filter to be used.
			s.addChoosableFileFilter(jpegFilter);
			int result = s.showSaveDialog(TransMenuBar.this.getParent());
			if (result == JFileChooser.APPROVE_OPTION){
				//Makes the new file in the same location as the other photos
				File file = new File(s.getSelectedFile().getPath()+".jpg");
				BufferedImage image = TransMenuBar.this.model.createBufferedImage();
				try {
					System.out.println();
					System.out.println("Saving file: "+file.getAbsolutePath());
					ImageIO.write(image, "jpg", file);
				} catch (IOException e) {
					System.out.println("Error trying to save file: "+file);
				}
			}
		}
	}


	private class ExitAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExitAction()
		{	super("Exit");
		}

		public void actionPerformed(ActionEvent evt)
		{	System.out.println();
			System.out.println("Thank you for using my awesome photoshop program!");
			System.exit(0);
		}
	}


	private class TransformAction extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TransformAction(String name)
		{	super(name);
		}

		/**
		 * Uses the menu text to figure out the name of the transformation
		 * that the user is requesting, requests the image to perform that transformation.
		 */
		public void actionPerformed(ActionEvent e)
		{	JMenuItem source = (JMenuItem) e.getSource();
			model.performTransformation(source.getText());
		}
	}
}
