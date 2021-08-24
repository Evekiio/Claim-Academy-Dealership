package com.autodealer;

public class Vehicle
{
	private int id;
	private int year;
	private String manufacturer;
	private String type;
	private String trim;
	private int mileage;
	private String exterior;
	private String interior;
	private String drivetrain;
	private int price;
	private String details;
	private String acquired;
	private int imageCount;
	private int condition;
	private String photoLocation;
	
	public Vehicle(int id, int year, String manufacturer, String type, String trim, int mileage, String exterior, String interior, String drivetrain, int price, String details, String acquired, int imageCount, int condition)
	{
		super();
		this.id = id;
		this.year = year;
		this.manufacturer = manufacturer;
		this.type = type;
		this.trim = trim;
		this.mileage = mileage;
		this.exterior = exterior;
		this.interior = interior;
		this.drivetrain = drivetrain;
		this.price = price;
		this.details = details;
		this.acquired = acquired;
		this.imageCount = imageCount;
		this.condition = condition;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}
	
	public String getType()
	{
		return type;
	}

	public void setModel(String type)
	{
		this.type = type;
	}

	public String getTrim()
	{
		return trim;
	}

	public void setTrim(String trim)
	{
		this.trim = trim;
	}

	public int getMileage()
	{
		return mileage;
	}

	public void setMileage(int mileage)
	{
		this.mileage = mileage;
	}

	public String getExterior()
	{
		return exterior;
	}

	public void setExterior(String exterior)
	{
		this.exterior = exterior;
	}

	public String getInterior()
	{
		return interior;
	}

	public void setInterior(String interior)
	{
		this.interior = interior;
	}

	public String getDrivetrain()
	{
		return drivetrain;
	}

	public void setDrivetrain(String drivetrain)
	{
		this.drivetrain = drivetrain;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public String getAcquired()
	{
		return acquired;
	}

	public void setAcquired(String acquired)
	{
		this.acquired = acquired;
	}

	public int getImageCount()
	{
		return imageCount;
	}

	public void setImageCount(int imageCount)
	{
		this.imageCount = imageCount;
	}

	public int getCondition()
	{
		return condition;
	}

	public void setCondition(int condition)
	{
		this.condition = condition;
	}

	public String getPhotoLocation()
	{
		return photoLocation;
	}

	public void setPhotoLocation(String photoLocation)
	{
		this.photoLocation = photoLocation;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	
}
