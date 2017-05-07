package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelData {
	public static enum CopyDepth {
		SHALLOW, ROOMS
	};
	private String code;
	private String name;
	private List<RoomData> rooms = new ArrayList<RoomData>();
	public HotelData() {
	}

	public HotelData(Hotel hotel, CopyDepth depth) {
		this.setName(hotel.getName());
		this.setCode(hotel.getCode());
		
		
		switch (depth) {
		case ROOMS:
			for (Room room : hotel.getRoomSet()) {
				this.rooms.add(new RoomData(room, RoomData.CopyDepth.SHALLOW));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<RoomData> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<RoomData> rooms) {
		this.rooms = rooms;
	}
}
