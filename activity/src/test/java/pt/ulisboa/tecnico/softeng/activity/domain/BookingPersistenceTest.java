package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BookingPersistenceTest {
	private static final String PROVIDER_CODE = "Xtreme";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 23);
	private static String code;
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		ActivityProvider provider = new ActivityProvider(PROVIDER_CODE, "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);
		this.code = activity.getCode();
		ActivityOffer offer = new ActivityOffer(activity, this.begin, this.end);
		new Booking(provider, offer);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Activity activity = ActivityProvider.getActivityProviderByCode(PROVIDER_CODE).getActivityByCode(this.code);
		assertEquals(1, activity.getActivityOfferSet().size());
		for (ActivityOffer offer : activity.getOffers()) {
			assertEquals(1, offer.getBookingSet().size());
			for(Booking booking : offer.getBookingSet()) {
				assertTrue(booking.getReference().contains(PROVIDER_CODE));
				assertEquals(offer, booking.getOffer());
			}
		}
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider: FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
		
	}

}