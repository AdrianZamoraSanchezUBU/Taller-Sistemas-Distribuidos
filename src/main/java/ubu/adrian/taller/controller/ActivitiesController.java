package ubu.adrian.taller.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ubu.adrian.taller.model.Activity;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.model.User;
import ubu.adrian.taller.services.ActivityServices;
import ubu.adrian.taller.services.EventServices;
import ubu.adrian.taller.services.UserServices;
import ubu.adrian.taller.services.UserServicesImpl;

/**
 * Controlador la gestión de actividades de eventos
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class ActivitiesController {
	
	EventServices eventServices;
	ActivityServices activityServices;
	UserServicesImpl userServices;
	
	/**
	 * Constructor del controlador de actividades
	 * 
	 * @param eventServices servicio de eventos
	 * @param activityServices servicio de actividades
	 */
    public ActivitiesController(EventServices eventServices, ActivityServices activityServices, UserServicesImpl userServices) {
        this.eventServices = eventServices;
        this.activityServices = activityServices;
        this.userServices = userServices;
    }
	
	/**
	 * Gestiona la creacion de una actividad para un evento
	 * 
	 * @param eventId
	 * @param model
	 * @return
	 */
	@GetMapping("/activity/manage/{eventId}")
	public String activityList(@PathVariable long eventId, Model model) {
		Event event = eventServices.findById(eventId);
	    
		if (event == null) {
	        return "redirect:/event/list";
	    }

	    List<Activity> activities = event.getActivities();

	    // Se agregan los datos al modelo
	    model.addAttribute("event", event);
	    model.addAttribute("activities", activities);

	    return "activityList";
	}
	
	/**
	 * Gestiona la eliminación de una actividad
	 * 
	 * @param eventId
	 * @param model
	 * @return
	 */
	@GetMapping("/activity/delete/{id}")
	public String deleteActivity(@PathVariable("id") long activityId, @RequestParam("eventId") long eventId) {
	    Activity activity = activityServices.findById(activityId);

	    if (activity != null) {
	        activityServices.deleteActivity(activity);
	    }

	    // Redirige de nuevo a la gestión de actividades del evento
	    return "redirect:/activity/manage/" + eventId;
	}
	
	/**
	 * Gestiona la creacion de una actividad para un evento
	 * 
	 * @param eventId
	 * @param model
	 * @return
	 */
	@GetMapping("/activity/create/{eventId}")
	public String createActivityForm(@PathVariable long eventId, Model model) {
	    Event event = eventServices.findById(eventId);
	    if (event == null) {
	        return "redirect:/event/list"; // O error
	    }

	    Activity activity = new Activity();
	    activity.setEvent(event);

	    model.addAttribute("activity", activity);
	    model.addAttribute("event", event);
	    return "createActivity";
	}
	
	/**
	 * Gestiona la creacion de una actividad para un evento
	 * 
	 * @param eventId
	 * @param model
	 * @return
	 */
	@PostMapping("/activity/new-activity")
	public String saveActivity(@ModelAttribute Activity activity, 
			@RequestParam("eventId") long eventId,
			Authentication authentication) {
	    // Nos aseguramos de que el evento esté bien enlazado
	    Event event = eventServices.findById(eventId);
	    
	    // Validar autoría del evento
	    User user = userServices.findByUsername(authentication.getName());
	    if (event == null || !event.getOwner().equals(user)) {
	        return "redirect:/login";
	    }
	    
	    activity.setEvent(event);

	    activityServices.saveActivity(activity);

	    return "redirect:/event/info/" + event.getId();
	}
}