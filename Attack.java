import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class Attack extends JFrame implements ActionListener{
	JPanel stats_panel, sprites_panel, left_panel, main_panel, nav_panel;;
	JScrollPane scroller;
	JButton attack_button, logout_button, sel_base, build_base, sel_back, base1;
	JLabel base_panel, player, damage, points;
	ButtonGroup sprite_group;
	public static String s;
	public CardLayout cardlayout;
	private boolean running=false;
	private Client client;
	String name, address;
	Thread listen,run;
	int port, clientID;
	int place;

	public Attack(final String name,final String address,final int port, final int clientID, final String base){
	
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
		main_panel.setBackground(Color.RED);
		c.add(main_panel, BorderLayout.CENTER);

		//navigation panel
		nav_panel = new JPanel();
		nav_panel.setBackground(Color.BLUE);

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
				//FieldButtons.CreateFieldButton();
				//FieldButtons.CreateBuildButton();
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

		//game area
		left_panel = new JPanel();
		left_panel.setLayout(new BorderLayout());

		//play area
		base_panel = new JLabel();
		ImageIcon icon = new ImageIcon("sprites/buildings/background.png"); 
		base_panel.setIcon(icon);
		base_panel.setLayout(new GridLayout(1,0));
		GridLayout fieldLayout = new GridLayout(15,20);
		FieldButtons.field_panel.setLayout(fieldLayout);
		FieldButtons.field_panel.setOpaque(false);
		base_panel.add(FieldButtons.field_panel);

		//game and player statistics area
		stats_panel = new JPanel();
		stats_panel.setBackground(Color.ORANGE);
		
		player = new JLabel("PLAYER NAME: "+ this.name);
		attack_button = new JButton("ATTACK A BASE");
		attack_button.setBackground(Color.GREEN);
		logout_button = new JButton("LOGOUT");
		logout_button.setBackground(Color.RED);

		stats_panel.add(player);
		stats_panel.add(attack_button);
		stats_panel.add(logout_button);

		//troops and buildings chooser area
		sprites_panel = new JPanel();
		FlowLayout fieldLayout1 = new FlowLayout();
		FieldButtons.chooser_panel.setLayout(fieldLayout1);
		sprites_panel.add(FieldButtons.chooser_panel);
		scroller = new JScrollPane(sprites_panel);

		//complete the left panel
		left_panel.add(base_panel, BorderLayout.CENTER);
		left_panel.add(stats_panel, BorderLayout.NORTH);
		left_panel.add(scroller, BorderLayout.SOUTH);
	
		//add the navigation panel to the main panel
		main_panel.add(nav_panel, "Navigation");

		//add the left to the main panel
		main_panel.add(FieldButtons.selBase_panel, "SelectBase");

		//add the left to the main panel
		main_panel.add(left_panel, "BuildBase");

		//initialize the base, and the sprites_panel
		//.txt files containing the paths of the buildings', and the troops' sprites
		FieldButtons.ImageList("buildings_list.txt");
		FieldButtons.ImageList("troops_list.txt");
		
		attack_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getEnemy();
			}
		});
		
		build_base.doClick();
		
		
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
		int fieldStates[][] = new int[15][20];
		int cnt = 0;
		
		for(int a = 0; a < 15; a++){
			for(int b = 0; b < 20; b++){
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
	String message = "/a/" + clientID+ "/e/";
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
						
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						//console(text);
					}else if (message.startsWith("/u/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String[] names = text.split("/n/");
						
						//console(Arrays.toString(names));
					}  
					else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						//send(text, false);
					} else if (message.startsWith("/fish/")){
						String text = message.substring(6);
						// type,dir,x,y
						
						text = text.split("/e/")[0];
						String[] data = text.split("/");
						//createFishes(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3]));
						// console(text);
					}
				}
			}
		};
		listen.start();
	}
}
