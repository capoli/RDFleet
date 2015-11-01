package com.realdolmen.rdfleet.webmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by OCPAX79 on 1/11/2015.
 */
//@EnableWebSecurity
//public class MultipleHttpSecurityConfig {
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select email, password, true from user where email = ?")
//                .authoritiesByUsernameQuery("select email, ROLES from user where email = ?");
//    }
//
////    @Configuration
////    @Order(1)
////    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
////        @Override
////        protected void configure(HttpSecurity http) throws Exception {
////            http
////                    .antMatcher("/api/**")
////                    .authorizeRequests()
////                        .anyRequest().hasRole("RdEmployee")
////                        .and()
////                    .httpBasic();
////        }
////    }
//
//    @Configuration
//    @Order(1)
//    public static class FormLoginWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests()
//                        .anyRequest().authenticated()
//                        .and()
//                    .formLogin()
//                        .loginPage("/#/app/login")
//                        .permitAll();
//        }
//    }
//}
