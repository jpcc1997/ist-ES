package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class RoomData {
	public static enum CopyDepth {
		SHALLOW, BOOKINGS
	};
	
	private String number;
	private Room.Type type;
	private List<RoomBookingData> bookings = new ArrayList<RoomBookingData>();
	public RoomData() {
	}

	public RoomData(Room room, CopyDepth depth) {
		
		this.setNumber(room.getNumber());
		this.setType(room.getType());
		
		
		switch (depth) {
		case BOOKINGS:
			for (Booking booking : room.getBookingSet()) {
				this.bookings.add(new RoomBookingData(room, booking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}


	public Room.Type getType() {
		return this.type;
	}

	public void setType(Room.Type type) {
		this.type = type;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<RoomBookingData> getBookings() {
		return this.bookings;
	}
	
	public void setBookings(List<RoomBookingData> bookings) {
		this.bookings = bookings;
	}
}
