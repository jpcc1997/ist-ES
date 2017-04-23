package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class BookingPersistenceTest {
	
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final LocalDate arrival2 = new LocalDate(2017, 12, 19);
	private final LocalDate departure2 = new LocalDate(2017, 12, 21);	
	
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}
	
	
	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		Room room = new Room(hotel,"01",Type.SINGLE);
		hotel.reserveRoom(Type.SINGLE, arrival, departure);
		hotel.reserveRoom(Type.SINGLE, arrival2, departure2);
		
	}
	
	
	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());
		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		assertEquals(HOTEL_NAME, hotels.get(0).getName());
		assertEquals(HOTEL_CODE, hotels.get(0).getCode());		
		
		assertEquals(1, hotels.get(0).getRoomSet().size());			
		List<Room> rooms = new ArrayList<>(hotels.get(0).getRoomSet());
		assertEquals("01",rooms.get(0).getNumber());
		assertEquals(Type.SINGLE, rooms.get(0).getType());
		
		assertEquals(2, rooms.get(0).getBookingSet().size());
		List<Booking> bookings = new ArrayList<>(rooms.get(0).getBookingSet());
		assertEquals(arrival2,bookings.get(0).getArrival());
		assertEquals(departure2,bookings.get(0).getDeparture());
		assertEquals(arrival, bookings.get(1).getArrival());
		assertEquals(departure, bookings.get(1).getDeparture());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}
}
