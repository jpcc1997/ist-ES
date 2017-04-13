package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ist.fenixframework.FenixFramework;

public class OperationPersistenceTest {
	private static final String BANK_CODE = "BK01";
	private static String ref;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank("Money", BANK_CODE);
		Operation operation = new Operation(Type.DEPOSIT, new Account(bank, new Client(bank, "António")),1000);
		this.ref = operation.getReference();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		Operation operation = Bank.getBankByCode(BANK_CODE).getOperation(this.ref);

		assertEquals(Type.DEPOSIT, operation.getType());
		assertEquals(1000, operation.getValue());
		assertTrue(operation.getTime() != null);
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
