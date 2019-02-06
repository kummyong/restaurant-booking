package com.sds.cleancode.restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class BookingSchedulerTest {
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER= DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
	private static final DateTime ON_THE_HOUR= DATE_TIME_FORMATTER.parseDateTime("2017/06/19 17:00");
	private static final DateTime NOT_ONT_THE_HOUR= new DateTime(2017, 2, 6, 17, 5);
	private static final Customer CUSTOMER= new Customer("Fake name", "010-1234-4727");
	private static final int MAX_CAPACITY = 3;
	private static final int UNDER_CAPACITY = 1;
	private BookingScheduler bookingScheduler= new BookingScheduler(MAX_CAPACITY);
	
	@Test(expected = RuntimeException.class)
	public void Step1_예약은_정시에만_가능하다_정시가_아닌경우_예외발생() {
		
		// arrange
		Schedule schedule= new Schedule(NOT_ONT_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		// expected runtime exception
	}
	
	@Test
	public void Step2_예약은_정시에만_가능하다_정시인_경우_스케줄_추가_성공() {
		
		// arrange
		Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		assertThat(bookingScheduler.hasSchedule(schedule), is(true));
	}
	
	// Step3. 테스트 코드 리팩토링
}
