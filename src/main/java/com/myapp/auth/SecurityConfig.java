package com.myapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
//@EnableWebSecurity
//@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfigurerAdapter.class);


    @Resource
    private UserDetailsService userService;

    private final StatelessAuthenticationFilter statelessAuthenticationFilter;


    @Value("${rememberMe.privateKey}")
    private String rememberMeKey;

    @Autowired
    public SecurityConfig(StatelessAuthenticationFilter statelessAuthenticationFilter) {
        super(true);
        this.statelessAuthenticationFilter = statelessAuthenticationFilter;
    }


    @Bean
    public RememberMeServices rememberMeServices() {

        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeKey, userService);

        // See http://stackoverflow.com/questions/25565809/implementing-a-remember-me-for-spring-social
        rememberMeServices.setAlwaysRemember(true);

        return rememberMeServices;

    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating password encoder bean");
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we use jwt so that we can disable csrf protection
        http.csrf().disable();

        http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/me").hasRole("BUYER")
                .antMatchers(HttpMethod.PATCH, "/api/users/me").hasRole("BUYER")
                .antMatchers(HttpMethod.GET, "/api/users/me/microposts").hasRole("BUYER")
                .antMatchers(HttpMethod.POST, "/api/microposts/**").hasRole("BUYER")
                .antMatchers(HttpMethod.DELETE, "/api/microposts/**").hasRole("BUYER")
                .antMatchers(HttpMethod.POST, "/api/relationships/**").hasRole("BUYER")
                .antMatchers(HttpMethod.DELETE, "/api/relationships/**").hasRole("BUYER")
                .antMatchers(HttpMethod.GET, "/api/feed").hasRole("BUYER")
                .and().apply(new SpringSocialConfigurer())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("'Bearer token_type=\"JWT\"'"));

/**
        http
                .formLogin()
                .loginPage("/signin")
                .permitAll().and()
                .rememberMe().key(rememberMeKey).rememberMeServices(rememberMeServices()).and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and().apply(new SpringSocialConfigurer());**/

       // http.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
/**
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }**/

    /**
     * Prevent StatelessAuthenticationFilter will be added to Spring Boot filter chain.
     * Only Spring Security must use it.
     */
   @Bean
    public FilterRegistrationBean registration(StatelessAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

  /**  @Override
    protected UserDetailsService userDetailsService() {
        return userService;
    }**/

}


