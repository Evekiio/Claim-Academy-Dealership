package com.autodealer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.connection.DatabaseController;
import com.objects.Vehicle;
import com.utilities.Sort;

@Controller
@RequestMapping("/")
public class MainController extends HttpServlet
{	
	private static final long serialVersionUID = 1432838501783080330L;
	ArrayList<Vehicle> inventory = new ArrayList<>();
	DatabaseController dbConn = new DatabaseController();
	
	// ************************************************
	// INDEX (VIEW ENTRY POINT) + Entire Inventory
	// ************************************************
	@GetMapping("/")
	public String index(Model model)
	{
		// Query database for list of vehicles depending on out query string. 
		inventory = dbConn.dbQuery("SELECT * FROM Inventory");
				
		// Add the results of the query to the model container.
		model.addAttribute("inventory", inventory);
		
		return "index";
	}
	
	// Retrieve all vehicles (rows) from database that have condition: (Condition == 0) :: NEW VEHICLES
	// SELECT * FROM Inventory WHERE Condition = 0;
	@GetMapping("/new")
	public Model newVehicles(Model model)
	{
		// Query database for list of vehicles depending on out query string. 
		inventory = dbConn.dbQuery("SELECT * FROM Inventory WHERE Condition = 0;");
		
		// Add the results of the query to the model container.
		model.addAttribute("inventory", inventory);
		return model;
	}
	
	// Retrieve all vehicles (rows) from database that have condition: (Condition == 1) :: USED VEHICLES
	// SELECT * FROM Inventory WHERE Condition >= 1;
	@GetMapping("/used")
	public Model usedVehicles(Model model)
	{
		// Get all vehicles that are used (Condition == 1)
		inventory = dbConn.dbQuery("SELECT * FROM Inventory WHERE Condition = 1;");
		
		// Add the attributes to the model.
		model.addAttribute("inventory", inventory);
		return model;
	}

	// Retrieves Data depending on User Selections
	@GetMapping("/reports")
	public String reports()
	{
		return "reports";
	}
	
	// Retrieves Data depending on User Selections
	@GetMapping("/manager")
	public String manager(Model model)
	{
		inventory = dbConn.dbQuery("SELECT * FROM Inventory");
		
		Collections.sort(inventory, new Sort());
		
		model.addAttribute("inventory", inventory);
		return "manager";
	}

	
	@GetMapping("/remove")
	public void removeVehicle(@RequestParam("vehicleId") int id)
	{
		
	}
	
	// BASIC SEARCH FUNCTIONALITY
	@GetMapping("/search")
	public String search(@RequestParam("searchInput") String searchInput, Model model)
	{
		String[] formattedInput = searchInput.split("\\s+");
		ArrayList<Vehicle> outputInventory = new ArrayList<>();
		int index = 0;
		
		inventory.clear();
		inventory = dbConn.dbQuery("SELECT * FROM Inventory;");	
		
		System.out.println("FUNCTION: Search executed by user!");
		try 
		{
			while(index < formattedInput.length) 
			{
				for (Vehicle vehicle : inventory) 
				{
					if (String.valueOf(vehicle.getYear()).equalsIgnoreCase(formattedInput[index].trim()) || vehicle.getManufacturer().equalsIgnoreCase(formattedInput[index].trim()) || vehicle.getType().equalsIgnoreCase(formattedInput[index].trim()))
					{
						if (!outputInventory.contains(vehicle))
						{
							outputInventory.add(vehicle);
							
						}	
					}
				}
				
				index++;
			}
		}
		
		catch(Exception e)
		{
			System.err.println("Error: Issues while trying to filter search results...");
			e.printStackTrace();
			return "index";
		}
		
		if (outputInventory.size() > 0)
		{
			model.addAttribute("inventorySize", outputInventory.size());
			model.addAttribute("inventory", outputInventory);	
		}
		else 
		{
			model.addAttribute("inventorySize", outputInventory.size());
			model.addAttribute("resultMessage", "0 vehicles identified matching your criteria, please try again...");
		}
		
		return "results";		
	}
	
	
	// *********************
	//    TESTING METHODS
	// *********************
	
	// Uploads Image to Database
	@GetMapping("/add-image")
	public void testAddImage()
		{
			int id = 20;
			String imageLocation = "C:\\Users\\sande\\Desktop\\capture.PNG";
			String updateQuery = "UPDATE Inventory SET Image = ?, ImgCount = 1 WHERE Identifier = ?";
			
			try(Connection conn = DatabaseController.getConnection(); PreparedStatement statement = conn.prepareStatement(updateQuery))
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
