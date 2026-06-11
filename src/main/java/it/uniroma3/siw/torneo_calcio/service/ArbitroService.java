package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Arbitro;
import it.uniroma3.siw.torneo_calcio.repository.ArbitroRepository;
import it.uniroma3.siw.torneo_calcio.repository.PartitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final PartitaRepository partitaRepository;
    
    

    public ArbitroService(ArbitroRepository arbitroRepository, PartitaRepository partitaRepository) {

		this.arbitroRepository = arbitroRepository;
        this.partitaRepository = partitaRepository;
	}

	@Transactional(readOnly = true)
    public List<Arbitro> findAll() {
        return arbitroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Arbitro> findById(Long id) {
        return arbitroRepository.findById(id);
    }

    @Transactional
    public Arbitro save(Arbitro arbitro) {
        return arbitroRepository.save(arbitro);
    }

    @Transactional
    public boolean deleteIfNotUsedInPartite(Long id) {
        if (partitaRepository.countByArbitroId(id) > 0) {
            return false;
        }
        arbitroRepository.deleteById(id);
        return true;
    }
}
