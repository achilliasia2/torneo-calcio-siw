package it.uniroma3.siw.torneo_calcio.repository;

import it.uniroma3.siw.torneo_calcio.model.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentoRepository extends JpaRepository<Commento, Long> {
    List<Commento> findByPartitaId(Long partitaId);
}