package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;


public class ActivityData {
	
	public static enum CopyDepth {
		SHALLOW, ACTIVITYOFFERS
	};
	
	private ActivityProvider activityProvider;
	private String code;
	private String name;
	private int minAge;
	private int maxAge;
	private int capacity;
	
	//TODO private List<ActivityOfferData> activityOffers = new ArrayList<ActivityOfferData>();

	public ActivityData() {
	}

	public ActivityData(ActivityProvider provider, Activity activity, CopyDepth depth) {
		this.setActivityProvider(activity.getActivityProvider());
		this.setCode(activity.getCode());
		this.setName(activity.getName());
		this.setCode(activity.getCode());
		this.setMinAge(activity.getMinAge());
		this.setMaxAge(activity.getMaxAge());
		this.setCapacity(activity.getCapacity());
		
		switch (depth) {
		/*TODO case ACTIVITYOFFERS:
			for (ActivityOffer activityOffer : activity.getActivityOfferSet()) {
				this.activityOffers.add(new ActivityOfferData(activity, activityOffer, ActivityOfferData.CopyDepth.SHALLOW));
			}
			break;*/
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public ActivityProvider getActivityProvider() {
		return activityProvider;
	}

	public void setActivityProvider(ActivityProvider activityProvider) {
		this.activityProvider = activityProvider;
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
	
	// TODO Implement ActivityOffer dataobject class
		/*public List<ActivityOffer> getActivityOffers() {
			return activityOffers;
		}
		
		public void setActivityOffers(List<ActivityOffer> activityOffers) {
			this.activityOffers = activityOffers;
		}*/

	
}
