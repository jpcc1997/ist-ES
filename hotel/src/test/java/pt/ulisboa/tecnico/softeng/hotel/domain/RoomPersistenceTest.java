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
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		new Room(hotel, "01", Type.SINGLE);
		new Room(hotel, "02", Type.DOUBLE);
		new Room(hotel, "03", Type.SINGLE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());
		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		assertEquals(HOTEL_NAME, hotels.get(0).getName());
		assertEquals(HOTEL_CODE, hotels.get(0).getCode());		
		assertEquals(3, hotels.get(0).getRoomSet().size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
