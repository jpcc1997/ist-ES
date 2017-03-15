package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}
	
	@Test
	public void noVacancy() {
		LocalDate arrival = new LocalDate(2017, 1, 1);
		LocalDate departure = new LocalDate(2017, 1, 15);
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
		Assert.assertNull(hotel.hasVacancy(Type.DOUBLE, arrival, departure));
	}
	
	@Test
	public void noVacancy2() {
		LocalDate arrival = new LocalDate(2018, 1, 1);
		LocalDate departure = new LocalDate(2018, 1, 15);
		LocalDate arrival2 = new LocalDate(2018, 1, 5);
		LocalDate departure2 = new LocalDate(2018, 1, 10);
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
		Assert.assertNull(hotel.hasVacancy(Type.DOUBLE, arrival2, departure2));
	}
	
	@Test
	public void noVacancy3() {
		LocalDate arrival = new LocalDate(2019, 1, 5);
		LocalDate departure = new LocalDate(2019, 1, 25);
		LocalDate arrival2 = new LocalDate(2019, 1, 1);
		LocalDate departure2 = new LocalDate(2019, 1, 28);
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
		Assert.assertNull(hotel.hasVacancy(Type.DOUBLE, arrival2, departure2));
	}
	
	@Test
	public void noVacancy4() {
		LocalDate arrival = new LocalDate(2020, 1, 5);
		LocalDate departure = new LocalDate(2020, 1, 25);
		LocalDate arrival2 = new LocalDate(2020, 1, 8);
		LocalDate departure2 = new LocalDate(2020, 1, 28);
		Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
		Assert.assertNull(hotel.hasVacancy(Type.DOUBLE, arrival2, departure2));
	}
	
	@Test
	public void noVacancy5() {
		new Room(this.hotel, "15", Type.SINGLE);
		LocalDate arrival = new LocalDate(2021, 1, 5);
		LocalDate departure = new LocalDate(2021, 1, 25);
		LocalDate arrival2 = new LocalDate(2021, 1, 1);
		LocalDate departure2 = new LocalDate(2021, 1, 22);
		Hotel.reserveHotel(Type.SINGLE, arrival, departure);
		Assert.assertNull(hotel.hasVacancy(Type.SINGLE, arrival2, departure2));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
