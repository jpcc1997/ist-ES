package pt.ulisboa.tecnico.softeng.broker.domain;

import java.rmi.RemoteException;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	//private final LocalDate end2 = new LocalDate(2016,12,19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;


	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
	}		
	
	@Test
	public void ReserveActivityException(@Mocked final ActivityInterface activityInterface){
		
		new Expectations(){
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
				this.result = new ActivityException();
			}
		};
		this.adventure.process();
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		
	}
	
	@Test
	public void ReserveActivityRemoteAccessException(@Mocked final ActivityInterface activityInterface){
		
		new Expectations(){
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
				this.result = new RemoteAccessException();
			}
		};
		for (int i=0; i<5; i++){
		this.adventure.process();
		}
		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void ReservedActivity(@Mocked final ActivityInterface activityInterface){
		
		new Expectations(){
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
	}
	
	
	
	@Test
	public void SameDateReservedActivity(@Mocked final ActivityInterface activityInterface){
		this.adventure = new Adventure(this.broker, this.begin, this.begin, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
		new Expectations(){
			{
				ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getBegin(), adventure.getAge());
			}
		};
		
		this.adventure.process();
		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	
	
	
	
	
	
}

