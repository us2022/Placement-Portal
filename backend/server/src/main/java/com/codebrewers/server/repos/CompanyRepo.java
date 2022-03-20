package com.codebrewers.server.repos;

import java.util.List;
import java.util.Optional;

import com.codebrewers.server.models.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    
    List<Company> findByCompanyName(String Name);

    Optional<Company> findByCompanyCin(String companyCin);
}
