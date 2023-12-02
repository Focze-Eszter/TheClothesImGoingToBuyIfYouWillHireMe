package com.EszterFocze.TCIGTB.admin.user;


import com.EszterFocze.TCIGTB.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //because Spring Data JPA runs the test against an in-memory database
@Rollback(false) //to tell jpa to commit the changes after running the test
public class RoleRepositoryTests {

    //reference to the RoleRepository
    @Autowired //Let Spring inject an instance of the RoleRepository
    private RoleRepository repo;

    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Admin", "Manage everything");
        Role savedRole = repo.save(roleAdmin); //for the purpose of assertion we save the repo.save()
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRole() {

        Role roleSalesperson = new Role("Salesperson", "Manage shipping rates," +
                " view products, update products price, manage orders, manage customers, manage sales " +
                "report");
        Role roleEditor = new Role("Editor", "Manage categories, manage articles," +
                " manage menus, manage products, manage brands");
        Role roleShipper = new Role("Shipper", "View products, view orders, update order status");
        Role roleAssistant = new Role("Assistant", "Manage questions, manage reviews");
        repo.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant)); //return an immutable list containing the specify objects
    }
}
