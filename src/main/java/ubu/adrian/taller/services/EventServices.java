package ubu.adrian.taller.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.repository.EventRepository;

/**
 * Servicio para la gestión de eventos
 */
@Service
public class EventServices {

	@Autowired
	private EventRepository eventRepository;

    // Inyección del repositorio a través del constructor
    public EventServices(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    /**
	 * Devuelve una lista de todos los eventos
	 * 
	 * @return List<eventos> Lista de todos los eventos
	 */
    public List <Event> findAll() {
        return eventRepository.findAll();
    }
    
    /**
	 * Guarda al evento en la base de datos
	 * 
	 * @param event Evento que se quiere guardar
	 */
    public void saveEvent(Event event) {
    	eventRepository.save(event);
    }

    /**
     * Devuelve los eventos tras aplicarle unos filtros
     * 
     * @param category Categorías que se quiere obtener
     * @param capacity Disponibilidad de plazas
     * @return Eventos que cumplen con los filtros
     */
	public List<Event> findByFilters(List<Categories> categories, String capacity) {
		if (categories == null && capacity == null) {
	        return eventRepository.findAll();
	    }

	    if (categories != null && "FULL".equals(capacity)) {
	        return eventRepository.findByCategoriesAndFull(categories);
	    } else if (categories != null && "AVAILABLE".equals(capacity)) {
	        return eventRepository.findByCategoriesAndAvailable(categories);
	    } else if (categories != null) {
	        return eventRepository.findByCategories(categories);
	    } else if ("FULL".equals(capacity)) {
	        return eventRepository.findFull();
	    } else if ("AVAILABLE".equals(capacity)) {
	        return eventRepository.findAvailable();
	    }

	    return eventRepository.findAll();
	};
}
