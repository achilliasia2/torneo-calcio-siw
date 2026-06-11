package it.uniroma3.siw.torneo_calcio.service;

import it.uniroma3.siw.torneo_calcio.model.Partita;
import it.uniroma3.siw.torneo_calcio.model.Squadra;
import it.uniroma3.siw.torneo_calcio.model.StatoPartita;
import it.uniroma3.siw.torneo_calcio.model.Torneo;
import it.uniroma3.siw.torneo_calcio.repository.PartitaRepository;
import it.uniroma3.siw.torneo_calcio.repository.SquadraRepository;
import it.uniroma3.siw.torneo_calcio.repository.TorneoRepository;
import it.uniroma3.siw.torneo_calcio.repository.CommentoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class TorneoService {

	private TorneoRepository torneoRepository;
	private PartitaRepository partitaRepository;
	private SquadraRepository squadraRepository;
    private CommentoRepository commentoRepository;

	
	public TorneoService(TorneoRepository torneoRepository,
	                     PartitaRepository partitaRepository,
	                     SquadraRepository squadraRepository,
                         CommentoRepository commentoRepository) {
	    this.torneoRepository = torneoRepository;
	    this.partitaRepository = partitaRepository;
	    this.squadraRepository = squadraRepository;
        this.commentoRepository = commentoRepository;
	}

	@Transactional(readOnly = true)
    public List<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Torneo> cercaPerNome(String nome) {
        return torneoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public Optional<Torneo> findById(Long id) {
        return torneoRepository.findById(id);
    }

    @Transactional
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public void deleteById(Long id) {
        commentoRepository.deleteByTorneoId(id);
        torneoRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Torneo> findByIdWithSquadre(Long id) {
        return torneoRepository.findWithSquadreById(id);
    }

    @Transactional(readOnly = true)
    public List<Squadra> findSquadreByTorneoId(Long torneoId) {
        return torneoRepository.findWithSquadreById(torneoId)
                .map(Torneo::getSquadre)
                .orElse(List.of());
    }
    @Transactional(readOnly = true)
    public List<Map<String, Object>> calcolaClassifica(Long torneoId) {
        List<Partita> partite = partitaRepository.findByTorneoId(torneoId);
        Map<Long, Map<String, Object>> classifica = new LinkedHashMap<>();

        for (Partita p : partite) {
            if (p.getStato() != StatoPartita.PLAYED) continue;

            Squadra home = p.getSquadraHome();
            Squadra away = p.getSquadraAway();

            classifica.putIfAbsent(home.getId(), creaRiga(home));
            classifica.putIfAbsent(away.getId(), creaRiga(away));

            Map<String, Object> rigaHome = classifica.get(home.getId());
            Map<String, Object> rigaAway = classifica.get(away.getId());

            int gh = p.getGoalsHome();
            int ga = p.getGoalsAway();

            aggiorna(rigaHome, gh, ga);
            aggiorna(rigaAway, ga, gh);
        }

        List<Map<String, Object>> lista = new ArrayList<>(classifica.values());
        lista.sort((a, b) -> (int) b.get("punti") - (int) a.get("punti"));
        return lista;
    }

    private Map<String, Object> creaRiga(Squadra s) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("squadra", s.getNome());
        r.put("punti", 0);
        r.put("vittorie", 0);
        r.put("pareggi", 0);
        r.put("sconfitte", 0);
        r.put("gf", 0);
        r.put("gs", 0);
        return r;
    }

    private void aggiorna(Map<String, Object> riga, int gf, int gs) {
        riga.put("gf", (int) riga.get("gf") + gf);
        riga.put("gs", (int) riga.get("gs") + gs);
        if (gf > gs) {
            riga.put("vittorie", (int) riga.get("vittorie") + 1);
            riga.put("punti", (int) riga.get("punti") + 3);
        } else if (gf == gs) {
            riga.put("pareggi", (int) riga.get("pareggi") + 1);
            riga.put("punti", (int) riga.get("punti") + 1);
        } else {
            riga.put("sconfitte", (int) riga.get("sconfitte") + 1);
        }
    }
    
    @Transactional
    public void aggiungiSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = torneoRepository.findWithSquadreById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo non trovato"));
        Squadra squadra = squadraRepository.findById(squadraId)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));
        
        boolean giàPresente = torneo.getSquadre().stream()
                .anyMatch(s -> s.getId().equals(squadraId));
        
        if (!giàPresente) {
            torneo.getSquadre().add(squadra);
            torneoRepository.save(torneo);
        }
    }

    @Transactional
    public void rimuoviSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = torneoRepository.findWithSquadreById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo non trovato"));
        torneo.getSquadre().removeIf(s -> s.getId().equals(squadraId));
        torneoRepository.save(torneo);
    }
    
    @Transactional(readOnly = true)
    public Optional<Torneo> findByIdWithJoinFetch(Long id) {
        return torneoRepository.findWithJoinFetchById(id);
    }
    
}
