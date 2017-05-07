package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;


import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityOfferData {
	public static enum CopyDepth {
		SHALLOW, BOOKINGS
	};
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	
	private int capacity;
	private List<ActivityReservationData> bookings = new ArrayList<ActivityReservationData>();
	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer a, CopyDepth depth) {
		this.setBegin(a.getBegin());
		this.setEnd(a.getEnd());
		this.setCapacity(a.getCapacity());
		
		
		switch (depth) {
		case BOOKINGS:
			for (Booking booking : a.getBookingSet()) {
				this.bookings.add(new ActivityReservationData(a.getActivity().getActivityProvider(), a, booking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public List<ActivityReservationData> getBookings() {
		return this.bookings;
	}
	
	public void setBookings(List<ActivityReservationData> bookings) {
		this.bookings = bookings;
	}
}