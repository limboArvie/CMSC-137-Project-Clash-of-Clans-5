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

public class Buildings{

	public static int width = 132;
    public static int height = 145;
	
	public static int bombWidth = 65;
    public static int bombHeight = 74;
    
    private static final int IMG_WIDTH = 390;
	private static final int IMG_HEIGHT = 256;
	
	public static int X = 0;
	public static int Y = 0;
	
	public static int targetX = 0;
	public static int targetY = 0;
	
	public static int HP = 500;
	public static int bID;

	public static final int frameCount = 4;
	public static final int bombCount = 16;
	private  JLabel botImage;
	private static BufferedImage mainImage;
	private static BufferedImage bombImage;
	
	private static BufferedImage[] buildings;
	private static BufferedImage[] bomb;
	public static int[] buildingsDamage;

	public static final int bot_NORTH = 1;
	public static final int bot_SOUTH = 2;
	public static final int bot_WEST = 3;
	public static final int bot_EAST = 4;
	public static final int bot_NORTHWEST = 5;
	public static final int bot_NORTHEAST = 6;
	public static final int bot_SOUTHWEST = 7;
	public static final int bot_SOUTHEAST = 8;
	int temp = 0;
	public static boolean terminate = false;
	
	/**
	*returns the starting image of the bots
	*/
	public static ImageIcon getStartingImage(int x){
		switch(x){
		case 1: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buildings[0].getSource()));
				
		case 2: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buildings[1].getSource()));
					
		case 3: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buildings[2].getSource()));
					
		case 4: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buildings[3].getSource()));
		
		case 7: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(bomb[0].getSource()));
		
		default: return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buildings[0].getSource()));
		}
	}

	/**
	*Returns the direction of the bots
	*/
	public static BufferedImage[] getBufferedImageArray(){
		return bomb;
		
	}
	
	/**
	*loads the sprites to be used for the bots
	*/
	public static void loadSprites(){
	
		try{
		
			mainImage = ImageIO.read(new File("build/buildings_new.png"));
			bombImage = ImageIO.read(new File("char/bomb.png"));
			
			buildings = new BufferedImage[frameCount];
			bomb = new BufferedImage[bombCount];
			buildingsDamage = new int[frameCount];
			
			for(int i=0; i<frameCount; i++){
				buildings[i] = mainImage.getSubimage(i*width, height*0, width, height);
				buildingsDamage[i] = 50;
			}
			
			for(int k=0; k<bombCount; k++){
				bomb[k] = bombImage.getSubimage(k*bombWidth, bombHeight*0, bombWidth, bombHeight);
			}
			
		}
		catch(Exception e){
			System.out.println("Spritesheet Not Found!");
		}
	
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		
		return resizedImage;
    }
	
	public static int locationX(){
		return X;
	}
	
	/**
	*Returns the position of the user's character in the Y-axis
	*/
	public static int locationY(){
		return Y;
	}
	
	public static int getHP(){
		return HP;
	}
	
	public static int getID(){
		return bID;
	}
	
	public static void setID(int change){
		bID = change;
	}
	
	
	public static void setHP(int change){
		HP = change;
	}
	
	public static void terminateHP(int X, int Y){
		targetX = X;
		targetY = Y;
	
	}
	
}
	
/**
*Timertask class for the bots
*/
class buildingsTimerTask extends TimerTask{

	JLabel botLabel;
	JLabel bombLabel;
	int movementIndex = 0;
	int movementCount = 0;
	int movementFactor = 0;
	int temp = 0;
	boolean target = false, troopExhaust = false, indic = true;
	int[] troopLocationX = new int[100];
	int[] troopLocationY = new int[100];
	int[] troopHP = new int[100];
	int targIndex, targetX, targetY, targetHP = 10, buildX, buildY, centerX, centerY;
	
	String name, address;
	int port, index, clientID, enemyClientID;
	
	
	private boolean listening=false;
	private Client client;
	Thread listen, run2;
	
	
	
