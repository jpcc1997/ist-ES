package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class BankData {
	public static enum CopyDepth {
		SHALLOW, CLIENTS, OPERATIONS
	};
	
	private String code;
	private String name;
	
	private List<ClientData> clients = new ArrayList<ClientData>();
	//TODO private List<BankOperationData> operations = new ArrayList<BankOperationData>();
	
	public List<ClientData> getClients() {
		return clients;
	}

	public void setClients(List<ClientData> clients) {
		this.clients = clients;
	}

	public BankData() {
	}

	public BankData(Bank bank, CopyDepth depth) {
		this.setName(bank.getName());
		this.setCode(bank.getCode());
		
		switch (depth) {
		case CLIENTS:
			for (Client client : bank.getClientSet()) {
				this.clients.add(new ClientData(client, ClientData.CopyDepth.SHALLOW));
			}
			break;
//		TODO
//		case OPERATIONS:
//			for (Operation operation : bank.getOperationSet()) {
//				this.operations.add(new BankOperationData(operation));
//			}
//			break;
		case SHALLOW:
			break;
		default:
			break;
		}
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