package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.fail;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderCancelReservationMethodTest {

	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		ActivityProvider.providers.clear();
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);

		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
	}
	
	@Test
	public void success() {
		String confirmation = ActivityProvider.reserveActivity(this.begin, this.end, AGE);
		ActivityProvider.cancelReservation(confirmation);
		Assert.assertEquals(0,this.offer.getNumberOfBookings());
		Assert.assertEquals(1, this.provider.findOffer(this.begin, this.end, AGE).size());
	}
	
	@Test(expected = ActivityException.class)
	public void nullReservationConfirmation() {
		ActivityProvider.cancelReservation(null);
	}
	
	@Test(expected = ActivityException.class)
	public void nonExistingReservationConfirmation() {
		ActivityProvider.cancelReservation("res");
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}