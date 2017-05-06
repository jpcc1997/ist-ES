package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class ClientData {
	public static enum CopyDepth {
		SHALLOW, ACCOUNTS
	};
	
	private String name;
	private String ID;
	
	//TODO private List<AccountData> accounts = new ArrayList<AccountData>();
	
	public ClientData() {
	}
	
	public ClientData(Client client, CopyDepth depth) {
		this.name = client.getName();
		this.ID = client.getID();
		
		switch (depth) {
		//TODO
//		case ACCOUNTS:
//			for (Account account : client.getAccountSet()) {
//				this.accounts.add(new AccountData(client));
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

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}
