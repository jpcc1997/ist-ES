package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConflictMethodTest {
	Booking booking;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
	
		this.booking = new Booking(hotel, arrival, departure);
	}
	
	@Test
	public void ConflictArrivalBetween() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		
		Assert.assertTrue(this.booking.conflict(arrival, this.booking.getDeparture()));
	}

	@Test
	public void noConflictBefore() {
		LocalDate arrival = new LocalDate(2016, 12, 16);
		LocalDate departure = new LocalDate(2016, 12, 19);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void ConflictDepartureBetween() {
		LocalDate departure = new LocalDate(2016, 12, 20);
		
		Assert.assertTrue(this.booking.conflict( this.booking.getArrival(), departure));
	}
	
	@Test
	public void noConflictAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 24);
		LocalDate departure = new LocalDate(2016, 12, 30);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void ConflictDateBigger() {
		LocalDate arrival = new LocalDate(2016, 12, 16);
		LocalDate departure = new LocalDate(2016, 12, 27);
		
		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void ConflictDateSmaller() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 22);
		
		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void ConflictMatching() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		
		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
