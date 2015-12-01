import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {
	String name;
	InetAddress address;
	DatagramSocket socket;
	InetAddress ip;
	int port;
	Thread send,receive;
	int ID=-1;
	int place;
	
	public Client(String name, String address, int port){
		this.name=name;
		try{
		this.address=InetAddress.getByName(address);
		}catch(IOException e){};
		this.port=port;
	}
	
	public boolean conn(String address){
		try{
			socket = new DatagramSocket();
			ip= InetAddress.getByName(address);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public void send(final byte[] data){
		send=new Thread("send"){
			public void run(){
				DatagramPacket packet=new DatagramPacket(data, data.length,ip,port);	
				try{
					socket.send(packet);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public String receive(){
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data,data.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = new String(packet.getData());
		return message;
	}
	
	public String getName(){
		return name;
	}
	
	public InetAddress getAddress(){
		return address;
	}
	
	public int getPort(){
		return port;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public void disconnect() {
		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();
	}
}
