package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
    boolean existsByUsername(String username);
}
