import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import java.util.Random;


public class Server implements Runnable, ActionListener{
	public Timer fishGenerator = new Timer(3000, this);

	public ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();
	//private ArrayList<Integer> clientResponse = new ArrayList<Integer>();

	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, manage, send, receive;
	private final int MAX_ATTEMPTS = 5;

	private boolean raw = false;

	public int playerCount = 0;
	public int packetCount = 0;
	Random rand;
	public Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
		fishGenerator.start();
	}

	public void run() {
		running = true;
		System.out.println("Server started on port " + port);
		manageClients();
		receive();
		Scanner scanner = new Scanner(System.in);
		while (running) {
			String text = scanner.nextLine();
			if (!text.startsWith("/")) {
				sendToAll("/m/Server: " + text + "/e/");
				continue;
			}
			text = text.substring(1);
		}
		scanner.close();
	}

	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendToAll("/i/server");
					sendStatus();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		manage.start();
	}

	private void sendStatus() {
		if (clients.size() <= 0) return;
		String users = "/u/";
		for (int i = 0; i < clients.size() - 1; i++) {
			users += clients.get(i).name + "/n/";
		}
		users += clients.get(clients.size() - 1).name + "/e/";
		sendToAll(users);
	}

	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (SocketException e) {
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	private void sendToAll(String message) {
		if (message.startsWith("/m/")) {
			String text = message.substring(3);
			text = text.split("/e/")[0];
		}
		for (int i = 0; i < clients.size(); i++) {
			ClientInfo client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}

	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}

	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		if (raw) System.out.println(string);
		if (string.startsWith("/c/")) {
			// UUID id = UUID.randomUUID();

			

			int id = new SecureRandom().nextInt();
			if(id < 0) id = id * -1;
			String str = string.split("/c/|/e/")[1];
			String name = str.split("/x/")[0];
			String base = "null";
			if(str.split("/x/").length > 1){
				base = str.split("/x/")[1];
				playerCount++;
				System.out.println(name + "(" + id + ") connected!");
				System.out.println("No. of connected: "+playerCount);
			}
			clients.add(new ClientInfo(name, base,  packet.getAddress(), packet.getPort(), id));
			
			String ID = "/c/" + id;
			String notifConn = "/n/" + name + "/x/" + id + "/e/";
			
			if(base != "null") sendToAll(notifConn);
			send(ID, packet.getAddress(), packet.getPort());
			send("/place/" + playerCount, packet.getAddress(), packet.getPort());
		} else if (string.startsWith("/m/")) {
			sendToAll(string);
		}else if(string.startsWith("/d/")){
			System.out.println("A client has disconnected!");
		}else if (string.startsWith("/a/")){
			
			
    	
    		String tmp2 = string.split("/a/|/e/")[1];
    		String att = tmp2.split("/x/")[1];
    		String attName = tmp2.split("/x/")[0];
    		String def;
    		ClientInfo client;
    		int x = -1;
    	
    		Random ran = new Random();
    		
			if(playerCount>1)x = ran.nextInt(clients.size());
    		
    		if(x != -1){
		 		do{
		 		client = clients.get(x);
		 		def=Integer.toString(client.getID());
		 		x = ran.nextInt(clients.size());
		 		}while(Integer.parseInt(att) == client.getID()|| client.base == "null");
		 				 		
		 		String test = "/a/" + attName + "/x/" + att + "/x/" + client.name + "/x/" +def+ "/x/" + client.base + "/e/";
		 		sendToAll(test);
    		}else{
    			String test = "/a/" + "fail" + "/x/" + "0" + "/x/" + "fail" + "/x/" +"0"+ "/x/" + "0" + "/e/";
		 		sendToAll(test);
    		
    		}
			//send(test, packet.getAddress(), packet.getPort());
			
		}else if (string.startsWith("/terminate/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/troop/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/index/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/terminateTroop/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/troopExhaust/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/result/")){
			String message = string;
			sendToAll(message);
		}else if (string.startsWith("/packet/")){
			packetCount++;
			System.out.println(packetCount + " total packets sent!");
		}
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == fishGenerator){
			//sendFishData();
		}
	}
	public ArrayList<ClientInfo> getClients()    {
    
    	return clients;
 	}
}


