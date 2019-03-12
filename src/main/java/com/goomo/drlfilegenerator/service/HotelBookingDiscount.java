package com.goomo.drlfilegenerator.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HotelBookingDiscount {
	public static void main(String[] args) {
		DrlLogic booking = new DrlLogic();
		String drlString = booking.generate();

		String drlFilePath = "C:\\Learning\\" + "hotelsDiscount.drl";
		File file = new File(drlFilePath);
		String path = file.getAbsolutePath();
		PrintWriter out;
		try {
			out = new PrintWriter(path);
			out.println(drlString);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("DRL file for 20% discount is successfully generated at: " + path);
	}
}
