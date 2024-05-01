package br.com.marcelbritto.kumuluscad.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
	public static Integer getAge(Date birthDate) {
		GregorianCalendar today = new GregorianCalendar();
		GregorianCalendar calendar = new GregorianCalendar();
		if(birthDate != null){
			calendar.setTime(birthDate);
		}		
		int yearToday = today.get(Calendar.YEAR);
		int year = calendar.get(Calendar.YEAR);
		return Integer.valueOf(yearToday-year);
	}

}
