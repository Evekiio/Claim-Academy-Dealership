package com.autodealer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DataController extends HttpServlet
{	
	private static final long serialVersionUID = 1432838501783080330L;
	ArrayList<Vehicle> inventory = new ArrayList<>();
	
	/************************************************
	DATABASE QUERIES & DATABASE MANIPULATION METHODS
	*************************************************/
	// Connect to Database (Verify & Validate Connection)
	private Connection connect()
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

	// Entry point for user.
	@GetMapping("/index")
	public static void getIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		resp.sendRedirect("/");
	}
	
	// Retrieve all vehicles (rows) from database that have condition: (Condition = 0) :: NEW VEHICLES
	// SELECT * FROM Inventory WHERE Condition = 0;
	@GetMapping("/new")
	public Model getNewInventory(Model model)
	{
		Connection conn = null;
		Statement statement = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(dataFileLocation);
			conn.setAutoCommit(false);
			
			statement = conn.createStatement();
			
			// Database condition : WHERE Condition = 0 (NEW)
			ResultSet rs = statement.executeQuery("SELECT * FROM Inventory WHERE ImgCount = 1;");
			
			// Clear inventory before repopulating with database information. (Prevents Duplicate Data within List) 
			inventory.clear();
			
			String fileName = null;
			String directory = System.getProperty("user.dir");
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
						rs.getInt("ImgCount"),
						rs.getInt("Condition")
						);
				
				// READ BYTE FROM BLOB IN DATABASE
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
				inventory.add(vehicle);
				outputStream.close();
				
				
				
				
			}
			
			conn.close();
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		model.addAttribute("inventory", inventory);
		return model;
	}
	
	// Retrieve all vehicles (rows) from database that have condition: (Condition >= 1) :: USED VEHICLES
	// SELECT * FROM Inventory WHERE Condition >= 1;
	@GetMapping("/used")
	public Model getUsedInventory(Model model)
	{
		Connection conn = null;
		Statement statement = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(dataFileLocation);
			conn.setAutoCommit(false);
			
			statement = conn.createStatement();
			
			// Database condition : WHERE Condition = 1 (USED)
			ResultSet rs = statement.executeQuery("SELECT * FROM Inventory WHERE Condition = 1;");
			
			// Clear inventory before repopulating with database information. (Prevents Duplicate Data within List) 
			inventory.clear();
			
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
						rs.getInt("ImgCount"),
						rs.getInt("Condition")
						);
				
				// Add the vehicle to the inventory (List of Vehicles)
				inventory.add(vehicle);
			}
			
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		model.addAttribute("inventory", inventory);
		return model;
	}
	
	// Retrieve all vehicles (rows) from database (NO CONDITION -- SELECT * FROM Inventory)
	// SELECT * FROM Inventory
	public static void getAllInventory(String searchData) 
	{
		
		Connection conn = null;
		Statement statement = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(dataFileLocation);
			conn.setAutoCommit(false);
			System.out.println("DATABASE CONNECTION: SUCCESS");
			
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Inventory;");
			
			while (rs.next())
			{
				int id = rs.getInt("Identifier");
				int year = rs.getInt("Year");
				String manufacturer = rs.getString("Manufacturer");
				String model = rs.getString("Model");
				String trim = rs.getString("Trim");
				int mileage = rs.getInt("Mileage");
				String exterior = rs.getString("Exterior");
				String interior = rs.getString("Interior");
				String drivetrain = rs.getString("Drivetrain");
				int price = rs.getInt("Price");
				String details = rs.getString("Details");
				String acquired = rs.getString("Acquired");
				int imageCount = rs.getInt("ImgCount");
				int condition = rs.getInt("Condition");
				
				
				
				System.out.println("Identifier " + id + ":" + " " + year + " " + manufacturer + " " + model + " " + trim);
				System.out.println
				(
					"Condition: " + condition + "\n" +	
					"Exterior: " + exterior + "\n" +
					"Interior: " + interior + "\n" +
					"Drivetrain: " + drivetrain + "\n" +
					"Price: " + price + "\n" +
					"Notes: " + details + "\n" +
					"Date Acquired: " + acquired + "\n"
					
				);
				
				
			}
		
			
		}
		catch (Exception e)
		{
			System.out.println("This sucks! " + e.getMessage());
		}
		
	}
	
	// Retrieve all vehicles (rows) from database that have condition: Meeting User Input Criteria
	// SELECT * FROM Inventory WHERE Model = "User Input Criteria"
	@GetMapping("/search")
	public void searchInventory(HttpServletRequest request, HttpServletResponse response)
	{
		String searchData = request.getParameter("searchdata");
		getAllInventory(searchData);
	}

	// Retrieves Data depending on User Selections
	@GetMapping("/reports")
	public Model returnTest(Model model)
	{
		Connection conn = null;
		Statement statement = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
		
		try 
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(dataFileLocation);
			conn.setAutoCommit(false);
			
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Inventory;");
			
			// Clear inventory before repopulating with database information. (Prevents Duplicate Data within List) 
			inventory.clear();
			
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
						rs.getInt("ImgCount"),
						rs.getInt("Condition")
						);
				
				// Add the vehicle to the inventory (List of Vehicles)
				inventory.add(vehicle);
			}
			
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		model.addAttribute("inventory", inventory);
		return model;
	}
	
	// Uploads Image to Database
	@GetMapping("/add-image")
	public void testAddImage()
		{
			int id = 10;
			String imageLocation = "C:\\Users\\sande\\Desktop\\capture.PNG";
			String updateQuery = "UPDATE Inventory SET Image = ?, ImgCount = 1 WHERE Identifier = ?";
			
			try(Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(updateQuery))
			{
				statement.setBytes(1, readImage(imageLocation));
				statement.setInt(2, id);
				
				statement.executeUpdate();
				System.out.println("Image Stored...");
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}

	// Read Image to Bytes
	private byte[] readImage(String file)
	{
		ByteArrayOutputStream outputStream = null;
		
		try
		{
			File imageFile = new File(file);
			FileInputStream inputStream = new FileInputStream(imageFile);
			byte[] buffer = new byte[1024];
			outputStream = new ByteArrayOutputStream();
			
			for (int len; (len = inputStream.read(buffer)) != -1;)
			{
				outputStream.write(buffer, 0, len);
			}
		}
		catch(FileNotFoundException e1)
		{
			System.err.println(e1.getMessage());
		}
		catch(IOException e2)
		{
			System.err.println(e2.getMessage());
		}
		
		return outputStream != null ? outputStream.toByteArray() : null;
	}

	
}
