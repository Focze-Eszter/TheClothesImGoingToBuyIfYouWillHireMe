package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired //annotation to let spring framework to inject an instance at runtime
    private UserService service;

    @GetMapping("/users") //handler method to handle the request to see the user listing page
    public String listAll(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers", listUsers); //the listUsers obj will be avaiable in the view
        return "users"; //return the logical view name that spring will resolve into physical view file
    }

    @GetMapping("/users/new") //admin form
    public String newUser(Model model) { //model - put the User obj into the form
        List<Role> listRoles = service.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles); //need the roles info in the form for admin to check one
        return "user_form";
    }

    @PostMapping("users/save") //method triggered by the save button in the form => <form th:action="@{/users/save}" method="post"
    public String saveUser(User user, RedirectAttributes redirectAttributes) { // reference redirectAttribute cuz after persisting the user obj into the db, we return a redirect view and we have an attribute avaiable in the redirected users view
        System.out.println(user);
        service.save(user);
        redirectAttributes.addFlashAttribute("message", "The user have been saved successfully");
        return "redirect:/users";
    }
}
