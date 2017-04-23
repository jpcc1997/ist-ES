package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BulkRoomBooking extends BulkRoomBooking_Base{
	public static final int MAX_HOTEL_EXCEPTIONS = 3;
	public static final int MAX_REMOTE_ERRORS = 10;

	private final Set<String> references = new HashSet<>();

	public BulkRoomBooking(int number, LocalDate arrival, LocalDate departure) {
		setNumber(number);
		setArrival(arrival);
		setDeparture(departure);
	}

	public Set<String> getReferences() {
		return this.references;
	}
	
	public void processBooking() {
		if (getCancelled()) {
			return;
		}

		try {
			this.references.addAll(HotelInterface.bulkBooking(getNumber(), getArrival(), getDeparture()));
			setNumberOfHotelExceptions(0);
			setNumberOfRemoteErrors(0);
			return;
		} catch (HotelException he) {
			setNumberOfHotelExceptions(1+getNumberOfHotelExceptions());
			if (getNumberOfHotelExceptions() == MAX_HOTEL_EXCEPTIONS) {
				setCancelled(true);
			}
			setNumberOfRemoteErrors(0);
			return;
		} catch (RemoteAccessException rae) {
			setNumberOfRemoteErrors(1 + getNumberOfRemoteErrors());
			if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				setCancelled(true);
			}
			setNumberOfHotelExceptions(0);
			return;
		}
	}

	public String getReference(String type) {
		if (getCancelled()) {
			return null;
		}

		for (String reference : this.references) {
			RoomBookingData data = null;
			try {
				data = HotelInterface.getRoomBookingData(reference);
				setNumberOfRemoteErrors(0);
			} catch (HotelException he) {
				setNumberOfRemoteErrors(0);
			} catch (RemoteAccessException rae) {
				setNumberOfRemoteErrors(1+getNumberOfRemoteErrors());
				if (getNumberOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					setCancelled(true);
				}
			}

			if (data != null && data.getRoomType().equals(type)) {
				this.references.remove(reference);
				return reference;
			}
		}
		return null;
	}

	public void delete() {
		setBroker(null);
		
		//delete references
		
		deleteDomainObject();
	}
}
