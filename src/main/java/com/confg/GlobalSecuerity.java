package com.confg;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.service.UserService;

@Configuration
@EnableWebSecurity
public class GlobalSecuerity {

	private final BCryptPasswordEncoder encoder;
	private final UserService userDetailsService;

	@Autowired
	public GlobalSecuerity(BCryptPasswordEncoder encoder, UserService userDetailsService) {
		super();

		this.encoder = encoder;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	SecurityFilterChain setSecuerity(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

		AuthenticationManagerBuilder authMangerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authMangerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder);

		AuthenticationManager authManger = authMangerBuilder.build();
		http.authenticationManager(authManger);


		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

		// My enpdoints start from /user so this pattern is ok for me
		// http.csrf(csrfConfigurer ->
		// csrfConfigurer.ignoringRequestMatchers(mvcMatcherBuilder.pattern("/user/**")));
	

		
		
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(mvcMatcherBuilder.pattern("/user/save"),
						(AntPathRequestMatcher.antMatcher("/swagger-ui/**")))
				.permitAll()

				// This line is optional in .authenticated() case as
				// .anyRequest().authenticated() would be applied for H2 path anyway
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**")).authenticated().anyRequest()
				.authenticated());

		http.addFilter(new AuthenticationFilter(authManger));
		http.addFilter(new AuthorizationFilter(authManger));
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//		http.formLogin(Customizer.withDefaults());
//		http.httpBasic(Customizer.withDefaults());
//		 http
//			.formLogin(form -> form
//				.loginPage("/login")
//				.permitAll()
//			);

		return http.build();

	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//	    CorsConfiguration configuration = new CorsConfiguration();
//	    configuration.setAllowedOrigins(Arrays.asList("*"));
//	    configuration.setAllowedMethods(Arrays.asList("*"));
//	    configuration.setAllowedHeaders(Arrays.asList("*"));
//	    org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new  org.springframework.web.cors.UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", configuration);
//	    return source;
//	    
//	}

}
