package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomConstructorMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}
	
	@Test(expected = HotelException.class)
	public void isnumber() {
		Room room = new Room(this.hotel, "letras", Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void isnumber2() {
		Room room = new Room(this.hotel, "28,28", Type.DOUBLE);
	}
	
	@Test(expected = HotelException.class)
	public void isuniquenumber() {
		Room room = new Room(this.hotel, "25", Type.DOUBLE);
		Room room2 = new Room(this.hotel, "25", Type.DOUBLE);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
