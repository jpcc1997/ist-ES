package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private Broker broker;
	private LocalDate begin;
	private LocalDate end;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2016, 12, 21);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", 300);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals("BK011234567", adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}
	
	@Test
	public void nullBroker(){
		try {
			new Adventure(null, this.begin, this.end, 20, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void nullBeginDate(){
		try {
			new Adventure(this.broker, null, this.end, 20, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void nullEndDate(){
		try {
			new Adventure(this.broker, this.begin, null, 20, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void beginDateAfterEndDate(){
		try {
			new Adventure(this.broker, this.end, this.begin, 20, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void sameBeginAndEndDate(){
		try {
			new Adventure(this.broker, this.begin, this.begin, 20, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void nullIBAN(){
		try {	
			new Adventure(this.broker, this.begin, this.end, 20, null, 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void emptyIBAN(){
		try {	
			new Adventure(this.broker, this.begin, this.end, 20, "    ", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void negativeAmount(){
		try {	
			new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", -100);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void zeroAmount(){
		try {
			new Adventure(this.broker, this.begin, this.end, 20, "BK011234567", 0);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}	
	}
	
	@Test
	public void overMaximumAge(){
		try {
			new Adventure(this.broker, this.begin, this.end, 100, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@Test
	public void belowMinimumAge(){
		try {
			new Adventure(this.broker, this.begin, this.end, 17, "BK011234567", 300);
			Assert.fail();
		} 
		catch (BrokerException e) {
			Assert.assertEquals(0, this.broker.getNumberOfAdventures());
		}
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
