package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Squadra;
import it.uniroma3.siw.torneo_calcio.repository.SquadraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service

public class SquadraService {

    private final SquadraRepository squadraRepository;
    
    

    public SquadraService(SquadraRepository squadraRepository) {
		super();
		this.squadraRepository = squadraRepository;
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
    
    @Transactional(readOnly = true)
    public Optional<Squadra> findByIdWithGiocatori(Long id) {
        return squadraRepository.findWithGiocatoriById(id);
    }
}