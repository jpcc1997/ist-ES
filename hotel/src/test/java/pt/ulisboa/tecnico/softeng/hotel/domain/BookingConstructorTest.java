package pt.ulisboa.tecnico.softeng.hotel.domain;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;




public class BookingConstructorTest {

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}
	@Test(expected = HotelException.class)
	public void valid_dates(){
		Hotel hotel = new Hotel("Hotel", "Paris");
		LocalDate arrival = new LocalDate(2016, 12, 21);
		LocalDate departure = new LocalDate(2016, 12, 19);
		Booking booking = new Booking(hotel, arrival, departure);
	}
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
