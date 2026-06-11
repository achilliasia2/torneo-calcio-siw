package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentoRepository extends JpaRepository<Commento, Long> {
    List<Commento> findByPartitaId(Long partitaId);

    @Modifying
    @Query("DELETE FROM Commento c WHERE c.partita.torneo.id = :torneoId")
    void deleteByTorneoId(@Param("torneoId") Long torneoId);
}
