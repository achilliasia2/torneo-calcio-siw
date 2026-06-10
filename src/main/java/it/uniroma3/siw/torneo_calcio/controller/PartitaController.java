package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Partita;
import it.uniroma3.siw.torneo_calcio.model.StatoPartita;
import it.uniroma3.siw.torneo_calcio.service.ArbitroService;
import it.uniroma3.siw.torneo_calcio.service.PartitaService;
import it.uniroma3.siw.torneo_calcio.service.SquadraService;
import it.uniroma3.siw.torneo_calcio.service.TorneoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller

public class PartitaController {

    private final PartitaService partitaService;
    private final TorneoService torneoService;
    private final SquadraService squadraService;
    private final ArbitroService arbitroService;
    
    

    public PartitaController(PartitaService partitaService, TorneoService torneoService, SquadraService squadraService,
			ArbitroService arbitroService) {
		super();
		this.partitaService = partitaService;
		this.torneoService = torneoService;
		this.squadraService = squadraService;
		this.arbitroService = arbitroService;
	}

	@GetMapping("/tornei/{torneoId}/partite")
    public String calendarioPartite(@PathVariable Long torneoId, Model model) {
        model.addAttribute("partite", partitaService.findByTorneoId(torneoId));
        torneoService.findById(torneoId).ifPresent(t -> model.addAttribute("torneo", t));
        return "partite/calendario";
    }

    @GetMapping("/admin/partite/nuova")
    public String nuovaPartitaForm(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("arbitri", arbitroService.findAll());
        return "admin/partite/form";
    }

    @PostMapping("/admin/partite/nuova")
    public String salvaPartita(@Valid @ModelAttribute Partita partita, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            return "admin/partite/form";
        }
        if (partita.getSquadraHome() != null
                && partita.getSquadraAway() != null
                && partita.getSquadraHome().getId().equals(partita.getSquadraAway().getId())) {
            bindingResult.reject("squadre.uguali", "Le due squadre devono essere diverse");
            model.addAttribute("tornei", torneoService.findAll());
            model.addAttribute("squadre", squadraService.findAll());
            model.addAttribute("arbitri", arbitroService.findAll());
            return "admin/partite/form";
        }
        partita.setStato(StatoPartita.SCHEDULED);
        partitaService.save(partita);
        return "redirect:/tornei";
    }

    @GetMapping("/admin/partite/{id}/risultato")
    public String risultatoForm(@PathVariable Long id, Model model) {
        partitaService.findById(id).ifPresent(p -> model.addAttribute("partita", p));
        return "admin/partite/risultato";
    }

    @PostMapping("/admin/partite/{id}/risultato")
    public String salvaRisultato(@PathVariable Long id,
                                  @RequestParam int goalsHome,
                                  @RequestParam int goalsAway,
                                  Model model) {
        if (goalsHome < 0 || goalsAway < 0) {
            partitaService.findById(id).ifPresent(p -> model.addAttribute("partita", p));
            model.addAttribute("errore", "I gol non possono essere negativi");
            return "admin/partite/risultato";
        }
        partitaService.inserisciRisultato(id, goalsHome, goalsAway);
        return "redirect:/tornei";
    }

    @PostMapping("/admin/partite/{id}/elimina")
    public String eliminaPartita(@PathVariable Long id) {
        partitaService.deleteById(id);
        return "redirect:/tornei";
    }
}
