package ubu.adrian.taller.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.repository.EventRepository;

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
    public List <Event> obtenerTodosLosEventos() {
        return eventRepository.findAll();
    }
    
    /**
	 * Guarda al evento en la base de datos
	 * 
	 * @param event Evento que se quiere guardar
	 */
    public void saveEvent(Event event) {
    	eventRepository.save(event);
    };
}
