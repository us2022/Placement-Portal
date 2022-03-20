package com.codebrewers.server.services;

import java.util.List;
import java.util.Optional;

import com.codebrewers.server.exceptions.ResourceConflictException;
import com.codebrewers.server.exceptions.ResourceNotFoundException;
import com.codebrewers.server.models.College;
import com.codebrewers.server.models.Company;
import com.codebrewers.server.models.User;
import com.codebrewers.server.payload.auth.UserRegistrationDto;
import com.codebrewers.server.repos.CollegeRepo;
import com.codebrewers.server.repos.CompanyRepo;
import com.codebrewers.server.repos.UserRepo;
import com.codebrewers.server.shared.AuthUserDetails;
import com.codebrewers.server.shared.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    CollegeRepo collegeRepo;

    @Autowired
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User save(UserRegistrationDto userRegistrationDto) throws ResourceConflictException {
        // if (userRepo.existsByEmail(userRegistrationDto.getEmail())) {
        //     throw new ResourceConflictException("Email is already taken");
        // }

        User user = new User(userRegistrationDto.getUserName(),
                userRegistrationDto.getMobileNumber(),
                userRegistrationDto.getEmail(),
                passwordEncoder().encode(userRegistrationDto.getPassword()),
                UserType.USER);
        return userRepo.save(user);
    }

    public User getAuthUser() {
        AuthUserDetails authuser = (AuthUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return authuser.getUser();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
        return new AuthUserDetails(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User getUserByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail).get();
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }

    public List<User> getUsersByCompany(Optional<Company> company) {
        return userRepo.findByUserCompany(company);
    }

    public List<User> getUsersByCollege(Optional<College> college) {
        return userRepo.findByUserCollege(college);
    }

    public User updateUserApprovalStatus(Long userId) throws ResourceNotFoundException {
        Optional<User> user = this.getUserById(userId);
        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.setAdminApprovalStatus(true);
            return dontShowPassword(currentUser);
        } else {
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public User updateUserType(Long userId, UserType userType) throws ResourceNotFoundException {
        Optional<User> user = this.getUserById(userId);
        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.setUserType(userType);
            return dontShowPassword(currentUser);
        } else {
            throw new ResourceNotFoundException("User not found!");
        }
    }

//    public User updateUserCompany(Long userId, Company company) throws ResourceNotFoundException {
//        Optional<User> user = this.getUserById(userId);
//        if (user.isPresent()) {
//            User currentUser = user.get();
//            currentUser.setUserCompany(company);
//            return userRepo.save(currentUser);
//        } else {
//            throw new ResourceNotFoundException("User not found!");
//        }
//    }
    public User updateUserCompany(Company company) throws ResourceNotFoundException {
        Optional<User> current_user= this.getUserById(this.getAuthUser().getUserAccountId());
        Optional<Company> current_company=companyRepo.findByCompanyCin(company.getCompanyCin());
        if (current_company.isPresent()) {
            User currentUser = current_user.get();
            currentUser.setUserCompany(company);
            this.updateUserType(this.getAuthUser().getUserAccountId(),UserType.COMPANY_SPOC);
            return dontShowPassword(currentUser);
        } else {
            throw new ResourceNotFoundException("Company Not Found");
        }
}

    public User updateUserCollege(College college) throws ResourceNotFoundException {
        Optional<User> current_user= userRepo.findById(this.getAuthUser().getUserAccountId());
//        Optional<College> current_college=collegeRepo.findByCollegeName(college.getCollegeName());
        if (current_user.isPresent()) {
            User currentUser = current_user.get();
            currentUser.setUserCollege(college);this.updateUserType(this.getAuthUser().getUserAccountId(),UserType.COLLEGE_SPOC);
            return dontShowPassword(currentUser);
        } else {
            throw new ResourceNotFoundException("College Not Found");
        }
    }

    public User updateUser(User user) throws ResourceNotFoundException {
        Optional<User> current = userRepo.findById(user.getUserAccountId());
        if (current.isPresent()) {
            User currentUser = current.get();
            currentUser.setUserName(user.getUserName());
            currentUser.setMobileNumber(user.getMobileNumber());
            return dontShowPassword(currentUser);
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    public void deleteUser(Long userId) throws ResourceNotFoundException {
        Optional<User> current = userRepo.findById(userId);
        if (current.isPresent()) {
            userRepo.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    public boolean userPasswordCheck (String password,long user_id){
        User current_user=userRepo.getById(user_id);
        PasswordEncoder passencoder = new BCryptPasswordEncoder();
        String encodedPassword = current_user.getPassword();
        return passencoder.matches(password, encodedPassword);

    }

 public User dontShowPassword(User user){
     User current_user=userRepo.save(user);
//     current_user.setPassword("*****");
     return current_user;
 }


}
