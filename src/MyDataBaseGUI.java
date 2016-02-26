import java.awt.EventQueue;  
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*; 
import java.util.Properties;

import javax.swing.*;
import net.proteanit.sql.DbUtils; // useful and very important to get the infomation to populate

public class MyDataBaseGUI {

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
					MyDataBaseGUI window = new MyDataBaseGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public MyDataBaseGUI() throws SQLException {
		initialize();
	}

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
		conn = this.getConnection();
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadDatabase = new JButton("LOAD DATABASE");
		btnLoadDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					String query = "select * from STUDENTS1";
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
