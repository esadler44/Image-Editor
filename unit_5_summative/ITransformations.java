package unit_5_summative; 


/** <p>Classes implementing the <code>ITransformations</code> interface can be
 * used with the {@link ImageTransformerGUI} class to transform grey-scale images.  
 * <code>ITransformations</code> instances do the actual work of transforming
 * the images:  brightening, rotating, flipping, etc.  The GUI finds the 
 * available transformations by calling <code>getTransformationNames</code>
 * and makes a button for each name in the returned array.  The 
 * <code>performTransformation</code> method is called each time one of these
 * buttons is clicked by the user with the appropriate transformation name
 * passed as a parameter.</p> 
 *
 * <p>Each pixel ("picture element") in the image to be transformed is 
 * represented by an integer between 0 and 255.  A small value represents 
 * a dark color and a
 * larger value represents a light color.  These integers are stored in a two-
 * dimensional array.  The value in <code>pixels[0][0]</code> represents the upper-
 * left corner of the image.  By rearranging the order of these values
 * the image can be rotated or flipped.  By changing the magnitude of each
 * value the image can be lighted or darkened.
 * </p>
 *
 * <p>A class implementing <code>ITransformations</code> would typically be
 * structured like this:</p>
 * <pre>
 *    public class MyTransformations extends Object implements ITransformations
 *    {   private int[][] pixels;
 *        
 *        public MyTransformations() {  ... }
 *
 *        public void setPixels(int[][] newPix)
 *        {  this.pixels = newPix;
 *        }
 *
 *        public int[][] getPixels()
 *        {  return this.pixels;
 *        }
 *
 *        public void performTransformation(String transformationName)
 *        { ...
 *        }
 *    
 *        public String[] getTransformationNames()
 *        {  return new String[] {"Darken", "Lighten", "FlipX", "FlipY", "Reset"};
 *        }
 *    }
 * </pre>
 *
 * <p>It is up to the implementor whether transformations are cumulative 
 * (each transformation changes the <code>pixels</code> array) or independent
 * (the transformed image is copied into a new array that is returned, 
 * leaving <code>pixels</code> untouched).</p>
 *
 * @author    Michael DiBernardo and Byron Weber Becker */
public interface ITransformations
{

   /** Set the image to be transformed to a new set of pixels.
    * @param newPix The new image to be used for subsequent transformations. */ 
   public void setPixels(int[][] newPix);
   
   /** Get the image that was transformed.
    * @return The pixels representing the image. */
   public int[][] getPixels();
   
   /** Perform the transformation indicated.
    * @param transformationName The name of the transformation to perform.  Must
    * be one of the transformation names returned by {@link #getTransformationNames getTransformationNames}. */
   public void performTransformation(String transformationName);
   
   /** Perform the transformation indicated by using an added amount.
    * @param transformationName The name of the transformation to perform.  Must
    * be one of the transformation names returned by {@link #getTransformationNames getTransformationNames}.
    * int amount - the amount you want to alter the transformation by. */
   public void performTransformation(String transformationName, int amount);

   /** A array filled with the names of the transformations implemented by this
    * class.
    * @return The array of transformation names. */
   public String[] getTransformationNames();
}
