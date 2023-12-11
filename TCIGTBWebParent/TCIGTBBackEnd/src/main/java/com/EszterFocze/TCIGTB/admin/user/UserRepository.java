package com.EszterFocze.TCIGTB.admin.user;

import com.EszterFocze.TCIGTB.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    //extending the crudRepository gives access to methods like save(), findAll(), findById()...
    //this method is used to verify the email uniqueness
    //PagingAndSortingRepository -> Page<T> FindAll(Pageable pageable)

    @Query("SELECT u FROM User u WHERE u.email = :email") //to specify a custom jpa query; :email -> parameter email
    public User getUserByEmail(@Param("email") String email); //retrieve user obj by email

    public Long countById(Integer id); //we use this because we don't want the .get() method that returns a full User object with all the details
    //just check the existence of an user

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%") //%?1% => placeholder for the value of the first parameter (keyword)
    public Page<User> findAll(String keyword, Pageable pageable); //filter/search functionality working with pagination and sorting
    //u.firstName LIKE %?1% OR u.lastName LIKE %?1% OR u.email LIKE %?1%"; CONCAT - with spaces


    @Query("UPDATE User u SET u.enabled =?2 where u.id = ?1 ") //set the value to the second parameter of the method where u.id is set to the first parameter of the method
    @Modifying //because it's an update method we need to use the modifying annotation
    public void updateEnabledStatus(Integer id, boolean enabled);
}