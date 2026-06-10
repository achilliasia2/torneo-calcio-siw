package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Commento;
import it.uniroma3.siw.torneo_calcio.repository.CommentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentoService {

    private CommentoRepository commentoRepository;

   
    public CommentoService(CommentoRepository commentoRepository) {
        this.commentoRepository = commentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Commento> findByPartitaId(Long partitaId) {
        return commentoRepository.findByPartitaId(partitaId);
    }

    @Transactional(readOnly = true)
    public Optional<Commento> findById(Long id) {
        return commentoRepository.findById(id);
    }

    @Transactional
    public Commento save(Commento commento) {
        return commentoRepository.save(commento);
    }

    @Transactional
    public void deleteById(Long id) {
        commentoRepository.deleteById(id);
    }
}