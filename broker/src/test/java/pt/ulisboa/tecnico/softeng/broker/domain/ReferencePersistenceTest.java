package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ist.fenixframework.FenixFramework;

@RunWith(JMockit.class)
public class ReferencePersistenceTest {
	
	private static final String REFERENCE = "XPTO123";
	
	private static final String BROKER_NAME = "Happy Going";
	private static final String BROKER_CODE = "BK1017";

	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final int number = 1;
	
	@Mocked
	HotelInterface hotelInterface;
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME);

		new StrictExpectations() {
			{
				HotelInterface.bulkBooking(number, arrival, departure);
				this.result = new HashSet<>(Arrays.asList(REFERENCE));
			}
		};

		broker.bulkBooking(number, arrival, departure);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getBrokerSet().size());

		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());

		List<BulkRoomBooking> bulkRoomBookings = new ArrayList<>(brokers.get(0).getBulkRoomBookingSet());
		assertEquals(1, bulkRoomBookings.size());
		
		List<Reference> refs = new ArrayList<>(bulkRoomBookings.get(0).getReferenceSet());
		assertEquals(1, refs.size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
