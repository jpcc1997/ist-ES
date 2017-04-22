package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base{
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	private final int number;
	private final LocalDate arrival;
	private final LocalDate departure;
	private boolean cancelled = false;
	private int numberOfHotelExceptions = 0;
	private int numberOfRemoteErrors = 0;

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		this.number = number;
		this.arrival = arrival;
		this.departure = departure;
	}
	
	public void delete() {
		setBroker(null);
		
		for (Reference reference : getReferenceSet())
			reference.delete();
		
		deleteDomainObject();
	}

	public int getNumber() {
		return this.number;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public void processBooking() {
		if (this.cancelled) {
			return;
		}

		try {
			for (String reference : HotelInterface.bulkBooking(this.number, this.arrival, this.departure))
				addReference(new Reference(reference));
			this.numberOfHotelExceptions = 0;
			this.numberOfRemoteErrors = 0;
			return;
		} catch (HotelException he) {
			this.numberOfHotelExceptions++;
			if (this.numberOfHotelExceptions == MAX_HOTEL_EXCEPTIONS) {
				this.cancelled = true;
			}
			this.numberOfRemoteErrors = 0;
			return;
		} catch (RemoteAccessException rae) {
			this.numberOfRemoteErrors++;
			if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
				this.cancelled = true;
			}
			this.numberOfHotelExceptions = 0;
			return;
		}
	}

	public String getReference(String type) {
		if (this.cancelled) {
			return null;
		}

		for (Reference ref : getReferenceSet()) {
			String reference = ref.getReference();
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
				this.numberOfRemoteErrors = 0;
			} catch (HotelException he) {
				this.numberOfRemoteErrors = 0;
			} catch (RemoteAccessException rae) {
				this.numberOfRemoteErrors++;
				if (this.numberOfRemoteErrors == MAX_REMOTE_ERRORS) {
					this.cancelled = true;
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				ref.delete();
				return reference;
			}
		}
		return null;
	}
}
