package com.spring.security.springsecurity.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 * auth.inMemoryAuthentication() .withUser("hiren") .password("hiren")
		 * .roles("USER") .and() .withUser("admin") .password("admin") .roles("ADMIN");
		 */

		// we will use H2 database
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, authority from authorities where username = ?");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/h2/**").permitAll().antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/user").hasAnyRole("ADMIN", "USER").antMatchers("/").permitAll().and().formLogin();

		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	/**
	 * Clear text password is strict NO in Spring. Just for tutorial purpose we are
	 * passing this.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
