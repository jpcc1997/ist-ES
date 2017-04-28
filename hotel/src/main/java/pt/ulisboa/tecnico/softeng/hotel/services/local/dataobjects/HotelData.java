package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	public static enum CopyDepth {
		SHALLOW, ROOMS
	};
	private String code;
	private String name;
	// TODO private List<String> rooms = new ArrayList<String>();
	public HotelData() {
	}

	public HotelData(Hotel hotel, CopyDepth depth) {
		this.setName(hotel.getName());
		this.setCode(hotel.getCode());
		
		
		switch (depth) {
		// TODO Implement RoomData dataobject class
		/*case ROOMS:
			for (Room room : hotel.getRoomSet()) {
				this.rooms.add(new RoomData(room));
			}
			break;*/
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

	// TODO Implement RoomData dataobject class
	/*public List<RoomData> getRooms() {
		return rooms;
	}
	
	public void setRooms(List<String> rooms) {
		this.rooms = rooms;
	}*/
}
