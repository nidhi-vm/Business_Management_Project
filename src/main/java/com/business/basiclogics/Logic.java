package com.business.basiclogics;

public class Logic
{
	private Logic() {
		// Private constructor to prevent instantiation
	}

	public static double countTotal(double price,int quantity)
	{
		return price * quantity;
	}
}