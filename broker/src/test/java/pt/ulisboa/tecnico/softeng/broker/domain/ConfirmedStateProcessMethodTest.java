package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
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
		this.adventure.setState(State.CONFIRMED);
	}
	
	@Test
	public void oneBankException (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
				
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void fourBankExceptions (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 4;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		for(int i = 0; i < 4; i++) this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void fiveBankExceptions (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 5;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		for(int i = 0; i < 5; i++) this.adventure.process();

		assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void operation10RemoteAccessExceptions (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 10;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		for(int i = 0; i < 10; i++) this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void operation19RemoteAccessExceptions (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 19;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		for(int i = 0; i < 19; i++) this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void opration20RemoteAccessExceptions (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 20;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 0;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		for(int i = 0; i < 20; i++) this.adventure.process();

		assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void oneActivityException (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void activityRemoteAccessException (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneHotelException (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new HotelException();
				this.times = 1;
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void bookingRemoteAccessException (@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.result = new RemoteAccessException();
				this.times = 1;
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void noExceptionsNoRoomConfirmation(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 0;
				
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void noExceptions(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.times = 1;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.times = 1;

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.times = 1;
			}
		};

		this.adventure.process();

		assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
}
