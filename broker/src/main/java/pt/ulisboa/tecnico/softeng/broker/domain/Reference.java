package pt.ulisboa.tecnico.softeng.broker.domain;

public class Reference extends Reference_Base{
	private final String reference;
	
	public Reference(String ref) {
		reference = ref;
	}

	public String getReference() {
		return reference;
	}
}
