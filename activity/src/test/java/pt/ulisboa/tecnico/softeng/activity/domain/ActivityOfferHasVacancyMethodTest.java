package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferHasVacancyMethodTest {
	private ActivityOffer offer;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void successZeroBookinks() {
		Assert.assertTrue(this.offer.hasVacancy());
	}
	@Test(expected = ActivityException.class)
	public void nullActivityTest() {
		new ActivityOffer(null, new LocalDate(2016, 12, 19),new LocalDate(2016, 12, 21));
		Assert.fail();
	}
	
	@Test(expected = ActivityException.class)
	public void nullBeginTest() {
		this.offer.matchDate(null, new LocalDate(2016, 12, 21));
		Assert.fail();
	}
	
	@Test(expected = ActivityException.class)
	public void nullEndTest() {
		this.offer.matchDate(new LocalDate(2016, 12, 19), null);
		Assert.fail();
	}
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
