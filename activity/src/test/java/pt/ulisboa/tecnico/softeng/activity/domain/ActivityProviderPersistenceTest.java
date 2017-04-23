package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class ActivityProviderPersistenceTest {
	private static final String PROVIDER_CODE = "Xtreme";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		new ActivityProvider(PROVIDER_CODE, "Rappel");
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		ActivityProvider provider = ActivityProvider.getActivityProviderByCode(PROVIDER_CODE);

		assertEquals("Rappel", provider.getName());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider: FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}

}
