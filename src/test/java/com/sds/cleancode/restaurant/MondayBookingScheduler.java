package com.sds.cleancode.restaurant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class MondayBookingScheduler extends BookingScheduler {

	public MondayBookingScheduler(int capacityPerHour) {
		super(capacityPerHour);
	}
	
	@Override
	public DateTime getNow() {
		return DateTimeFormat.forPattern("YYYY/MM/dd HH:mm").parseDateTime("2019/02/04 17:00");
	}
}