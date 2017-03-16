package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;


public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
	}

	
	@Test
	public void nullCode() {
		try {
			new Bank("Money", null);
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}

	
	@Test
	public void emptyCode() {
		try {
			new Bank("Money", "");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}
	
	
	@Test
	public void blankCode() {
		try {
			new Bank("Money", "    ");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}

	
	@Test
	public void uniqueCode() {
		Bank bank = new Bank("Money", "BK01");

		try {
			new Bank("Money", "BK01");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(1, Bank.banks.size());
			Assert.assertTrue(Bank.banks.contains(bank));
		}
	}
	
	
	@Test
	public void bigCode() {
		try {
			new Bank(null, "BK001");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}
	
	
	@Test
	public void smallCode() {
		try {
			new Bank(null, "BK1");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}

	@Test
	public void nullName() {
		try {
			new Bank(null, "BK01");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}

	
	@Test
	public void emptyName() {
		try {
			new Bank("", "BK01");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}
	
	
	@Test
	public void blankName() {
		try {
			new Bank("    ", "BK01");
			Assert.fail();
		} catch (BankException b) {
			Assert.assertEquals(0, Bank.banks.size());
		}
	}

	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
