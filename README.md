# Restaurant Booking 소개 
레스토랑 예약 시스템 
* BookingScheduler를 통해 시간대별 예약관리
* 예약은 정시에만 가능하다.
** → ex. 09:00(0), 09:03(x)
* 시간대별 수용가능 인원을 정할 수 있다.
** → 모든 시간대에 동일한 인원수 적용
* 일요일은 예약이 불가하다.
** → ex. ‘20180916(일)’에 ‘20180917(월)’ 이용 예약 불가
** → ex. ‘20180917(월)’에 ‘20180923(일)’ 이용 예약 가능
* 예약완료 시 SMS발송
* 이메일 주소가 있는 경우는 메일 발송