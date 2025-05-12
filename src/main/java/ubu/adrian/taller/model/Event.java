package ubu.adrian.taller.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad que modela un evento
 */
@Entity
@Table(name = "events")
public class Event {
	// ID (único)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	/**
     * Título del evento
     */
    @Column(nullable = false)
    private String titulo;
    
    /**
     * Descripción del evento
     */
    @Column(length = 1000)
    private String descripcion;

    /**
     * Fecha de inicio del evento
     */
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /**
     * Fecha de finalización del evento
     */
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    /**
     * Lugar donde se celebra el evento
     */
    @Column(nullable = false)
    private String lugar;
    
    /**
     * Imagen del evento
     */
    @Column(name = "imagen", nullable = false)
    private String imagen;

    /**
     * Devuelve el ID del evento
     * 
     * @return ID del evento
     */
    public long getId() {
        return id;
    }

    /**
     * Establece el ID del evento
     * 
     * @param id ID del evento
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Devuelve el título del evento
     * 
     * @return Título del evento
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del evento
     * 
     * @param titulo Título del evento
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Devuelve la descripción del evento
     * 
     * @return Descripción del evento
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del evento
     * 
     * @param descripcion Descripción del evento
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la fecha de inicio del evento
     * 
     * @return Fecha de inicio
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del evento
     * 
     * @param fechaInicio Fecha de inicio
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Devuelve la fecha de finalización del evento
     * 
     * @return Fecha de fin
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de finalización del evento
     * 
     * @param fechaFin Fecha de fin
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Devuelve el lugar del evento
     * 
     * @return Lugar del evento
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Establece el lugar del evento
     * 
     * @param lugar Lugar del evento
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
    
    /**
     * Devuelve la ruta a la imagen del evento
     * 
     * @return imagen Ruta de la imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen asociada al evento
     * 
     * @param imagen Ruta a la imagen del evento
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    /**
     * Tras la subida de la imagen establece una por defecto
     */
    @PostLoad
    private void asignarImagenPorDefecto() {
        if (this.imagen == null || this.imagen.isBlank()) {
            this.imagen = "default-event.png";
        }
    }

}
