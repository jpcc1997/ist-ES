package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;

public class BankData {
	
	private String code;
	private String name;
	public BankData() {
	}

	public BankData(Bank bank) {
		this.setName(bank.getName());
		this.setCode(bank.getCode());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}