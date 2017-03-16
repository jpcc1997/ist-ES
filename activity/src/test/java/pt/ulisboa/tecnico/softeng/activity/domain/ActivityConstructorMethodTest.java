package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityConstructorMethodTest {
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
	}

	@Test
	public void success() {
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}
	
	@Test
	public void minAgeTest() {
		
		try {
			new Activity(this.provider, "Bush Walking", 17, 80, 25);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@Test
	public void maxAgeTest() {
		
		try {
			new Activity(this.provider, "Bush Walking", 18, 100, 25);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@Test
	public void minMaxAgeTest() {
		
		try {
			new Activity(this.provider, "Bush Walking", 25, 24, 25);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@Test
	public void AgeLimTest() {
		
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 99, 10);
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(99, activity.getMaxAge());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());

	}

	@Test
	public void capacityTest() {
		
		try {
			new Activity(this.provider, "Bush Walking", 30, 80, 0);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@Test
	public void capacityLimTest() {
		
		Activity activity = new Activity(this.provider, "Bush Walking", 30, 80, 1);
		Assert.assertEquals(1, activity.getCapacity());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());

	}
	
	@Test
	public void nullProvTest() {
		
		try {
			new Activity(null, "Bush Walking", 18, 99, 25);
			Assert.fail();
		}
		catch(ActivityException e) {}
	}
	
	@Test
	public void nullNameTest() {
		
		try {
			new Activity(this.provider, null, 18, 99, 25);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@Test
	public void emptyNameTest() {
		
		try {
			new Activity(this.provider, "", 18, 99, 25);
			Assert.fail();
		}
		catch(ActivityException e) {
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
