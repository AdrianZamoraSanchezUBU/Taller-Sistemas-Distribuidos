package ubu.adrian.taller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ubu.adrian.taller.services.EventServices;
import ubu.adrian.taller.services.UserServices;
import ubu.adrian.taller.dto.NewEventDTO;
import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.model.EventCategory;
import ubu.adrian.taller.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador de las páginas relacionadas con la gestión de usuarios
 * 
 * @author Adrián Zamora Sánchez (azs1004@alu.ubu.es)
 */
@Controller
public class EventController {
	// Servicio de eventos
	@Autowired
	private EventServices eventServices;
	
	// Servicio de usuarios
	@Autowired
	private UserServices userServices;

    
	/**
	 * Constructor del controlador de usuarios
	 * 
	 * @param userServices servicio de usuarios
	 */
    public EventController(EventServices eventServices) {
        this.eventServices = eventServices;
    } 
    
    /**
	 * Gestiona las solicitudes de la ruta /create-event
	 * 
	 * @return página de creación de eventos
	 */
    @GetMapping("/event-list")
    public String eventList() {
        return "redirect:/";
    }

	/**
	 * Gestiona las solicitudes de la ruta /create-event
	 * 
	 * @return página de creación de eventos
	 */
    @GetMapping("/create-event")
    public String createEvent(Model model) {
        model.addAttribute("categories", Categories.values());
        return "createEvent";
    }
    
    @Value("${app.upload.dir:/app/uploads}")
    private String uploadDir;

    
    /**
     * Gestiona la creación de un nuevo evento
     * 
     * @return Redirección a la lista de eventos
     */
    @PostMapping("/new-event")
    public String newEvent(@ModelAttribute NewEventDTO eventForm,
                          RedirectAttributes redirectAttributes) throws IOException {
        
    	// Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Buscar usuario en BD
        User owner = userServices.findByUsername(username);
    	
        // Crear nueva entidad Event
        Event event = new Event();
        event.setTitulo(eventForm.getTitulo());
        event.setDescripcion(eventForm.getDescripcion());
        event.setFechaInicio(eventForm.getFechaInicio());
        event.setFechaFin(eventForm.getFechaFin());
        event.setLugar(eventForm.getLugar());
        event.setOwner(owner);
        
        // Manejar la imagen
        if (!eventForm.getImagen().isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + eventForm.getImagen().getOriginalFilename();
            
            // Ruta dentro del contenedor
            Path uploadPath = Paths.get(uploadDir + "/img/event/");
            Files.createDirectories(uploadPath);
            
            Path filePath = uploadPath.resolve(nombreArchivo);
            Files.copy(eventForm.getImagen().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            event.setImagen(nombreArchivo); // Solo nombre del archivo
        } else {
            event.setImagen("default-event.png"); // Imagen por defecto
        }
        
        // Convertir Strings a Categories
        List<Categories> categoryEnums = new ArrayList<>();
        if(eventForm.getCategories() != null) {
            for(String catStr : eventForm.getCategories()) {
                try {
                    Categories catEnum = Categories.valueOf(catStr);
                    categoryEnums.add(catEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("Categoria no valida: " + catStr);
                }
            }
        }
        
        // Crear EventCategory para cada enum
        List<EventCategory> eventCategories = categoryEnums.stream()
            .map(catEnum -> new EventCategory(catEnum, event))
            .collect(Collectors.toList());
        
        event.getCategories().addAll(eventCategories);
        
        // Guardar el evento (se guardan en cascada las categorías)
        eventServices.saveEvent(event);
        
        return "redirect:/event-list";
    }
}