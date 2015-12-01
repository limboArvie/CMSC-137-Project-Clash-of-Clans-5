import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.EventQueue;


public class Base extends JFrame implements ActionListener{
	JPanel stats_panel, sprites_panel, left_panel, main_panel, navleft_panel, navbutton_panel, navbuttonup_panel, navbuttondown_panel, navright_panel;
	JScrollPane scroller;
	JButton save_button, logout_button, sel_base, build_base, sel_back;
	JLabel base_panel, player, nav_panel;
	ButtonGroup sprite_group;
	public static String s;
	public CardLayout cardlayout;

	public Base(final String name,final String address,final int port){
	
		Container c = getContentPane();
		setTitle("Clash of Clans");
		setSize(1090,720);
		setVisible(true);
		setResizable(false);

		//main panel
		main_panel = new JPanel();
		main_panel.setLayout(new CardLayout());
		cardlayout = (CardLayout) main_panel.getLayout();

		main_panel.setPreferredSize(new Dimension(1090,720));
		main_panel.setBackground(Color.RED);
		c.add(main_panel, BorderLayout.CENTER);

		//navigation panel for the customizing the base
		nav_panel = new JLabel();
		ImageIcon navicon = new ImageIcon("buttons/background1.png"); 
		nav_panel.setIcon(navicon);
		nav_panel.setLayout(new BorderLayout());

		navleft_panel = new JPanel();
		navleft_panel.setBackground(Color.WHITE);
		navleft_panel.setOpaque(false);
		navleft_panel.setPreferredSize(new Dimension(363,720));
		nav_panel.add(navleft_panel, BorderLayout.WEST);

		navbutton_panel = new JPanel();
		navbutton_panel.setBackground(Color.WHITE);
		navbutton_panel.setOpaque(false);
		navbutton_panel.setLayout(new BorderLayout());
		nav_panel.add(navbutton_panel, BorderLayout.CENTER);

		navbuttonup_panel = new JPanel();
		navbuttonup_panel.setBackground(Color.WHITE);
		navbuttonup_panel.setPreferredSize(new Dimension(363,240));
		navbuttonup_panel.setOpaque(false);
		navbutton_panel.add(navbuttonup_panel, BorderLayout.NORTH);

		navbuttondown_panel = new JPanel();
		navbuttondown_panel.setBackground(Color.WHITE);
		navbuttondown_panel.setPreferredSize(new Dimension(363,480));
		navbuttondown_panel.setOpaque(false);
		GridLayout gridButtons = new GridLayout(4,1);
		gridButtons.setVgap(10);
		navbuttondown_panel.setLayout(gridButtons);
		navbutton_panel.add(navbuttondown_panel, BorderLayout.CENTER);
		

		navright_panel = new JPanel();
		navright_panel.setBackground(Color.WHITE);
		navright_panel.setPreferredSize(new Dimension(363,720));
		navright_panel.setOpaque(false);
		nav_panel.add(navright_panel, BorderLayout.EAST);

		//Select a Base Button
		ImageIcon selectIcon = new ImageIcon("buttons/selectBase.png"); 
		sel_base = new JButton();
		sel_base.setIcon(selectIcon);

		//When clicked, 4 predefined bases will be shown
		sel_base.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				FieldButtons.BaseList();
				cardlayout.show(main_panel, "SelectBase");
			}
		});

		//Build a Base Button
		//When clicked, build mode for a base will be shown
		ImageIcon buildIcon = new ImageIcon("buttons/buildBase.png");
		build_base = new JButton();
		build_base.setIcon(buildIcon);

		build_base.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent s) {
				FieldButtons.CreateInitialState();
				FieldButtons.CreateFieldButton();
				FieldButtons.CreateBuildButton();
				cardlayout.show(main_panel, "BuildBase");
			}
		});

		navbuttondown_panel.add(sel_base);
		navbuttondown_panel.add(build_base);

		//Bases Button
		GridLayout grid1 = new GridLayout(2,2);
		grid1.setHgap(5);
		grid1.setVgap(5);
		FieldButtons.selBase_panel.setLayout(grid1);
		CreateBaseButton();
		
		//Back Button from Select a Base
		//When clicked, it will show the navigation
		sel_back = new JButton("Back");
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
		GridLayout fieldLayout = new GridLayout(10,10);
		FieldButtons.field_panel.setLayout(fieldLayout);
		FieldButtons.field_panel.setOpaque(false);
		base_panel.add(FieldButtons.field_panel);

		//game and player statistics area
		stats_panel = new JPanel();
		stats_panel.setBackground(Color.BLACK);
		
		player = new JLabel("PLAYER NAME: "+name);

		save_button = new JButton();
		ImageIcon saveicon = new ImageIcon("buttons/saveBase.png"); 
		save_button.setIcon(saveicon);
		save_button.setPreferredSize(new Dimension(250,94));

		logout_button = new JButton();
		ImageIcon logouticon = new ImageIcon("buttons/logout.png"); 
		logout_button.setIcon(logouticon);
		logout_button.setPreferredSize(new Dimension(250,94));
		
		stats_panel.add(player);
		stats_panel.add(save_button);
		stats_panel.add(logout_button);

		//buildings chooser area
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

		FieldButtons.selBase_panel.setBackground(Color.BLACK);

		//initialize the base, and the sprites_panel
		//.txt files containing the paths of the buildings', and the troops' sprites
		FieldButtons.ImageList("buildings_list.txt");
		FieldButtons.ImageList("troops_list.txt");
		
		save_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = 0, b = 0;
				String base = "";

				FieldButtons.buttonsFlag = 1;  //newly added

				for(int d = 0; d < 5; d++){ //newly added
					FieldButtons.buildButton[d].setEnabled(false);// newly added
				} //newly added
		
				for(a=0; a<FieldButtons.fieldStates.length; a++){
					for(b=0; b<FieldButtons.fieldStates[0].length; b++){
					base += Integer.toString(FieldButtons.fieldStates[a][b]) + "#";
			
					}
				}
				//System.out.println(base);
			
				client(name + "/x/" + base,address,port);
			}
		});
		
	
	}
	
	private void client(String name, String address, int port){
		this.dispose();
		new ClientWindow(name,address,port);
	}

	public void CreateBaseButton(){

		FieldButtons.BaseList();

		int i = 0, j = 0, k = 0;
		
		for(i = 0; i < 2; i++){
			for(j = 0; j < 2; j++){
				FieldButtons.baseButton[i][j] = new JButton(FieldButtons.bases.get(k));
				FieldButtons.baseButton[i][j].setBackground(Color.BLACK);
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

	public void actionPerformed(ActionEvent evt){
		try{

		}
		catch(Exception e){

		}
	}
}
