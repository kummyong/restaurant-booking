package com.sds.cleancode.restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BookingSchedulerTest {

	@Test(expected = RuntimeException.class)
	public void Step1_예약은_정시에만_가능하다_정시가_아닌경우_예외발생() {
		
		// arrange
		DateTime notOnTheHour= new DateTime(2017, 2, 6, 17, 5);
		Customer customer= new Customer("Fake name", "010-1234-4727");
		Schedule schedule= new Schedule(notOnTheHour, 1, customer);
		BookingScheduler bookingScheduler= new BookingScheduler(3);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		// expected runtime exception
	}
	
	@Test
	public void Step2_예약은_정시에만_가능하다_정시인_경우_스케줄_추가_성공() {
		
		// arrange
		DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
		DateTime onTheHour= dateTimeFormatter.parseDateTime("2017/06/19 17:00");
		Customer customer= new Customer("Fake name", "010-1234-4727");
		Schedule schedule= new Schedule(onTheHour, 1, customer);
		BookingScheduler bookingScheduler= new BookingScheduler(3);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		assertThat(bookingScheduler.hasSchedule(schedule), is(true));
	}
}
