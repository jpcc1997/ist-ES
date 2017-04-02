package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;


public class ActivityProviderGetActivityReservationDataMethodTest{

	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;
	private ActivityReservationData reservationData; 
	

	@Before
	public void setUp() {
		ActivityProvider.providers.clear();
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
		
		

		
	}
	@Test
	public void success() {
		String reference = ActivityProvider.reserveActivity(this.begin, this.end, this.AGE);
		reservationData = ActivityProvider.getActivityReservationData(reference);
		Assert.assertEquals(reservationData.getReference(), reference);
		Assert.assertEquals(reservationData.getName(), activity.getName());
		Assert.assertEquals(reservationData.getCode(), activity.getCode());
		Assert.assertEquals(reservationData.getBegin(), offer.getBegin());
		Assert.assertEquals(reservationData.getEnd(), offer.getEnd());

	}

	@Test(expected = ActivityException.class)
	public void nullArg()
	{
		reservationData = ActivityProvider.getActivityReservationData(null);
	}
	
	@Test(expected = ActivityException.class)
	public void emptyRef()
	{ 
		reservationData = ActivityProvider.getActivityReservationData("");
	}
	
	@Test(expected = ActivityException.class)
	public void blankSpaces()
	{ 
		reservationData = ActivityProvider.getActivityReservationData("     ");
	}
	
	@Test(expected = ActivityException.class)
	public void invalidRef()
	{
		reservationData = ActivityProvider.getActivityReservationData("EstaReferenciaNaoExiste");

	}
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}