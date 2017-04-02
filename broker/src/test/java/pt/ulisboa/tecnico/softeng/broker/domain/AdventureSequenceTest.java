package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

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
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
	}

	@Test
	public void reserve_activity_confirmedTest(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION; 
				
				ActivityInterface.reserveActivity(adventure.getBegin(),adventure.getEnd(), adventure.getAge());
				this.result = ACTIVITY_CONFIRMATION;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void book_room_confirmedTest(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION; 
				
				ActivityInterface.reserveActivity(adventure.getBegin(),adventure.getEnd(), adventure.getAge());
				this.result = ACTIVITY_CONFIRMATION;
				
				HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
				this.result = ROOM_CONFIRMATION;
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void confirmedTest(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION; 
				
				ActivityInterface.reserveActivity(adventure.getBegin(),adventure.getEnd(), adventure.getAge());
				this.result = ACTIVITY_CONFIRMATION;
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = 4;
			}
		};
		
		for(int i = 0; i < 6; i++) this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
}
