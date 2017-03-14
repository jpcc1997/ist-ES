package pt.ulisboa.tecnico.softeng.hotel.domain;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel1 = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel1.getName());
		Assert.assertTrue(hotel1.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel1.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());

		Hotel hotel2 = new Hotel("XPTO124", "Lisbon");

		Assert.assertEquals("Lisbon", hotel2.getName());
		Assert.assertTrue(hotel2.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(2, Hotel.hotels.size());
	}

	//test length of code - CODE_SIZE 7
	@Test(expected = HotelException.class)
	public void codeLengthError() {
		Hotel hotel = new Hotel("XPTO12", "Londres");
	}
	
	@Test(expected = HotelException.class) 
	public void nullArguments() {
		Hotel hotel = new Hotel(null, null);
	}

	//test if code is unique
	@Test(expected = HotelException.class)
	public void codeVerification() {
		Hotel hotel1 = new Hotel("XPTO123", "Londres");
		Hotel hotel2 = new Hotel("XPTO123", "Lisbon");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
