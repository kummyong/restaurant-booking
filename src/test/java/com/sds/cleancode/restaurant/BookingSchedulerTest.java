package com.sds.cleancode.restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class BookingSchedulerTest {

	private static final int CAPACITY_PER_HOUR = 3;
	private static final int NUMBER_OF_PEOPLE = 1;
	private static final Customer CUSTOMER = new Customer("", "");
	
	private BookingScheduler bookingScheduler;

	@Before
	public void setup() {
		bookingScheduler = new BookingScheduler(CAPACITY_PER_HOUR);
	}

	@Test
	public void isInstanceIsNotNullWhenBookingSchedulerWasCreated() {
		// when

		// then
		assertThat(bookingScheduler, is(notNullValue()));
	}

	@Test
	public void throwExceptionWhenBookingTimeIsNotOnTheHour() {
		// given
		DateTime dateTime = new DateTime(2019, 2, 13, 9, 10);
		
		
		Schedule schedule = new Schedule(dateTime, NUMBER_OF_PEOPLE, CUSTOMER);

		try {
			// when
			bookingScheduler.addSchedule(schedule);
			fail();
		} catch (RuntimeException e) {
			// then
			assertThat(e.getMessage(), is("Booking should be on the hour."));
		}
	}

	@Test
	public void scheduleIsAddedWhenBookingTimeIsOnTheHour() {
		// given
		DateTime dateTime = new DateTime(2019, 2, 13, 9, 0);
		Schedule schedule = new Schedule(dateTime, NUMBER_OF_PEOPLE, CUSTOMER);

		// when
		bookingScheduler.addSchedule(schedule);

		// then
		assertThat(bookingScheduler.hasSchedule(schedule), is(true));
	}
}
