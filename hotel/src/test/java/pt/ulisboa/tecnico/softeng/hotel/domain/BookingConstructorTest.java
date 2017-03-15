package pt.ulisboa.tecnico.softeng.hotel.domain;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;




public class BookingConstructorTest {
	Hotel hotel;
	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres");
	}

	@Test
	public void success() {

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(this.hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
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
	@Test(expected = HotelException.class)
	public void notnull1(){
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
		new Booking(null, arrival , departure);
	}
	
	@Test(expected = HotelException.class)
	public void notnull2(){
		LocalDate departure = new LocalDate(2016, 12, 21);
		new Booking(this.hotel,null , departure);
	}
	
	@Test(expected = HotelException.class)
	public void notnull3(){
		LocalDate arrival = new LocalDate(2016, 12, 19);
		new Booking(this.hotel,arrival, null);
	}
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
