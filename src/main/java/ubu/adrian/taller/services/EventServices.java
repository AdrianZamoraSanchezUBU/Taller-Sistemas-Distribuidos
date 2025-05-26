package ubu.adrian.taller.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.model.User;
import ubu.adrian.taller.repository.EventRepository;

/**
 * Servicio para la gestión de eventos
 */
public interface EventServices {
	public Event findById(long id);
	public List <Event> findAll();
	public List<Event> findByFilters(List<Categories> categories, String capacity);
	public void addParticipantToEvent(Event event, User user);
	public void removeParticipantToEvent(Event event, User user);
	public void saveEvent(Event event);
	public void deleteById(long id);
	public List<Event> findByOwner(User owner);
	public List<Event> findEventsByParticipant(User user);
}
