package edu.chnu.recruiting.utils;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateUtils {
	public static String format(TemporalAccessor dateTime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(dateTime);
	}
}
