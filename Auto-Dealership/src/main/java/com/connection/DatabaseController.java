package com.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.objects.Vehicle;

public class DatabaseController
{
	/************************************************
	DATABASE QUERIES & DATABASE MANIPULATION METHODS
	*************************************************/
	
	// Vehicle list to send back to main controller containing results from database query.
	ArrayList<Vehicle> results = new ArrayList<>();
	
	
	// Connect to Database (Verify & Validate Connection)
	private static Connection connect()
	{
		Connection conn = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
	
		try
		{
			conn = DriverManager.getConnection(dataFileLocation);	
		} 
		
		catch (SQLException e)
		{
			System.out.println("Connection Failed: " + e.getMessage());
		}
		
		return conn;
	}
	
	// Get the sqlite connection from DatabaseController class and return it to the calling class.
	public static Connection getConnection()
	{
		Connection SQLiteConnection = connect(); 
		return SQLiteConnection;
	}
	
	public ArrayList<Vehicle> dbQuery(String sqliteQueryString)
	{
		try 
		{
			// Get SQLite connection from DatabaseController class
			Connection conn = DatabaseController.getConnection();
			
			// Group-up SQL queries
			conn.setAutoCommit(false);
			
			// Create a new SQLite query statement
			Statement statement = conn.createStatement();
			
			// Database condition : WHERE Condition = 0 (NEW) 
			// Get results from database and assign them to a resultset.
			ResultSet rs = statement.executeQuery(sqliteQueryString);
			
			// Clear inventory before repopulating with database information. (Prevents Duplicate Data within List) 
			results.clear();
			
			// Get todays Julian Date in YYDDD format...
			String currentJulianDate = LocalDate.now().getYear() + "" + LocalDate.now().getDayOfYear();
			
			// Declaration for image/file generation (Image of Vehicle)
			String fileName = null;
			String directory = System.getProperty("user.dir");
			
			
			// Instantiate a vehicle object with results from database.
			while (rs.next())
			{
				
				// Instantiate a Vehicle Object
				Vehicle vehicle = new Vehicle(
						rs.getInt("Identifier"),
						rs.getInt("Year"),
						rs.getString("Manufacturer"),
						rs.getString("Type"),
						rs.getString("Trim"),
						rs.getInt("Mileage"),
						rs.getString("Exterior"),
						rs.getString("Interior"),
						rs.getString("Drivetrain"),
						rs.getInt("Price"),
						rs.getString("Details"),
						rs.getString("Acquired"),
						rs.getInt("AuctionExpDate"),
						rs.getInt("ImgCount"),
						rs.getInt("Condition")
						);
				
				vehicle.setAuctionDate(Integer.parseInt(currentJulianDate) - Integer.parseInt(vehicle.getAcquired()));
				
				// Read ByteStream from BLOB in database
				InputStream imageInput = rs.getBinaryStream("Image");
				
				// Name the file based of the Primary Key in Database
				fileName = String.valueOf(vehicle.getId());
				
				
				String imageLocation = directory + "\\src\\main\\resources\\static\\images\\" + fileName + ".png";
				String imageLocationRef = "\\images\\" + fileName + ".png";
				
				// Create New Image if it is missing...
				File file = new File(imageLocation);
				
				
				FileOutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				
				while(imageInput.read(buffer) > 0)
				{
					outputStream.write(buffer);
				}
				
				vehicle.setPhotoLocation(imageLocationRef);
				
				// Add the vehicle to the inventory (List of Vehicles)
				results.add(vehicle);
				outputStream.close();		
			}
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		return results;
	}
}
