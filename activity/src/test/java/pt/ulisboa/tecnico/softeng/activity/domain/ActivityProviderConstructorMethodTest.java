package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;


	public class ActivityProviderConstructorMethodTest {
		private ActivityProvider provider;
		@Before
		public void setUp() {
			this.provider = new ActivityProvider("XtremX", "Adventure++");

		}
		@Test
		public void success() {

			Assert.assertEquals("Adventure++", this.provider.getName());
			Assert.assertTrue(this.provider.getCode().length() == ActivityProvider.CODE_SIZE);
			Assert.assertEquals(1, ActivityProvider.providers.size());
			Assert.assertEquals(0, this.provider.getNumberOfActivities());
		}


		@Test
		public void EqualName() {
			try {
				new ActivityProvider("123456", "Adventure++");
				Assert.fail();
			}
			catch(ActivityException nameexepction) {
				Assert.assertEquals("Adventure++",this.provider.getName());;
			}
		}
		@Test
		public void ValidlegthCode() {
			try {
				new ActivityProvider("Xs", "Adventure01");
				Assert.fail();
			}
			catch(ActivityException lenghtinvalid) {
				Assert.assertFalse("Xs".length() == ActivityProvider.CODE_SIZE);
			}
		}

		@Test
		public void ValidUinqueCode() {
			try {
				new ActivityProvider("XtremX", "Adventure02");
				Assert.fail();
			}
			catch(ActivityException uniquecode) {
				Assert.assertEquals("XtremX", this.provider.getCode());
			}
		}

		@After
		public void tearDown() {
			ActivityProvider.providers.clear();
		}

	}
