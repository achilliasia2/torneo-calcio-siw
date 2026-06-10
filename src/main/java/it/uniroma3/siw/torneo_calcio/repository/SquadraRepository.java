package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Squadra;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadraRepository extends JpaRepository<Squadra, Long> {

    @EntityGraph(attributePaths = {"giocatori"})
    Optional<Squadra> findWithGiocatoriById(Long id);
}