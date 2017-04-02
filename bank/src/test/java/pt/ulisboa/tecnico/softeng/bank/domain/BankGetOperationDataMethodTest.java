package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetOperationDataMethodTest{
	Bank bank;
	Client client;
	Account account;
	Operation operation;
	
	@Before
	public void setUp() { 
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
		this.operation = new Operation(Type.DEPOSIT, this.account, 300);
	}

	
	@Test
	public void success() {
		
		String reference = this.operation.getReference();
		Assert.assertNotNull(reference);
		BankOperationData bop = Bank.getOperationData(reference);	
		Assert.assertEquals(reference, bop.getReference());
		Assert.assertEquals(this.operation.getTime(), bop.getTime());
		Assert.assertEquals(Type.DEPOSIT.toString(), bop.getType());
		Assert.assertEquals(this.account.getIBAN(), bop.getIban());
		Assert.assertEquals(300, bop.getValue());
	}

	
	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	
	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.getOperationData("");
	}

	
	@Test(expected = BankException.class)
	public void blankSpaceReference() {
		Bank.getOperationData("    ");
	}

	
	@Test(expected = BankException.class)
	public void inexistentReference() {
		Bank.getOperationData("refvalida");
	}
	

	@Test(expected = BankException.class)
	public void clearBanks() {
		String reference = this.operation.getReference();
		Bank.banks.clear();
		Bank.getOperationData(reference);
	}
	
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
