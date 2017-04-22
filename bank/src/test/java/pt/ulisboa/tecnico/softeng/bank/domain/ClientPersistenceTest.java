package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class ClientPersistenceTest {
	private static final String BANK_CODE = "BK01";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank("Money", BANK_CODE);
		new Client(bank,"Pedro");
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		assertEquals(1, FenixFramework.getDomainRoot().getBankSet().size());
		Bank bank = Bank.getBankByCode(BANK_CODE);
		assertEquals("Money", bank.getName());
		
		assertEquals(1, bank.getNumberOfClients());
		List<Client> clients = new ArrayList<Client>(bank.getClientSet());
		Client client1 = clients.get(0);
		String id = Integer.toString(client1.getCounter());
		assertEquals("Pedro", client1.getName());
		assertEquals(id, client1.getID());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
