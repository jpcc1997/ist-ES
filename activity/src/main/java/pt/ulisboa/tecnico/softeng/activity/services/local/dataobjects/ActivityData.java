package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

public class ActivityData {
	
	public static enum CopyDepth {
		SHALLOW, ACTIVITYOFFERS
	};
	
	private String code;
	private String name;
	private int minAge;
	private int maxAge;
	private int capacity;
	
	private List<ActivityOfferData> activityOffers = new ArrayList<ActivityOfferData>();

	public ActivityData() {
	}

	public ActivityData(Activity activity, CopyDepth depth) {
		this.setCode(activity.getCode());
		this.setName(activity.getName());
		this.setCode(activity.getCode());
		this.setMinAge(activity.getMinAge());
		this.setMaxAge(activity.getMaxAge());
		this.setCapacity(activity.getCapacity());
		
		switch (depth) {
		case ACTIVITYOFFERS:
			for (ActivityOffer activityOffer : activity.getActivityOfferSet()) {
				this.activityOffers.add(new ActivityOfferData(activityOffer, ActivityOfferData.CopyDepth.BOOKINGS));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public List<ActivityOfferData> getActivityOffers() {
		return activityOffers;
	}
	
	public void setActivityOffers(List<ActivityOfferData> activityOffers) {
		this.activityOffers = activityOffers;
	}

	
}