package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;

@Controller
@RequestMapping(value = "/hotels/{hotelCode}/rooms/{roomNr}/bookings")
public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String showBookings(Model model, @PathVariable String hotelCode, @PathVariable String roomNr) {
		logger.info("showBookings hotel code:{}, room code:{}", hotelCode, roomNr);

		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode, CopyDepth.ROOMS);
				
		RoomData roomData = HotelInterface.getRoomDataByNr(hotelCode, roomNr, RoomData.CopyDepth.BOOKINGS);
		

		if (hotelData == null) {
			model.addAttribute("error", "Error: it does not exist a hotel with the code " + hotelCode);
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());
			return "hotels";
		}
		else if (roomData == null) {
			model.addAttribute("error", "Error: it does not exist a room with the nr " + roomNr);
			model.addAttribute("hotel", hotelData);
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms(hotelCode));
			return "rooms";
		}
		else {
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("room", roomData);
			model.addAttribute("hotel", hotelData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitRoom(Model model, @PathVariable String hotelCode, @PathVariable String roomNr, @ModelAttribute RoomBookingData bookingData) {
		logger.info("cenas");
		logger.info("submitRoom arrival:{}, departure:{}", bookingData.getArrival() , bookingData.getDeparture());
	
		try {
			logger.info("fez cenas 2");
			HotelInterface.createBooking(hotelCode, roomNr, bookingData);
		} catch (HotelException he) {
			logger.info("deu erro");
			model.addAttribute("error", "Error: it was not possible to reserve the room");
			model.addAttribute("booking", bookingData);
			model.addAttribute("room", HotelInterface.getRoomDataByNr(hotelCode, roomNr, RoomData.CopyDepth.BOOKINGS));
			model.addAttribute("hotel", HotelInterface.getHotelDataByCode(hotelCode, CopyDepth.ROOMS));
			return "bookings";
		}

		return "redirect:/hotels/" + hotelCode + "/rooms/" + roomNr + "/bookings";
	}

}