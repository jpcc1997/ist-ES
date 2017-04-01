package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.*;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
	}
	
	//Input Tests
	@Test(expected = HotelException.class)
	public void negativeNumber() {
		Hotel.bulkBooking(-1, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(0, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(1, null, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(1, arrival, null);
	}
	
	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		LocalDate otherArrival = new LocalDate(2016, 12, 19);
		LocalDate otherDeparture = new LocalDate(2016, 12, 18);
		
		Hotel.bulkBooking(1, otherArrival, otherDeparture);
	}
	
	
	//Main Tests
	@Test
	public void success3DoubleRooms() {
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.DOUBLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		
		Set<String> refs = Hotel.bulkBooking(3, arrival, departure);
		
		assertEquals(refs.size(), 3);
	}
	
	@Test
	public void success3SingleRooms() {
		new Room(this.hotel, "01", Type.SINGLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.SINGLE);
		
		Set<String> refs = Hotel.bulkBooking(3, arrival, departure);
		
		assertEquals(refs.size(), 3);
	}
	
	@Test
	public void success2SingleDoubleRooms() {
		new Room(this.hotel, "01", Type.SINGLE);
		new Room(this.hotel, "02", Type.DOUBLE);
		
		Set<String> refs = Hotel.bulkBooking(2, arrival, departure);
		
		assertEquals(refs.size(), 2);
	}
	
	@Test
	public void notEnoughRooms() {
		Room a = new Room(this.hotel, "01", Type.DOUBLE);
		Room b = new Room(this.hotel, "02", Type.DOUBLE);
		Room c = new Room(this.hotel, "03", Type.DOUBLE);
		
		try {
			Hotel.bulkBooking(4, arrival, departure);
			fail();
		} catch (HotelException e) {
			assertTrue(a.isFree(a.getType(), arrival, departure));
			assertTrue(b.isFree(b.getType(), arrival, departure));
			assertTrue(c.isFree(c.getType(), arrival, departure));
		}
	}
	
	@Test
	public void notEnoughFreeRooms() {
		Room a = new Room(this.hotel, "01", Type.DOUBLE);
		Room b = new Room(this.hotel, "02", Type.DOUBLE);
		Room c = new Room(this.hotel, "03", Type.DOUBLE);
		
		a.reserve(Type.DOUBLE, arrival, departure);
		
		try {
			Hotel.bulkBooking(3, arrival, departure);
			fail();
		} catch (HotelException e) {
			assertTrue(b.isFree(b.getType(), arrival, departure));
			assertTrue(c.isFree(c.getType(), arrival, departure));
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
