package com.codebrewers.server.services;

import java.util.List;
import java.util.Map;

import com.codebrewers.server.models.Qualification;
import com.codebrewers.server.repos.QualificationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QualificationService {

    @Autowired
    QualificationRepo qualificationRepo;

    public Qualification registerQualification(Qualification qualification) {
        return qualificationRepo.save(qualification);
    }

    public List<Qualification> getAllQualifications() {
        return qualificationRepo.findAll();
    }

    public List<Qualification> getAllQualificationsCustom(Map<String, String> allParams) {
        // Extract params from url to pass into service

        String qualName = allParams.get("name") != null ? allParams.get("name") : null;
        String qualSpec = allParams.get("specialization") != null ? allParams.get("specialization") : null;

        return qualificationRepo.findByQualificationCustom(qualName, qualSpec);
    }
}