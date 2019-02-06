package com.sds.cleancode.restaurant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BookingSchedulerTest {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm");
	private static final DateTime ON_THE_HOUR = DATE_TIME_FORMATTER.parseDateTime("2017/06/19 17:00");
	private static final DateTime NOT_ONT_THE_HOUR = new DateTime(2017, 2, 6, 17, 5);
	private static final Customer CUSTOMER_WITHOUT_EMAIL = mock(Customer.class);
	private static final Customer CUSTOMER_WITH_EMAIL= mock(Customer.class, RETURNS_MOCKS);
	private static final int MAX_CAPACITY = 3;
	private static final int UNDER_CAPACITY = 1;

	@InjectMocks
	@Spy
	private BookingScheduler bookingScheduler= new BookingScheduler(MAX_CAPACITY);
	
	@Spy
	private List<Schedule> schedules= new ArrayList<Schedule>();
	
	@Spy
	private SmsSender smsSender= new SmsSender();
	
	@Spy
	private MailSender mailSender= new MailSender();
	
	@Before
	public void setUp() {
	}
	
	@Test(expected = RuntimeException.class)
	public void Step1_예약은_정시에만_가능하다_정시가_아닌경우_예외발생() {

		// arrange
		Schedule schedule = new Schedule(NOT_ONT_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITHOUT_EMAIL);

		// act
		bookingScheduler.addSchedule(schedule);

		// assert
		// expected runtime exception
	}

	@Test
	public void Step2_예약은_정시에만_가능하다_정시인_경우_스케줄_추가_성공() {

		// arrange
		Schedule schedule = new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITHOUT_EMAIL);

		// act
		bookingScheduler.addSchedule(schedule);

		// assert
		assertThat(bookingScheduler.hasSchedule(schedule), is(true));
	}

	// Step3. 테스트 코드 리팩토링

	@Test
	public void Step4_시간대별_인원제한이_있다_같은_시간대에_Capacity_초과할_경우_예외발생() {

		// arrange
		Schedule fullSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
		schedules.add(fullSchedule);
		bookingScheduler.setSchedules(schedules);

		try {
			// act
			Schedule newSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
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
		Schedule fullSchedule = new Schedule(ON_THE_HOUR, MAX_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
		schedules.add(fullSchedule);
		bookingScheduler.setSchedules(schedules);

		// act
		DateTime anotherHour= ON_THE_HOUR.plus(1);
		Schedule newSchedule = new Schedule(anotherHour, MAX_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
		bookingScheduler.addSchedule(newSchedule);

		// assert
		assertThat(bookingScheduler.hasSchedule(newSchedule), is(true));
	}
	
	// Step6. 테스트 코드 리팩토링
	
	@Test
	public void Step7_예약완료시_SMS는_무조건_발송() {
		
		// arrange
		Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		verify(smsSender, times(1)).send(schedule);
	}
	
	@Test
	public void Step8_이메일이_없는_경우에는_이메일_미발송() {
		
		// arrange
		Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITHOUT_EMAIL);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		verify(mailSender, never()).sendMail(schedule);
	}
	
	@Test
	public void Step9_이메일이_있는_경우에만_이메일_발송() {
		
		// arrange
		Schedule schedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITH_EMAIL);
		
		// act
		bookingScheduler.addSchedule(schedule);
		
		// assert
		verify(mailSender, times(1)).sendMail(schedule);
	}
	
	// Step10. 테스트 코드 리팩토링 
	
	@Test
	public void Step11_현재날짜가_일요일인_경우_예약불가_예외처리() {
		
		// arrange
		DateTime sunday= DATE_TIME_FORMATTER.parseDateTime("2019/02/03 17:00");
		when(bookingScheduler.getNow()).thenReturn(sunday);
		
		try {
			// act
			Schedule newSchedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITH_EMAIL);
			bookingScheduler.addSchedule(newSchedule);
			fail();
		} catch (RuntimeException e) {
			
			// assert
			assertThat(e.getMessage(), is("Booking system is not available on sunday"));
		}
	}
	
	@Test
	public void Step12_현재날짜가_월요일인_경우_예약가능() {
		
		// arrange
		DateTime monday= DATE_TIME_FORMATTER.parseDateTime("2019/02/04 17:00");
		when(bookingScheduler.getNow()).thenReturn(monday);
		
		// act
		Schedule newSchedule= new Schedule(ON_THE_HOUR, UNDER_CAPACITY, CUSTOMER_WITH_EMAIL);
		bookingScheduler.addSchedule(newSchedule);
		
		// assert
		assertThat(bookingScheduler.hasSchedule(newSchedule), is(true));
		
	}
	
	// Step13. 테스트 코드 리팩토링. TestableBookingScheduler 클래스로 일반화. SundayBookingScheduler, MondayBookingScheduler 삭제
	
	// Step14. Mockito mock 라이브러리를 활용하여 Customer dummy 객체 생성

	// Step15. Mockito @Spy를 활용하여 setter 대신 @Spy 어노테이션을 활용한 Injection 확인
	
	// Step16. Mockito @Spy를 활용하여 setter 대신 @Spy 어노테이션을 활용한 Injection 확인(setSmsSender) 및 Mockito verify 라이브러리를 활용한 메서드 호출여부 테스트
	
	// Step17. Mockito @Spy를 활용하여 setter 대신 @Spy 어노테이션을 활용한 Injection 확인(setMailSender) 및 Mockito verify 라이브러리를 활용한 메서드 호출여부 테스트
	
	// Step18. Mockito when, thenReturn 라이브러를 활용하여 stubbing 및 불필요 하게된 TestableBookingSchedule 클래스 삭제
	
	@Test
	public void 이메일이_없는_Customer_테스트() {
		
		// arrange
		Customer customer= new Customer("Ross", "010-1234-5678");
		
		// act
		String email= customer.getEmail();
		
		// assert
		assertNull(email);
	}
	
	@Test
	public void 이메일이_있는_Customer_테스트() {
		
		// arrange
		Customer customer= new Customer("Ross", "010-1234-5678", "abc@test.com");
		
		// act
		String email= customer.getEmail();
		
		// assert
		assertThat(email, is("abc@test.com"));
	}
}