package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class UndoStateProcessMethodTest {
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
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, AMOUNT);
		this.adventure.setState(State.UNDO);
	}
	
	@Test
	public void nothingToUndo(){	
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	// CancelPayment Exceptions Tests
	@Test
	public void cancelPaymentBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void cancelPaymentRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	
	// CancelReservation Exceptions Tests
	@Test
	public void cancelReservationActivityException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void cancelReservationRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);;
		
		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	
	// CancelBooking Exceptions Tests
	@Test
	public void cancelBookingHotelException(@Mocked final HotelInterface hotelInterface) {
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void cancelBookingRemoteAccessException(@Mocked final  HotelInterface hotelInterface) {
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		this.adventure.process();
		
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	
	// Successful Undo Transitions to Cancelled Test
	@Test
	public void undoPayment(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void undoReservation(@Mocked final  ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new Expectations() {
			{
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void undoBooking(@Mocked final  HotelInterface hotelInterface) {
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new Expectations() {
			{
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void undoPaymentReservationAndBooking(@Mocked final BankInterface bankInterface, 
			@Mocked final  ActivityInterface activityInterface, @Mocked final  HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

}
