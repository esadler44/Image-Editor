package image_editor;

import java.util.*;

public class Transformer extends Object implements ITransformations
{
	public static final int MIN_NUM_TRANS = 14;
	public static final String DARKEN = "Darken";
	public static final String BRIGHTEN = "Brighten";
	public static final String INVERT = "Invert";
	public static final String FLIPX = "Flip X";
	public static final String FLIPY = "Flip Y";
	public static final String ROTATE = "Rotate";
	public static final String SCALE50 = "Scale 50%";
	public static final String MIRRORX = "Mirror X";
	public static final String MIRRORY = "Mirror Y";
	public static final String BLUR = "Blur";
	public static final String UNDO = "Undo";
	public static final String REDO = "Redo";
	public static final String RESET = "Reset";
	public static final String KONAMI = "THE CODE";
	public static final String BLEND = "Blend";

	public static final String SLIDE = "Slide";

	private String[] transformations = new String[MIN_NUM_TRANS];
	private static final int MAX_INTENSITY = 255;   // the value for pure white
	private static final int MIN_INTENSITY = 0;     // the value for pure black
	private static final int INTENSITY_STEP = 10;   // the value used to increase or decrease the brightness

	private int[][] original;
	private int[][] transformedPic;
	//Holds the current position for where you are in the photo sequence.
	private int undoPos = 0;
	//Holds all of the photos that you can undo through.
	private ArrayList<int[][]> oldPics = new ArrayList<int[][]>();

	/** Construct a Transformer object by setting the possible transformations available. 
	 */
	public Transformer()
	{
		this.transformations[0] = DARKEN;
		this.transformations[1] = BRIGHTEN;
		this.transformations[2] = INVERT;
		this.transformations[3] = FLIPX;
		this.transformations[4] = FLIPY;
		this.transformations[5] = ROTATE;
		this.transformations[6] = SCALE50;
		this.transformations[7] = MIRRORX;
		this.transformations[8] = MIRRORY;
		this.transformations[9] = BLUR;
		this.transformations[10] = UNDO;
		this.transformations[11] = REDO;
		this.transformations[12] = RESET;
		this.transformations[13] = KONAMI;
	}

	/** Get the image that was transformed.
	 * @return The pixels representing the image. */
	public int[][] getPixels()
	{
		return this.transformedPic;
	}

	/** A array filled with the names of the transformations implemented by this
	 * class.
	 * @return The array of transformation names. */
	public String[] getTransformationNames()
	{
		return this.transformations;
	}

	/** Set the image to be transformed to a new set of pixels.
	 * @param picture - The new image to be used for subsequent transformations. */ 
	public void setPixels(int[][] picture)
	{
		//When a new photo is loaded, the original is saved and the picture to be
		//transformed is also made the original so it can be worked on.
		this.original = picture;
		this.transformedPic = this.original;
		//Wipes the list of old undo pictures to make sure that you can't undo into another image.
		this.oldPics.clear();
		//Adds the new picture to the list of existing pictures.
		this.oldPics.add(this.original);
		//Resets the undoPos back to 0, corresponding with the original image you just brought in.
		this.undoPos = 0;
		System.out.println();
		System.out.println("You're looking at the original image, a.k.a Image #1");
	}

	/**
	 * Perform the transformation indicated and perform that transformation using an amount to change it.
	 * @param whichTrans - The transformation name to be performed
	 * @param amount - The amount you want to alter the image by. (Brighten or darken)
	 */
	public void performTransformation(String whichTrans, int amount)
	{
		if (whichTrans == SLIDE){
			this.slide(amount);
		}

		//If you slid the bar while your undoPos is not at the end of the list
		if (this.undoPos != this.oldPics.size()-1){
			this.wipe();
		}

		//Adding the transformedPic to the list of pictures you have used already.
		this.oldPics.add(this.transformedPic);
		//Increasing the undoPos since you have an additional photo now
		this.undoPos++;
		
		//Prints useful information to the screen for the user.
		System.out.println();
		System.out.println("You're looking at image: "+(this.undoPos+1));
		System.out.println("You can undo/redo up to: "+this.oldPics.size()+" image(s).");
	}

