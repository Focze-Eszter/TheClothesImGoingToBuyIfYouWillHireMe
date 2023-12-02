package com.EszterFocze.TCIGTB.common.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="roles")
public class Role {

    //jpa annotation to map these fields to the corresponding columns in the roles table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tell to hibernate to generate automatically the values of this field
    private Integer id;
    @Column(name = "name", length = 40, nullable = false, unique = true)
    private String name;
    @Column(name="description", length = 150, nullable = false)
    private String description;

    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
