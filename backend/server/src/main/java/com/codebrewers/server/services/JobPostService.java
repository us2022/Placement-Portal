package com.codebrewers.server.services;

import static java.lang.Double.parseDouble;

import java.net.http.HttpRequest;
import java.util.*;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.*;
import com.codebrewers.server.repos.JobPostRepo;
import com.codebrewers.server.repos.UserRepo;
import com.codebrewers.server.shared.JobDomain;
import com.codebrewers.server.shared.JobOpenFor;
import com.codebrewers.server.shared.JobType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JobPostService {

    @Autowired
    JobPostRepo jobPostRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CollegeService collegeService;
    @Autowired
    UserService userService;

    public HashSet<JobPost> getJobsByQualification(List<Qualification> qualifications) {
        return jobPostRepo.findByQualificationIn(qualifications);
    }

    public HashSet<JobPost> getJobsByLocation(List<Location> locations) {
        return jobPostRepo.findByJobLocationIn(locations);
    }

    public HashSet<JobPost> getJobBySkillSet(List<SkillSet> skillSets) {
        return jobPostRepo.findBySkillSetIn(skillSets);
    }

    public List<JobPost> getJobsByOpenedFor(JobOpenFor jobOpenFor) {
        return jobPostRepo.findByJobOpenFor(jobOpenFor);
    }

    public Optional<JobPost> getJobPostById(Long id) {
        return jobPostRepo.findById(id);
    }
    public List<JobPost> getAllJobPosts() throws ResourceNotFoundException {
        User loginUser=userService.getUserById(this.userService.getAuthUser().getUserAccountId()).get();
        List<JobPost> applyJobsUser=collegeService.getAllJobs(loginUser.getUserCollege().getCollegeId());
        List<JobPost> allJobpost=jobPostRepo.findAll();
        List<JobPost> currentJobPosts=new ArrayList<>();
        for(JobPost jobPost:allJobpost){
            if(!applyJobsUser.contains(jobPost)){
                currentJobPosts.add(jobPost);
            }
        }
        return currentJobPosts ;
    }

    public List<JobPost> getJobPostsByType(JobType jobType) {
        return jobPostRepo.findByJobType(jobType);
    }

    public List<JobPost> getJobPostsByDomain(JobDomain jobDomain) {
        return jobPostRepo.findByJobDomain(jobDomain);
    }

    public List<JobPost> getJobPostsByRole(String jobRole) {
        return jobPostRepo.findByJobRole(jobRole);
    }

    public List<JobPost> getJobPostsByCompensation(double compensation) {
        return jobPostRepo.findByJobCompensation(compensation);
    }

    public JobPost registerJobPost(JobPost jobPost) throws ResourceNotFoundException{
        User jobPostUser=userService.getUserById(this.userService.getAuthUser().getUserAccountId()).get();
        if(jobPostUser.getAdminApprovalStatus().equals(true)){
            jobPost.setCompanyUser(jobPostUser);
            return jobPostRepo.save(jobPost);
        }
        else{
            throw new ResourceNotFoundException("You don't have company admin approval");
        }
    }

    public JobPost updateJobPost(JobPost jobPost) throws ResourceNotFoundException {
        Optional<JobPost> current = jobPostRepo.findById(jobPost.getJobPostId());
        if (current.isPresent()) {
            JobPost currentJobPost = current.get();
            currentJobPost.setJobCompensation(jobPost.getJobCompensation());
            currentJobPost.setJobDescription(jobPost.getJobDescription());
            currentJobPost.setJobDomain(jobPost.getJobDomain());
            currentJobPost.setJobOpenFor(jobPost.getJobOpenFor());
            currentJobPost.setJobType(jobPost.getJobType());
            currentJobPost.setJobRole(jobPost.getJobRole());
            currentJobPost.setJobLocation(jobPost.getJobLocation());
            currentJobPost.setQualification(jobPost.getQualification());
            currentJobPost.setSkillSet(jobPost.getSkillSet());
            return jobPostRepo.save(currentJobPost);
        } else {
            throw new ResourceNotFoundException("JobPost Not Found");
        }
    }

    public void deleteJobPost(Long jobPostId) throws ResourceNotFoundException {
        Optional<JobPost> current = jobPostRepo.findById(jobPostId);
        if (current.isPresent()) {
            jobPostRepo.deleteById(jobPostId);
        } else {
            throw new ResourceNotFoundException("JobPost Not Found");
        }
    }

    public List<JobPost> getAllJobPostsCustom(Map<String, String> allParams) {
        // Extract params from url to pass into service

        String jobRole = allParams.get("jobrole") != null ? allParams.get("jobrole") : null;

        JobType jobType = allParams.get("jobtype") != null ? Enum.valueOf(JobType.class, allParams.get("jobtype"))
                : null;
        JobDomain jobDomain = allParams.get("jobdomain") != null
                ? Enum.valueOf(JobDomain.class, allParams.get("jobdomain"))
                : null;
        JobOpenFor jobOpenFor = allParams.get("jobopenfor") != null
                ? Enum.valueOf(JobOpenFor.class, allParams.get("jobopenfor"))
                : null;

        Double compensation_max = allParams.get("cmax") != null ? parseDouble(allParams.get("cmax")) : null;
        Double compensation_min = allParams.get("cmin") != null ? parseDouble(allParams.get("cmin")) : null;

        return jobPostRepo.findByJobPostCustom(jobRole, jobType, jobDomain, jobOpenFor, compensation_max,
                compensation_min);
    }

//    public void applyJobPost(JobPost jobPost,long userId) throws ResourceNotFoundException{
//        Optional<User> current=userRepo.findById(userId);
//
//        if(current.isPresent()){
//            List<JobPost>allJobsOfCollege=collegeService.getAllJobs(current.get().getUserCollege().getCollegeId());
//            if(allJobsOfCollege.contains(jobPost)==false) {
//                List<JobPost> currentJobPost = current.get().getCollegeUserJobPost();
//                currentJobPost.add(jobPost);
//                current.get().setCollegeUserJobPost(currentJobPost);
//            }
//            else{
//                throw new ResourceNotFoundException("Job is already Applied");
//            }
//        }
//        else {
//            throw new ResourceNotFoundException("User Not Found");
//        }
//}

    public User updateJobsPostByCollegeUser(long jobPostId) throws ResourceNotFoundException{
        Optional<User> currentUser=this.userRepo.findById(userService.getAuthUser().getUserAccountId());
        JobPost currentJobpost=jobPostRepo.getById(jobPostId);
        if(currentUser.get().getAdminApprovalStatus().equals(true)){
//            List<User>collegeUser=currentJobpost.getCollegeUsers();
//            collegeUser.add(currentUser);
//            currentJobpost.setCollegeUsers(collegeUser);
//            jobPostRepo.save(currentJobpost);
            List<JobPost>currentUserJobPost=currentUser.get().getCollegeUserJobPost();
            currentUserJobPost.add(currentJobpost);
            currentUser.get().setCollegeUserJobPost(currentUserJobPost);
            return userRepo.save(currentUser.get());

        }
        else {
            throw  new ResourceNotFoundException("You don't have admin approval to Apply a job");
        }

    }
}
