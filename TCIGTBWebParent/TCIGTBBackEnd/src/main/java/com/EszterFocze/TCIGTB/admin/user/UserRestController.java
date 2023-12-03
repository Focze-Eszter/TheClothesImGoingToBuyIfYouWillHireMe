package com.EszterFocze.TCIGTB.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController { //spring RESTful web controller
    //this controller is fired when the user click on save when the new user form is completed,
// and before the data is sent to be persisted, with JQuery this RestfulController is invoked (AJAX call) with the purpose
//of checking the email uniqueness in the db.
//with this controller, a pop-up is fired to warn about the duplicate email. if this RestController never
//existed, an error 500 always fired when a duplicate email is added to the db
    @Autowired
    private UserService service;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
        return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}

/* We use RESTful webservice for responsiveness of the application - meaning that the users
 can see the response immediate without seeing page reloaded.

In case of checking email uniqueness, the application will show a duplicate email warning in a
decent way (a modal dialog). If we use standard form submit process, then the page will be
reloaded and the password field will be reset (the user will have to enter password again
- which is not convenient) - and some other fields will lose user-entered values as well
on page reload.

Another reason is RESTful webservice is lightweight in terms of data transferred between
client-server.
 */

