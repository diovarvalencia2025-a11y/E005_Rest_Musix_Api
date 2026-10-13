package com.ipartek.configuracion;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("imagenes");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        File uploadFolder = uploadDir.toFile();
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        registry.addResourceHandler("/imagenes/**", "/uploads/**")
                .addResourceLocations(
                        "file:" + uploadPath + "/",
                        "classpath:/static/imagenes/",
                        "file:src/main/resources/static/imagenes/"
                );
    }
}
