package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired //annotation to let spring framework to inject an instance at runtime.
    // @Autowired annotation tells Spring picks up an object managed in the application context, and injects it into the current object.
    private UserService service;

    @GetMapping("/users") //handler method to handle the request to see the user listing page
    public String listAll(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers); //the listUsers obj will be available in the view
        return "users"; //return the logical view name that spring will resolve into physical view file
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
    public String saveUser(User user, RedirectAttributes redirectAttributes) { // reference redirectAttribute cuz after persisting the user obj into the db, we return a redirect view and we have an attribute avaiable in the redirected users view
        System.out.println(user);
        service.save(user);
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
}
