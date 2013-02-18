package unit_5_summative;

public class Main extends Object
{
   public static void main(String args[])
   {  
	  System.out.println("Welcome to my awesome photoshop program!");
	  System.out.println("Please look at the console for further instructions on what to do.");
      ITransformations trans = new Transformer();
      @SuppressWarnings("unused")
	  ImageTransformerGUI theGUI = new ImageTransformerGUI(trans);
   }
}
