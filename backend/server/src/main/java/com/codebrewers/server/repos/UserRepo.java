package com.codebrewers.server.repos;

import java.util.List;
import java.util.Optional;

import com.codebrewers.server.models.College;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.User;
import com.codebrewers.server.shared.UserType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    
    List<User> findByUserCompany(Optional<Company> company);

    List<User> findByUserCollege(Optional<College> college);

    List<User> findByUserType(UserType userType);

    Optional<User> findByEmail(String userEmail);

    Optional<User> findByUserName(String userName);

    Boolean existsByEmail(String email);

}
