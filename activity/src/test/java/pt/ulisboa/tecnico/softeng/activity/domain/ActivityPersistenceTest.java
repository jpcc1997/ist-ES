package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class ActivityPersistenceTest {
	private static final String PROVIDER_CODE = "Xtreme";
	private static String code;
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Activity activity = new Activity(new ActivityProvider(PROVIDER_CODE, "Rappel"), "X1",25, 50, 25);
		this.code = activity.getCode();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Activity activity = ActivityProvider.getActivityProviderByCode(PROVIDER_CODE).getActivityByCode(this.code);
		System.out.println(activity);
		assertEquals("X1", activity.getName());
		assertEquals(this.code, activity.getCode());
		assertEquals(25, activity.getMinAge());
		assertEquals(50, activity.getMaxAge());
		assertEquals(25, activity.getCapacity());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider: FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
		
	}


}