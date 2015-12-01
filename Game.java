import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class Game extends JFrame implements Runnable, ActionListener{
	public static JPanel stats_panel, sprites_panel, left_panel, main_panel, nav_panel, attack_panel, troops_panel, damage_panel, troops_chooser_panel, feature_panel, win_panel, defeat_panel;
	JScrollPane scroller, troops_scroller;
	public static JButton option_button, logout_button, sel_base, build_base, sel_back, attackOpp_button, chatOpp_button, mybase_button;
	JLabel base_panel, feature_label, opp_base_panel, player, opp_name, damage, points, win_label, defeat_label;
	ButtonGroup sprite_group;
	public static String s;
	public static CardLayout cardlayout;
	private boolean running=false;
	private Client client;
	Thread listen,run;
	
	String name, address;
	
	int port, clientID;
	int place;
	
	String enemyName = "", enemyBase = "0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#";

	int enemyClientID = 0;

    
	public Game(final String name,final String address,final int port, final int clientID){	
		client = new Client(name,address,port);
		boolean connected = client.conn(address);
		
		if(!connected){
			System.err.println("Connection failed!");
		
		}
		
		Container c = getContentPane();
		setTitle("Clash of Clans");
		setSize(1090,720);
		setVisible(true);
		setResizable(false);
		//main panel
		
		this.name = name.split("/x/")[0];
		this.address = address;
		this.port = port;
		this.clientID = clientID;
		
		
		System.out.println(Integer.toString(clientID));
		
		main_panel = new JPanel();
		main_panel.setLayout(new CardLayout());
		cardlayout = (CardLayout) main_panel.getLayout();

		main_panel.setPreferredSize(new Dimension(1090,720));
		main_panel.setBackground(Color.BLACK);
		c.add(main_panel, BorderLayout.CENTER);

		FieldButtons.CreatePlayerState(decodeBase(name));
		cardlayout.show(main_panel, "BuildBase");

		//navigation panel
		nav_panel = new JPanel();
		nav_panel.setBackground(new Color(102,102,102));

		//Select a Base Button
		sel_base = new JButton("Select a Base");

		//When clicked, build mode for a base will be shown
		sel_base.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				FieldButtons.BaseList();
				cardlayout.show(main_panel, "SelectBase");
			}
		});

		//Build a Base Button
		build_base = new JButton("Build a Base");

		//When clicked, build mode for a base will be shown
		build_base.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				FieldButtons.CreatePlayerState(decodeBase(name));
				cardlayout.show(main_panel, "BuildBase");
			}
		});

		nav_panel.add(sel_base);
		nav_panel.add(build_base);

		//Bases Button
		GridLayout grid1 = new GridLayout(2,2);
		grid1.setHgap(5);
		grid1.setVgap(5);
		FieldButtons.selBase_panel.setLayout(grid1);
		CreateBaseButton();
		
		//Back Button from Select a Base
		sel_back = new JButton("Back");

		//When clicked, it will show the navigation
		sel_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				cardlayout.show(main_panel, "Navigation");
			}
		});

		//This will be the panel for all the features of the game
		feature_label = new JLabel();
		ImageIcon featicon = new ImageIcon("buttons/background2.png"); 
		feature_label.setIcon(featicon);

		feature_panel = new JPanel();
		feature_panel.setOpaque(false);
		

		//BUTTONS IN THE FEATURE PANEL
		
		attack_panel = new JPanel();
		attack_panel.setLayout(new BorderLayout());
		
		final AttackRunner ar = new AttackRunner(this.name, this.address, this.port, this.clientID, this.enemyBase, this.enemyName, this.enemyClientID);
		attack_panel.add(ar);
		
		attackOpp_button = new JButton();
		ImageIcon attackicon = new ImageIcon("buttons/attack.png"); 
		attackOpp_button.setIcon(attackicon);
		attackOpp_button.setPreferredSize(new Dimension(250,94));

		attackOpp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
					
					//ar.setVisible(true);
				System.out.println("true");
				getEnemy();
				
				
				//cardlayout.show(main_panel, "AttackOpponent");
				//FieldButtons.CreateOppFieldButton();
				//FieldButtons.CreateTroopButton();
			}
		});
		

		//stats panel
		damage_panel = new JPanel();
		damage_panel.setBackground(Color.ORANGE);

		opp_name = new JLabel("OPPONENT'S NAME: ");
		damage = new JLabel("DAMAGE: ");
		points = new JLabel("0");
		points.setForeground(Color.RED);

		damage_panel.add(opp_name);
		damage_panel.add(damage);
		damage_panel.add(points);

		//opponent's base
		opp_base_panel = new JLabel();
		ImageIcon icon1 = new ImageIcon("sprites/buildings/background.png"); 
		opp_base_panel.setIcon(icon1);
		opp_base_panel.setLayout(new GridLayout(1,0));
		GridLayout opp_fieldLayout = new GridLayout(10,10);
		FieldButtons.opp_field_panel.setLayout(opp_fieldLayout);
		FieldButtons.opp_field_panel.setOpaque(false);
		opp_base_panel.add(FieldButtons.opp_field_panel);

		troops_panel = new JPanel();
		FlowLayout fLayout = new FlowLayout();
		FieldButtons.troops_chooser_panel.setLayout(fLayout);
		troops_panel.add(FieldButtons.troops_chooser_panel);
		troops_scroller = new JScrollPane(troops_panel);
		
		attack_panel.add(damage_panel, BorderLayout.NORTH);
		attack_panel.add(opp_base_panel, BorderLayout.CENTER);
		attack_panel.add(troops_scroller, BorderLayout.SOUTH);

		//My Base Button
		mybase_button = new JButton();
		ImageIcon mybaseicon = new ImageIcon("buttons/myBase.png"); 
		mybase_button.setIcon(mybaseicon);
		mybase_button.setPreferredSize(new Dimension(250,94));

		mybase_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				cardlayout.show(main_panel, "BuildBase");
			}
		});

		//Chat with Players Button
		chatOpp_button = new JButton();
		ImageIcon chaticon = new ImageIcon("buttons/chat.png"); 
		chatOpp_button.setIcon(chaticon);
		chatOpp_button.setPreferredSize(new Dimension(250,94));

		chatOpp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {

			}
		});


		//Logout Button		
		logout_button = new JButton();
		ImageIcon logouticon = new ImageIcon("buttons/logout.png"); 
		logout_button.setIcon(logouticon);
		logout_button.setPreferredSize(new Dimension(250,94));

		logout_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				
			}
		});

		//adding buttons to feature panel
		feature_panel.add(attackOpp_button);
		feature_panel.add(mybase_button);
		feature_panel.add(chatOpp_button);
		feature_panel.add(logout_button);

		feature_panel.add(feature_label);

		//game area
		left_panel = new JPanel();
		left_panel.setLayout(new BorderLayout());

		//player's base
		base_panel = new JLabel();
		ImageIcon icon2 = new ImageIcon("sprites/buildings/background.png"); 
		base_panel.setIcon(icon2);
		base_panel.setLayout(new GridLayout(1,0));
		GridLayout fieldLayout = new GridLayout(10,10);
		FieldButtons.field_panel.setLayout(fieldLayout);
		FieldButtons.field_panel.setOpaque(false);
		base_panel.add(FieldButtons.field_panel);

		//game and player statistics area
		stats_panel = new JPanel();
		stats_panel.setBackground(Color.BLACK);
		
		player = new JLabel("PLAYER NAME: "+ this.name);
		option_button = new JButton();
		option_button.setPreferredSize(new Dimension(250,94));
		ImageIcon optionicon = new ImageIcon("buttons/options.png"); 
		option_button.setIcon(optionicon);

		stats_panel.add(player);
		stats_panel.add(option_button);

		//buildings chooser area
		sprites_panel = new JPanel();
		FlowLayout fieldLayout1 = new FlowLayout();
		FieldButtons.chooser_panel.setLayout(fieldLayout1);
		sprites_panel.add(FieldButtons.chooser_panel);
		scroller = new JScrollPane(sprites_panel);

		//complete the left panel
		left_panel.add(stats_panel, BorderLayout.NORTH);
		left_panel.add(base_panel, BorderLayout.CENTER);
		left_panel.add(scroller, BorderLayout.SOUTH);

		//Victory Panel
		win_panel = new JPanel();
		win_label = new JLabel();
		ImageIcon winicon = new ImageIcon("buttons/victory.png"); 
		win_label.setIcon(winicon);
		win_panel.add(win_label);


		//Defeat Panel
		defeat_panel = new JPanel();
		defeat_label = new JLabel();
		ImageIcon defeaticon = new ImageIcon("buttons/defeat.png");
		defeat_label.setIcon(defeaticon);
		defeat_panel.add(defeat_label);
	
			//add the left to the main panel
		main_panel.add(left_panel, "BuildBase");

		
		//add the navigation panel to the main panel
		main_panel.add(nav_panel, "Navigation");

		//add the left to the main panel
		main_panel.add(FieldButtons.selBase_panel, "SelectBase");

	
		//add feature panel to the main panel
		main_panel.add(feature_panel, "Features");

		//add the attack_panel to the main panel
		main_panel.add(attack_panel,"AttackOpponent");

		//add victory panel to the main_panel
		main_panel.add(win_panel, "Victory");

		//add defeat panel to the main panel
		main_panel.add(defeat_panel, "Defeat");
		//initialize the base, and the sprites_panel
		//.txt files containing the paths of the buildings', and the troops' sprites
		FieldButtons.ImageList("buildings_list.txt");
		FieldButtons.ImageList("troops_list.txt");
		
		option_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cardlayout.show(main_panel, "Features");
				
			}
		});
		
		//console("Attempting a connection to " + this.address + ":" + port + ", user: " + name);
		String connection = "/c/" + clientID + "/e/";
		client.send(connection.getBytes());
		running = true;
		run=new Thread(this,"running");
		run.start();
		
		//build_base.doClick();
		
		
	}

	public void CreateBaseButton(){

		FieldButtons.BaseList();

		int i = 0, j = 0, k = 0;
		
		for(i = 0; i < 2; i++){
			for(j = 0; j < 2; j++){
				FieldButtons.baseButton[i][j] = new JButton(FieldButtons.bases.get(k));
				FieldButtons.selBase_panel.add(FieldButtons.baseButton[i][j]);
				k++;
				FieldButtons.baseButton[i][j].addActionListener(new ActionListener() { // This is for the action listener of each button
					public void actionPerformed(ActionEvent h){
						int a = 0, b = 0;
						Object selected;
						selected = h.getSource();
						
						for(a = 0; a < 2; a++){
							for(b = 0; b < 2; b++) {
								if(FieldButtons.baseButton[a][b] == selected) {
									if(a == 0 && b == 0){
										s = "base1.txt";
									}

									else if(a==0 && b == 1){
										s = "base2.txt";
									}

									else if(a==1 && b == 0){
										s = "base3.txt";
									}

									else if(a==1 && b == 1){
										s = "base4.txt";
									}		
								}
							}
						}

						try{
							File read_file = new File(s);
							Scanner sc = new Scanner(read_file);

							while(sc.hasNextLine()){
								String line = sc.nextLine();
								String state[] = line.split(" ");
								
								FieldButtons.UpdateState(state);
								cardlayout.show(main_panel, "BuildBase");

							}
						}

						catch(Exception e){

						}

					}

				});
			}
		}
	}
	
	public int[][] decodeBase(String createdBase){
		
		String tmp = createdBase.split("/x/")[1];
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

	public void actionPerformed(ActionEvent evt){
		try{

		}
		catch(Exception e){

		}
	}
	
	public void getEnemy(){
		String message = "/a/" + name+ "/x/"+ clientID+"/e/";
		sendToServer(message); 
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
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						//console("Successfully connected to server! ID: " + client.getID());
						//System.out.println("TEST1");
						
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						//System.out.println("TEST2");
						//console(text);
					}else if (message.startsWith("/u/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String[] names = text.split("/n/");
						//System.out.println("TEST3");
						
						//console(Arrays.toString(names));
					}  
					else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						//System.out.println("TEST4");
						//send(text, false);
					}else if (message.startsWith("/a/")){
						String text = message.split("/a/|/e/")[1];
						System.out.println(text.split("/x/")[4]);
						if(Integer.parseInt(text.split("/x/")[1]) != 0){
							if(Integer.parseInt(text.split("/x/")[1])==clientID){
								decodeAttack(text, 1);
							}else if(Integer.parseInt(text.split("/x/")[3])==clientID){
								decodeAttack(text, 2);
							}
						}else{
							System.out.println("No enemy Found!");
						}
						//System.out.println("TEST5");
					}else if(message.startsWith("/result/")){
						String text = message.split("/result/|/e/")[1];
						if(Integer.parseInt(text.split("/x/")[0])==clientID){
							result(0);
						}
						if(Integer.parseInt(text.split("/x/")[1])==clientID){
							result(1);
						}
						
						
					}else{
					
					//System.out.println("TEST6");
					} 
				
				}
			}
		};
		listen.start();
	}
	
	public void result(int x){
		System.out.println("TRUE");
		switch(x){
			case 0: 
					cardlayout.show(main_panel, "Victory");
					//this.dispose();
					//ClientWindow.closeClient();
					//AttackRunner.freeThreads();
					break;
			
			case 1:	
					cardlayout.show(main_panel, "Defeat"); 
					//AttackRunner.freeThreads();
					//this.dispose();
					//ClientWindow.closeClient();
					break;
		}
	}
	
	public void decodeAttack(String text, int type){
		
		
		String attacker = text.split("/x/")[0];
		String defender = text.split("/x/")[2];
		String baseDef = text.split("/x/")[4];
		//console(attacker + " started a war with "+defender+"!");
		
		
		enemyName = defender;
		enemyBase = baseDef;
		enemyClientID = Integer.parseInt(text.split("/x/")[3]);
		
		
		final AttackRunner ar2 = new AttackRunner(this.name, this.address, this.port, this.clientID, baseDef, defender, enemyClientID);
		attack_panel.removeAll();
		attack_panel.add(ar2);
		attack_panel.revalidate();
		cardlayout.show(main_panel, "AttackOpponent");
		
		if(type == 1){
			for(int i =0; i<7; i++){
				ar2.butt[i].setEnabled(true);
			}
			
			JLabel enemyBanner = new JLabel(defender + "'s Base");
			ar2.panelNorth.add(enemyBanner);
		
		}else if(type == 2){
			for(int i =0; i<7; i++){
				ar2.butt[i].setEnabled(false);
			}
			
			JLabel enemyBanner = new JLabel("You are under attack by "+ attacker + "!");
			ar2.panelNorth.add(enemyBanner);
		}		
	}
}
