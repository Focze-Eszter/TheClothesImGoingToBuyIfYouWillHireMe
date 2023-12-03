package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired //to let Spring to inject an instance at runtime
    private UserRepository userRepo; //reference to the UserRepository

    @Autowired
    private RoleRepository roleRepo;

    @Autowired //use the password encoder in the Spring Security configuration class
    private PasswordEncoder passwordEncoder;

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(); //repo.findAll() is an Iterable
    }

    public List<Role> listRoles() { //method that will return a list of Role objects from the db
        return (List<Role>) roleRepo.findAll();
    }

    public void save(User user) {
        encodePassword(user); //before persisting the user into the db, we encode the password
        userRepo.save(user);
    }

    private void encodePassword(User user) { //encode the user password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);//update the user password with the encoded value
    }

    public boolean isEmailUnique(String email) {//check the uniqueness of the email when creating a new user
        User userByEmail = userRepo.getUserByEmail(email); //we retrieve an user object based on the email
        return userByEmail == null; //true -> email is unique; null => no existing user in the db is having this email
    }
}
