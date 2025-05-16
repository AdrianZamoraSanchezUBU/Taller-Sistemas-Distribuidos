package ubu.adrian.taller.config;

import ubu.adrian.taller.model.User;
import ubu.adrian.taller.model.UserRol;
import ubu.adrian.taller.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Clase encargada de generar al usuario admin cuando se ejecura la app
 * 
 * Operación idempotente, una vez creado no hace nada, por más veces que se ejecute
 */
@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${app.upload.dir:/app/uploads}")
    private String uploadDir;

    /**
     * Ejecuta una comprobación de la existencia de un usuario administrador
     * en caso de no haber uno lo crea. En el caso contrario no hace nada.
     * 
     * @param args Argumentos de ejecución
     */
    @Override
    public void run(String... args) {
        // Obtiene al admin
    	User admin = userRepository.findByUsername("admin");
    	
    	// Comprueba si hay admin
    	if (admin == null) {
    		// Se genera al admin
            admin = new User();
            
            // Datos del admin
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRol(UserRol.ADMIN);
            
            userRepository.save(admin);
            System.out.println("Usuario \"admin\" operativo");
        }
    	
    	// Copia la imagen por defecto de eventos si no existe 
    	// (no lo conseguia hacer con docker y asi funciona...)
        try {
        	Path uploadPath = Paths.get(uploadDir + "/img/event/");
            Files.createDirectories(uploadPath);

            Path destination = uploadPath.resolve("default-event.png");
            Path source = Paths.get("/app/assets/default-event.png");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error al copiar la imagen default-event.png: " + e.getMessage());
        }
    }
    
}
