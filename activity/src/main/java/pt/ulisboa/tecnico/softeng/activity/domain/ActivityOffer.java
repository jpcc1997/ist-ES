package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOffer {
	private final LocalDate begin;
	private final LocalDate end;
	private final int capacity;
	private final Set<Booking> bookings = new HashSet<>();

	public ActivityOffer(Activity activity, LocalDate begin, LocalDate end) {
		checkActivity(activity);
		checkDates(begin,end);
		
		this.begin = begin;
		this.end = end;
		this.capacity = activity.getCapacity();

		activity.addOffer(this);
	}

	LocalDate getBegin() {
		return this.begin;
	}

	LocalDate getEnd() {
		return this.end;
	}

	int getNumberOfBookings() {
		return this.bookings.size();
	}

	void addBooking(Booking booking) {
		this.bookings.add(booking);

	}

	boolean available(LocalDate begin, LocalDate end) {
		return hasVacancy() && matchDate(begin, end);
	}

	boolean matchDate(LocalDate begin, LocalDate end) {
		if (begin == null || end == null)
			throw new ActivityException();
		return begin.equals(getBegin()) && end.equals(getEnd());
	}

	boolean hasVacancy() {
		return this.capacity > getNumberOfBookings();
	}
	
	private void checkActivity(Activity activity){
		if(activity == null)
			throw new ActivityException();
	}
	
	private void checkDates(LocalDate begin, LocalDate end){
		if(begin == null || end == null || end.isBefore(begin))
			throw new ActivityException();
	}

}
