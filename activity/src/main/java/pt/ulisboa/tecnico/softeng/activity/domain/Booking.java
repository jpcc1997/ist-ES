package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkProvider(provider);
		checkOffer(offer);
		checkCapacity(offer);
		
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}

	public String getReference() {
		return this.reference;
	}
	
	private void checkProvider(ActivityProvider provider) {
		if (provider == null)
			throw new ActivityException();
	}
	
	private void checkOffer(ActivityOffer offer) {
		if (offer == null)
			throw new ActivityException();
	}
	
	private void checkCapacity(ActivityOffer offer) {
		if (!offer.hasVacancy())
			throw new ActivityException();
	}
}
