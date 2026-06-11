package it.uniroma3.siw.torneo_calcio.security;

import it.uniroma3.siw.torneo_calcio.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final UtenteService utenteService;
    

    public SecurityConfig(UtenteService utenteService) {
		super();
		this.utenteService = utenteService;
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            // Forza l'accesso esplicito a registrazione e login prima di tutto il resto
	            .requestMatchers("/registrazione", "/login").permitAll() 
	            
	            // Tutte le altre pagine pubbliche
	            .requestMatchers("/", "/tornei", "/tornei/**", "/squadre", "/squadre/**", "/partite/**", "/react/**", "/api/**").permitAll()
	            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	            
	            // Ruoli e restrizioni
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/commenti/**").hasAnyRole("USER", "ADMIN")
	            
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .defaultSuccessUrl("/", true)
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutSuccessUrl("/")
	            .permitAll()
	        );

	    return http.build();
	}
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
            .userDetailsService(utenteService)
            .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}