	/** Perform the transformation indicated.
	 * @param transformationName The name of the transformation to perform.  Must
	 * be one of the transformation names returned by {@link #getTransformationNames getTransformationNames}. */
	public void performTransformation(String whichTrans)
	{
		//NOTE FOR TRANSFORMATIONS
		//If you want to find the number of rows in a picture just use this:
		//twoDArray.length
		//However if you want to find the number of columns, use this:
		//twoDArray[0].length

		//Buttons
		if (whichTrans == DARKEN){
			this.darken();
		}
		else if (whichTrans == BRIGHTEN){
			this.brighten();
		}
		else if (whichTrans  == INVERT){
			this.invert();
		}
		else if (whichTrans == FLIPX){
			this.flipX();
		}
		else if (whichTrans == FLIPY){
			this.flipY();
		}
		else if (whichTrans == ROTATE){
			this.rotate();
		}
		else if (whichTrans == SCALE50){
			this.scale50();
		}
		else if (whichTrans == MIRRORX){
			this.mirrorX();
		}
		else if (whichTrans == MIRRORY){
			this.mirrorY();
		}
		else if (whichTrans == BLUR){
			this.blur();
		}
		else if (whichTrans == UNDO){
			this.undo();
		}
		else if (whichTrans == REDO){
			this.redo();
		}
		else if (whichTrans == RESET){
			this.reset();
		}
		else if (whichTrans == KONAMI){
			System.out.println();
			System.out.println("Please enter the Konami code: ");
		}
		else if (whichTrans == BLEND){
			this.sort();
		}

		//If you have performed a new transformation while in the middle of
		//undoing and redoing steps, everything old gets overwritten.
		//If you have clicked a transformation while your undoPos is not at the end of the list
		if (whichTrans != UNDO && whichTrans != REDO && whichTrans != KONAMI && this.undoPos != this.oldPics.size()-1){
			//This will remove all of the other images from the oldPics array, assuring that you
			//can overwrite the pictures.
			this.wipe();
		}

		//If you aren't pressing UNDO, REDO, or The KONAMI Button, a.k.a. Changing the photo to something different.
		if (whichTrans != UNDO && whichTrans != REDO && whichTrans != KONAMI){
			//Adds the newly transformed picture to the ArrayList and increases the position of it by 1.
			this.oldPics.add(this.transformedPic);
			this.undoPos++;
		}
		
		//Gives important information to the user
		if (whichTrans!=KONAMI){
			System.out.println();
			System.out.println("You're looking at image: "+(this.undoPos+1));
			System.out.println("You can undo/redo up to: "+this.oldPics.size()+" image(s).");
		}

	}
	
	private void wipe(){
		//Find the next photo to get rid of.
		int clear = this.undoPos+1;
		//Get rid of all the photos to the end
		//The problem was that the length of the oldPics was changing as I did stuff.
		while (clear < this.oldPics.size()){
			this.oldPics.remove(clear);
		}
	}

