/**
 * @author Michael Avnyin
 * THIS IS A GUI APPLICATION OF MY DATABASE
 * 
 *  ************IMPORTANT***************
 *  
 * MyDbGui IS A SEPERATE JAVA FILE
 * IF YOU WOULD LIKE TO VIEW DATABASE IN GUI PLEASE RUN SEPERATLEY
 * ALSO IT IS IMPORTANT TO BUILD A PATH TO THE JAR FILE  LABELED "rs2xml" LOCATED IN THE FOLDER
 * ONCE YOU HAVE INCLUDED rs2xml JAR file to referenced library you can compile and the GUI WILL SHOW UP
 * CLICK "LOAD DATABASE" TO HAVE THE DB APPEAR IN JFRAME
 * 
 * ************IMPORTANT***************
 */
 

import java.awt.EventQueue; 
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*; 
import java.util.Properties;

import javax.swing.*;

import net.proteanit.sql.DbUtils; // useful and very important to get the infomation to populate

public class MyDbGui {

	private JFrame frame;
	private JTable table;
	private final String userName = "root";
	private final String password = "root";
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "test";
	private final String tableName = "STUDENTS1";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyDbGui window = new MyDbGui(); // create a new window
					window.frame.setVisible(true); // set the window visible
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application calls initialize.
	 * @throws SQLException 
	 */
	public MyDbGui() throws SQLException {
		initialize();
	}
	
	/**
	 * getConnection method establishes a connection to database to allow all the functionality 
	 * @return conn sends back the connection 
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);

		return conn;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	Connection conn = null;
	private void initialize() throws SQLException {
		
		conn = this.getConnection(); // create connection
		frame = new JFrame(); // 
		frame.setBounds(100, 100, 700, 400); // sets size of window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes when you close window
		frame.getContentPane().setLayout(null); //
		
		JButton btnLoadDatabase = new JButton("LOAD DATABASE"); // create our button
		btnLoadDatabase.addActionListener(new ActionListener() { // our button linked to action listener which will load our db
			public void actionPerformed(ActionEvent e) {
				
				try{
					String query = "select * from STUDENTS1";   // query gets all from database
					PreparedStatement pst = conn.prepareStatement(query);  
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
				} catch(Exception ee){
					ee.printStackTrace();
				}
			}
		});
		btnLoadDatabase.setBounds(272, 6, 150, 40);
		frame.getContentPane().add(btnLoadDatabase);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 58, 666, 302);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
}