package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.Role;
import com.EszterFocze.TCIGTB.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //because Spring Data JPA runs the test against an in-memory database
@Rollback(false) //to tell jpa to commit the changes after running the test
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager; //provided by spring data jpa for unit testing with repository

    @Test
    public void testCreateNewUserWithOneRole() {
        User userEva = new User("ema.b@gmail.com", "royal", "Emma", "Beatles");
        Role roleAdmin = entityManager.find(Role.class, 1); //to get a specific role from the database; class, id; 1 => admin
        userEva.addRole(roleAdmin);

        User savedUser = repo.save(userEva); //returns a persisted object; assign the saved obj to a new object to use the assertion statement
        assertThat(savedUser.getId()).isGreaterThan(0); //so this obj is a persisting obj
    }


    @Test
    public void testCreateNewUserWithTwoRoles() {
        User rioUser = new User("rio@yahoo.com", "08121990", "Ryunosuke", "Berserk");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        rioUser.addRole(roleEditor);
        rioUser.addRole(roleAssistant);

        User savedUser = repo.save(rioUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void getUserById() {
        User userOne = repo.findById(1).get(); //get() because repo.findById(1) is an optional type
        System.out.println(userOne);
        assertThat(userOne).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userOne = repo.findById(1).get();
        userOne.setEnabled(true); //by default is false in the db
        userOne.setEmail("emma_beatles@gmail.com");
        repo.save(userOne);
    }

    @Test
    public void testUpdateUserRoles() {
        User userTwo = repo.findById(1).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);
        userTwo.getRoles().remove(roleEditor);
        userTwo.addRole(roleSalesperson);

        repo.save(userTwo);
    }

    @Test
    public void deleteUserById() {
        Integer userId = 1;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "rio@yahoo.com";
        User user = repo.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 2;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 2;
        repo.updateEnabledStatus(1, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 2;
        repo.updateEnabledStatus(1, true);
    }

    @Test
    public void showFirstPage() {
        int pageNumber = 0; //first page
        int pageSize = 4; //4 elements per page
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "Nikaido";

        int pageNumber = 0; //first page
        int pageSize = 4; //4 elements per page
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}