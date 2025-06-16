package com.business.basiclogics;

public class Logic
{
	private Logic() {
		// Private constructor to hide the implicit public one
	}

	public static double countTotal(double price,int quantity)
	{
		return price * quantity;
	}
}