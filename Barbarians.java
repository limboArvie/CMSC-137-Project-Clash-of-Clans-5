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

public class Barbarians{
	private static final int width = 74;
	private static final int height = 56;

	public static final int frameCount = 5;
	public static final int frameCountAttack = 4;
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
	
	private static BufferedImage[] north_attack;
	private static BufferedImage[] south_attack;
	private static BufferedImage[] east_attack;
	private static BufferedImage[] west_attack;

	public static final int bot_NORTH = 1;
	public static final int bot_SOUTH = 2;
	public static final int bot_WEST = 3;
	public static final int bot_EAST = 4;
	public static final int bot_NORTHWEST = 5;
	public static final int bot_NORTHEAST = 6;
	public static final int bot_SOUTHWEST = 7;
	public static final int bot_SOUTHEAST = 8;
	
	public static final int bot_NORTHATTACK = 9;
	public static final int bot_SOUTHATTACK = 10;
	public static final int bot_WESTATTACK = 11;
	public static final int bot_EASTATTACK = 12;
	
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
				return north_west;
			case bot_NORTHEAST:
				return north_east;
			case bot_SOUTHWEST:
				return south_west;
			case bot_SOUTHEAST:
				return south_east;
			case bot_NORTHATTACK:
				return north_attack;
			case bot_SOUTHATTACK:
				return south_attack;
			case bot_WESTATTACK:
				return west_attack;
			case bot_EASTATTACK:
				return east_attack;
			default:
				return null;
		}
		
	}
	
	/**
	*loads the sprites to be used for the bots
	*/
	public static void loadSprites(){
	
		try{
			mainImageEast = ImageIO.read(new File("char/barbarian_east.png"));
			mainImageWest = ImageIO.read(new File("char/barbarian_west.png"));
			
			north = new BufferedImage[frameCount];
			south = new BufferedImage[frameCount];
			east = new BufferedImage[frameCount];
			west = new BufferedImage[frameCount];
			north_east = new BufferedImage[frameCount];
			north_west = new BufferedImage[frameCount];
			south_east = new BufferedImage[frameCount];
			south_west = new BufferedImage[frameCount];
			
			north_attack = new BufferedImage[frameCountAttack];
			south_attack = new BufferedImage[frameCountAttack];
			east_attack = new BufferedImage[frameCountAttack];
			west_attack = new BufferedImage[frameCountAttack];
			
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
			
			for(int j=frameCount; j<frameCount+frameCountAttack; j++){
				north_attack[j-frameCount] = mainImageEast.getSubimage(0*width, height*j, width, height);
				east_attack[j-frameCount] = mainImageEast.getSubimage(2*width, height*j, width, height);
				south_attack[j-frameCount] = mainImageEast.getSubimage(4*width, height*j, width, height);
				west_attack[j-frameCount] = mainImageWest.getSubimage(2*width, height*j, width, height);
			}
			
		}catch(ArrayIndexOutOfBoundsException et){
			System.out.println("Spritesheet Not Found!2");
		}
		catch(Exception e){
			System.out.println("Spritesheet Not Found!");
		}
	
	}
}
	
/**
*Timertask class for the bots
*/
class botTimerTask extends TimerTask{

	JLabel botLabel;
	int movementIndex = 0;
	int attackIndex = 0;
	int movementCount = 0;
	int movementFactor = 0;
	int ori = 0;
	int X = 0, Y = 0, HP = 0;
	int topX = 0, bottomX = 0, leftX = 0, rightX = 0;
	int topY = 0, bottomY = 0, leftY = 0, rightY = 0;
	int botX = 0, botY = 0, barbHP = 100;
	int targHP = 0, index;
	boolean targX = true;
	boolean targY = true;
	boolean life = true, indic = true;
	int[] locationsX;
	int[] locationsY;
	int[] buildingsHP;
	int targIndex = 0;
	int clientID, enemyClientID;
	private boolean listening=false;
	private Client client;
	Thread listen, run2;
	
	
		/**
	*Constructor of the botTimerTask with specified JLabel and integer parameters
	*/
	public botTimerTask(JLabel botLabel, int movementFactor, int[] locationsX, int[] locationsY, int[] buildingsHP, String name, String address, int port, int index, int clientID, int enemyClientID){
		this.botLabel = botLabel;
		this.movementFactor = movementFactor;
		this.locationsX = locationsX;
		this.locationsY = locationsY;
		this.buildingsHP = buildingsHP;
		this.index = index;
		this.clientID = clientID;
		this.enemyClientID = enemyClientID;
		
		client = new Client(name,address,port);
		boolean connected = client.conn(address);
		
		if(!connected){
			System.err.println("Connection failed!");
		}
		
		String connection = "/c/" + "test" + "/e/";
		client.send(connection.getBytes());
		listening = true;
		listen=new Thread(this,"running");
		listen.start();
		
	}
	
