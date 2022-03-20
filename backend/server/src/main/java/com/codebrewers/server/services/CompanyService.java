package com.codebrewers.server.services;

import java.util.List;
import java.util.Optional;

import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.User;
import com.codebrewers.server.repos.CompanyRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    UserService userService;

    public List<Company> getCompaniesByName(String name) {
        return companyRepo.findByCompanyName(name);
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepo.findById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    public Optional<Company> getCompanyByCin(String cin) {
        return companyRepo.findByCompanyCin(cin);
    }

    public Company registerCompany(Company company) throws ResourceNotFoundException {
        Optional<Company> checkCompany=getCompanyByCin(company.getCompanyCin());
        if(checkCompany.isPresent()) {
            throw  new ResourceNotFoundException("Company already exist");
        }
        else {
            return companyRepo.save(company);
        }
    }


    public Company updateCompany(Company company) throws ResourceNotFoundException {
        Optional<Company> current = companyRepo.findById(company.getCompanyId());
        if (current.isPresent()) {
            Company currentCompany = current.get();
            currentCompany.setCompanyCin(company.getCompanyCin());
            currentCompany.setCompanyDescription(company.getCompanyDescription());
            currentCompany.setCompanyName(company.getCompanyName());
            currentCompany.setCompanyImage(company.getCompanyImage());
            currentCompany.setCompanyWebsite(company.getCompanyWebsite());
            // save
            return companyRepo.save(currentCompany);
        } else {
            throw new ResourceNotFoundException("Company Not Found");
        }
    }

    public void deleteCompany(Long companyId) throws ResourceNotFoundException {
        Optional<Company> current = companyRepo.findById(companyId);
        if (current.isPresent()) {
            companyRepo.deleteById(companyId);
        } else {
            throw new ResourceNotFoundException("Company Not Found");
        }
    }

    public List<User> getAllCompanyUsers(Long companyId) throws ResourceNotFoundException {
        Optional<Company> current = companyRepo.findById(companyId);
        if (current.isPresent()) {
            return userService.getUsersByCompany(current);
        } else {
            throw new ResourceNotFoundException("Company Not Found");
        }

    }
}
