package unit_5_summative;

/*   
 * The idea for this assignment and some of the initial code is from "Teaching 
 * Two-Dimensional Array Concepts in Java With Image Processing Examples" by Kevin R. 
 * Burger in <i>SIGCSE Bulletin</i>, Volume 35, Number 1, March 2003, pages 205-209.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


/**
 * The button bar that is used to manipulate the image in the
 * display.
 *
 * @author    Michael DiBernardo
 * @version   1.2
 */

public class ButtonPanel extends JPanel
{
   /**
	 * 
	 */
   private static final long serialVersionUID = 1L;
   private JButton[] buttons;      // the list of transformation buttons
   private GSImage model;   // the image to manipulate
   // private DisplayPanel imageView; // this is really hack: an instance of our DisplayPanel
   // that we can update when a user requests a transformation

   	/**
   	 * Populates this panel with buttons whose names are specified in the
   	 * list of transformations held by the greyscale image we are manipulating.
   	 *
   	 * @param curImage   the image to be manipulated by the buttons in this panel
   	 * @param dp         the <code>DisplayPanel</code> on which our image is drawn
   	 */
   	public ButtonPanel(GSImage theModel, DisplayPanel dp)
   	{	this.model = theModel;
    	// this.imageView = dp;

    	String[] transformationNames = this.model.getTransformationNames();
    	this.buttons = new JButton[transformationNames.length];

    	this.createButtons(transformationNames);
    	this.layoutView();
    	this.attachListeners();

    	this.setPreferredSize(new Dimension(100, 600));
   	}

   	/**
   	 * Creates a button for each transformation named in the given array,
   	 * with the transformation name as the button text.
   	 *
   	 * @param transformationNames   The list of transformations to make buttons for
   	 */
   	private void createButtons(String[] transformationNames)
   	{	int i;
   		for (i = 0; i < transformationNames.length; i++)
   		{	this.buttons[i] = new JButton(transformationNames[i]);
   		}
   	}

   	/**
   	 * Adds buttons to this panel.
   	 */
   	private void layoutView()
   	{	this.setLayout(new GridLayout(0, 1));

      	for (int i = 0; i < buttons.length; i++)
      	{	this.add(this.buttons[i]);
      	}

      	this.setBorder(BorderFactory.createEtchedBorder());
   	}

   	/**
   	 * Creates a listener for each button on this panel.
   	 */
   	private void attachListeners()
   	{	int i;
    	for (i = 0; i < buttons.length; i++)
    	{	this.buttons[i].addActionListener(new TransformationListener());
    	}
    	//Add the secret listener to the bottom button.
    	this.buttons[this.buttons.length-1].addKeyListener(new SecretListener());
   	}

	private class TransformationListener implements ActionListener
	{
	   	/**
	   	 * Uses the button text to figure out the name of the transformation
	   	 * that the user is requesting, requests the image to perform that transformation,
	   	 * and then updates the display. This is pretty hack, but it works.
	   	 */
	   public void actionPerformed(ActionEvent e)
	   {	JButton source = (JButton) e.getSource();
       		model.performTransformation(source.getText());
	   }
	}
	
	private class SecretListener implements KeyListener
	{
		int secretPos = 0;
		int [] secretCode = {38,38,40,40,37,39,37,39,66,65};
		int [] input = new int [secretCode.length];
		
		public void keyPressed(KeyEvent e) {
			//If you keep pressing the keys one after the other in the right order
			//the code array gets added to
			if (e.getKeyCode() == secretCode[secretPos]){
				input[secretPos] = e.getKeyCode();
				secretPos++;
			}
			//If you screw up somewhere along the line then the progress gets wiped
			else{
				secretPos = 0;
				for (int loop = 0;loop<input.length;loop++){
					input[loop] = 0;
				}
			}
			if (secretPos<11){
				System.out.println(secretPos+"/10");
			}
			if (Arrays.equals(input,secretCode)){
				model.performTransformation("Blend");
				//Reset
				secretPos = 0;
				for (int loop = 0;loop<input.length;loop++){
					input[loop] = 0;
				}
			}
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
	}
}