	public int findTarget(int botX, int botY){
		int target = 0, targX = 0, targY = 0;
		int x1 = botX, y1 = botY, x2 = 0, y2 = 0;
		
		for(int i = 0; i < getLength(locationsX); i++){
			x2 = locationsX[i];
			y2 = locationsY[i];
			targX = locationsX[target];
			targY = locationsY[target];
			if(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)) < Math.sqrt((x1-targX)*(x1-targX) + (y1-targY)*(y1-targY)) && x2 != -1 && y2 != -1) target = i;
		}
		return target;
	}
	
	public boolean checkAvailableTargets(){
		for(int i = 0; i < getLength(locationsX) ; i++){
			if(locationsX[i] != -1 && locationsY[i] != -1) return true;
			//System.out.println(locationsX[i] + "X" + locationsY[i]);
		}
		
		return false;
	
	}
	
	public int getLength(int[] arr){
		int count = 0;
		for(int el : arr)
		    if (el != 0)
		        ++count;
		return count;
	}
	
	public void run(){
	
		movementCount++;
	
		movementIndex++;
		movementIndex = movementIndex % (Barbarians.frameCount-1);
		attackIndex = movementIndex % (Barbarians.frameCountAttack-1);
		
		botX = (int)botLabel.getLocation().getX();
		botY = (int)botLabel.getLocation().getY();
		
		String sendIndex = "/index/" + clientID + "/x/" + enemyClientID + "/x/" + index + "/x/" + botX + "/x/" + botY + "/x/" + barbHP + "/e/";
		sendToServer(sendIndex);
		
		targIndex = findTarget(botX, botY);
		
		if(targX && targY){
			X = locationsX[targIndex];
			HP = buildingsHP[targIndex];
			Y = locationsY[targIndex];
			targX = false;
			targY = false;
		}
		topX = bottomX = X + (Buildings.width/2);
		leftX = X-(Buildings.width/2);
		rightX = X +(Buildings.width/2);
		topY = Y;
		bottomY = Y + Buildings.height;
		leftY = rightY = Y + (Buildings.height/2);
		
		if(Math.abs(botX - bottomX) < 3) botX = bottomX;
		if(Math.abs(botY - leftY) < 3) botY = leftY;
	
		try{
			if(movementIndex%movementFactor==0 && checkAvailableTargets() == true && life == true){
			
		
				//bottom
				if(botX < bottomX && botY > bottomY){//225
					botLabel.setLocation((int)botLabel.getLocation().getX()+(movementFactor*3), (int)botLabel.getLocation().getY()-(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTHEAST)[movementIndex].getSource())));
				ori = 0;
				}else if(botX == bottomX && botY > bottomY){//270
					botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY()-(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTH)[movementIndex].getSource())));
				ori = 0;
				}else if(botX > bottomX && botY > bottomY){//315
					botLabel.setLocation((int)botLabel.getLocation().getX()-(movementFactor*3), (int)botLabel.getLocation().getY()-(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTHWEST)[movementIndex].getSource())));
				ori = 0;
				//top
				}else if(botX < topX && botY < topY){//135
					botLabel.setLocation((int)botLabel.getLocation().getX()+(movementFactor*3), (int)botLabel.getLocation().getY()+(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTHEAST)[movementIndex].getSource())));
				ori = 1;
				}else if(botX == topX && botY < topY){//90
					botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY()+(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTH)[movementIndex].getSource())));
				ori = 1;
				}else if(botX > topX && botY < topY){//45
					botLabel.setLocation((int)botLabel.getLocation().getX()-(movementFactor*3), (int)botLabel.getLocation().getY()+(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTHWEST)[movementIndex].getSource())));
				ori = 1;
				//left
				}else if(botX < leftX && botY < leftY){//45
					botLabel.setLocation((int)botLabel.getLocation().getX()+(movementFactor*3), (int)botLabel.getLocation().getY()+(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTHEAST)[movementIndex].getSource())));
				ori = 2;
				}else if(botX < leftX && botY == leftY){//0
					botLabel.setLocation((int)botLabel.getLocation().getX()+(movementFactor*3), (int)botLabel.getLocation().getY());
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_EAST)[movementIndex].getSource())));
				ori = 2;
				}else if(botX < leftX && botY > leftY){//315
					botLabel.setLocation((int)botLabel.getLocation().getX()+(movementFactor*3), (int)botLabel.getLocation().getY()-(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTHEAST)[movementIndex].getSource())));
				ori = 2;
				//right
				}else if(botX > rightX && botY < rightY){//135
					botLabel.setLocation((int)botLabel.getLocation().getX()-(movementFactor*3), (int)botLabel.getLocation().getY()+(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTHWEST)[movementIndex].getSource())));
				ori = 3;
				}else if(botX > rightX && botY == rightY){//180
					botLabel.setLocation((int)botLabel.getLocation().getX()-(movementFactor*3), (int)botLabel.getLocation().getY());
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_WEST)[movementIndex].getSource())));
				ori = 3;
				}else if(botX > rightX && botY > rightY){//225
					botLabel.setLocation((int)botLabel.getLocation().getX()-(movementFactor*3), (int)botLabel.getLocation().getY()-(movementFactor*3));
					botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTHWEST)[movementIndex].getSource())));
				ori = 3;
				}else{
					HP = HP - 50;
					AttackRunner.buildingsHP[targIndex] = HP;
					if(HP <= 0) {
						String message = "/terminate/" + targIndex + "/e/";
						sendToServer(message);
						
					}
					switch(ori){
						case 0:	botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY());
								botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTHATTACK)[attackIndex].getSource())));
								break;
						case 1:
								botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY());
								botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_SOUTHATTACK)[attackIndex].getSource())));
								break;
						case 2:
								botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY());
								botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_EASTATTACK)[attackIndex].getSource())));
								break;
						case 3:
								botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY());
								botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_WESTATTACK)[attackIndex].getSource())));
								break;
					
					}
					
				}
				//System.out.println(botX + " " + leftX + " " + botY+ " " +rightY);
				
				
				
			}else if(life == false){
				//dead
				
				
			}else if(checkAvailableTargets() == false && indic == true){
				System.out.println("Attacker wins!");
				botLabel.setLocation((int)botLabel.getLocation().getX(), (int)botLabel.getLocation().getY());
				botLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Barbarians.getBufferedImageArray(Barbarians.bot_NORTH)[0].getSource())));
				//win
				//Game.cardlayout.show(Game.main_panel, "Victory");
				String message = "/result/" + clientID + "/x/" + enemyClientID +"/e/";
				sendToServer(message);
				indic = false;
				//AttackRunner.tt[index].cancel();
				//AttackRunner.tt[index].purge();
			}
		}catch(Exception e){
			System.out.println("Barbarian error");
			
		}
		listen();
	}
	
	public void terminateBuilding(int x){
		try{
		AttackRunner.tb[x].cancel();
		AttackRunner.tb[x].purge();
		AttackRunner.la[x].setIcon(null);
		AttackRunner.bmb[x].setIcon(null);
		targX = true;
		targY = true;
		locationsX[x] = -1;
		locationsY[x] = -1;
		AttackRunner.locationsX[x] = -1;
		AttackRunner.locationsY[x] = -1;	
		}catch(Exception e){
			System.out.println("Buildings termination error.");
		}
	}
	
	public void sendToServer(String message){
		client.send(message.getBytes());
	}

	public void listen() {
		
		listen = new Thread("Listen") {
			public void run() {
				while (listening) {
					//System.out.println("TEST1");
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
					}else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
					}else if (message.startsWith("/u/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String[] names = text.split("/n/");
					}else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
					}else if (message.startsWith("/terminate/")){
						String text = message.split("/terminate/|/e/")[1];
						int x = Integer.parseInt(text);
						terminateBuilding(x);
					}else if (message.startsWith("/terminateTroop/")){
						String text = message.split("/terminateTroop/|/e/")[1];
						int targetIndex = Integer.parseInt(text.split("/x/")[2]);
						//System.out.println(index);
						if(Integer.parseInt(text.split("/x/")[0])==clientID || Integer.parseInt(text.split("/x/")[1])==clientID){
							//attacker
							if(index == targetIndex) life = false;
						}
					}else{
					
					//System.out.println("TEST6");
					} 
				}
			}
		};
		listen.start();
	}

}
