package ubu.adrian.taller.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import ubu.adrian.taller.model.User;

public class NewEventDTO {
    private String title;
    private String description;
    private MultipartFile imagen;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ubication;
    private User owner;
    private List<String> categories = new ArrayList<>();
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public MultipartFile getImagen() {
		return imagen;
	}
	public void setImagen(MultipartFile imagen) {
		this.imagen = imagen;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getUbication() {
		return ubication;
	}
	public void setUbication(String ubication) {
		this.ubication = ubication;
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
