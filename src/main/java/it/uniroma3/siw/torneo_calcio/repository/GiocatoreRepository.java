package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
}