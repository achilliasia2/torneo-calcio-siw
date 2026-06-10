package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Giocatore;
import it.uniroma3.siw.torneo_calcio.repository.GiocatoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class GiocatoreService {

    
    private GiocatoreRepository giocatoreRepository;
    

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
		super();
		this.giocatoreRepository = giocatoreRepository;
	}

	@Transactional(readOnly = true)
    public Optional<Giocatore> findById(Long id) {
        return giocatoreRepository.findById(id);
    }

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }
}