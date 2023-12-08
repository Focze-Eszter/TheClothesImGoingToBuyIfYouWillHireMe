package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional /* Consider using @Transactional in Service classes to ensure data integrity.
It can be use at class level or method level.
You must use @Transactional in Service class if you execute an update query in repository class.
 Note that all methods declared in repository classes are transactional by default.
*/
public class UserService {

    public static final int USERS_PER_PAGE = 4;

    @Autowired //to let Spring to inject an instance at runtime
    private UserRepository userRepo; //reference to the UserRepository

    @Autowired
    private RoleRepository roleRepo;

    @Autowired //use the password encoder in the Spring Security configuration class
    private PasswordEncoder passwordEncoder;

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(); //repo.findAll() is an Iterable
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort); // page num is 0 based, but in the method it starts from 1;
        return userRepo.findAll(pageable);
    }
    /*pagination & sorting on the server side is the best practice as it performs fast: only a small amount of data (a page) is returned to the client (web browser).
    Whereas with pagination on client side, the web browser is loaded with huge amount of data, but showing only a few to users (a page) - thus it is not efficient.*/



    public List<Role> listRoles() { //method that will return a list of Role objects from the db
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        //encodePassword(user); //before persisting the user into the db, we encode the password
        boolean isUpdatingUser = (user.getId() != null); //is in updating mode
        if (isUpdatingUser) { //updating mode; need to retrieve the user from the db to check the password
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                //check if the password on the form is empty or not
                user.setPassword(existingUser.getPassword());
            } else { // if the password from the user form is not empty, we encode the user password
                encodePassword(user);
            }
        } else { //new mode
            encodePassword(user); //before persisting the user into the db, we encode the password
        }
        return userRepo.save(user);
    } //the user is being edited with new password or not

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepo.updateEnabledStatus(id, enabled);//delegate the call to the user Repo updateEnabledStatus() method
    }

    private void encodePassword(User user) { //encode the user password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);//update the user password with the encoded value
    }

    public boolean isEmailUnique(Integer id, String email) {//check the uniqueness of the email when creating a new user
        User userByEmail = userRepo.getUserByEmail(email); //we retrieve an user object based on the email
        if(userByEmail == null) return true; //no user is found with such email, email unique in the db
        boolean isCreatingNew = (id == null); //the form is in new mode
        if (isCreatingNew) { //the id is not nul, the user is existing and edited
            if(userByEmail != null) return false;//there is another user with the same email, the uniqueness of the email is false
        } else { //edit user mode
            if(userByEmail.getId() != id) { //if the id of the userByEmail is different than the passed id from the method parameter (user being edited), the email is not unique
                return false; //the email is not unique
            }
        }
        return true; //true -> email is unique; null => no existing user in the db is having this email
    } //an existing user is edited

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get(); //findById is an optional; get can throw a no such element exception
        } catch(NoSuchElementException e) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id); //countById = findById
        if (countById == null || countById == 0) { //no user exists in the db; countById because returns a long, findById would return a User obj
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepo.deleteById(id);
    }
}
