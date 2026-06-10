package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.service.TorneoService;
import it.uniroma3.siw.torneo_calcio.service.SquadraService;
import it.uniroma3.siw.torneo_calcio.service.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private PartitaService partitaService;

    @GetMapping("/")
    public String index(Model model) {
        // 1. Diciamo al layout di mostrare la sezione Hero (blu)
        model.addAttribute("isHome", true);
        
        // 2. Passiamo i numeri reali per le statistiche
        model.addAttribute("numeroTornei", torneoService.findAll().size());
        model.addAttribute("numeroSquadre", squadraService.findAll().size());
        model.addAttribute("numeroPartite", partitaService.findAll().size());
        
        // 3. Passiamo la lista tornei per le card in basso
        model.addAttribute("tornei", torneoService.findAll());
        
        return "index"; // Deve esserci un file templates/index.html
    }
}