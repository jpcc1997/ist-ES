package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomPersistenceTest {

	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel1 = new Hotel(HOTEL_CODE, HOTEL_NAME);
		Hotel hotel2 = new Hotel("XPTO124", "dubai");
		new Room(hotel1, "01", Type.SINGLE);
		new Room(hotel1, "02", Type.DOUBLE);
		new Room(hotel2, "01", Type.SINGLE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(2, FenixFramework.getDomainRoot().getHotelSet().size());
		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		assertEquals(HOTEL_NAME, hotels.get(1).getName());
		assertEquals(HOTEL_CODE, hotels.get(1).getCode());		
		
		assertEquals(2, hotels.get(1).getRoomSet().size());			
		List<Room> rooms = new ArrayList<>(hotels.get(1).getRoomSet());
		assertEquals("01",rooms.get(1).getNumber());
		assertEquals(Type.SINGLE, rooms.get(1).getType());
		assertEquals("02",rooms.get(0).getNumber());
		assertEquals(Type.DOUBLE, rooms.get(0).getType());
		
		assertEquals(1, hotels.get(0).getRoomSet().size());
		List<Room> rooms2 = new ArrayList<>(hotels.get(0).getRoomSet());
		assertEquals("01",rooms2.get(0).getNumber());
		assertEquals(Type.SINGLE, rooms2.get(0).getType());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
