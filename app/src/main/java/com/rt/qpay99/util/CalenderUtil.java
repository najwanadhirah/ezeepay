package com.rt.qpay99.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static Calendar cal = Calendar.getInstance();

	public static String getStringAddDate(Date dt, String format, int addDay) {
		sdf = new SimpleDateFormat(format);
		cal.setTime(dt);
		cal.add(Calendar.DATE, addDay); // number of days to add
		return sdf.format(cal.getTime());

	}

	public static Date addDays(Date date, int days) {
		cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

}
