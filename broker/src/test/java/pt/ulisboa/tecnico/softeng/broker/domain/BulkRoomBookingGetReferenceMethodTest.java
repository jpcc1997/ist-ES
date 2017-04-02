package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BulkRoomBookingGetReferenceMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 30);
	private final int number = 3;
	private BulkRoomBooking bulkroombooking;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.bulkroombooking = new BulkRoomBooking(number, begin, end);
		//		this.bulkroombooking.addReference("ref");
	}

	@Test
	public void RemoteAccess_exception1(@Mocked final HotelInterface hotelinterface) {
		/* Simulate one RemoteExcception in order to check the numberOfRemoteErrors is
		 incremented*/
		this.bulkroombooking.setNumberOfRemoteErrors(0);
		new StrictExpectations() {
			{	
				/* create a new HashSet of references to use getReference method on */
				HotelInterface.bulkBooking(number, begin, end);
				this.result = new HashSet<String>(Arrays.asList("ref1"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
			}
		};
		this.bulkroombooking.processBooking();
		this.bulkroombooking.getReference("SINGLE");
		Assert.assertEquals(1, bulkroombooking.getNumberOfRemoteErrors());
		Assert.assertFalse(this.bulkroombooking.getCancelled());
	}
	
	@Test
	public void Hotel_exception1(@Mocked final HotelInterface hotelinterface) {
		/* set attribute numberOfRemoteErrors to 5 in order to check if it changes 
		back to 0 when an HotelException occurs*/
		this.bulkroombooking.setNumberOfRemoteErrors(5);
		new StrictExpectations() {
			{
				/* create a new HashSet of references to use getReference method on */
				HotelInterface.bulkBooking(number, begin, end);
				this.result = new HashSet<String>(Arrays.asList("ref1"));
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();
			}
		};
		this.bulkroombooking.processBooking();
		this.bulkroombooking.getReference("SINGLE");
		Assert.assertEquals(0, bulkroombooking.getNumberOfRemoteErrors());
		Assert.assertFalse(this.bulkroombooking.getCancelled());
	}
	
	@Test
	public void Remote_exception10(@Mocked final HotelInterface hotelinterface) {
		/* Simulate 10 RemoteAccessException in order to check if 
		 attribute cancelled changes to True*/
		this.bulkroombooking.setNumberOfRemoteErrors(0);
		new StrictExpectations() {
			{
				/* create a new HashSet of references to use getReference method on */
				HotelInterface.bulkBooking(number, begin, end);
				this.result = new HashSet<String>(Arrays.asList("ref1"));
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = 10;
			}
		};
		this.bulkroombooking.processBooking();
		
		for(int i = 0; i < 10; i++){
			this.bulkroombooking.getReference("SINGLE");
		}
		Assert.assertEquals(10, bulkroombooking.getNumberOfRemoteErrors());
		Assert.assertTrue(this.bulkroombooking.getCancelled());
	}
	
	public void Remote_exception9(@Mocked final HotelInterface hotelinterface) {
		/* Simulate 9 RemoteAccessException in order to check if 
		 attribute cancelled doesn't change*/
		this.bulkroombooking.setNumberOfRemoteErrors(0);
		new StrictExpectations() {
			{
				/* create a new HashSet of references to use getReference method on */
				HotelInterface.bulkBooking(number, begin, end);
				this.result = new HashSet<String>(Arrays.asList("ref1"));
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = 9;
			}
		};
		this.bulkroombooking.processBooking();
		
		for(int i = 0; i < 9; i++){
			this.bulkroombooking.getReference("SINGLE");
		}
		Assert.assertEquals(9, bulkroombooking.getNumberOfRemoteErrors());
		Assert.assertFalse(this.bulkroombooking.getCancelled());
	}
	
	@Test
	public void reference_removed(@Mocked final HotelInterface hotelinterface, @Mocked final RoomBookingData data) {
		new StrictExpectations() {
			{
				/* create a new HashSet with three references to use getReference method on */
				HotelInterface.bulkBooking(number, begin, end);
				this.result = new HashSet<String>(Arrays.asList("ref1", "ref2", "ref3"));
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = data; 
				this.times = 1;
				
				data.getRoomType();
				this.result = "SINGLE";
				this.times = 1;
			}
		};
		this.bulkroombooking.processBooking();
		this.bulkroombooking.getReference("SINGLE");
		Assert.assertEquals(2, this.bulkroombooking.getReferences().size()); //expect a two references since one was removed
		Assert.assertFalse(this.bulkroombooking.getCancelled());
	}
	
	@Test (expected = BrokerException.class)
	public void null_argument(){
		this.bulkroombooking.getReference(null);
	}

}