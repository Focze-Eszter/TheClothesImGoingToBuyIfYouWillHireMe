package com.EszterFocze.TCIGTB.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {//utility class, so the method is static
        Path uploadPath = Paths.get(uploadDir); //create a directory, pass directory from the uploadDirectory;
        // Path - An object that may be used to locate a file in a file system. It will represent a system dependent file path.
        //Paths - This class consists exclusively of static methods that return a Path by converting a path string or URI.
        //static Path get(URI uri) - Converts a path string, or a sequence of strings that when joined form a path string, to a Path.
        //MultipartFile - A representation of an uploaded file received in a multipart request.
        //The file contents are either stored in memory or temporarily on disk. In either case, the user is responsible for copying file contents to a session-level
        // or persistent store as and if desired.
        //The temporary storage will be cleared at the end of request processing.
        if (!Files.exists(uploadPath)) { //if the upload path doesn't exist, then create the directory
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) { // try with resources statement; create a path for the file path, uploadPath
            Path filePath = uploadPath.resolve(fileName); // path of the file is relative to the upload directory, fileName(passed from the method argument
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING); //overrides the existing files with the same name
        } catch (IOException ex) {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

    public static void cleanDir(String dir) { // if you update the user photo, the old one wil be deleted so it will remain only one user photo in the dir
        Path dirPath = Paths.get(dir);
        try {
            //Files utility class - list all the subdirectories and files in the given directory
            Files.list(dirPath).forEach(file -> {
                if(!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch(IOException ex) {
                        System.out.println("Could not delete file " + file);
                    }
                }
            });
        } catch (IOException ex) {
            System.out.println("Could not list directory: " + dirPath);
        }
    }
}
