package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;

@Controller
@RequestMapping(value = "/activityProviders/{activityProviderCode}/activities/{activityCode}/activityOffers")
public class ActivityOfferController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(method = RequestMethod.GET) 
	public String showActivityOffers(Model model, @PathVariable String activityProviderCode, @PathVariable String activityCode) {
		logger.info("showActivityOffers activityProviderCode:{} activityCode:{}", activityProviderCode, activityCode);
		
		ActivityProviderData activityProviderData = ActivityInterface.getActivityProviderDataByCode(activityProviderCode, CopyDepth.ACTIVITIES);
		ActivityData activityData = ActivityInterface.getActivityDataByCode(activityCode, activityProviderCode, ActivityData.CopyDepth.ACTIVITYOFFERS);
		
		if (activityProviderData == null) {
			model.addAttribute("error", "Error: it does not exist an activityProvider with the code " + activityProviderCode);
			model.addAttribute("activityProvider", new ActivityProviderData());
			model.addAttribute("activityProviders", ActivityInterface.getActivityProviders());
			return "activityProviders";
		} else if (activityData == null) {
			model.addAttribute("error", "Error: it does not exist an activity with the code " + activityCode);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("activityProvider", activityProviderData);
			return "activity";
		} else {
			model.addAttribute("activityOffer", new ActivityOfferData());
			model.addAttribute("activity", activityData);
			model.addAttribute("activityProvider", activityProviderData);
			return "activityOffers";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String activityOfferSubmit(Model model, @PathVariable String activityProviderCode, @PathVariable String activityCode, 
			@ModelAttribute ActivityOfferData activityOfferData){
		logger.info("activityOfferSubmit activityCode:{}", activityCode);
		
		try {
			ActivityInterface.createActivityOffer(activityProviderCode, activityCode, activityOfferData);
		} catch (ActivityException e) {
			model.addAttribute("error", "Error: it was not possible to create the activity offer");
			model.addAttribute("activityOffer", activityOfferData);
			model.addAttribute("activityProvider", ActivityInterface.getActivityProviderDataByCode(activityProviderCode, CopyDepth.ACTIVITIES));
			model.addAttribute("activity", ActivityInterface.getActivityDataByCode(activityCode, activityProviderCode, ActivityData.CopyDepth.ACTIVITYOFFERS));
			return "activityOffers";
		}

		return "redirect:/activityProviders/" + activityProviderCode + "/activities/" + activityCode + "/activityOffers";
	}
 
}