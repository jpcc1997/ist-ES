package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Account extends Account_Base{
	private static int counter = 0;

	private final Bank bank;
	//private final String IBAN;
	private final Client client;
	//private int balance;

	public Account(Bank bank, Client client) {
		checkArguments(bank, client);

		this.bank = bank;
		setIBAN(bank.getCode() + Integer.toString(++Account.counter));
		this.client = client;
		setBalance(0);
 
		bank.addAccount(this); //fix
	}

	private void checkArguments(Bank bank, Client client) {
		if (bank == null || client == null) {
			throw new BankException();
		}

		if (!bank.hasClient(client)) {
			throw new BankException();
		}

	}

	Bank getBank() {
		return this.bank;
	}

//	public String getIBAN() {
//		return this.IBAN;
//	}

	public Client getClient() {
		return this.client;
	}

//	public int getBalance() {
//		return this.balance;
//	}

	public String deposit(int amount) {
		if (amount <= 0) {
			throw new BankException();
		}
		int balance = getBalance();
		balance = balance + amount;

		Operation operation = new Operation(Operation.Type.DEPOSIT, this, amount);
		return operation.getReference();
	}

	public String withdraw(int amount) {
		if (amount <= 0 || amount > getBalance()) {
			throw new BankException();
		}
		int balance = getBalance();
		balance = balance - amount;

		return new Operation(Operation.Type.WITHDRAW, this, amount).getReference();
	}

}
