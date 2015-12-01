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


public class LoadTester implements Runnable{

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
	/*
	* This is the same as client but an automated version which we are creating clients based on the number input by the user
	*/
	public LoadTester(String name,String address,int port) {
		
		client = new Client(name,address,port);
		boolean connected = client.conn(address);
	
		if(!connected){
			System.err.println("Connection failed!");
		}
		
		String connection = "/packet/" + name + "/e/";
		client.send(connection.getBytes());
		running = true;
		run=new Thread(this,"running");
		run.start();
	}

	
	public void send(String message){
		if(message.equals(""))return;
		message = client.getName()+": "+message;
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
	
	public void run() {
	}

}
