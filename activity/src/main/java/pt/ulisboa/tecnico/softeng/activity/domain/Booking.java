package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private String cancelled;
	private LocalDate cancellationDate;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);
		this.cancelled = null;

		offer.addBooking(this);
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}

	}

	public String getReference() {
		return this.reference;
	}
	
	public String getCancelled() {
		return this.cancelled;
	}
	
	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public void setCancelled(String ref) {
		this.cancelled = ref;
	}
	
	public void setCancellationDate(LocalDate date) {
		this.cancellationDate = date;
	}
	
}
