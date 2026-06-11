package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Squadra;
import it.uniroma3.siw.torneo_calcio.model.Torneo;
import it.uniroma3.siw.torneo_calcio.repository.PartitaRepository;
import it.uniroma3.siw.torneo_calcio.repository.SquadraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class SquadraService {

    private final SquadraRepository squadraRepository;
    private final PartitaRepository partitaRepository;
    
    

    public SquadraService(SquadraRepository squadraRepository, PartitaRepository partitaRepository) {
		super();
		this.squadraRepository = squadraRepository;
        this.partitaRepository = partitaRepository;
	}

	@Transactional(readOnly = true)
    public List<Squadra> findAll() {
        return squadraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Squadra> findById(Long id) {
        return squadraRepository.findById(id);
    }

    @Transactional
    public Squadra save(Squadra squadra) {
        return squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteById(Long id) {
        squadraRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteIfNotUsedInPartite(Long id) {
        if (partitaRepository.countBySquadraHomeIdOrSquadraAwayId(id, id) > 0) {
            return false;
        }

        squadraRepository.findWithTorneiById(id).ifPresent(squadra -> {
            for (Torneo torneo : new ArrayList<>(squadra.getTornei())) {
                torneo.getSquadre().remove(squadra);
            }
            squadraRepository.delete(squadra);
        });

        return true;
    }
    
    @Transactional(readOnly = true)
    public Optional<Squadra> findByIdWithGiocatori(Long id) {
        return squadraRepository.findWithGiocatoriById(id);
    }
}
