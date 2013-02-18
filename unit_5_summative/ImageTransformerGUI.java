package unit_5_summative;

/*   
 * The idea for this assignment and some of the initial code is from "Teaching 
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R. 
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */


import javax.swing.*;


import java.awt.*;


/** The ImageTransformerGUI provides a graphical user interface (GUI) for 
 * transforming graphic images via a class implementing {@link ITransformations}
 * provided to its constructor. See the  
 * {@link becker.xtras.imageTransformation} package overview for more details.
 *
 * @author    Michael DiBernardo and Byron Weber Becker
 * @version   1.1
 * 
 * Modified by Sandy Graham August 2004
 */
public class ImageTransformerGUI extends Object
{
	private JFrame frame;
	//I changed these numbers around so that you can rotate the image and see the whole thing still.
	private static final int FRAME_WIDTH = 915;
	private static final int FRAME_HEIGHT = 915;

	/**
	 * Create a GUI that allows manipulation of images.
	 *
	 * @param trans The object that will do the transformations of the image.
	 */
	@SuppressWarnings("deprecation")
	public ImageTransformerGUI(ITransformations trans)
	{	super();
		this.frame = new JFrame("Elliot's Awesome Greyscale Photoshop Program");
		this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		try
		{	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (java.security.AccessControlException e)
		{	this.frame.dispose();	
		}

		this.centreWindow();

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		
		//RIGHT HERE WE MAKE THE GSIMAGE
		GSImage model = new GSImage(trans);
		//THIS IS THE TRANSMENUBAR CREATION
		this.frame.setJMenuBar(new TransMenuBar(model));

		DisplayPanel dp = new DisplayPanel(model);
		ButtonPanel bp = new ButtonPanel(model, dp);
		//Created a new slider panel here
		SliderPanel sp = new SliderPanel(model);

		content.add(dp, BorderLayout.CENTER);
		content.add(bp, BorderLayout.EAST);
		//Added a slider panel here
		content.add(sp, BorderLayout.SOUTH);

		this.frame.setContentPane(content);
		this.frame.show();
	}

	/**
	 * Used to centre the window in the user's environment. Credit goes to
	 * Steven Furino for this one.
	 */
	private void centreWindow()
	{	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.frame.getSize();
		int x = (screenSize.width - frameSize.width) / 2;
		int y = (screenSize.height - frameSize.height) / 2;
		this.frame.setLocation(x, y);
	}
}
