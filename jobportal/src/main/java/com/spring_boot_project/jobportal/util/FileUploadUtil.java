package com.spring_boot_project.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String filename, MultipartFile multipartFile) throws IOException { // multi-part file actually has the image file that the user uploaded from the form
        /* Paths.get(uploadDir);
         * Cnrl + alt + v*/
        Path uploadPath = Paths.get(uploadDir);

        // given directory exist if not, Create it
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        /* multipartFile.getInputStream();
         * Cnrl + alt + v
        InputStream inputStream = multipartFile.getInputStream();
        * and throw into try(){}
        */
        try (InputStream inputStream = multipartFile.getInputStream();) {
            /* uploadPath.resolve(filename);
             * Cnrl + alt + v*/
            Path path = uploadPath.resolve(filename);
            System.out.println("FilePath " + path);
            System.out.println("fileName " + filename);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + filename, ioe);
        }


    }
}
