package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;

import com.codebrewers.server.models.Location;
import com.codebrewers.server.models.SkillSet;
import com.codebrewers.server.repos.SkillSetRepo;
import com.codebrewers.server.services.SkillSetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/skillsets")
public class SkillSetController {

    @Autowired
    SkillSetService skillSetService;

    @Autowired
    SkillSetRepo skillSetRepo;

    @GetMapping()
    public ResponseEntity<List<SkillSet>> displaySkillSets() {
        try {
            List<SkillSet> allSkillSets = new ArrayList<SkillSet>();
            allSkillSets = skillSetService.getAllSkillSets();
            return new ResponseEntity<>(allSkillSets,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> postAllSkillSets(@RequestBody List<SkillSet> skillSets){
        try{
            skillSetRepo.saveAll(skillSets);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
