package com.codebrewers.server.controllers;

import com.codebrewers.server.models.College;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.User;
import com.codebrewers.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/apply")
public class ApplyForCompanyCollegeController {

    @Autowired
    UserService userService;

    @PutMapping("/company")
    public ResponseEntity<User> updateUserCompany(@RequestBody Company company){
        try {
            return new ResponseEntity<>(userService.updateUserCompany(company), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/college")
    public ResponseEntity<User> updateUserCollege(@RequestBody College college){
        try {
            return new ResponseEntity<>(userService.updateUserCollege(college), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
