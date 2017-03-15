package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {

	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	@Test
	public void successIn() {
		Assert.assertTrue(this.activity.matchAge(50));
	}
	
	@Test
	public void successMin() {
		Assert.assertFalse(this.activity.matchAge(17));
	}

	@Test
	public void successMax() {
		Assert.assertFalse(this.activity.matchAge(81));
	}

	@Test
	public void successNeg() {
		Assert.assertFalse(this.activity.matchAge(-3));
	}

	@Test
	public void successMinLim() {
		Assert.assertTrue(this.activity.matchAge(18));
	}

	@Test
	public void successMaxLim() {
		Assert.assertTrue(this.activity.matchAge(80));
	}
	

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
