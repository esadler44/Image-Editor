package image_editor;

/* Imperial Oil Computer Science Summer Institute - August 2004 University of Waterloo
 *
 * The SliderPanel class builds a JSlider to allow the user to brighten and darken the picture.
 * This class was adapted from ButtonPanel included with this package. The class uses an inner 
 * class called TransformationListener as done in ButtonPanel. Inside this class you will see the area
 * to enter code to make the slider work.
 *
 * The idea for this assignment and some of the initial code is from "Teaching
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R.
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */

import javax.swing.*;
import javax.swing.event.*; // needed to add this import to get the changeListener working
import java.awt.*;


/**
 * Based upon the ButtonPanel class
 *
 * ButtonPanel written by:  Michael DiBernardo
 * @version   1.2
 */

public class SliderPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider slider;     // the JSlider to control picture brightness
	private JLabel blank;       // instructions for use

	private GSImage model;


	public SliderPanel (GSImage theModel)
	{

		Font gameFont = new Font ("Helvetica", Font.PLAIN, 14);

		this.model = theModel;

		// JSlider into memory
		this.slider = new JSlider ();

		// set the scale for the slider 0 - 100 and display it
		this.slider.setMajorTickSpacing (10);
		this.slider.setMinorTickSpacing (1);
		this.slider.setPaintTicks (true);
		this.slider.setPaintLabels (true);

		// set up the label containing slider instructions
		this.blank = new JLabel ("<-- Move the slider to the left to darken, or to the right to brighten");
		this.blank.setFont (gameFont);
		this.blank.setForeground (Color.red);
		this.blank.setHorizontalAlignment (0);

		this.layoutView ();
		this.attachListeners ();

		this.setPreferredSize (new Dimension (500, 50));
	}


	private void layoutView ()
	{
		this.setLayout (new GridLayout (0, 2));
		this.add (slider);
		this.add (blank);
		this.setBorder (BorderFactory.createEtchedBorder ());
	}


	/**
	 * Creates a listener for each button on this panel.
	 */
	private void attachListeners ()
	{
		slider.addChangeListener (new TransformationListener ());

	}

	// TransformationListener captures the change events
	private class TransformationListener implements ChangeListener
	{

		// last used value
		private int previousValue;  // previousValue records the last location of the slider

		// initial set-up value is 50
		private TransformationListener ()
		{
			this.previousValue = 50;
		}

		// the stateChanged method is required by the ChangeListener interface
		public void stateChanged (ChangeEvent e)
		{
			// source will provide information from the slider
			JSlider source = (JSlider) e.getSource ();


			if (!source.getValueIsAdjusting ()) // changes the image after sliding
			{
				// current value of the slide bat
				int fps = (int)source.getValue();
				int change = fps-this.previousValue;
				model.performTransformation("Slide",change);
				// save the current slider position value for the next move
				this.previousValue = fps;

			}
		}
	}
}

