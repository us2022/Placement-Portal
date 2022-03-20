package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.codebrewers.server.models.Qualification;
import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.repos.QualificationRepo;
import com.codebrewers.server.services.QualificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/qualifications")
public class QualificationController {

    @Autowired
    QualificationService qualificationService;

    @Autowired
    QualificationRepo qualificationRepo;

    @GetMapping()
    public ResponseEntity<List<Qualification>> displayQualification(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<Qualification> allQualifications = new ArrayList<Qualification>();

            if (allParams.isEmpty()) {
                allQualifications = qualificationService.getAllQualifications();
            } else {
                allQualifications = qualificationService.getAllQualificationsCustom(allParams);
            }
            return new ResponseEntity<>(allQualifications,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> postAllQualifications(@RequestBody List<Qualification> qualifications){
        try{
            qualificationRepo.saveAll(qualifications);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
