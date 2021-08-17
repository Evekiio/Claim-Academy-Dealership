package com.autodealer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConn
{

	public static void connect()
	{
		Connection conn = null;
		
		try
		{
			String url = "jbdc:sqlite:/Dealership/src/main/data/database.db";
			conn = DriverManager.getConnection(url);
			
			System.out.println("CONNECTION STATUS: SUCCESS!");
		}
		catch (SQLException e)
		{
			System.out.println("CONNECTION STATUS: FAILED >> " + e.getMessage());
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
			catch (SQLException ex)
			{
				System.out.println("CONNECTION STAUS: FAILED >> " + ex.getMessage());
			}
		}
	}
}
