package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.College;
import com.codebrewers.server.services.CollegeService;
import com.codebrewers.server.services.UserService;
import com.codebrewers.server.shared.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/colleges")
public class CollegeController {

    @Autowired
    CollegeService collegeService;

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<List<College>> displayColleges(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<College> allColleges = new ArrayList<College>();

            if (allParams.isEmpty()) {
                allColleges = collegeService.getAllColleges();
            } else {
                allColleges = collegeService.getAllCollegesCustom(allParams);
            }
            return new ResponseEntity<>(allColleges, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<College> displayCollegeById(@PathVariable("id") long id) {
        try {
            Optional<College> college = collegeService.getCollegeById(id);
            return college.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<College> createCollege(@RequestBody College college) {
        try {
            ResponseEntity<College> savedCollege = new ResponseEntity<>(
                    collegeService.registerCollege(college), HttpStatus.CREATED);

            long userId = this.userService.getAuthUser().getUserAccountId();
            userService.updateUserApprovalStatus(userId);
            userService.updateUserCollege(college);
            userService.updateUserType(userId, UserType.COLLEGE_ADMIN);
            return savedCollege;

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // to be decided: remove method or not
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCollege(@PathVariable long id) {
        if (this.userService.getAuthUser().getUserCollege().getCollegeId() == id) {
            try {
                collegeService.deleteCollege(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (ResourceNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<College> updateCollege(@PathVariable("id") long id, @RequestBody College college) {
        if (this.userService.getAuthUser().getUserCollege().getCollegeId() == id) {
            try {
                return new ResponseEntity<>(collegeService.updateCollege(college), HttpStatus.OK);
            } catch (ResourceNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
