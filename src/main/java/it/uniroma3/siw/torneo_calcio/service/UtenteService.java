package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Utente;
import it.uniroma3.siw.torneo_calcio.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    
    

    public UtenteService(UtenteRepository utenteRepository) {
		super();
		this.utenteRepository = utenteRepository;
	}

	@Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utente non trovato: " + username));

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPassword())
                .roles(utente.getRuolo())
                .build();
    }

    @Transactional
    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return utenteRepository.existsByUsername(username);
    }
}