	/**
	*Constructor of the botTimerTask with specified JLabel and integer parameters
	*/
	public buildingsTimerTask(JLabel botLabel, int movementFactor, JLabel bombLabel, String name, String address, int port, int index, int clientID, int enemyClientID){
		this.botLabel = botLabel;
		this.movementFactor = movementFactor;
		this.bombLabel = bombLabel;
		this.name = name;
		this.address = address;
		this.port = port;
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
		
		for(int i = 0; i < getLength(troopLocationX); i++){
			x2 = troopLocationX[i];
			y2 = troopLocationY[i];
			targX = troopLocationX[target];
			targY = troopLocationY[target];
			if(Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)) < Math.sqrt((x1-targX)*(x1-targX) + (y1-targY)*(y1-targY)) && x2 != -1 && y2 != -1) target = i;
		}
		return target;
	}
	
	public boolean checkAvailableTargets(){
		for(int i = 0; i < getLength(troopLocationX) ; i++){
			if(troopLocationX[i] != -1 && troopLocationY[i] != -1) return true;
			//System.out.println(troopLocationX[i] + "X" + troopLocationY[i]);
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
		movementIndex = movementIndex % Buildings.bombCount;
		
		buildX = (int)botLabel.getLocation().getX();
		buildY = (int)botLabel.getLocation().getY();
		
		centerX = buildX + (Buildings.width/2);
		centerY = buildY + (Buildings.height/2);
		
		if(target == false && checkAvailableTargets() == true){
			targIndex = findTarget(buildX, buildY);
			targetX = troopLocationX[targIndex];
			targetY = troopLocationY[targIndex];
		}
		if(Math.abs(centerX - targetX) < 150 && Math.abs(centerY - targetY) < 150) target = true;
		try{
			if(movementIndex%movementFactor==0 && target == true && checkAvailableTargets() == true){
				
				bombLabel.setLocation(targetX, targetY);
				bombLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Buildings.getBufferedImageArray()[movementIndex].getSource())));
				
				//System.out.println(targetHP);
				if(movementIndex == Buildings.bombCount-1){
					targetHP = targetHP - Buildings.buildingsDamage[0];
					
				}
				if(targetHP <=0){
					String terminate = "/terminateTroop/" + clientID + "/x/" + enemyClientID + "/x/" + targIndex + "/e/";
					sendToServer(terminate);
					
				}
			}else if(troopExhaust == true && indic == true && checkAvailableTargets() == false){
				System.out.println("Defender wins!");
				String message = "/result/" + clientID + "/x/" + enemyClientID +"/e/";
				sendToServer(message);
				//troopExhaust = false;
				//AttackRunner.tb[index].cancel();
			//	AttackRunner.tb[index].purge();
				indic = false;
			
			}/*else if(target == false){
				bombLabel.setLocation(centerX, centerY);
				bombLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Buildings.getBufferedImageArray()[movementIndex].getSource())));
				
			}*/
		}catch(Exception e){
			
			System.out.println("Building error");
		}
		listen();
	}
	
	public void terminateTroop(int x){
	
		AttackRunner.tt[x].cancel();
		AttackRunner.tt[x].purge();
		AttackRunner.l1[x].setIcon(null);
		troopLocationX[x] = -1;
		troopLocationY[x] = -1;
		target = false;	
		
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
					}else if (message.startsWith("/index/")){
						String text = message.split("/index/|/e/")[1];
						int index = Integer.parseInt(text.split("/x/")[2]);
						int x = Integer.parseInt(text.split("/x/")[3]);
						int y = Integer.parseInt(text.split("/x/")[4]);
						int HP = Integer.parseInt(text.split("/x/")[5]);
						
						if(Integer.parseInt(text.split("/x/")[0])==clientID || Integer.parseInt(text.split("/x/")[1])==clientID){
							//attacker
							troopLocationX[index] = x;
							troopLocationY[index] = y;
							if(troopHP[index] == 0)troopHP[index] = HP;
						}
						
					}else if (message.startsWith("/terminateTroop/")){
						String text = message.split("/terminateTroop/|/e/")[1];
						int index = Integer.parseInt(text.split("/x/")[2]);
						//System.out.println(index);
						if(Integer.parseInt(text.split("/x/")[0])==clientID || Integer.parseInt(text.split("/x/")[1])==clientID){
							//attacker
							terminateTroop(index);
						}
						
					}else if (message.startsWith("/troopExhaust/")){
						String text = message.split("/troopExhaust/|/e/")[1];
						//System.out.println(index);
						if(Integer.parseInt(text.split("/x/")[0])==clientID || Integer.parseInt(text.split("/x/")[1])==clientID){
							//attacker
							troopExhaust = true;
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
