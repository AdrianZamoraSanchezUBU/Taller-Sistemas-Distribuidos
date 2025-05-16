package ubu.adrian.taller.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad que modela la relación entre eventos y sus 
 * categorias asociadas
 */
@Entity
@Table(name = "events_categories")
public class EventCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /**
     * Constructor con categoría y evento
     * 
     * @param category categoria de la relacion
     * @param event    evento cuya categoria se guarda
     */
    public EventCategory(Categories category, Event event) {
        this.category = category;
        this.event = event;
    }
    
    /**
     * Constructor por defecto
     */
    public EventCategory() {}
    
    /**
     * Establece la categoria
     */
    public void setCategory(Categories category) {
    	this.category = category;
    }
    
    /**
     * Devuelve la categoria
     * 
     * @return category categoria de la relacion
     */
    public Categories getCategory() {
    	return category;
    }
    
    /**
     * Establece el evento asociado 
     */
    public void setEvent(Event event) {
    	this.event = event;
    }
    
    /**
     * Devuelve el evento asociado 
     * 
     * @return event Evento asociado a esta categoria
     */
    public Event getEvent() {
    	return event;
    }
}
