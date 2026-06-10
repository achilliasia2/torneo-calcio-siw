package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Partita;
import it.uniroma3.siw.torneo_calcio.model.StatoPartita;
import it.uniroma3.siw.torneo_calcio.repository.PartitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service

public class PartitaService {

    private final PartitaRepository partitaRepository;
    
    

    public PartitaService(PartitaRepository partitaRepository) {
		super();
		this.partitaRepository = partitaRepository;
	}

	@Transactional(readOnly = true)
    public List<Partita> findAll() {
        return partitaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Partita> findById(Long id) {
        return partitaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Partita> findByTorneoId(Long torneoId) {
        return partitaRepository.findByTorneoId(torneoId);
    }

    @Transactional(readOnly = true)
    public long countByTorneoId(Long torneoId) {
        return partitaRepository.countByTorneoId(torneoId);
    }

    @Transactional
    public Partita save(Partita partita) {
        return partitaRepository.save(partita);
    }

    @Transactional
    public Partita inserisciRisultato(Long id, int goalsHome, int goalsAway) {
        Partita partita = partitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));
        partita.setGoalsHome(goalsHome);
        partita.setGoalsAway(goalsAway);
        partita.setStato(StatoPartita.PLAYED);
        return partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        partitaRepository.deleteById(id);
    }
}
