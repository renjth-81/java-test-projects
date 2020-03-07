package com.ren.streams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCompare {
	private static String inputDateStr = "05-05-2020 00:00:00 AM";
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance(); 
		Date startDate = sdf.parse("03/05/2020");
		System.out.println(startDate);
		c.setTime(startDate);
		
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 11);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.AM_PM, Calendar.PM);
		Date endDate = c.getTime();
		System.out.println(endDate);
		
		
		
		Date inputDate = sdf1.parse(inputDateStr);
		
		System.out.println(inputDate.getTime() >= startDate.getTime() && inputDate.getTime() <= endDate.getTime() );
		// another way to compare dates
		System.out.println(!startDate.after(inputDate) && !endDate.before(inputDate));
	}
	
	

}
