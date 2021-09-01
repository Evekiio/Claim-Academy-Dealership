package com.utilities;

import java.util.Comparator;

import com.objects.Vehicle;

public class Sort implements Comparator <Vehicle>
{
	@Override
	public int compare(Vehicle v1, Vehicle v2)
	{
		return v1.getManufacturer().compareTo(v2.getManufacturer());
	}
}
