package org.htw.student.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry denisKunzRegistry) {
        // sachen aus dem Upload Ordner sind zugänglich
        denisKunzRegistry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
