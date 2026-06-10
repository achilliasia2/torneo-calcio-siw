package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Torneo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    @EntityGraph(attributePaths = {"partite"})
    Optional<Torneo> findWithPartiteById(Long id);
    
 // Strategia 1: LAZY (default)
    Optional<Torneo> findById(Long id); // già presente

    // Strategia 2: EAGER con EntityGraph
    @EntityGraph(attributePaths = {"squadre"})
    Optional<Torneo> findWithSquadreById(Long id); // già presente

    // Strategia 3: JOIN FETCH con query JPQL
    @Query("SELECT t FROM Torneo t LEFT JOIN FETCH t.squadre WHERE t.id = :id")
    Optional<Torneo> findWithJoinFetchById(@Param("id") Long id);
}