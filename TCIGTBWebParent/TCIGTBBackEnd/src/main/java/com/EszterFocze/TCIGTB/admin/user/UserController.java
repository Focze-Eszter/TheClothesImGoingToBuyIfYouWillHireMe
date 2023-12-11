package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.admin.FileUploadUtil;
import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired //annotation to let spring framework to inject an instance at runtime.
    // @Autowired annotation tells Spring picks up an object managed in the application context, and injects it into the current object.
    private UserService service;

    @GetMapping("/users") //handler method to handle the request to see the user listing page
    public String listFirstPage(Model model) {
        /*List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers); //the listUsers obj will be available in the view
        return "users"; //return the logical view name that spring will resolve into physical view file */
        return listByPage(1, model, "firstName", "asc", null); //firstName = field in the entity class, not in the db
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, String sortField, String sortDir, String keyword) {
        //have reference to Spring MVC model; @RequestParam requires the parameters to be present    System.out.println("Sort field - " + sortField);
        System.out.println("Sort order - " + sortDir);
        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE -1;
        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword); //send the keyword to the view

        return "users";
       }

    @GetMapping("/users/new") //admin form
    public String newUser(Model model) { //model - put the User obj into the form  - the roles
        List<Role> listRoles = service.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user); //form -> db
        model.addAttribute("listRoles", listRoles); //need the roles info in the form for admin to check one; db -> form
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("users/save") //method triggered by the save button in the form => <form th:action="@{/users/save}" method="post"
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException, IOException { // reference redirectAttribute cuz after persisting the user obj into the db, we return a redirect view and we have an attribute available in the redirected users view
        //the image attribute from the @RequestParam(image) is taken from <img name="image"/>
        //MultipartFile interface - is used in conjunction with the Spring MVC framework to handle file uploads in a web application.
        //When a user submits a form with a file input, the associated file is sent as part of the HTTP request. Provides methods to access the contents of the file, its metadata, and other information. This abstraction allows developers to handle file uploads easily in a Spring-based web application.
        if(!multipartFile.isEmpty()) {//the form sent a file or not? The form has an upload file
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());//it is recommended to use the StringUtils class from Spring to to clean the path
            //Path Normalization: Problem: Paths can be specified in different formats or may contain redundant elements (e.g., multiple slashes, dot segments like ./ or ../).
            //Solution: cleanPath can normalize paths, resolving dot segments and removing any unnecessary elements to create a clean and standardized path representation.
            user.setPhotos(fileName); //set the user field with the photo name
            User savedUser = service.save(user);
            //from the persisted User obj, use the id to create the folder, User ID directory to store photos
            String uploadDir = "user-photos/" + savedUser.getId();
            System.out.println(uploadDir + " - uploadDir");
            FileUploadUtil.cleanDir(uploadDir); //delete old user photo
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else { //the form hasn't any file upload
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }
        redirectAttributes.addFlashAttribute("message", "The user have been saved successfully");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}") //handler method for editing the user; {id} - placeholder for the id from the url
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model, //reference to the model
                           RedirectAttributes redirectAttributes) { //map the value of {id] from the url; redirect attribute for sending the message
        try { //try, catch from the original method in service
            User user = service.get(id); //retrieve user obj by id
            List<Role> listRoles = service.listRoles();
            model.addAttribute("user", user); //user obj is found so we set it on the model, Send it to html
            model.addAttribute("pageTitle", "Edit user " + "(ID: " + id + ")");
            model.addAttribute("listRoles", listRoles); //need the roles info in the form; db -> form

            return "user_form"; //return the logical name of the view user_form
        } catch(UserNotFoundException ex) { // this exception will be shown in the form
            redirectAttributes.addFlashAttribute("message", ex.getMessage()); //getMessage from the catch in the service: throw new UserNotFoundException("Could not find any user with ID " + id);
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                             Model model, //reference to the model
                             RedirectAttributes redirectAttributes) {

        try { //try, catch from the original method in service
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user with the ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException ex) { // this exception will be shown in the form
            redirectAttributes.addFlashAttribute("message", ex.getMessage()); //getMessage from the catch in the service: throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        return "redirect:/users"; //in either case we return this redirect
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        //redirectAttributes -send message to the view
        service.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user with the ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }

    //@Param is used in queries of repository interfaces.
    //@RequestParam annotation is optional if the data type is String or Integer. You can just declare parameters like this:
    //public void handlerMethod(String sortField, String sortDir) { ... }
}
