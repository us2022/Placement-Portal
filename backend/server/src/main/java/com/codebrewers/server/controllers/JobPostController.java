package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.User;
import com.codebrewers.server.repos.UserRepo;
import com.codebrewers.server.services.CompanyService;
import com.codebrewers.server.services.JobPostService;
import com.codebrewers.server.services.UserService;

import org.hibernate.engine.HibernateIterator;
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
@RequestMapping(path = "/api/jobposts")
public class JobPostController {

    @Autowired
    JobPostService jobPostService;

    @Autowired
    UserService userService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CompanyService companyService;
//
//    @GetMapping("/all")
//    public ResponseEntity<List<JobPost>> display(){
//        return new ResponseEntity<>(jobPostService.getAllJobPosts(),HttpStatus.OK);
//
//    }
    //It's not Working
    @GetMapping()
    public ResponseEntity<List<JobPost>> displayJobPosts(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<JobPost> allJobPosts = new ArrayList<JobPost>();

            if (allParams.isEmpty()) {
                allJobPosts = jobPostService.getAllJobPosts();
            } else {
                allJobPosts = jobPostService.getAllJobPostsCustom(allParams);
            }
            return new ResponseEntity<>(allJobPosts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPost> displayJobPostById(@PathVariable("id") long id) {
        try {
            Optional<JobPost> jobPost = jobPostService.getJobPostById(id);
            return jobPost.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPost jobPost) {
//        if(jobPost.getCompanyUser().getUserAccountId().equals(this.userService.getAuthUser().getUserAccountId())){
            try {
                return new ResponseEntity<>(jobPostService.registerJobPost(jobPost), HttpStatus.CREATED);

            } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        }

//        else{
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteJobPost(@PathVariable long id) {
        // jobPostId--->companyUserId
        // Auth-->userId
        if (this.userService.getAuthUser().getUserAccountId() == this.jobPostService.getJobPostById(id).get()
                .getCompanyUser().getUserAccountId()) {
            try {
                jobPostService.deleteJobPost(id);
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
    public ResponseEntity<JobPost> updateJobPost(@PathVariable("id") long id, @RequestBody JobPost jobPost) {
        System.out.print(this.userService.getAuthUser().getUserAccountId());
        System.out.print(this.jobPostService.getJobPostById(id).get()
                .getCompanyUser().getUserAccountId());
        if (this.userService.getAuthUser().getUserAccountId().equals( this.jobPostService.getJobPostById(id).get()
                .getCompanyUser().getUserAccountId())) {
            System.out.println("hello");
            try {
                return new ResponseEntity<>(jobPostService.updateJobPost(jobPost), HttpStatus.OK);
            } catch (ResourceNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping("/company/{id}")
    public ResponseEntity<List<JobPost>> displayJobPostByCompany(@PathVariable("id") long companyId) {
            try {
                List<JobPost> allJobPosts = new ArrayList<JobPost>();
                List<User> allUsers = companyService.getAllCompanyUsers(companyId);
                for (User user : allUsers) {
                    for (JobPost jobPost : user.getCompanyUserJobPost()) {
                        allJobPosts.add(jobPost);
                    }
                }
                return new ResponseEntity<>(allJobPosts, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
//
    }
    @GetMapping("/company/user/{id}")
    public ResponseEntity<List<JobPost>> displayJobPostByUserPost(@PathVariable("id") long userId){
        if (this.userService.getAuthUser().getUserAccountId() == userId) {
            try {

                return new ResponseEntity<>(userService.getUserById(userId).get().getCompanyUserJobPost(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
    @GetMapping("/applycolleges/{id}")
    public ResponseEntity<List<User>> displayJobPostByApplyColleges(@PathVariable("id") long jobPostId) throws ResourceNotFoundException {
        Optional<JobPost> jobPost=jobPostService.getJobPostById(jobPostId);
        String currentCompnayCin=userService.getUserById(this.userService.getAuthUser().getUserAccountId()).get().getUserCompany().getCompanyCin();
        if(jobPost.isPresent() ){
            if(jobPost.get().getCompanyUser().getUserCompany().getCompanyCin().equals(currentCompnayCin)){
                try {
                    return new ResponseEntity<>(jobPost.get().getCollegeUsers(), HttpStatus.OK);
                }
                catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }

        }
        else {
            throw  new ResourceNotFoundException("Job Not Found");
        }

    }
}




    // apply jobpost-->similar organisation check           done
    // get similar organisation --->applied/posted jobposts done
    // approve account by admin
    //G

