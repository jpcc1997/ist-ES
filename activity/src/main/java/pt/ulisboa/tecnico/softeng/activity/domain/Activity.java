package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Activity {
	private static int counter = 0;

	private final String name;
	private final String code;
	private final int minAge;
	private final int maxAge;
	private final int capacity;
	private final Set<ActivityOffer> offers = new HashSet<>();

	public Activity(ActivityProvider provider, String name, int minAge, int maxAge, int capacity) {
		checkProvider(provider);
		checkName(name);
		checkMinAge(minAge);
		checkMaxAge(maxAge);
		checkMinMaxAge(minAge, maxAge);
		checkCapacity(capacity);
		
		this.code = provider.getCode() + Integer.toString(++Activity.counter);
		this.name = name;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.capacity = capacity;

		provider.addActivity(this);
	}

	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getMinAge() {
		return this.minAge;
	}

	int getMaxAge() {
		return this.maxAge;
	}

	int getCapacity() {
		return this.capacity;
	}

	int getNumberOfOffers() {
		return this.offers.size();
	}

	void addOffer(ActivityOffer offer) {
		this.offers.add(offer);
	}

	Set<ActivityOffer> getOffers(LocalDate begin, LocalDate end, int age) {
		Set<ActivityOffer> result = new HashSet<>();
		for (ActivityOffer offer : this.offers) {
			if (matchAge(age) && offer.available(begin, end)) {
				result.add(offer);
			}
		}
		return result;
	}

	boolean matchAge(int age) {
		return age >= this.minAge && age <= this.maxAge && age >= 18 && age <100;
	}
	
	void checkProvider(ActivityProvider provider) {
		if (provider == null)
			throw new ActivityException();
	}
	
	void checkName(String name) {
		if ((name == null) || (name.trim().length() == 0))
			throw new ActivityException();
	}
	
	void checkMinAge(int minAge) {
		if (minAge < 18)
			throw new ActivityException();
	}
	
	void checkMaxAge(int maxAge) {
		if (maxAge >= 100)
			throw new ActivityException();
	}
	
	void checkMinMaxAge(int minAge, int maxAge) {
		if (minAge>maxAge)
			throw new ActivityException();
	}
	
	void checkCapacity(int capacity) {
		if (capacity<1)
			throw new ActivityException();
	}
	

}
