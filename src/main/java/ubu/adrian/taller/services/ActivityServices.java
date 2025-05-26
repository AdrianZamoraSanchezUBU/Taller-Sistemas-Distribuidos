package ubu.adrian.taller.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ubu.adrian.taller.model.Activity;
import ubu.adrian.taller.repository.ActivityRepository;

/**
 * Servicio para la gestión de actividades
 */
@Service
public interface ActivityServices {
	public static final ActivityRepository activityRepository = null;
	
	public Activity findById(long id);
	public List <Activity> findAll();
	public void saveActivity(Activity activity);
	public void deleteActivity(Activity activity);
}
