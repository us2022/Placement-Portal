package com.codebrewers.server.controllers;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.User;
import com.codebrewers.server.repos.CollegeRepo;
import com.codebrewers.server.repos.CompanyRepo;
import com.codebrewers.server.repos.UserRepo;
import com.codebrewers.server.services.CollegeService;
import com.codebrewers.server.services.CompanyService;
import com.codebrewers.server.services.UserService;
import com.codebrewers.server.shared.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/admin")
public class AdminApprovalController {
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CollegeRepo collegeRepo;
    @Autowired
    CollegeService collegeService;


    @GetMapping("/company/{id}")
    public ResponseEntity<List<User>> displayAllCompanyUsers(@PathVariable("id") long companyId){
        User current=this.userService.getAuthUser();
        if(current.getUserCompany().getCompanyCin().equals(companyRepo.findById(companyId).get().getCompanyCin()) ){
            try {
                return new ResponseEntity<>(companyService.getAllCompanyUsers(companyId),HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/company/approval/{id}")
    public ResponseEntity<User> approveCompanyUserAccount(@PathVariable("id") long userId) throws ResourceNotFoundException{
        User current=this.userService.getAuthUser();
        Optional<User> user_spoc=userRepo.findById(userId);
        if (current.getUserCompany().getCompanyCin().equals(user_spoc.get().getUserCompany().getCompanyCin())) {
            try {
                return new ResponseEntity<>(userService.updateUserApprovalStatus(userId),HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/college/{id}")
    public ResponseEntity<List<User>> displayAllCollegeUsers(@PathVariable("id") long collegeId){
        User current=this.userService.getAuthUser();
        if(current.getUserCollege().getCollegeName().equals(collegeRepo.findById(collegeId).get().getCollegeName())){
            try {
                return new ResponseEntity<>(collegeService.getAllCollegeUsers(collegeId),HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/college/approval/{id}")
    public ResponseEntity<User> approveCollegeUserAccount(@PathVariable("id") long userId) throws ResourceNotFoundException{
        User current=this.userService.getAuthUser();
        Optional<User> user_spoc=userRepo.findById(userId);
        if (current.getUserCollege().getCollegeName().equals(user_spoc.get().getUserCollege().getCollegeName())) {
            try {
                return new ResponseEntity<>(userService.updateUserApprovalStatus(userId),HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    //Is it necessary to check weather approved person ins admin or not


}
