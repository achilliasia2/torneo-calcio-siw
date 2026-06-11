package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Partita;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartitaRepository extends JpaRepository<Partita, Long> {
    List<Partita> findByTorneoId(Long torneoId);
    long countByTorneoId(Long torneoId);
    long countBySquadraHomeIdOrSquadraAwayId(Long squadraHomeId, Long squadraAwayId);
    long countByArbitroId(Long arbitroId);
}
