package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired //to let Spring to inject an instance at runtime
    private UserRepository userRepo; //reference to the UserRepository

    @Autowired
    private RoleRepository roleRepo;

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(); //repo.findAll() is an Iterable
    }

    public List<Role> listRoles() { //method that will return a list of Role objects from the db
        return (List<Role>) roleRepo.findAll();
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
