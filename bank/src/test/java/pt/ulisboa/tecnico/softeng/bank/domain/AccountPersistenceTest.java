package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class AccountPersistenceTest {
	private static final String BANK_CODE = "BK01";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank("Money", BANK_CODE);
		Client client = new Client(bank,"Joao");
		new Account(bank, client);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		assertEquals(1, FenixFramework.getDomainRoot().getBankSet().size());
		Bank bank = Bank.getBankByCode(BANK_CODE);
		assertEquals("Money", bank.getName());

		assertEquals(1,bank.getNumberOfAccounts());
		
		List<Account> accounts = new ArrayList<Account>(bank.getAccountSet());
		Account account = accounts.get(0);
		assertEquals(0,account.getBalance());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
