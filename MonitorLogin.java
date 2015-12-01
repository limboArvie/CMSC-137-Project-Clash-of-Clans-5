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


public class MonitorLogin extends JFrame {

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
					MonitorLogin frame = new MonitorLogin();
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
	public MonitorLogin() {
		super("Monitor Login");
		
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
		

		nameField = new JTextField("Mark");
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
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login(nameField.getText(),addressField.getText(),Integer.parseInt(portField.getText()));
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		loginButton.setBounds(98, 287, 89, 23);
		contentPane.add(loginButton);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblName.setBounds(120, 51, 45, 14);
		contentPane.add(lblName);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddress.setBounds(116, 127, 52, 14);
		contentPane.add(lblAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPort.setBounds(127, 209, 30, 14);
		contentPane.add(lblPort);

		

	}

	
	private void login(String name, String address, int port){
		this.dispose();
		new Monitor(name,address,port);
	}
}
