/**
 * @author Michael Avnyin
 * THIS PROGRAM CREATES CONNECTION TO DATABASE 
 * CREATES A DATABASE
 * CREATES A TABLE IN THE DATABASE
 * POPULATES THE TABLE THROUGH TEXT FILE
 * DOES BASIC QUERYING,UPDATING,INSERTING,AND DELETING OF DATABASE FUNCTIONS
 * OUTPUTS TO TEXT FILE A LOG OF TRANSCACTIONS i.e queries etc...
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
 * 
 */
import java.sql.*;     
import java.util.*;
import java.util.Date;
import java.io.*;
import javax.swing.*;

public class MyDataBase{
	
	private final String userName = "root";
	private final String password = "root";
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "test";
	private final String tableName = "STUDENTS1";
	File inputFile;// for input file 
	File outputFile; // for output file 
	FileWriter fWriter; // to send/write to output file 
	PrintWriter pWriter; // to print to output file
	String sID;  
	String sFirst;
	String sLast;		// variables declared for the 
	String sAddress;	// the use of input file 
	String sCity;
	String sState;
	String sZip;

	/**
	 * viewTable method allows us to view entire table by calling a query to the db.
	 * @param con variable to set up connection to DB
	 * @param dbName variable with the name of our DB
	 * @throws SQLException
	 */
	public static void viewTable(Connection con, String dbName)
		    throws SQLException {

		    Statement stmt = null;
		    //our query returns the entire table
		    String query =
		        "select STUDENT_ID, FIRST_NAME, LAST_NAME, " +
		        "ADDRESS, CITY, STATE, ZIP " +
		        "from " + dbName + ".STUDENTS1";
		    
		    try {
		        stmt = con.createStatement();
		        ResultSet rs = stmt.executeQuery(query);
		        while (rs.next()) {
		            String studentID = rs.getString("STUDENT_ID");
		            String firstName = rs.getString("FIRST_NAME");
		            String lastName = rs.getString("LAST_NAME");
		            String address = rs.getString("ADDRESS");
		            String city = rs.getString("CITY");
		            String state = rs.getString("STATE");
		            String zip = rs.getString("ZIP");
		            System.out.println(studentID + "\t" + firstName +
		                               "\t" + lastName + "\t" + address +
		                               "\t" + city + "\t" + state + "\t" + zip);
		        
		        }
		    } catch (SQLException e ) {
		    	e.printStackTrace();
		    } finally {
		        if (stmt != null) { stmt.close(); }
		    }
		}

	/**
	 * createTable method creates our new table and intializes our columns 
	 * @throws SQLException
	 */
	public void createTable() throws SQLException {
		
		Connection conn = null;
		conn = this.getConnection();
		// string is our query to create new table and label all columns 
		String createString = 
				"CREATE TABLE " + dbName + 
				".STUDENTS1" +
				" ( STUDENT_ID varchar(10) NOT NULL, " +
				"FIRST_NAME varchar(15) NOT NULL, " +
				"LAST_NAME varchar(20) NOT NULL, " +
				"ADDRESS varchar(30) NOT NULL, " +
				"CITY varchar(20) NOT NULL, " +
				"STATE varchar(2) NOT NULL, " +
				"ZIP varchar(5), " +
				"PRIMARY KEY (STUDENT_ID) )";
		
		
		  Statement stmt = null;
		    try {
		    	log(createString);
		        stmt = conn.createStatement(); // connects to db
		        stmt.executeUpdate(createString); // sends to db 
		        
		    } catch (SQLException | IOException e) {
		    	  e.printStackTrace();
		    } finally {
		        if (stmt != null) { stmt.close(); }
		    }
	}
	
