package com.codebrewers.server.config;

import com.codebrewers.server.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("*"));
    //     configuration.setAllowedMethods(Arrays.asList("*"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    // userRole is checked-->what endpoints it can
    // access?-->Controller--->services-->
    // user/:userId put userId==userLoggedInId ---->
    // xyz.com
    // Hacker-->xyz.com-->put post
    // put user/:userid--->Controller-->Service userLoggedinId!=userId--->403
    // forebidden

    // base64encoded(email:password)
    // authorisation header-->email-->user (findByEmail)-->userid--->
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().
                csrf().disable().authorizeRequests()
                // use case: home page
                .antMatchers(HttpMethod.GET, "/api/colleges", "/api/companies").permitAll()
                // use case: anyone can login, register
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/logout").permitAll()
                // ---- LOCATIONS, SKILLSETS, QUALIFICATIONS---
                .antMatchers(HttpMethod.GET, "/api/locations/**", "/api/qualifications/**", "/api/skillsets/**")
                .hasAnyAuthority("COLLEGE_SPOC", "COLLEGE_ADMIN", "COMPANY_SPOC", "COMPANY_ADMIN","USER")
                .antMatchers("/api/users")
                .hasAnyAuthority("COLLEGE_SPOC", "COLLEGE_ADMIN", "COMPANY_SPOC", "COMPANY_ADMIN", "USER")
                // -----JOBPOSTS-----
                .antMatchers(HttpMethod.GET, "/api/jobposts/**")
                .hasAnyAuthority("COLLEGE_SPOC", "COLLEGE_ADMIN", "COMPANY_SPOC", "COMPANY_ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/jobposts/**")
                .hasAnyAuthority("COMPANY_SPOC", "COMPANY_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/jobposts/**")
                .hasAnyAuthority("COMPANY_SPOC", "COMPANY_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/jobposts/**")
                .hasAnyAuthority("COMPANY_SPOC", "COMPANY_ADMIN")

                // -----COMPANIES-----
                .antMatchers(HttpMethod.PUT, "/api/companies")
                .hasAnyAuthority("COMPANY_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/companies")
                .hasAnyAuthority("COMPANY_ADMIN")
                // -----COLLEGES-----
                .antMatchers(HttpMethod.PUT, "/api/colleges")
                .hasAnyAuthority("COLLEGE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/colleges")
                .hasAnyAuthority("COLLEGE_ADMIN")
                // ----Register college/company
                .antMatchers(HttpMethod.POST, "/api/companies", "/api/colleges")
                .hasAnyAuthority("USER")
                // ------ADMIN APPROVAL
                .antMatchers(HttpMethod.POST,"/api/admin/company/approval/**")
                .hasAnyAuthority("COMPANY_ADMIN")
                .antMatchers(HttpMethod.GET,"/api/admin/company/**")
                .hasAnyAuthority("COMPANY_ADMIN")
                .antMatchers(HttpMethod.GET,"/api/admin/college/**")
                .hasAnyAuthority("COLLEGE_ADMIN")
                .antMatchers(HttpMethod.POST,"/api/admin/college/approval/**")
                .hasAnyAuthority("COLLEGE_ADMIN")
                .antMatchers(HttpMethod.GET,"/api/college/applyjobs/**")
                .hasAnyAuthority("COLLEGE_ADMIN","COLLEGE_SPOC")
                .antMatchers(HttpMethod.POST,"/api/college/applyjobs/**")
                .hasAnyAuthority("COLLEGE_ADMIN","COLLEGE_SPOC")
                .anyRequest().authenticated().and().httpBasic()
                .and().logout().clearAuthentication(true);
        // .anyRequest().authenticated()
        // .and()
        // .formLogin().permitAll()
        // .and()
        // .logout().permitAll()
        // .and()
        // .exceptionHandling().accessDeniedPage("/403")
        ;
    }
}

/*
 * COLLEGE_ADMIN
 * /api/auth/** GET POST
 * /api/colleges GET, POST(only 1?), PUT, DELETE?
 * /api/companies GET
 * /api/jobposts GET, Apply?
 * /api/location GET
 * /api/qualifications GET
 * /api/skillsets GET
 * /api/users GET only for its college (no password shown)
 * /api/users PUT only on approval status of its college spocs
 */

/*
 * COLLEGE_SPOC
 * /api/auth/** GET POST
 * /api/colleges GET
 * /api/companies GET
 * /api/jobposts GET, Apply?
 * /api/location GET
 * /api/qualifications GET
 * /api/skillsets GET
 * /api/users GET only for its college (no password shown)
 */

/*
 * COMPANY_ADMIN
 * /api/auth/** GET POST
 * /api/colleges GET
 * /api/companies GET ,POST (only 1),PUT, DELETE?
 * /api/jobposts GET, POST, PUT, DELETE (only the ones posted by this company,
 * ever for GET?)
 * /api/location GET
 * /api/qualifications GET
 * /api/skillsets GET
 * /api/users GET only for its company (no password shown)
 * /api/users PUT only on approval status of its company spocs
 */

/*
 * COMPANY_SPOC
 * /api/auth/** GET POST
 * /api/colleges GET
 * /api/companies GET
 * /api/jobposts GET, POST, PUT, DELETE (only the ones posted by this company,
 * ever for GET?)
 * /api/users GET only for its company (no password shown)
 */
