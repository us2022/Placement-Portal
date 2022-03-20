package com.codebrewers.server.repos;

import java.util.List;

import com.codebrewers.server.models.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {

        @Query(value = "SELECT * FROM location l WHERE " +
                        "(:district is null OR l.location_district = :district) AND " +
                        "(:city is null OR l.location_city = :city) AND " +
                        "(:state is null OR l.location_state = :state) AND " +
                        "(:country is null OR l.location_country = :country) AND " +
                        "(:pincode is null OR l.location_pincode = :pincode)", nativeQuery = true)
        List<Location> findByLocationCustom(@Param("district") String district,
                        @Param("city") String city,
                        @Param("state") String state,
                        @Param("country") String country,
                        @Param("pincode") String pincode);
}
