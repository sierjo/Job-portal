package com.spring_boot_project.jobportal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {/*Этот класс конфигурации сопоставит запрос из
                                                /photos с файлами сервера из каталога в нашей файловой системе
                                                This configuration class will
                                                 map request from /photos to server files from a directory on our file system*/

    private static final String UPLOAD_DIR = "photos";

    // implements method by Ctrl + o
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { /* Override the default
                        implement method and set up a customize resource handler*/
        exposeDirectory(UPLOAD_DIR, registry);
    }

    /*exposeDirectory -> Converts the uploadDir string to a Path
    Maps requests starting with
    "/photos/"*"
    to a file system location
    file:<absolute path to photos directory>
    The " will match on all sub-directories*/
    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadDir);
        registry.addResourceHandler("/" + uploadDir + "/**").addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }
}
