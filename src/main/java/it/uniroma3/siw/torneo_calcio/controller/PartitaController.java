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

import java.util.List;

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
    public String nuovaPartitaForm(@RequestParam(required = false) Long torneoId, Model model) {
        Partita partita = new Partita();
        if (torneoId != null) {
            torneoService.findById(torneoId).ifPresent(partita::setTorneo);
        }

        model.addAttribute("partita", partita);
        caricaDatiForm(model, torneoId);
        return "admin/partite/form";
    }

    @PostMapping("/admin/partite/nuova")
    public String salvaPartita(@Valid @ModelAttribute Partita partita, BindingResult bindingResult, Model model) {
        Long torneoId = partita.getTorneo() != null ? partita.getTorneo().getId() : null;
        if (bindingResult.hasErrors()) {
            caricaDatiForm(model, torneoId);
            return "admin/partite/form";
        }
        if (partita.getSquadraHome() != null
                && partita.getSquadraAway() != null
                && partita.getSquadraHome().getId().equals(partita.getSquadraAway().getId())) {
            bindingResult.reject("squadre.uguali", "Le due squadre devono essere diverse");
            caricaDatiForm(model, torneoId);
            return "admin/partite/form";
        }
        if (!squadreIscritteAlTorneo(torneoId, partita)) {
            bindingResult.reject("squadre.non.iscritte", "Le squadre devono essere iscritte al torneo selezionato");
            caricaDatiForm(model, torneoId);
            return "admin/partite/form";
        }
        partita.setStato(StatoPartita.SCHEDULED);
        partitaService.save(partita);
        return "redirect:/tornei/" + torneoId;
    }

    private void caricaDatiForm(Model model, Long torneoId) {
        model.addAttribute("tornei", torneoService.findAll());
        model.addAttribute("squadre", torneoId != null ? torneoService.findSquadreByTorneoId(torneoId) : List.of());
        model.addAttribute("arbitri", arbitroService.findAll());
        model.addAttribute("torneoSelezionatoId", torneoId);
    }

    private boolean squadreIscritteAlTorneo(Long torneoId, Partita partita) {
        if (torneoId == null || partita.getSquadraHome() == null || partita.getSquadraAway() == null) {
            return false;
        }
        List<Long> squadreIscritte = torneoService.findSquadreByTorneoId(torneoId)
                .stream()
                .map(squadra -> squadra.getId())
                .toList();
        return squadreIscritte.contains(partita.getSquadraHome().getId())
                && squadreIscritte.contains(partita.getSquadraAway().getId());
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
