package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;


import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

public class ActivityProviderData {
	public static enum CopyDepth {
		SHALLOW, ACTIVITIES
	};
	private String code;
	private String name;
	//private List<ActivityData> activities = new ArrayList<ActivityData>();
	public ActivityProviderData() {
	}

	public ActivityProviderData(ActivityProvider ap,CopyDepth depth) {
		this.setCode(ap.getCode());
		this.setName(ap.getName());
		
		
		
		switch (depth) {
		// TODO Implement ActivityData dataobject class
		/*case ACTIVITIES:
			for (Activity activity : ap.getActivitySet()) {
				this.activities.add(new ActivityData(activity));
			}
			break;
		*/
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

	// TODO Implement ActivityData dataobject class
	/*public List<ActivityData> getActivities() {
		return this.activities;
	}
	
	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}*/
}