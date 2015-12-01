import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;


public class LoadTesterLogin extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField portField;
	JButton loginButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoadTesterLogin frame = new LoadTesterLogin();
					 frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoadTesterLogin() {
		super("Load Tester");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(301, 370);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		nameField = new JTextField("0");
		nameField.setBounds(61, 67, 163, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		addressField = new JTextField("localhost");
		addressField.setColumns(10);
		addressField.setBounds(61, 143, 163, 20);
		contentPane.add(addressField);
		
		portField = new JTextField("7777");
		portField.setColumns(10);
		portField.setBounds(61, 225, 163, 20);
		contentPane.add(portField);
		
		
		/**
		 * succ contains the notifier of the number and success of the connections
		 */
		final JLabel succ = new JLabel();
		
		loginButton = new JButton("Load");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/**
				 * nameField contains the number of connections/ can be infinite by typing "forever"
				 * addressField contains the IP
				 * portField contains the port
				 * clicking the Load button will trigger the login function below
				 */
				succ.setText("");
				//trigger to login function
				int i = login(nameField.getText(),addressField.getText(),Integer.parseInt(portField.getText()));
				
				if(i == -1)succ.setText("Invalid no. of connections!");
				else succ.setText(Integer.toString(i) + " connections are connected!");
				succ.setFont(new Font("Tahoma", Font.PLAIN, 13));
				succ.setBounds(40, 21, 250, 14);
				succ.setForeground(Color.RED);
				contentPane.add(succ);
				repaint();
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		loginButton.setBounds(98, 287, 89, 23);
		contentPane.add(loginButton);
		
		JLabel lblName = new JLabel("No. of Packets");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(61, 51, 163, 14);
		contentPane.add(lblName);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddress.setBounds(61, 127, 163, 14);
		contentPane.add(lblAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPort.setBounds(61, 209, 163, 14);
		contentPane.add(lblPort);
		
		

		

	}

	
	private int login(String noConn, String address, int port){
		//if the connection is "forever", it will result in an infinite loop
		//if it is an integer, it will loop to the same input number
		//else it will return -1 that mean invalid
		int i = 0;
		
		if(noConn.equals("infinite")){
			while(true){
				new LoadTester("test",address,port);
			}
		}else if(isInteger(noConn)){
			int tmp = Integer.parseInt(noConn);
			while(tmp > i){
				new LoadTester("test",address,port);
				i++;
			}
			return i;
		}else{
			return -1;
		
		}
	}
	
	public static boolean isInteger(String s) {
		try { 
		    Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
		    return false; 
		} catch(NullPointerException e) {
		    return false;
		}
		// only got here if we didn't return false
		return true;
	}
}
