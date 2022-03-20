package com.codebrewers.server.services;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.College;
import com.codebrewers.server.models.JobPost;
import com.codebrewers.server.models.Location;
import com.codebrewers.server.models.User;
import com.codebrewers.server.repos.CollegeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollegeService {

    @Autowired
    CollegeRepo collegeRepo;
    @Autowired
    UserService userService;

    public List<College> getCollegesByName(String name) {
        return collegeRepo.findByCollegeName(name);

    }

    public Optional<College> getCollegeById(Long id) {
        return collegeRepo.findById(id);
    }

    public List<College> getAllColleges() {
        return collegeRepo.findAll();
    }

    public List<College> getCollegesByLocation(Location location) {
        return collegeRepo.findByCollegeLocation(location);
    }

    public List<College> getCollegeByNirfRanking(int nirf) {
        return collegeRepo.findByCollegeNirfRanking(nirf);
    }

    public List<College> getCollegesSortedByNirfRanking() {
        return collegeRepo.findByOrderByCollegeNirfRankingAsc();
    }

    public List<College> getCollegesByAicteAffiliation(boolean flag) {
        return collegeRepo.findByCollegeAicteAffiliation(flag);
    }

    public College registerCollege(College college) {
        return collegeRepo.save(college);
    }

    public College updateCollege(College college) throws ResourceNotFoundException {
        Optional<College> current = collegeRepo.findById(college.getCollegeId());
        if (current.isPresent()) {
            College currentCollege = current.get();
            currentCollege.setCollegeEmail(college.getCollegeEmail());
            currentCollege.setCollegeName(college.getCollegeName());
            currentCollege.setCollegeLocation(college.getCollegeLocation());
            currentCollege.setCollegeWebsite(college.getCollegeWebsite());
            currentCollege.setCollegeAicteAffiliation(college.isCollegeAicteAffiliation());
            currentCollege.setCollegeNirfRanking(college.getCollegeNirfRanking());
            return collegeRepo.save(currentCollege);
        } else {
            throw new ResourceNotFoundException("College Not Found");
        }
    }

    public void deleteCollege(Long collegeId) throws ResourceNotFoundException {
        Optional<College> current = collegeRepo.findById(collegeId);
        if (current.isPresent()) {
            collegeRepo.deleteById(collegeId);
        } else {
            throw new ResourceNotFoundException("College Not Found");
        }
    }

    public List<College> getAllCollegesCustom(Map<String, String> allParams) {
        // localhost:8080/api/college?name=xyz&aicte=true
        // Extract params from url to pass into service

        String name = allParams.get("name") != null ? allParams.get("name") : null;
        Integer nirf = allParams.get("nirf") != null ? parseInt(allParams.get("nirf")) : null;
        Boolean aicte = allParams.get("aicte") != null ? parseBoolean(allParams.get("aicte")) : null;
        return collegeRepo.findByCollegeCustom(name, nirf, aicte);
    }

    public List<User> getAllCollegeUsers(Long collegeId) throws ResourceNotFoundException {
        Optional<College> current = collegeRepo.findById(collegeId);
        if (current.isPresent()) {
            return userService.getUsersByCollege(current);
        } else {
            throw new ResourceNotFoundException("College Not Found");
        }

    }
    public List<JobPost> getAllJobs(long collegeId) throws ResourceNotFoundException{
        List<JobPost> allJobPosts = new ArrayList<JobPost>();
        List<User> allUsers = getAllCollegeUsers(collegeId);
        for (User user : allUsers) {
            for (JobPost jobPost : user.getCollegeUserJobPost()) {
                allJobPosts.add(jobPost);
            }
        }
        return allJobPosts;


    }

}
