package com.codebrewers.server.controllers;

import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.User;
import com.codebrewers.server.services.CollegeService;
import com.codebrewers.server.services.JobPostService;
import com.codebrewers.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/college/applyjobs")
public class CollegeApplyJobPostController {
    @Autowired
    CollegeService collegeService;
    @Autowired
    UserService userService;
    @Autowired
    JobPostService jobPostService;

    @GetMapping("/{id}")
    public ResponseEntity<List<JobPost>> displayCollegeApplyJobs(@PathVariable("id") long collegeId ) {
//
        try {
            return new ResponseEntity<>(collegeService.getAllJobs(collegeId), HttpStatus.OK);
            }
        catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    //
    }
    @GetMapping()
    public  ResponseEntity<List<JobPost>> displayCollegeUserJobpost(){
        User user=userService.getUserById(this.userService.getAuthUser().getUserAccountId()).get();
        try{
            System.out.println("hello everyyone");
           return new ResponseEntity<>(user.getCollegeUserJobPost(),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/{id}")
    public ResponseEntity<User> applyForJobPost(@PathVariable("id") long jobPostid){
        try {
            System.out.println("i am in controller");
            return new ResponseEntity<User>(jobPostService.updateJobsPostByCollegeUser(jobPostid),HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
