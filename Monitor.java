import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import javax.swing.Timer;
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
import java.util.ArrayList;


public class Monitor extends JFrame implements Runnable,WindowListener{

	private JPanel contentPane;
	private JPanel mainPane;
	private JPanel gamePane;
	private JTextField chatField;
	//public ImagePanel gamePane;
	JTextArea chatHistory;
	JButton sendButton;
	private DefaultCaret caret;
	private boolean running=false;
	private Client client;
	Thread listen,run;
	int place;
	ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
	int clientID;
	String name, address;
	int port;
	
	public Monitor(String name,String address,int port) {
		super("Monitor");
		
		client = new Client(name,address,port);
		boolean connected = client.conn(address);
	
		if(!connected){
			System.err.println("Connection failed!");
			console("Connection failed");
		}
		
		this.name = name;
		this.address= address;
		this.port=port;
		
		
		setResizable(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 358, 381);
		mainPane = new JPanel();
		contentPane = new JPanel();
		gamePane = new JPanel();
		
		
		mainPane.setSize(1200, 720);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		gamePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		GridLayout grid = new GridLayout(1,1);
		mainPane.setLayout(grid);
		
		//Game g = new Game(name, address, port);

		//g.setSize(1024,768);
		//g.setSize(1090,720);
		//g.setVisible(true);
		//g.setResizable(false);

	
		contentPane.setLayout(null);
		setContents();
		//mainPane.add(gamePane);
		mainPane.add(contentPane);
		
		//gamePane.add(g);
		this.setVisible(true);
		console("Attempting a connection to " + address + ":" + port + ", user: " + name);
		String connection = "/c/" + name + "/e/";
		client.send(connection.getBytes());
		running = true;
		run=new Thread(this,"running");
		run.start();
	}

	private void setContents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		} 
	
		chatHistory = new JTextArea();
		chatHistory.setEditable(false);
		chatHistory.setSize(324, 324);
		
		JScrollPane scroll = new JScrollPane(chatHistory);
		scroll.setBounds(10, 11,324,298); 
		contentPane.add(scroll);
		caret=(DefaultCaret)	chatHistory.getCaret();
		/*
		chatField = new JTextField();
		chatField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					send(chatField.getText());
				}
			}
		});
		chatField.setBounds(10, 316, 247, 31);
		contentPane.add(chatField);
		chatField.setColumns(10);
		chatField.requestFocusInWindow();
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(chatField.getText());
			}
		});
		sendButton.setBounds(263, 318, 71, 27);
		contentPane.add(sendButton);
		*/
	}
	
	public void send(String message){
		if(message.equals(""))return;
		message = client.getName()+": "+message;
		//console(message);
		message = "/m/"+message;
		client.send(message.getBytes());
		chatField.setText("");
		chatField.requestFocusInWindow();
	}
	
	public void sendToServer(String message){
		client.send(message.getBytes());
	}
	

	private void send(String message, boolean text) {
		if (message.equals("")) return;
		if (text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message + "/e/";
			chatField.setText("");
		}
		client.send(message.getBytes());
	}
	
	public void console(String message){
		chatHistory.append(message+"\n");
		chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
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
						console("Successfully connected to server! ID: " + client.getID());
						//clientID = client.getID();
						//new Game(name,address,port, clientID);
						
					}  else if (message.startsWith("/n/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String name = text.split("/x/")[0];
						String ID = text.split("/x/")[1];	
						console(name +"["+ID+"]"+" has connected to the server!");
					}else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
						
					}else if (message.startsWith("/u/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						String[] names = text.split("/n/");
						
						//console(Arrays.toString(names));
					}  
					else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
						
					}else if (message.startsWith("/a/")){
					
						decodeAttack(message);
					}else if (message.startsWith("/result/")){
						String text = message.split("/result/|/e/")[1];
						console(text.split("/x/")[0] + " is victorious in the war!");
						console(text.split("/x/")[1] + " is defeated in the war!");						
					}
				}
			}
		};
		listen.start();
	}
	
	public void decodeAttack(String att){
		String text = att.substring(3);
		String attacker = text.split("/x/")[0];
		String defender = text.split("/x/")[2];
		String baseDef = text.split("/x/")[4];
		console(attacker + " started a war with "+defender+"!");
	
	}

	public void windowClosing(WindowEvent e) {
		
		String disconnect = "/d/" + client.getID() + "/e/";
		send(disconnect, false);
		running = false;
		client.disconnect();
	}
	

	public void windowActivated(WindowEvent arg0) {
				
	}

	public void windowClosed(WindowEvent arg0) {
	
	}
	
	public void windowDeactivated(WindowEvent arg0) {
	
	}

	public void windowDeiconified(WindowEvent arg0) {
	
	}

	
	public void windowIconified(WindowEvent arg0) {
				
	}

	public void windowOpened(WindowEvent arg0) {
		
	}
	
	
}
