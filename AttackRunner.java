import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.util.Random;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.Container;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class AttackRunner extends JInternalFrame implements Runnable{
	
	public static boolean dec= true;
	private static BufferedImage mainImage;
	public static final int buildsCount = 11;
	
	private static BufferedImage[] builds;

	//JInternalFrame mainFrame = new JInternalFrame("Test");
	JPanel mainPanel = new JPanel();
	JPanel panelHome = new JPanel(new FlowLayout());
	JPanel panel = new JPanel(new BorderLayout());
	final JPanel panelCenter = new JPanel(null);
	JPanel panelNorth = new JPanel(new GridLayout(1,2));
	JPanel panelDown = new JPanel();
	JButton butt[] = new JButton[7];
	String[] buttonName = new String[7];
	int[] buttonSummon = new int[7];
	
	public static int barb = 0;
	public static int bui = 0;
	int baseStates[][] = new int[10][10];
	int mouseStartX, mouseStartY;
	
	String name, address, enemyBase, enemyName;
	int port, clientID, enemyClientID;
	public static Buildings[] bi = new Buildings[100];
	public static Timer[] tb = new Timer[100];
	public static JLabel[] la = new JLabel[100];
	public static JLabel[] bmb = new JLabel[100];
	
	public static Barbarians[] t1 = new Barbarians[100];
	public static JLabel[] l1 = new JLabel[100];
	public static Timer[] tt = new Timer[100];
	
	public static int[] locationsX = new int[100];
	public static int[] locationsY = new int[100];
	public static int[] buildingsHP = new int[100];
	
	//public static int[] troopLocationsX = new int[100];
	//public static int[] troopLocationsY = new int[100];
	//public static int[] troopHP = new int[100];
	private boolean running=false;
	private Client client;
	Thread listen, run;

	
	/**
	*Concern primarily with the layout of the program
	*/
	
	
	public AttackRunner(final String name,final String address,final int port, final int clientID, final String enemyBase, final String enemyName, final int enemyClientID){

		JLabel stickman = new JLabel();
		
		client = new Client(name,address,port);
		boolean connected = client.conn(address);
		
		if(!connected){
			System.err.println("Connection failed!");
		
		}
		
		this.name = name;
		this.address = address;
		this.port = port;
		this.clientID = clientID;
		
		this.enemyName = enemyName;
		this.enemyBase = enemyBase;
		this.enemyClientID = enemyClientID;
		
		buttonName[0] = "Barbarian";
		buttonName[1] = "Archer";
		buttonName[2] = "Giant";
		buttonName[3] = "Wizard";
		buttonName[4] = "Dragon";
		buttonName[5] = "Wall Breaker";
		buttonName[6] = "Hog Rider";
		
		for(int d=0; d<7; d++){
			buttonSummon[d] = 5;
			butt[d] = new JButton(buttonName[d] + " [" + buttonSummon[d] + "]");
		}
				
		panel.add(panelNorth, BorderLayout.NORTH);
		
		Dimension d = new Dimension(1000,50);
		Dimension d2 = new Dimension(1800,1800);
		panelDown.setBackground(new Color(0,0,0));
		panelDown.setPreferredSize(d);
		panelDown.add(butt[0]);
		panelDown.add(butt[1]);
		panelDown.add(butt[2]);
		panelDown.add(butt[3]);
		panelDown.add(butt[4]);
		panelDown.add(butt[5]);
		panelDown.add(butt[6]);
		panel.add(panelDown, BorderLayout.SOUTH);
		
		panelNorth.setBackground(new Color(0,0,0));
		panelNorth.setPreferredSize(d);
		panel.add(panelNorth, BorderLayout.NORTH);
		
		
		panelCenter.setBackground(new Color(102,102,102));
		panelCenter.add(stickman);
		panelCenter.setPreferredSize(d2);
		final JScrollPane scrollPane = new JScrollPane(panelCenter);
		MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
           
		   	public void mouseDragged(MouseEvent e) {

		       JViewport viewPort = scrollPane.getViewport();
		       Point vpp = viewPort.getViewPosition();
		       vpp.translate(mouseStartX-e.getX(), mouseStartY-e.getY());
		       panelCenter.scrollRectToVisible(new Rectangle(vpp, viewPort.getSize()));

			}

			public void mousePressed(MouseEvent e) {

				mouseStartX = e.getX();
				mouseStartY = e.getY();

			}
        };
        
        scrollPane.getViewport().addMouseListener(mouseAdapter);
        scrollPane.getViewport().addMouseMotionListener(mouseAdapter);
		
		panel.add(scrollPane);
		setVisible(true);
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1090, 720);
		baseStates = decodeBase(this.enemyBase);
		
		int x=150, y=150;
		
		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				//fieldStates[a][b] = Integer.parseInt(arr[cnt]);
				//cnt++;
				
				createBuildings(baseStates[a][b], x, y);
				x+=150;
			}
			
			y+=150;
			x=150;
		}
		
		int i = 0;
		do{
		final int tmp = i;
			butt[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent s) {
					//levels(tmp);
					buttonSummon[tmp]--;
					butt[tmp].setText(buttonName[tmp] + " [" + buttonSummon[tmp] + "]");
					if(buttonSummon[tmp] <= 0) butt[tmp].setEnabled(false);
					String message = "/troop/" + clientID + "/x/" + tmp + "/x/" + enemyClientID + "/e/";
					sendToServer(message);
				}
			});
			i++;
		}while(i < 7);
		
		String connection = "/c/" + clientID + "/e/";
		client.send(connection.getBytes());
		running = true;
		run=new Thread(this,"running");
		run.start();
	}
	
	public int[][] decodeBase(String createdBase){
		
		String tmp = createdBase;
		String[] arr = tmp.split("#");
		int fieldStates[][] = new int[10][10];
		int cnt = 0;
		
		for(int a = 0; a < 10; a++){
			for(int b = 0; b < 10; b++){
				fieldStates[a][b] = Integer.parseInt(arr[cnt]);
				cnt++;
			}
		}
		return fieldStates;
	}
	
	public void createBuildings(int num, int x, int y){
		
	
		la[bui] = new JLabel();
		bmb[bui] = new JLabel();
		panelCenter.add(la[bui]);
		panelCenter.add(bmb[bui]);
		bi[bui] = new Buildings();
		tb[bui] = new Timer();
			
		if(num!=0){
			bi[bui].loadSprites();
			bi[bui].setID(bui);
			la[bui].setBounds(x, y,132,145);
			la[bui].setIcon(bi[bui].getStartingImage(num));
			bmb[bui].setIcon(bi[bui].getStartingImage(7));
			bmb[bui].setBounds(x, y,130,145);
			locationsX[bui] = x;
			locationsY[bui] = y;
			buildingsHP[bui] = 500;
			buildingsTimerTask ctt1 = new buildingsTimerTask(la[bui], 1, bmb[bui], name, address, port, bui, clientID, enemyClientID);
			tb[bui].schedule(ctt1, 0, 75);
			bui++;
		}	
	}
	
	/**
	*The levels of the game .
	*The only component changing in different levels is the number of bots.
	*/
	public void levels(int x){
			
		l1[barb] = new JLabel();
		panelCenter.add(l1[barb]);
		t1[barb] = new Barbarians();
		tt[barb] = new Timer();
		
		Timer t = new Timer();
		try{
		switch(x){
		case 0: t1[barb].loadSprites();
				l1[barb].setBounds(200,440,74,57);
				l1[barb].setIcon(Barbarians.getStartingImage());
				botTimerTask ctt = new botTimerTask(l1[barb], 1, locationsX, locationsY, buildingsHP, name, address, port, barb, clientID, enemyClientID);
				tt[barb].schedule(ctt, 0, 75);
				barb++;
				break;
		default:t1[barb].loadSprites();
				l1[barb].setBounds(200,440,74,57);
				l1[barb].setIcon(Barbarians.getStartingImage());
				botTimerTask ctt_def = new botTimerTask(l1[barb], 1, locationsX, locationsY, buildingsHP, name, address, port, barb, clientID, enemyClientID);
				tt[barb].schedule(ctt_def, 0, 75);
				barb++;
				break;
		}
		}catch(Exception e){}
	}
	
	public boolean checkTroops(){
		for(int i=0; i<7; i++){
			if(buttonSummon[0]>0) return false;
		}
		return true;
	}
	
	public static void freeThreads(){
		int i;
		barb = 0;
		bui = 0;
		
			for(i=0; i<tb.length; i++){
				try{
				tb[i].cancel();
				tb[i].purge();
				}catch(Exception e){}

			}
			
			for(i=0; i<tt.length; i++){
				try{
				tt[i].cancel();
				tt[i].purge();
				}catch(Exception e){}
			}
		
		
	}
	
	public void sendToServer(String message){
		client.send(message.getBytes());
	}
	
	public void run() {
		listen();
	}
	
	public void listen() {
		
		listen = new Thread("Listen") {
			public void run() {
				while (running) {
				//System.out.println("TEST6");
				
					if(checkTroops() == true){
						String text = "/troopExhaust/" + clientID + "/x/" + enemyClientID + "/e/";
						sendToServer(text);
					}
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));		
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
					}else if (message.startsWith("/u/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String[] names = text.split("/n/");
					}  else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
					}else if (message.startsWith("/a/")){
						String text = message.substring(3);
						if(Integer.parseInt(text.split("/x/")[1])==clientID){
							//decodeAttack(text, 1);
						}else if(Integer.parseInt(text.split("/x/")[3])==clientID){
							//decodeAttack(text, 2);
						}
					}else if(message.startsWith("/troop/")){
						String text = message.split("/troop/|/e/")[1];
						int x = Integer.parseInt(text.split("/x/")[1]);
						if(Integer.parseInt(text.split("/x/")[0])==clientID || Integer.parseInt(text.split("/x/")[2])==clientID){
							System.out.println("Fight!");
							levels(x);
						}
						
					}else{
					
				
					} 
				
				}
			}
		};
		listen.start();
	}
}
