package ubu.adrian.taller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.EventCategory;
import ubu.adrian.taller.model.User;

public class NewEventDTO {
    private String titulo;
    private String descripcion;
    private MultipartFile imagen;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String lugar;
    private User owner;
    private List<String> categories = new ArrayList<>();
    
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public MultipartFile getImagen() {
		return imagen;
	}
	public void setImagen(MultipartFile imagen) {
		this.imagen = imagen;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) { 
		this.categories = categories; 
	}
}
