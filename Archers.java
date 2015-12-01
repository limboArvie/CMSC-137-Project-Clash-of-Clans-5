import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.util.Random;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;

public class Archers{

	private static int width = 58;
	private static int height = 71;

	public static final int frameCount = 4;
	public static final int frameCountAttack = 3;
	private  JLabel botImage;
	private static BufferedImage mainImageEast;
	private static BufferedImage mainImageWest;
	
	private static BufferedImage[] north;
	private static BufferedImage[] south;
	private static BufferedImage[] east;
	private static BufferedImage[] west;
	private static BufferedImage[] north_east;
	private static BufferedImage[] north_west;
	private static BufferedImage[] south_east;
	private static BufferedImage[] south_west;

	public static final int bot_NORTH = 1;
	public static final int bot_SOUTH = 2;
	public static final int bot_WEST = 3;
	public static final int bot_EAST = 4;
	public static final int bot_NORTHWEST = 5;
	public static final int bot_NORTHEAST = 6;
	public static final int bot_SOUTHWEST = 7;
	public static final int bot_SOUTHEAST = 8;
	int temp = 0;
	
	/**
	*returns the starting image of the bots
	*/
	public static ImageIcon getStartingImage(){
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(north[0].getSource()));
	}
	
	/**
	*Returns the direction of the bots
	*/
	public static BufferedImage[] getBufferedImageArray(int direction){
	
		switch(direction){
			case bot_NORTH:
				return north;
			case bot_SOUTH:
				return south;
			case bot_WEST:
				return west;
			case bot_EAST:
				return east;
			case bot_NORTHWEST:
				return north;
			case bot_NORTHEAST:
				return south;
			case bot_SOUTHWEST:
				return west;
			case bot_SOUTHEAST:
				return east;
			default:
				return null;
		}
		
	}
	
	/**
	*loads the sprites to be used for the bots
	*/
	public static void loadSprites(){
	
		try{
		
			mainImageEast = ImageIO.read(new File("char/archer_east.png"));
			mainImageWest = ImageIO.read(new File("char/archer_west.png"));
			
			
			north = new BufferedImage[frameCount];
			south = new BufferedImage[frameCount];
			east = new BufferedImage[frameCount];
			west = new BufferedImage[frameCount];
			north_east = new BufferedImage[frameCount];
			north_west = new BufferedImage[frameCount];
			south_east = new BufferedImage[frameCount];
			south_west = new BufferedImage[frameCount];
			
			for(int i=0; i<frameCount; i++){
				north[i] = mainImageEast.getSubimage(0*width, height*i, width, height);
				north_east[i] = mainImageEast.getSubimage(1*width, height*i, width, height);
				east[i] = mainImageEast.getSubimage(2*width, height*i, width, height);
				south_east[i] = mainImageEast.getSubimage(3*width, height*i, width, height);
				south[i] = mainImageEast.getSubimage(4*width, height*i, width, height);
				
				north_west[i] = mainImageWest.getSubimage(3*width, height*i, width, height);
				west[i] = mainImageWest.getSubimage(2*width, height*i, width, height);
				south_west[i] = mainImageWest.getSubimage(1*width, height*i, width, height);
			}
			
		}
		catch(Exception e){
			System.out.println("Spritesheet Not Found!");
		}
	
	}
}
	
/**
*Timertask class for the bots
*/
class botTimerTask2 extends TimerTask{

	JLabel botLabel;
	int movementIndex = 0;
	int movementCount = 0;
	int movementFactor = 0;
	int temp = 0;
	
	/**
	*Constructor of the botTimerTask with specified JLabel and integer parameters
	*/
	public botTimerTask2(JLabel botLabel, int movementFactor){
		this.botLabel = botLabel;
		this.movementFactor = movementFactor;
	}
	
	/**
	*The run method of this Timer Task
	*The bots tend to follow the character of the user wherever it go. 
	*When the user's char is on the opposite floor, the bots will go to the portal(door) leading to the same floor where the character is
	*If the character and the bots are on the same floor, the bots will go to the direction where the user's character is.
	*The effect of the fighting or pushing of the user's character is also defined here. 
	*With the user of bare hand, the user's push to the bots is stronger but of limited range. On the other side, with the use of the gun, the user's range is wide but the push is weak
	*/
	public void run(){
	
		movementCount++;
	
		movementIndex++;
		movementIndex = movementIndex % Archers.frameCount;
		if(movementIndex%movementFactor==0){
		
			if(temp == 0){
				botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY()-(movementFactor*3));
				botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Archers.getBufferedImageArray(Archers.bot_NORTH)[movementIndex].getSource())));
				if((int)botLabel.getLocation().getY()<=0)
				temp = 1;//botLabel.setIcon(null);
						
				//botLabel.setBounds(1000, 440, 115, 144);
				
			}else if(temp == 1){
				botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY()+(movementFactor*3));
				botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Archers.getBufferedImageArray(Archers.bot_SOUTH)[movementIndex].getSource())));
				if((int)botLabel.getLocation().getY()>=400)
						temp = 0;
				
				
			}
		}
	}

}
