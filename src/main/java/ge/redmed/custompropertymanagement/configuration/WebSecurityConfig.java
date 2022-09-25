package ge.redmed.custompropertymanagement.configuration;

import com.sun.istack.NotNull;
import ge.redmed.custompropertymanagement.filter.CustomAuthenticationFilter;
import ge.redmed.custompropertymanagement.filter.CustomAuthorizationFilter;
import ge.redmed.custompropertymanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/users/token/refresh/**", "/files/**").permitAll()
				.antMatchers("/properties/**").hasAnyAuthority("ROLE_USER", "ROLE_PROPERTY_ADMIN", "ROLE_ADMIN")
				.antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ROLE_PROPERTY_ADMIN")
				.antMatchers(HttpMethod.GET, "/users/**", "/properties/**").hasAnyAuthority("ROLE_SUPPORT")
				.anyRequest().authenticated();

		http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	public static @NotNull User getCurrentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			throw new RuntimeException("Not authorised");
		}
		return (User) auth.getPrincipal();
	}

	public static long getCurrentUserId() {
		var user = getCurrentUser();
		return user.getId();
	}
}
