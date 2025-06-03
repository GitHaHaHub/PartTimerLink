package com.ptlink.ptlink_server.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.ptlink.ptlink_server.repository.UserRepository;

import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	//Using spring security oauth2 resource server with JWT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
            .csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/SignIn", "/AddUser", "/FindUser/*", "/UpdateUser", "/DeleteUser").permitAll()
				.anyRequest().hasRole("user")
			)
			.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}

	// authenticationManager used for SignIn
	@Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
}

	// UserDetailsService used for SignIn
	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				com.ptlink.ptlink_server.model.User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));

				// Customize role mapping if needed
				GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

				return new org.springframework.security.core.userdetails.User(
					user.getUsername(),
					user.getPassword(),
					List.of(authority)
				);
			}
		};
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean
	public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String SECRET) {
		// Use the same secret key used in JwtTokenGenerator
		SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

		// Spring Security's decoder with HS256
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

}