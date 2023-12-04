package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    //extending the crudRepository gives access to methods like save(), findAll(), findById()...
    //this method is used to verify the email uniqueness
    @Query("SELECT u FROM User u WHERE u.email = :email") //to specify a custom jpa query; :email -> parameter email
    public User getUserByEmail(@Param("email") String email); //retrieve user obj by email

    public Long countById(Integer id); //we use this because we don't want the .get() method that returns a full User object with all the details
    //just check the existence of an user
}