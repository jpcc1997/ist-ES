package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Arrays;
import java.util.HashSet;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingProcessBookingMethodTest {
	
	private int number = 10;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private BulkRoomBooking bulkroombooking;
	
	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.bulkroombooking = new BulkRoomBooking(number,begin, end); 
		
	}

	@Test
	public void onehotelexception(@Mocked final HotelInterface hotelInterface) {
		
		new Expectations() {
			{
				HotelInterface.bulkBooking(number,begin, end);
				this.result = new HotelException();
			}
		};
		
		this.bulkroombooking.processBooking();
		Assert.assertEquals(1, bulkroombooking.getNHotelsExceptions());
		Assert.assertEquals(false, bulkroombooking.getCancelled());
		Assert.assertEquals(0, bulkroombooking.getNRemoteErrors());


	}
	
	
	@Test
	public void  oneremoteexception(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new RemoteAccessException();
			}
		};
		
		this.bulkroombooking.processBooking();
		
		Assert.assertEquals(1, bulkroombooking.getNRemoteErrors());
		Assert.assertEquals(0, bulkroombooking.getNHotelsExceptions());
		Assert.assertEquals(false, bulkroombooking.getCancelled());

	}
	@Test
	public void  nineRemoteErrors(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new RemoteAccessException();
			this.times = 9;
			}
		};
		
		for(int i = 0; i<9; i++) {this.bulkroombooking.processBooking();}
		
		Assert.assertEquals(false, bulkroombooking.getCancelled());
		Assert.assertEquals(0, bulkroombooking.getNHotelsExceptions());
		Assert.assertEquals(9, bulkroombooking.getNRemoteErrors());

	}
	@Test
	public void  tenRemoteErrors(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new RemoteAccessException();
			this.times = 10;
			}
		};
		
		for(int i = 0; i<10; i++) {this.bulkroombooking.processBooking();}
		
		Assert.assertEquals(true, bulkroombooking.getCancelled());
		Assert.assertEquals(0, bulkroombooking.getNHotelsExceptions());
		Assert.assertEquals(10, bulkroombooking.getNRemoteErrors());

	}
	@Test
	public void  twoHotelExceptions(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new HotelException();
			this.times = 2;
			}
		};
		
		for(int i = 0; i<2; i++) {this.bulkroombooking.processBooking();}
		
		
		Assert.assertEquals(false, bulkroombooking.getCancelled());
		Assert.assertEquals(2, bulkroombooking.getNHotelsExceptions());
		Assert.assertEquals(0, bulkroombooking.getNRemoteErrors());
		

	}
	
	@Test
	public void  threeHotelExceptions(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new HotelException();
			this.times = 3;
			}
		};
		
		for(int i = 0; i<3; i++) {this.bulkroombooking.processBooking();}
		
		Assert.assertEquals(true, bulkroombooking.getCancelled());
		Assert.assertEquals(0, bulkroombooking.getNRemoteErrors());
		Assert.assertEquals(3, bulkroombooking.getNHotelsExceptions());
		

	}
	@Test
	public void  noExceptions(@Mocked final HotelInterface hotelinterface) {
		
		new Expectations() {
			{
			HotelInterface.bulkBooking(number, begin, end);
			this.result = new HashSet<String>(Arrays.asList("Room1","Room2","Room3","Room4","Room5",
															"Room6", "Room7", "Room8","Room9","Room10"));
			}
		};
		
		this.bulkroombooking.processBooking();
		
		Assert.assertEquals(10, bulkroombooking.getReferences().size());
		Assert.assertEquals(false, bulkroombooking.getCancelled());
		Assert.assertEquals(0, bulkroombooking.getNRemoteErrors());
		Assert.assertEquals(0, bulkroombooking.getNHotelsExceptions());
	}
	
	
	
}