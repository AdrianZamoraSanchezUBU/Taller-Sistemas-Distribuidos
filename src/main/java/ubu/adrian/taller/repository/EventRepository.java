package ubu.adrian.taller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.model.User;

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
    
    List<Event> findByOwner(User owner);

    List<Event> findByParticipantsContaining(User participants);

    @Query("SELECT DISTINCT e FROM Event e " +
    	       "JOIN e.categories c " +
    	       "WHERE c.category IN :categories " +
    	       "AND e.numParticipants >= e.maxCapacity")
    	List<Event> findByCategoriesAndFull(@Param("categories") List<Categories> categories);

    @Query("SELECT DISTINCT e FROM Event e " +
    	       "JOIN e.categories c " +
    	       "WHERE c.category IN :categories " +
    	       "AND e.numParticipants < e.maxCapacity")
	List<Event> findByCategoriesAndAvailable(@Param("categories") List<Categories> categories);



     @Query("SELECT e FROM Event e " +
            "WHERE e.numParticipants >= e.maxCapacity")
     List<Event> findFull();
  

     @Query("SELECT e FROM Event e " +
            "WHERE e.numParticipants < e.maxCapacity")
     List<Event> findAvailable();

     @Query("SELECT DISTINCT e FROM Event e " +
    	       "JOIN e.categories c " +
    	       "WHERE c.category IN :categories")
	List<Event> findByCategories(@Param("categories") List<Categories> categories);

}