package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}
	
	Set<Room> getRooms() {
		return rooms;
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String roomConfirmation) {

		// TODO implement
		if(roomConfirmation == null){
			throw new HotelException();
		}
		else if(roomConfirmation.trim().equals("")){
			throw new HotelException();
		}
		else if(roomConfirmation.trim().equals("")){
			throw new HotelException();
		}
			for (Hotel hotel : Hotel.hotels) {
				for (Room room : hotel.rooms) {
					for(Booking booking : room.getBookings() )
						if (booking.getReference() == roomConfirmation) {
							if (booking.getCancellation() != "" ){
								throw new HotelException();
							}
							booking.setCancellation();
							LocalDate cancellationdate = LocalDate.now();
							booking.setCancellationDate(cancellationdate);
							room.setNCancelledBookings();
							return booking.getCancellation();
						}	
				}
			}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		
		if(reference == null)
			throw new HotelException();
		RoomBookingData rbd = new RoomBookingData();
		Booking b = null;
		for(Hotel hotel : Hotel.hotels) {
			if(hotel.getCode().equals(reference.substring(0,7)))
				for(Room r : hotel.rooms) {
					b = r.getBooking(reference);
					if(b != null) {
						rbd.setHotelCode(hotel.getCode());
						rbd.setHotelName(hotel.getName());
						rbd.setArrival(b.getArrival());
						rbd.setDeparture(b.getDeparture());
						rbd.setCancellation(b.getCancellation());
						rbd.setCancellationDate(b.getCancellationDate());
						rbd.setReference(reference);
						rbd.setRoomNumber(r.getNumber());
						rbd.setRoomType(r.getType().toString());
						return rbd;
					}
				}
		}
		throw new HotelException();
	}
	
	public int getNumberOfFreeRooms(LocalDate arrival, LocalDate departure) {
		int number = 0;
		for (Room r : this.rooms) {
			if (r.isFree(r.getType(), arrival, departure))
				number++;
		}
		return number;
	}
	
	public static int getTotalNumberOfFreeRooms(LocalDate arrival, LocalDate departure) {
		int number = 0;
		for (Hotel h : Hotel.hotels) {
			number += h.getNumberOfFreeRooms(arrival, departure);
		}
		return number;
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		// Check arguments
		if (number <= 0)
			throw new HotelException();
		if (arrival == null || departure == null)
			throw new HotelException();
		if (departure.isBefore(arrival))
			throw new HotelException();
		
		//Check if there are enough free rooms
		if (Hotel.getTotalNumberOfFreeRooms(arrival, departure) < number)
			throw new HotelException();
		
		Set<String> result = new HashSet<>();
		
		try {
			while (number > 0) {
				// this will throw an HotelException if there are not enough SINGLE rooms
				String ref = Hotel.reserveRoom(Type.SINGLE, arrival, departure);
				result.add(ref);
				number--;
			}
		} catch (HotelException e) {
			// if there are not enough SINGLE rooms, then we book DOUBLE rooms for the rest
			while (number > 0) {
				String ref = Hotel.reserveRoom(Type.DOUBLE, arrival, departure);
				result.add(ref);
				number--;
			}
		}

		return result;
	}

}