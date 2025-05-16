package ubu.adrian.taller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // Recursos estáticos
        registry.addResourceHandler("/static/**")
                .addResourceLocations(
                    "classpath:/app/static");
        
        registry.addResourceHandler("/img/event/**")
        	.addResourceLocations("file:/app/uploads/img/event/");
    }
}