package pt.ulisboa.tecnico.softeng.bank.domain;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankHasAccountMethodTest {
	Bank bank;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
	}

	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);

		Account result = this.bank.getAccount(account.getIBAN());

		Assert.assertEquals(account, result);
	}
	
	
	@Test(expected = BankException.class)
	public void nullArgs(){
			this.bank.getAccount(null);		
			Assert.fail();}
	
	@Test(expected = BankException.class)
	public void emptyArgs(){
			this.bank.getAccount("");
			Assert.fail();}
	
	@Test(expected = BankException.class)
	public void blankArgs(){
			this.bank.getAccount("   ");
			Assert.fail();}
	
	
	@Test(expected = BankException.class)
	public void wrongIBAN(){
			new Account(this.bank, this.client);
			new Account(this.bank, new Client(this.bank,"Pedro"));
			new Account(this.bank, new Client(this.bank, "Diogo"));
			this.bank.getAccount("BK019923");
			Assert.fail(); }
	
	@Test(expected = BankException.class)
	public void emptyBank(){
			this.bank.getAccount("BK021");
			Assert.fail();}
	

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
