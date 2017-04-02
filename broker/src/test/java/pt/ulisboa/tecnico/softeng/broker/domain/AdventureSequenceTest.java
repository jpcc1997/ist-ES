package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;




@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
	}
	
	@Test
	public void  processPaymentBankException(@Mocked final BankInterface bankInterface) {
		
		new StrictExpectations() {
			{
			BankInterface.processPayment(IBAN, AMOUNT);
			this.result = new BankException();
			this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void  processPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {
		
		new StrictExpectations() {
			{
			BankInterface.processPayment(IBAN, AMOUNT);
			this.result = new RemoteAccessException();
			this.times = 3;
			}
		};
		
		for(int i = 0; i < 3; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void reserveActivityActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION; 
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = new ActivityException();
				this.times = 1;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION; 
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = new RemoteAccessException();
				this.times = 5;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		for(int i = 0; i < 5; i++) this.adventure.process();	
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityConfirmedBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, begin, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
			
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 5;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		for(int i = 0; i < 5; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityConfirmedRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, begin, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
			
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 20;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		for(int i = 0; i < 20; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void reserveActivityConfirmedActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin,begin, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
				this.times = 1;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;

			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());

		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = new HotelException();
				this.times = 1;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = new RemoteAccessException();
				this.times = 10;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		for(int i = 0; i < 10; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomConfirmedBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = ROOM_CONFIRMATION;
				this.times = 1;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 5;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		for(int i = 0; i < 5; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomConfirmedRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = ROOM_CONFIRMATION;
				this.times = 1;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 20;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		for(int i = 0; i < 20; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomConfirmedActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = ROOM_CONFIRMATION;
				this.times = 1;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
				this.times = 1;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void bookRoomConfirmedHotelException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
				
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;
				this.times = 1;
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = ACTIVITY_CONFIRMATION;
				this.times = 1;
				
				HotelInterface.reserveRoom(Type.SINGLE, begin, end);
				this.result = ROOM_CONFIRMATION;
				this.times = 1;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();
				this.times = 1;
				
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				this.times = 1;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				this.times = 1;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
}
