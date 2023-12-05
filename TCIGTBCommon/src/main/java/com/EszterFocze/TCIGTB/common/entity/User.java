package com.EszterFocze.TCIGTB.common.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity //to indicate to jpa that we map this class to the table in the db
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String email;
    @Column(length = 64, nullable = false) //because we will use the password encoder with the length of 64
    private String password;
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;
    @Column(length = 64)
    private String photos;
    private boolean enabled;

    @ManyToMany //between users and roles; unidirectional - only to user to role
    @JoinTable (
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"), //foreign key in the intermediate table
            inverseJoinColumns = @JoinColumn(name = "role_id") //foreign key in the intermediate table
    ) //the intermediate table
    private Set<Role> roles = new HashSet<>();//map the set collection to many to many

    public User() {
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public String getFormattedRoles() {
        return roles.stream().map(Role::getName).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Transient //indicate that this getter is not mapped with any field in the db
    public String getPhotosImagePath() {
        if(id == null || photos == null) return "/assets/images/default-user.png";
        return "/user-photos/" + this.id + "/" + this.photos;
    }
}
