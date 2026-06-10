package it.uniroma3.siw.torneo_calcio;

import it.uniroma3.siw.torneo_calcio.model.Torneo;
import it.uniroma3.siw.torneo_calcio.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("analisi")
public class AnalisiPrestazioniTest implements CommandLineRunner {

    
    private TorneoRepository torneoRepository;
    
    

    public AnalisiPrestazioniTest(TorneoRepository torneoRepository) {
		super();
		this.torneoRepository = torneoRepository;
	}



	@Override
    @Transactional
    public void run(String... args) throws Exception {
        Long torneoId = 1L;
        int iterazioni = 10;

        System.out.println("\n=== ANALISI PRESTAZIONI FETCH STRATEGIES ===\n");

        // Strategia 1: LAZY
        long totLazy = 0;
        for (int i = 0; i < iterazioni; i++) {
            long start = System.currentTimeMillis();
            Torneo t = torneoRepository.findById(torneoId).orElse(null);
            if (t != null) t.getSquadre().size(); // forza il caricamento
            totLazy += System.currentTimeMillis() - start;
        }
        System.out.printf("LAZY:        media %d ms su %d iterazioni%n",
                totLazy / iterazioni, iterazioni);

        // Strategia 2: EntityGraph
        long totEntityGraph = 0;
        for (int i = 0; i < iterazioni; i++) {
            long start = System.currentTimeMillis();
            Torneo t = torneoRepository.findWithSquadreById(torneoId).orElse(null);
            if (t != null) t.getSquadre().size();
            totEntityGraph += System.currentTimeMillis() - start;
        }
        System.out.printf("ENTITY GRAPH: media %d ms su %d iterazioni%n",
                totEntityGraph / iterazioni, iterazioni);

        // Strategia 3: JOIN FETCH
        long totJoinFetch = 0;
        for (int i = 0; i < iterazioni; i++) {
            long start = System.currentTimeMillis();
            Torneo t = torneoRepository.findWithJoinFetchById(torneoId).orElse(null);
            if (t != null) t.getSquadre().size();
            totJoinFetch += System.currentTimeMillis() - start;
        }
        System.out.printf("JOIN FETCH:   media %d ms su %d iterazioni%n",
                totJoinFetch / iterazioni, iterazioni);

        System.out.println("\n=== FINE ANALISI ===\n");
    }
}