package com.codebrewers.server.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.codebrewers.server.models.Location;
import com.codebrewers.server.repos.LocationRepo;
import com.codebrewers.server.services.LocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
@RequestMapping(path = "/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;
    @Autowired
    LocationRepo locationRepo;

    @GetMapping()
    public ResponseEntity<List<Location>> displayLocations(
            @RequestParam(required = false) Map<String, String> allParams) {
        try {
            List<Location> allLocations = new ArrayList<Location>();

            if (allParams.isEmpty()) {
                allLocations = locationService.getLocations();
            } else {
                allLocations = locationService.getAllLocationsCustom(allParams);
            }
            return new ResponseEntity<>(allLocations,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> postAllLocation(@RequestBody List<Location> locations){
        try{
                locationRepo.saveAll(locations);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
