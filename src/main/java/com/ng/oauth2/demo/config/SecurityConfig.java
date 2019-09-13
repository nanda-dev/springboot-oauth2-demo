package com.ng.oauth2.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//TODO: CHECK - Does this simply mean ALL endpoints are authenticated? 
		//What's the point in listing out the /login and /oauth/authorize endpoints specifically, then?
		http.requestMatchers().antMatchers("/login", "/oauth/authorize").and().authorizeRequests().anyRequest()
				.authenticated();

		// This will enable h2 web console, but it disables security for ALL endpoints
		// in the app.
		/*
		 * http.authorizeRequests()
		 * .antMatchers("/h2-console/**").permitAll()//disabling security to make h2 web
		 * console freely accessible for convenience's sake .antMatchers("/login",
		 * "/oauth/authorize").authenticated();
		 */

		// This will enable h2 web console, but creates problems when trying to access
		// secured app endpoints,
		// even after providing valid token. 'Anonymous' user detected.
		// http.authorizeRequests()
		// .anyRequest().authenticated()
		// .antMatchers("/h2-console/**").permitAll(); // disabling security to make h2
		// web console
		// freely accessible for convenience's sake

		// Following two lines were added to fully enable h2 web console usage.
		// http.csrf().disable();
		// http.headers().frameOptions().disable();

		// Tried added CORS config to resolve the 'Anonymous' user issue, but no luck!
		// http.cors();

	}

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().
	 * encode("pwd")).roles("USER").and()
	 * .withUser("admin").password(passwordEncoder().encode("pwd")).roles("ADMIN");
	 * }
	 */

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() { final
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * configuration.addAllowedOrigin("*");
	 * //configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST",
	 * "PUT", "DELETE", "PATCH")); configuration.addAllowedMethod("*"); //
	 * setAllowCredentials(true) is important, otherwise: // The value of the
	 * 'Access-Control-Allow-Origin' header in the response must // not be the
	 * wildcard '*' when the request's credentials mode is 'include'.
	 * configuration.setAllowCredentials(true); // setAllowedHeaders is important!
	 * Without it, OPTIONS preflight request // will fail with 403 Invalid CORS
	 * request //configuration.setAllowedHeaders(ImmutableList.of("Authorization",
	 * "Cache-Control", "Content-Type")); configuration.addAllowedHeader("*"); final
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); return source; }
	 */

}
