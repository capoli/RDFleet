package com.realdolmen.rdfleet.webmvc.config;

//import com.realdolmen.rdfleet.webmvc.filters.CsrfHeaderFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Created by JSTAX29 on 22/10/2015.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, inService from user where email = ?")
                .authoritiesByUsernameQuery("select email, role from user where email = ?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/rd/**").hasAnyRole("RDEMPLOYEE", "FLEETEMPLOYEE")
                    .antMatchers("/fleet/**").hasRole("FLEETEMPLOYEE")
                    .antMatchers("/index", "/").authenticated()
                    .anyRequest().permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                    .rememberMe();
    }

}
