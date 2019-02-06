package com.sds.cleancode.restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

public class BookingSchedulerTest {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
	private static final DateTime ON_THE_HOUR = DATE_TIME_FORMATTER.parseDateTime("2017/06/19 17:00");
	private static final DateTime NOT_ONT_THE_HOUR = new DateTime(2017, 2, 6, 17, 5);
	private static final Customer CUSTOMER = new Customer("Fake name", "010-1234-4727");
	private static final int MAX_CAPACITY = 3;
	private static final int UNDER_CAPACITY = 1;
	private BookingScheduler bookingScheduler = new BookingScheduler(MAX_CAPACITY);
	private List<Schedule> schedules = new ArrayList<Schedule>();

	
	@Before
	public void setUp() {
		bookingScheduler.setSchedules(schedules);
	}
	
	@Test(expected = RuntimeException.class)
	public void Step1_예약은_정시에만_가능하다_정시가_아닌경우_예외발생() {

		// arrange
		Schedule schedule = new Schedule(NOT_ONT_THE_HOUR, UNDER_CAPACITY, CUSTOMER);

		// act
		bookingScheduler.addSchedule(schedule);

		// assert
		// expected runtime exception
	}

	@Test
	public void Step2_예약은_정시에만_가능하다_정시인_경우_스케줄_추가_성공() {

		// arrange
		Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);

		// act
		bookingScheduler.addSchedule(schedule);

		// assert
		assertThat(bookingScheduler.hasSchedule(schedule), is(true));
	}

	// Step3. 테스트 코드 리팩토링

	@Test
	public void Step4_시간대별_인원제한이_있다_같은_시간대에_Capacity_초과할_경우_예외발생() {

		// arrange
		Schedule fullSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER);
		schedules.add(fullSchedule);
		bookingScheduler.setSchedules(schedules);

		try {
			// act
			Schedule newSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER);
			bookingScheduler.addSchedule(newSchedule);
			fail();
		} catch (RuntimeException e) {
			// assert
			assertThat(e.getMessage(), is("Number of people is over restaurant capacity per hour"));
		}
	}

	@Test
	public void Step5_시간대별_인원제한이_있다_시간대가_다르면_Capacity_차있어도_스케줄_추가_성공() {

		// arrange
		Schedule fullSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER);
		schedules.add(fullSchedule);
		bookingScheduler.setSchedules(schedules);

		// act
		DateTime anotherHour= ON_THE_HOUR.plus(1);
		Schedule newSchedule = new Schedule(anotherHour, MAX_CAPACITY, CUSTOMER);
		bookingScheduler.addSchedule(newSchedule);

		// assert
		assertThat(bookingScheduler.hasSchedule(newSchedule), is(true));
	}
	
	// Step6. 테스트 코드 리팩토링
	
	@Test
	public void Step7_예약완료시_SMS는_무조건_발송() {
		
		// arrange
		TestableSmsSender testableSmsSender= new TestableSmsSender(); 
		
		Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER);
		BookingScheduler bookingScheduler= new BookingScheduler(MAX_CAPACITY); 
		bookingScheduler.setSmsSender(testableSmsSender);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		assertThat(testableSmsSender.isSendMethodCalled(), is(true));
	}
}