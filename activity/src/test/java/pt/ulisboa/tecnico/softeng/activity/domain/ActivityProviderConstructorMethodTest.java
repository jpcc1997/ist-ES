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
		
		@Test
		public void NullName() {
			try {
				new ActivityProvider("123436",null);
				Assert.fail();
			}
			catch(ActivityException nullname) {
				Assert.assertEquals("Adventure++",this.provider.getName());;
			}
		}
		@Test
		public void BlankName() {
			try {
				new ActivityProvider("123436"," ");
				Assert.fail();
			}
			catch(ActivityException blankname) {
				Assert.assertEquals("Adventure++",this.provider.getName());;
			}
		}
		
		@Test
		public void NostringName() {
			try {
				new ActivityProvider("123436","");
				Assert.fail();
			}
			catch(ActivityException nostringname) {
				Assert.assertEquals("Adventure++",this.provider.getName());;
			}
		}
		@Test
		public void NullCode() {
			try {
				new ActivityProvider(null, "Adventure02");
				Assert.fail();
			}
			catch(ActivityException nullcode) {
				Assert.assertEquals("XtremX",this.provider.getCode());;
			}
		}		
		@Test
		public void BlankCode() {
			try {
				new ActivityProvider(" ","Adventure++");
				Assert.fail();
			}
			catch(ActivityException blankcode) {
				Assert.assertEquals("XtremX",this.provider.getCode());;
			}
		}
		
		@Test
		public void NostringCode() {
			try {
				new ActivityProvider("","Adventure++");
				Assert.fail();
			}
			catch(ActivityException nostringcode) {
				Assert.assertEquals("XtremX",this.provider.getCode());;
			}
		}
		
		@After
		public void tearDown() {
			ActivityProvider.providers.clear();
		}

	}