	private void slide(int amount){
		//Holds the new photo.
		int [][] adjusted = new int[this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				//Gets the current pixel
				int newPix = this.transformedPic[rows][columns];
				//Adjusts it by the amount you slid the bar.
				newPix = newPix + amount;
				if (newPix < MIN_INTENSITY){
					newPix = MIN_INTENSITY;
				}
				else if (newPix > MAX_INTENSITY){
					newPix = MAX_INTENSITY;
				}
				//Saves the pixel into the new array.
				adjusted[rows][columns] = newPix;
			}
		}
		this.transformedPic = adjusted;
	}

	private void darken(){
		//Holds the new photo.
		int [][] darker = new int[this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				int newPix = this.transformedPic[rows][columns];
				//Lowers the pixel value by 10 and stores it in teh new array.
				newPix -= INTENSITY_STEP;
				if (newPix < MIN_INTENSITY){
					newPix = MIN_INTENSITY;
				}
				darker[rows][columns] = newPix;
			}
		}
		this.transformedPic = darker;
	}

	private void brighten(){
		//Holds the new photo.
		int [][] brighter = new int[this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				int newPix = this.transformedPic[rows][columns];
				newPix += INTENSITY_STEP;
				if (newPix > MAX_INTENSITY){
					newPix = MAX_INTENSITY;
				}
				brighter[rows][columns] = newPix;
			}
		}
		this.transformedPic = brighter;
	}

	private void invert(){
		//Holds the new photo.
		int [][] inverted = new int[this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				int pixel = this.transformedPic[rows][columns];
				pixel = MAX_INTENSITY-pixel;
				inverted[rows][columns] = pixel;
			}
		}
		this.transformedPic = inverted;
	}

	private int[][] flipX(){
		//Holds the new photo.
		int [][] flipx = new int[this.transformedPic.length][this.transformedPic[0].length];
		int height = this.transformedPic.length-1;
		for (int rows = 0;rows < this.transformedPic.length/2;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				int heldPixel = this.transformedPic[rows][columns];
				flipx[rows][columns] = this.transformedPic[height-rows][columns];
				flipx[height-rows][columns] = heldPixel;
			}
		}
		this.transformedPic = flipx;
		return this.transformedPic;
	}

	private int[][] flipY(){
		//Holds the new photo.
		int [][] flipy = new int[this.transformedPic.length][this.transformedPic[0].length];
		int width = this.transformedPic[0].length-1;
		for (int columns = 0; columns < this.transformedPic[0].length/2; columns++){
			for (int rows = 0;rows < this.transformedPic.length;rows++){
				int heldPixel = this.transformedPic[rows][columns];
				flipy[rows][columns] = this.transformedPic[rows][width-columns];
				flipy[rows][width-columns] = heldPixel;
			}
		}
		this.transformedPic = flipy;
		return transformedPic;
	}

	private void rotate(){
		int picHeight = this.transformedPic.length;
		int picWidth = this.transformedPic[0].length;
		//Holds the new photo.
		int [][] rotated = new int [picWidth][picHeight];
		for (int columns = 0; columns < picWidth; columns++){
			for (int rows = 0;rows < picHeight;rows++){
				rotated[columns][picHeight-1-rows] = this.transformedPic[rows][columns];
			}
		}
		this.transformedPic = rotated;
	}

	private void scale50(){
		int newPicHeight = this.transformedPic.length/2;
		int newPicWidth = this.transformedPic[0].length/2;
		//Makes sure that the picture can not spit back an error for being too small
		if (newPicHeight != 0 && newPicWidth != 0){
			//Holds the new photo.
			int [][] smaller = new int[newPicHeight][newPicWidth];
			int smallerRow = -1;
			int smallerCol = 0;
			for (int rows = 0;rows < this.transformedPic.length;rows+=2){
				if (rows >= this.transformedPic.length){
					break;
				}
				smallerRow++;
				if (smallerRow >= newPicHeight){
					break;
				}
				for (int columns = 0; columns < this.transformedPic[0].length; columns+=2){
					if (columns >= this.transformedPic[0].length){
						break;
					}
					smaller[smallerRow][smallerCol] = this.transformedPic[rows][columns];
					smallerCol++;
					if (smallerCol >= newPicWidth){
						smallerCol = 0;
					}
				}
			}
			this.transformedPic = smaller;
		}
	}

	private void mirrorX(){
		int newPicHeight = this.transformedPic.length*2;
		//Holds the new photo.
		int [][] mirrorXPic = new int [newPicHeight][this.transformedPic[0].length];
		//Putting the current picture into the first half of the newPic
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				mirrorXPic[rows][columns] = this.transformedPic[rows][columns];
			}
		}
		//Making a flipped version of the picture
		int [][] flippedPic = flipX();
		//Adding the flipped piece of the picture to the mirrored picture
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				mirrorXPic[rows+this.transformedPic.length][columns] = flippedPic[rows][columns];
			}
		}
		this.transformedPic = mirrorXPic;
	}

	private void mirrorY(){
		int newPicWidth = this.transformedPic[0].length*2;
		//Holds the new photo.
		int [][] mirrorYPic = new int [this.transformedPic.length][newPicWidth];
		//Putting the current picture into the first half of the newPic
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				mirrorYPic[rows][columns] = this.transformedPic[rows][columns];
			}
		}
		//Making a flipped version of the picture
		int [][] flippedPic = flipY();
		//Adding the flipped piece of the picture to the mirrored picture
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				mirrorYPic[rows][columns+this.transformedPic[0].length] = flippedPic[rows][columns];
			}
		}
		this.transformedPic = mirrorYPic;
	}

	private void blur(){
		int average = 0;
		//Holds the new photo.
		int [][] blurred = new int [this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				average = surroundAvg(rows,columns);
				blurred[rows][columns] = average;
			}
		}
		this.transformedPic = blurred;
	}

	private int surroundAvg(int rows, int columns){
		int average;
		int C = this.transformedPic[rows][columns];
		//VARIABLES!
		//Different values for surrounding pixels are here:
		//If you are on the top row
		if (rows == 0){
			if (columns == 0){
				int E = this.transformedPic[rows][columns+1];
				int SE = this.transformedPic[rows+1][columns+1];
				int S = this.transformedPic[rows+1][columns];
				average = (E+SE+S+C)/4;
			}
			else if (columns == this.transformedPic[0].length-1){
				int S = this.transformedPic[rows+1][columns];
				int SW = this.transformedPic[rows+1][columns-1];
				int W = this.transformedPic[rows][columns-1];
				average = (S+SW+W+C)/4;
			}
			else{
				int E = this.transformedPic[rows][columns+1];
				int SE = this.transformedPic[rows+1][columns+1];
				int S = this.transformedPic[rows+1][columns];
				int SW = this.transformedPic[rows+1][columns-1];
				int W = this.transformedPic[rows][columns-1];
				average = (E+SE+S+SW+W+C)/6;
			}
		}
		//If you are on the bottom, only make the top ones
		else if (rows == this.transformedPic.length-1){
			if (columns == 0){
				int N = this.transformedPic[rows-1][columns];
				int NE = this.transformedPic[rows-1][columns+1];
				int E = this.transformedPic[rows][columns+1];
				average = (N+NE+E+C)/4;
			}
			else if (columns == this.transformedPic[0].length-1){
				int W = this.transformedPic[rows][columns-1];
				int NW = this.transformedPic[rows-1][columns-1];
				int N = this.transformedPic[rows-1][columns];
				average = (W+NW+N+C)/4;
			}
			else{
				int W = this.transformedPic[rows][columns-1];
				int NW = this.transformedPic[rows-1][columns-1];
				int N = this.transformedPic[rows-1][columns];
				int NE = this.transformedPic[rows-1][columns+1];
				int E = this.transformedPic[rows][columns+1];
				average = (W+NW+N+NE+E+C)/6;
			}
		}
		//Only make the right ones
		else if (columns == 0){
			int N = this.transformedPic[rows-1][columns];
			int NE = this.transformedPic[rows-1][columns+1];
			int E = this.transformedPic[rows][columns+1];
			int SE = this.transformedPic[rows+1][columns+1];
			int S = this.transformedPic[rows+1][columns];
			average = (N+NE+E+SE+S+C)/6;
		}
		//Only make the left ones
		else if (columns == this.transformedPic[0].length-1){
			int S = this.transformedPic[rows+1][columns];
			int SW = this.transformedPic[rows+1][columns-1];
			int W = this.transformedPic[rows][columns-1];
			int NW = this.transformedPic[rows-1][columns-1];
			int N = this.transformedPic[rows-1][columns];
			average = (S+SW+W+NW+N+C)/6;
		}
		//Average a normal pixel that is completely surrounded
		else{
			int NW = this.transformedPic[rows-1][columns-1];
			int N = this.transformedPic[rows-1][columns];
			int NE = this.transformedPic[rows-1][columns+1];
			int E = this.transformedPic[rows][columns+1];
			int SE = this.transformedPic[rows+1][columns+1];
			int S = this.transformedPic[rows+1][columns];
			int SW = this.transformedPic[rows+1][columns-1];
			int W = this.transformedPic[rows][columns-1];
			average = (NW+N+NE+E+SE+S+SW+W+C)/9;
		}
		return average;
	}

	private void sort(){
		//Holds the new photo.
		int [][] fullysorted = new int[this.transformedPic.length][this.transformedPic[0].length];
		for (int rows = 0;rows < this.transformedPic.length;rows++){
			int [] sortedRow = new int [this.transformedPic[0].length];
			for (int columns = 0; columns < this.transformedPic[0].length; columns++){
				sortedRow[columns] = this.transformedPic[rows][columns];
			}
			Arrays.sort(sortedRow);
			fullysorted[rows] = sortedRow;
		}
		this.transformedPic = fullysorted;
	}

	private void undo(){
		//If the undoPos is greater than 0, then you want to get the last image and lower the undoPos to match it.
		if (this.undoPos > 0){
			this.transformedPic = this.oldPics.get(this.undoPos-1);
			this.undoPos--;
		}
	}

	private void redo(){
		//If you can redo a photo.
		//The position for a redo is one more than the undoPos, and the undoPos, is always on the currently selected photo.
		if (this.undoPos < this.oldPics.size()-1){
			//Change the transformed picture to the picture at the current undoPos
			this.transformedPic = this.oldPics.get(this.undoPos+1);
			this.undoPos++;
		}
	}

	private void reset(){
		//Switches back to the original, but keeps it in the list of pictures so you can undo it if you want.
		this.transformedPic = this.original.clone();
	}

}
