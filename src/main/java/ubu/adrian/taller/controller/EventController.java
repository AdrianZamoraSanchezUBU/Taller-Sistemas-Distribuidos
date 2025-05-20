package ubu.adrian.taller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ubu.adrian.taller.services.EventServices;
import ubu.adrian.taller.services.UserServices;
import ubu.adrian.taller.dto.NewEventDTO;
import ubu.adrian.taller.model.Categories;
import ubu.adrian.taller.model.Event;
import ubu.adrian.taller.model.EventCategory;
import ubu.adrian.taller.model.User;
import ubu.adrian.taller.model.UserRol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
	 * Constructor del controlador de eventos
	 * 
	 * @param eventServices servicio de eventos
	 */
    public EventController(EventServices eventServices) {
        this.eventServices = eventServices;
    }
    
    /**
     * 
     */
    @GetMapping("/event/my-events")
    public String myEvents(Model model, Authentication authentication) {
        User user = userServices.findByUsername(authentication.getName());

        List<Event> events;

        if (user.getRol() == UserRol.ORGANIZADOR) {
            events = eventServices.findByOwner(user);
        } else if (user.getRol() == UserRol.PARTICIPANTE) {
            events = eventServices.findEventsByParticipant(user);
        } else {
            events = new ArrayList<>();
        }

        model.addAttribute("eventList", events);
        return "myEvents";
    }
    
    /**
     * Elimina un evento de este usuario
     * 
     * @return Al home o a la lista de eventos
     */
    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Event event = eventServices.findById(id);
        User user = userServices.findByUsername(authentication.getName());

        // Autorización: solo el creador o un ADMIN puede borrar
        if (event.getOwner().equals(user) || user.getRol() == UserRol.ADMIN) {
            eventServices.deleteById(id);
            return "redirect:/";
        }

        return "redirect:/event/my-events";
    }
    
    /**
     * Añade un participante al evento
     * 
     * @return Página de login en caso de necesitar login o información del evento
     */
    @PostMapping("/event/join/{id}")
    public String joinEvent(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userServices.findByUsername(username);
        Event event = eventServices.findById(id);

        if (event.getParticipants().contains(user)) {
            redirectAttributes.addFlashAttribute("message", "Ya estás inscrito en este evento.");
        } else {
            eventServices.addParticipantToEvent(event, user);
        }

        return "redirect:/event/info/" + id;
    }
    
    /**
     * Gestiona la página de visionado de un evento en la ruta
     * /event/info/id_evento
     * 
     * @return página de información del evento
     */
    @GetMapping("/event/info/{id}")
    public String eventInfo(@PathVariable long id, Model model, Authentication authentication){
    	
        Event event = eventServices.findById(id);
        
        // Si está logueado se obtiene su info, sino queda null
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            user = userServices.findByUsername(authentication.getName());
        }

        model.addAttribute("event", event);
        model.addAttribute("user", user);
    	
    	return "eventInfo";
	}
    
    /**
	 * Gestiona las solicitudes de la ruta /event/search
	 * 
	 * @return página de creación de eventos
	 */
    @GetMapping("/event/search")
    public String eventList(
    		@RequestParam(required = false) List<Categories> categories,
            @RequestParam(required = false) String capacity,
            Model model
    ) {
        List<Event> eventList;

        // Lógica de filtrado
        if (categories == null && (capacity == null || capacity.isEmpty())) {
            eventList = eventServices.findAll();
        } else {
            // Filtrar según los parámetros
        	eventList = eventServices.findByFilters(categories, capacity);
        }

        model.addAttribute("eventList", eventList);
        model.addAttribute("categories", Categories.values());

        // Para mantener el estado del filtro en la vista
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("selectedCapacity", capacity);

        return "eventList";
    }
    
    /**
	 * Gestiona las solicitudes de la ruta /event/filter
	 * 
	 * @return página de creación de eventos
	 */
    @GetMapping("/event/filter")
    public String eventFilter(Model model) {
        // Obtiene y añade la lista de eventos al modelo
        List<Event> eventList = eventServices.findAll();
        model.addAttribute("eventList", eventList);
        
        // Se añaden las categorías disponibles
        model.addAttribute("categories", Categories.values());
        
        return "eventList";
    }

	/**
	 * Gestiona las solicitudes de la ruta /event/create
	 * 
	 * @return página de creación de eventos
	 */
    @GetMapping("/event/create")
    public String createEvent(Model model) {
        model.addAttribute("categories", Categories.values());
        return "createEvent";
    }
    
    // Ubicación para ruta de almacenamiento de imágenes
    @Value("${app.upload.dir:/app/uploads}")
    private String uploadDir;
    
    /**
     * Gestiona la creación de un nuevo evento
     * 
     * @return Redirección a la lista de eventos
     */
    @PostMapping("/new-event")
    public String newEvent(@ModelAttribute NewEventDTO newEventDTO,
                          RedirectAttributes redirectAttributes) throws IOException {
        
    	// Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Buscar usuario en BD
        User owner = userServices.findByUsername(username);
    	
        // Crear nueva entidad Event
        Event event = new Event();
        event.setTitle(newEventDTO.getTitle());
        event.setDescription(newEventDTO.getDescription());
        event.setStartDate(newEventDTO.getStartDate());
        event.setEndDate(newEventDTO.getEndDate());
        event.setUbication(newEventDTO.getUbication());
        event.setOwner(owner);
        event.setMaxCapacity(newEventDTO.getMaxCapacity());
        
        // Manejar la imagen
        if (!newEventDTO.getImagen().isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + newEventDTO.getImagen().getOriginalFilename();
            
            // Ruta dentro del contenedor
            Path uploadPath = Paths.get(uploadDir + "/img/event/");
            Files.createDirectories(uploadPath);
            
            Path filePath = uploadPath.resolve(nombreArchivo);
            Files.copy(newEventDTO.getImagen().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            event.setImagen(nombreArchivo); // Solo nombre del archivo
        } else {
            event.setImagen("default-event.png"); // Imagen por defecto
        }
        
        // Convertir Strings a Categories
        List<Categories> categoryEnums = new ArrayList<>();
        if(newEventDTO.getCategories() != null) {
            for(String catStr : newEventDTO.getCategories()) {
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
        
        return "redirect:/event/search";
    }
}