	/**
	 * method populateTable inserts our students information from our text file 
	 * @throws SQLException
	 */
	public void populateTable() throws SQLException {
		
		Statement stmt2 = null;
		Connection conn = null;

		try{
			conn = this.getConnection(); // get connection 
			stmt2 = conn.createStatement(); //connected and creates statement 

			Scanner inFile = new Scanner(inputFile); // scanner to read input file 

			while(inFile.hasNext()){  // while file is not empty 

			String input = inFile.nextLine(); // reads in line by line 
			String[] info = input.split("/"); // split brakes up each input from file by "/" pattern and places them in separate indexes

			sID = info[0];  // places student id in index 0
			sFirst = info[1]; // places student first name in index 1
			sLast = info[2]; // etc....
			sAddress = info[3];
			sCity = info[4];
			sState = info[5];
			sZip = info[6];

			// execute update inserts students information per row / each variable is 
			stmt2.executeUpdate(  
					"insert into " + dbName +
					".STUDENTS1 " + 
					"values('"+sID+"', '"+sFirst+"', '"+sLast+"', '"+sAddress+"', '"+sCity+"', '"+sState+"', '"+sZip+"') ");
			log("insert into  + dbName +.STUDENTS1  + values('"+sID+"', '"+sFirst+"', '"+sLast+"', '"+sAddress+"', '"+sCity+"', '"+sState+"', '"+sZip+"') ");
			}
			inFile.close(); // close file 
		}
		catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldnt add to DB");
	    } catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if (stmt2 != null) { stmt2.close(); } // close statement 
	    }
		System.out.println();
	}


	/**
	 * run method connects to db sets up our table and then calls createtable and populates it
	 * @throws SQLException
	 */
	public void run() throws SQLException{
		
		Connection conn = null;
		try {
			conn = this.getConnection(); // establishes connection 
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

		 try {
		     Statement stmt = conn.createStatement(); 
		     String newDatabaseString =
		          "CREATE DATABASE IF NOT EXISTS " + dbName;
		     log(newDatabaseString);
		     stmt.executeUpdate(newDatabaseString);
		    
		     System.out.println("Created database " + dbName);
		      } catch (SQLException | IOException e) {
		    	  e.printStackTrace();
		      }
		
		createTable(); // run calls create table method
 
		populateTable(); // run calls populate table method 
		
		System.out.println("THIS IS OUR INITIAL VIEW OF OUR DATABASE AFTER IT IS POPULATED");
		viewTable(conn,dbName); // run calls viewTable to see whats in our table 
		System.out.println();
	}
	
	/**
	 * setFileForArg sets inputFile equal to file our args[0]
	 * @param file
	 */
	public void setFileForArg(File file, File file2){
		inputFile = file; // equals our args[0]
		outputFile = file2;
	}
	
	public void log(String s) throws IOException{

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
	    Date date = new Date();
	    Timestamp ptime = new Timestamp(date.getTime());
	    writer.append(ptime + ": " + s);
	    writer.append(System.getProperty("line.separator"));
	    writer.close();
	
	}

	/**
	 * This method will insert a new row into the database (variable methods are initialized in main)
	 * @param studentID new student ID 
	 * @param firstName new student name
	 * @param lastName new student last name
	 * @param address new address
	 * @param city new city 
	 * @param state new state
	 * @param zip new zip
	 * @throws SQLException throws SQL if any invalid statements
	 */
	public void insertRow(String studentID, String firstName, String lastName, 
			String address, String city, String state, String zip) throws SQLException {

		Statement stmt = null;
		Connection conn = null;
		conn = this.getConnection();
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			//our result set is our accesses all in our table 
			
			ResultSet uprs = stmt.executeQuery(
					"SELECT * FROM " + dbName +
					".STUDENTS1");
			log("SELECT * FROM  + dbName +.STUDENTS1");
			//result set uprs updates our table by adding a new student 
			uprs.moveToInsertRow();
			uprs.updateString("STUDENT_ID", studentID); // adds new student id
			uprs.updateString("FIRST_NAME", firstName); // adds new first name
			uprs.updateString("LAST_NAME", lastName); // adds new last name
			uprs.updateString("ADDRESS", address); // adds new address
			uprs.updateString("CITY", city); // adds new city
			uprs.updateString("STATE", state); // adds new state
			uprs.updateString("ZIP", zip); // // adds new 
			uprs.insertRow(); // inserts new row into db
			uprs.beforeFirst();
			
		} catch (SQLException | IOException e ) {
			e.printStackTrace();
		} finally {
			if (stmt != null) { stmt.close(); }
		}
	}
	
	/**
	 * getConnection method establishes a connection to database to allow all the functionality 
	 * @return conn sends back the connection 
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException{

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName); // inputs our user name "root"
		connectionProps.put("password", this.password); //inputs our password "root"
		
		//essential for establishing our connection includes -> dbms , server, port number, our db name, and our username and password 
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);

		return conn; 
	}
	
	/**
	 * deleteRow method removes student from the database by its primary key (STUDENT_ID)
	 * @param con for connection
	 * @param deleteEntry variable that hold STUDENT_ID
	 * @throws SQLException
	 */
	public void deleteRow(Connection con, String deleteEntryID) throws SQLException{
		
		Statement stmt = null;
		 try {
			stmt = con.createStatement(); // establishes connection 
			String query = "DELETE FROM STUDENTS1 " +  // our query to send to db
	                   "WHERE STUDENT_ID = " + deleteEntryID;
		  log(query);
	      stmt.executeUpdate(query); // execute of query 
	      
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}finally {
	        if (stmt != null) { stmt.close(); } // close statement
	    }
	}
	
	/**
	 * modifyFullAddress method updates students full address(city,state,zip) if student moves or any changes need to be made. 
	 * @param con for connection
	 * @param studentID to locate primary key
	 * @param newAddress for new address
	 * @param newCity for new city 
	 * @param newState for new state
	 * @param newZip for new zip
	 * @throws SQLException
	 */
	public void modifyFullAddress(Connection con, String studentID, String newAddress, 
			String newCity, String newState, String newZip) throws SQLException{

		Statement stmt = null;
		 try {
			stmt = con.createStatement(); // get a connection
			
			String query = "UPDATE STUDENTS1 SET ADDRESS = '"+newAddress+"', CITY = '"+newCity+"', STATE = '"+newState+"', ZIP = '"+newZip+"'  WHERE STUDENT_ID = '"+studentID+"' ";
			stmt.executeUpdate(query); // send our query to database
			log(query);
		} catch (SQLException | IOException e) {
			e.printStackTrace(); // catch any exceptions
		}finally {
	        if (stmt != null) { stmt.close(); } // close statement 
	    }	
	}
	
	public static void main (String [] args) throws SQLException{
		
		Connection myConnection = null; //create connection variable 
		String name = "test"; // variable for our parameter viewtable()
		File file = new File(args[0]); // for our input text file that populates our table initially 
		File file2 = new File(args[1]);
		MyDataBase db = new MyDataBase(); // create new variable of type MyDataBase
		myConnection = db.getConnection(); // get a connection to DataBase
		db.setFileForArg(file,file2); // send our input file to MyDataBase class
		db.run(); // run method initializes creation of db,populate,etc.
		
		//QUERY TO DEMONSTRATE (INSERT) OF A NEW ROW 
		db.insertRow("10000013", "Danielle", "Free","55 Notion Drive","Gainsville","FL","92311");
		System.out.println("THIS IS OUR TABLE AFTER A NEW INSERT");
		db.viewTable(myConnection, name); // new view of table
		System.out.println();
		
		//QUERY TO DEMONSTRATE (UPDATE) STUDENTS ADDRESS IF THEY MOVE
		db.modifyFullAddress(myConnection, "10000011", "1101 Pleasant Drive", "Surf City", "NJ", "53422");
		System.out.println("THIS IS OUR TABLE AFTER AN UPDATE TO ADRESS");
		db.viewTable(myConnection, name); // new view of table
		System.out.println();
		
		//QUERY TO DEMONSTRATE (DELETE) POSSIBLY IF DECEASED, EXPELLED, NO LONGER WANTS TO ATTEND, OR GRADUATED
		//THIS QUERY WILL DELETE STUDENT WITH ID NUMBER = 10000003
		db.deleteRow(myConnection,"10000003");
		System.out.println("THIS IS OUR TABLE AFTER A DELETE");
		db.viewTable(myConnection, name); // new view of table
			
	}
}