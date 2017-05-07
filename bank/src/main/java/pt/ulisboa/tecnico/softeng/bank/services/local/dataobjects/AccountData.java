package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;


public class AccountData {
	private String IBAN;
	private int Balance;
	
	public AccountData() {
	}
	
	public AccountData(Account account) {
		this.IBAN = account.getIBAN();
		this.Balance = account.getBalance();
		
		
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public int getBalance() {
		return Balance;
	}

	public void setBalance(int balance) {
		Balance = balance;
	}

}
