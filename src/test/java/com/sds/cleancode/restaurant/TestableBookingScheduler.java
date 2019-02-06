package com.sds.cleancode.restaurant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TestableBookingScheduler extends BookingScheduler {
	private String dateTime;
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
	
	public TestableBookingScheduler(int capacityPerHour, String dateTime) {
		super(capacityPerHour);
		this.dateTime= dateTime;
	}
	
	@Override
	public DateTime getNow() {
		return DATE_TIME_FORMATTER.parseDateTime(dateTime);
	}
}