# Restaurant Booking 소개 
### 레스토랑 예약 시스템 
* BookingScheduler를 통해 시간대별 예약관리
* 예약은 정시에만 가능하다.
	* ex) 09:00(0), 09:03(x)
* 시간대별 수용가능 인원을 정할 수 있다.
	* 모든 시간대에 동일한 인원수 적용
	* 시간대별 수용가능 인원이상 예약요청시 Exception 발생
* 일요일은 예약이 불가하다.
	* ex) ‘20180916(일)’에 ‘20180917(월)’ 이용 예약 불가
	* ex) ‘20180917(월)’에 ‘20180923(일)’ 이용 예약 가능
* 예약완료 시 SMS발송
* E-Mail 주소가 있는 경우는 메일 발송

# 실습 내용 
### Test Case 작성
* JUnit 활용하여 Test Coverage 100% 되도록 작성.
* Mock Framework를 사용하지 않고 Test Case 작성.
* Eclemma Test Coverage 툴 활용.

### Test Case 리팩토링
* 상수처리
* @Before

### Test Double을 활용하여 Test Case 작성
* Testable 클래스 작성

### Mock Framework를 사용하여 Test Case 리팩토링
* Mockito Framework를 활용하여 리팩토링
* @RunWith(MockitoJunitRunner.class)
* @InjectMocks
* @Mock
* @Spy

### 디자인 패턴을 활용한 리팩토링
* Observer 패턴을 활용하여 리팩토링.
