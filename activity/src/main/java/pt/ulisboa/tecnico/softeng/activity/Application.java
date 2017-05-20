package pt.ulisboa.tecnico.softeng.activity;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;

@SpringBootApplication
public class Application implements InitializingBean {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Atomic
	@Override
	public void afterPropertiesSet() throws Exception {
		ActivityProvider provider = FenixFramework.getDomainRoot().getActivityProviderSet().stream()
				.filter(b -> b.getCode().equals("AP1234")).findFirst().orElse(null);
		if (provider == null) {
			provider = new ActivityProvider("AP1234", "XPTO");
		}
		Activity activity = new Activity(provider, "CCCC", 18, 99, 2000);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate dt1 = dtf.parseLocalDate("2017-01-20");
		LocalDate dt2 = dtf.parseLocalDate("2017-01-21");
		//LocalDate today = new LocalDate();
		//LocalDate tomorrow = today.plusDays(1);
		new ActivityOffer(activity, dt1, dt1);
		new ActivityOffer(activity, dt1, dt2);
	}
}