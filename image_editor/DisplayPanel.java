package image_editor;

/*   
 * The idea for this assignment and some of the initial code is from "Teaching 
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R. 
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */



import javax.swing.*;
import java.awt.*;
import becker.util.*;


/**
 * A simple panel used to display the image under scrutiny.
 *
 * @author    Michael DiBernardo
 * @version   1.1
 */

public class DisplayPanel extends JPanel implements IView
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GSImage model;
	private static final int PANEL_WIDTH = 1100;
	private static final int PANEL_HEIGHT = 900;

	/**
	 * Formats the display panel.
	 *
	 * @param curImage   the image to be displayed in this panel.
	 */
	public DisplayPanel(GSImage theModel)
	{	this.model = theModel;
		this.model.addView(this);
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		this.setBorder(BorderFactory.createBevelBorder(1));
	}

	public void updateView()
	{	this.repaint();
	}

	/**
	 * Redraws the greyscale image on the panel.
	 *
	 * @param g   the graphics object for this panel
	 */
	public void paintComponent(Graphics g)
	{	super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		this.model.draw(g2, 0, 0);
	}
}
