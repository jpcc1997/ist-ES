package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankCancelPaymentMethodTest{
	Bank bank;
	Client client;
	Account account;
	String reference_deposit;
	
	@Before
	public void setUp() { 
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
		this.reference_deposit = this.account.deposit(200);
		
	}

	@Test
	public void success() {
		String reference = this.account.withdraw(100);
		String operation_ref = Bank.cancelPayment(reference);
		Assert.assertEquals(this.account.getBalance(), 200);
		Assert.assertNotNull(operation_ref);
		Operation op = bank.getOperation(operation_ref);
		Assert.assertEquals(Operation.Type.DEPOSIT, op.getType());
		Assert.assertEquals(this.account, op.getAccount());
		Assert.assertEquals(100, op.getValue());
	}

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.cancelPayment(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.cancelPayment("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.cancelPayment("    ");
	}
	
	@Test(expected = BankException.class)
	public void invalidReference() {
		Bank.cancelPayment("XPTO");
	}
	
	@Test(expected = BankException.class)
	public void cancelPaymentWithReferenceToDeposit() {
		Bank.cancelPayment(this.reference_deposit);
	}
	
	@Test(expected = BankException.class)
	public void noBanks() {
		String reference = this.account.withdraw(100);
		Bank.banks.clear();
		Bank.cancelPayment(reference);
	}
	

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
