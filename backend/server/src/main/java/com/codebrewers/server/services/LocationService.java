package com.codebrewers.server.services;

import java.util.List;
import java.util.Map;

import com.codebrewers.server.models.Location;
import com.codebrewers.server.repos.LocationRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    LocationRepo locationRepo;

    public Location registerLocation(Location location) {
        return locationRepo.save(location);
    }

    public List<Location> getLocations() {
        return locationRepo.findAll();
    }

    public List<Location> getAllLocationsCustom(Map<String, String> allParams) {
        // Extract params from url to pass into service

        String locationDistrict = allParams.get("district") != null ? allParams.get("district") : null;
        String locationCity = allParams.get("city") != null ? allParams.get("city") : null;
        String locationState = allParams.get("state") != null ? allParams.get("state") : null;
        String locationCountry = allParams.get("country") != null ? allParams.get("country") : null;
        String locationPincode = allParams.get("pincode") != null ? allParams.get("pincode") : null;

        return locationRepo.findByLocationCustom(locationDistrict, locationCity, locationState, locationCountry,
                locationPincode);
    }
}