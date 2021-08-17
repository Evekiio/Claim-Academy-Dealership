package com.autodealer;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DAO extends HttpServlet
{
	@GetMapping("/new")
	public void newInventory(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("text/html");
		
		try
		{
			PrintWriter pw = response.getWriter();
			String title = "This is a servlet!";
			
			
			
			
			pw.println
			(
					  "<!DOCTYPE>\n"
					+ "<html>\n"
					+ "<head><title>" + title + "</title></head>"
					+ "<body>" + request.getParameter("username") + "</body>"
					+ "</html>"
			);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@GetMapping("/search")
	public void searchInventory(HttpServletRequest request, HttpServletResponse response)
	{
		
		String searchData = request.getParameter("searchdata");
		
		try
		{
			Class.forName("org.sqlite.JDBC");
		} 
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		searchQuery(searchData);
	}
	
	public static void connectToDatabase()
	{
		Connection conn = null;
		String dataFileLocation = "jdbc:sqlite:C:\\Users\\sande\\Desktop\\Claim-Academy-Dealership\\Auto-Dealership\\src\\main\\java\\com\\connection\\database.db";
	
		try
		{
			conn = DriverManager.getConnection(dataFileLocation);
			
			System.out.println("Connection to SQLite Database has been established.");
			
		} 
		
		catch (SQLException e)
		{
			System.out.println("Connection Failed: " + e.getMessage());
		}
		
		finally
		{
			try
			{
				if (conn != null)
				{
					conn.close();
				}
			}
			
			catch (SQLException e)
			{
				System.out.println("Connection Failed: " + e.getMessage());
			}
		}
	}

	public static void searchQuery(String searchData) 
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
				int images = rs.getInt("Images");
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
	
	@GetMapping("/old")
	public String oldInventory()
	{
		return "This is old stuff...";
	}
}
