package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room {
	private static Logger logger = LoggerFactory.getLogger(Room.class);

	public static enum Type {
		SINGLE, DOUBLE
	}

	private final Hotel hotel;
	private final String number;
	private final Type type;
	private final Set<Booking> bookings = new HashSet<>();

	public Room(Hotel hotel, String number, Type type) {
		checkHotel(hotel);
		checkNumber(number, hotel);
		checkType(type);
		this.hotel = hotel;
		this.number = number;
		this.type = type;

		this.hotel.addRoom(this);
	}
	
	private void checkNumber(String number, Hotel hotel) {
		//Verify if the number already exists
		for(Room r : hotel.getRooms()){
			if((r.getNumber()).equals(number)) {
				throw new HotelException();
			}
		}
		//Verify if the number is an actual number
		try{  
			Integer.parseInt(number);  
		}  
		catch(NumberFormatException nfe){  
		    throw new HotelException();  
		}
	}
	
	private void checkHotel(Hotel hotel){
		//checks if hotel is not null
		if (hotel == null){
			throw new HotelException();
		}
	}
	
	private void checkType(Type type){
		//checks if type is not null
		if (type == null){
			throw new HotelException();
		}
	}

	Hotel getHotel() {
		return this.hotel;
	}

	String getNumber() {
		return this.number;
	}

	Type getType() {
		return this.type;
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.bookings) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (!isFree(type, arrival, departure)) {
			throw new HotelException();
		}

		Booking booking = new Booking(this.hotel, arrival, departure);
		this.bookings.add(booking);

		return booking;
	}

}
