package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.*;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;

public class HotelGetRoomBookingDataMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Londres");
		this.room = new Room(hotel, "1", Type.SINGLE);
	}

	@Test
	public void oneRoom() {
		Booking booking = room.reserve(Type.SINGLE, this.arrival, this.departure);
		RoomBookingData b = Hotel.getRoomBookingData(booking.getReference());
		
		Assert.assertEquals(b.getReference(), booking.getReference());
	}
	
	@Test(expected=HotelException.class)
	public void insuccess() {
		room.reserve(Type.SINGLE, this.arrival, this.departure);
		System.out.println(Hotel.getRoomBookingData("AOAOSJD"));
	}
	
	@Test
	public void twoRooms() {
		this.room = new Room(hotel, "2", Type.SINGLE);
		Booking booking = room.reserve(Type.SINGLE, this.arrival, this.departure);
		RoomBookingData b = Hotel.getRoomBookingData(booking.getReference());
		
		Assert.assertEquals(b.getReference(), booking.getReference());
		Assert.assertEquals(b.getHotelCode(), this.hotel.getCode());
		Assert.assertEquals(b.getHotelName(), this.hotel.getName());
		Assert.assertEquals(b.getRoomNumber(), this.room.getNumber());
		Assert.assertEquals(b.getRoomType(), this.room.getType().toString());
		Assert.assertEquals(b.getArrival(), booking.getArrival());
		Assert.assertEquals(b.getDeparture(), booking.getDeparture());
	}
	
	@Test
	public void threeRooms() {
		Room roomBooking = this.room;
		Booking booking = room.reserve(Type.SINGLE, this.arrival, this.departure);
		this.room = new Room(hotel, "2", Type.SINGLE);
		this.room = new Room(hotel, "3", Type.SINGLE);
		RoomBookingData b = Hotel.getRoomBookingData(booking.getReference());
		
		Assert.assertEquals(b.getReference(), booking.getReference());
		Assert.assertEquals(b.getHotelCode(), this.hotel.getCode());
		Assert.assertEquals(b.getHotelName(), this.hotel.getName());
		Assert.assertEquals(b.getRoomNumber(), roomBooking.getNumber());
		Assert.assertEquals(b.getRoomType(), roomBooking.getType().toString());
		Assert.assertEquals(b.getArrival(), booking.getArrival());
		Assert.assertEquals(b.getDeparture(), booking.getDeparture());
	}
	
	@Test
	public void twoHotelsGetSecond() {
		Hotel hotel1 = new Hotel("XPTO124", "Lisbon");
		this.room = new Room(hotel1, "2", Type.SINGLE);
		Booking booking = room.reserve(Type.SINGLE, this.arrival, this.departure);
		RoomBookingData b = Hotel.getRoomBookingData(booking.getReference());
		
		Assert.assertEquals(b.getReference(), booking.getReference());
		Assert.assertEquals(b.getHotelCode(), hotel1.getCode());
		Assert.assertEquals(b.getHotelName(), hotel1.getName());
		Assert.assertEquals(b.getRoomNumber(), this.room.getNumber());
		Assert.assertEquals(b.getRoomType(), this.room.getType().toString());
		Assert.assertEquals(b.getArrival(), booking.getArrival());
		Assert.assertEquals(b.getDeparture(), booking.getDeparture());
	}
	
	@Test
	public void twoHotelsGetFirst() {
		Booking booking = room.reserve(Type.SINGLE, this.arrival, this.departure);
		Hotel hotel1 = new Hotel("XPTO124", "Lisbon");
		new Room(hotel1, "2", Type.SINGLE);
		RoomBookingData b = Hotel.getRoomBookingData(booking.getReference());
		
		Assert.assertEquals(b.getReference(), booking.getReference());
		Assert.assertEquals(b.getHotelCode(), this.hotel.getCode());
		Assert.assertEquals(b.getHotelName(), this.hotel.getName());
		Assert.assertEquals(b.getRoomNumber(), this.room.getNumber());
		Assert.assertEquals(b.getRoomType(), this.room.getType().toString());
		Assert.assertEquals(b.getArrival(), booking.getArrival());
		Assert.assertEquals(b.getDeparture(), booking.getDeparture());
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}