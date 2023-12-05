package com.EszterFocze.TCIGTB.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
        *** User photos handler ***
        In Spring MVC, addResourceHandler is a method used to define a handler for serving static resources such as
         CSS, JavaScript, images, etc. It is commonly used to configure the mapping of resource URLs to the actual locations
          of static resources in your project.
          addResourceHandler("/static/**"): This defines the URL pattern that will be used to access the static resources.
          In this case, any URL starting with "/static/" will be handled by this resource handler.
          * we can tell Spring MVC to map a given URL (e.g. "/user-photos") to any directory
          * (e.g. D:/ShopmeProject/ShopmeBackend/user-photos) and expose the resources (images, js, css...) in this directory
          * to the web clients (browsers) - that's the primary purpose of using resource handlers in Spring MVC.
         */
        //expose directory at the file system; to be accessible by the clients
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath(); //File toFile() - Returns a File object representing this path. This method is equivalent to returning a File object
        // constructed with the String representation of this path.
        registry.addResourceHandler("/" + dirName + "/**") // "/**" to allow all the files under this directory to be available to the web clients
                .addResourceLocations("file:" + userPhotosPath + "/"); //addResourceLocations() - to map this directory with the physical absolute path

        //for Windows OS due to different file scheme - .addResourceLocations("file:/" + userPhotosPath + "/")

    }
}