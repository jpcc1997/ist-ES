package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelCancelBookingMethodTest {

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	
	private Hotel  hotel;
	private Room room;
	private Booking booking;	


	@Before
	public void setUp() {
		this.hotel = new Hotel("TESTCOD", "Hotel_4_estrelas");
		this.room = new Room(this.hotel,"01",Type.SINGLE);
		this.booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure);
	}

	@Test
	public void success() {
		String confirmation = booking.getReference();
		Hotel.cancelBooking(confirmation);
		Assert.assertEquals("cancelled" + confirmation, booking.getCancellation());
		Assert.assertEquals(this.room.getNCancelledBookings(), 1);
		Assert.assertTrue((this.room.isFree(Type.SINGLE, this.arrival, this.departure)));
	}
	
	@Test(expected = HotelException.class)
	public void nullReservationConfirmation() {
		Hotel.cancelBooking(null);
	}
	
	@Test(expected = HotelException.class)
	public void nonExistingReservationConfirmation() {
		Hotel.cancelBooking("reservation");
	}

	@Test(expected = HotelException.class)
	public void noStringReservationConfirmation() {
		Hotel.cancelBooking("");
	}	
	
	@Test(expected = HotelException.class)
	public void emptyStringReservationConfirmation() {
		Hotel.cancelBooking("      ");
	}
	
	@Test(expected = HotelException.class)
	public void doubleCancelation() {
		String confirmation = booking.getReference();
		Hotel.cancelBooking(confirmation);
		Hotel.cancelBooking(confirmation);
	}
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}
