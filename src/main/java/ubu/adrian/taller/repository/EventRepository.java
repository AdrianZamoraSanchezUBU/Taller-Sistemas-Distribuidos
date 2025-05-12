package ubu.adrian.taller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ubu.adrian.taller.model.Event;

/**
 * Repositorio de la entidad User
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	
	/**
     * Busca un evento por su ID
     * 
     * @param eventID ID del evento
     * @return Event encontrado o null
     */
    public Event findById(long eventID);
}