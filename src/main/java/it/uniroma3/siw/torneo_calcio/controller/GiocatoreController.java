package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Giocatore;
import it.uniroma3.siw.torneo_calcio.model.Squadra;
import it.uniroma3.siw.torneo_calcio.service.GiocatoreService;
import it.uniroma3.siw.torneo_calcio.service.SquadraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class GiocatoreController {

   
    private GiocatoreService giocatoreService;
    private SquadraService squadraService;


    public GiocatoreController(GiocatoreService giocatoreService, SquadraService squadraService) {
		super();
		this.giocatoreService = giocatoreService;
		this.squadraService = squadraService;
	}



	// form per aggiungere giocatore a una squadra
    @GetMapping("/admin/squadre/{squadraId}/giocatori/nuovo")
    public String nuovoGiocatoreForm(@PathVariable Long squadraId, Model model) {
        squadraService.findById(squadraId)
                      .ifPresent(s -> model.addAttribute("squadra", s));
        model.addAttribute("giocatore", new Giocatore());
        return "admin/giocatori/form";
    }

    

	// salva nuovo giocatore
    @PostMapping("/admin/squadre/{squadraId}/giocatori/nuovo")
    public String salvaGiocatore(@PathVariable Long squadraId,
                                  @Valid @ModelAttribute Giocatore giocatore,
                                  BindingResult bindingResult,
                                  Model model) {
        Optional<Squadra> squadra = squadraService.findById(squadraId);
        if (bindingResult.hasErrors()) {
            squadra.ifPresent(s -> model.addAttribute("squadra", s));
            return "admin/giocatori/form";
        }
        squadra.ifPresent(s -> {
            giocatore.setSquadra(s);
            giocatoreService.save(giocatore);
        });
        return "redirect:/squadre/" + squadraId;
    }

    // form modifica giocatore
    @GetMapping("/admin/giocatori/{id}/modifica")
    public String modificaGiocatoreForm(@PathVariable Long id, Model model) {
        giocatoreService.findById(id).ifPresent(g -> {
            model.addAttribute("giocatore", g);
            model.addAttribute("squadra", g.getSquadra());
        });
        return "admin/giocatori/form";
    }

    // salva modifiche giocatore
    @PostMapping("/admin/giocatori/{id}/modifica")
    public String aggiornaGiocatore(@PathVariable Long id,
                                     @Valid @ModelAttribute Giocatore giocatore,
                                     BindingResult bindingResult,
                                     Model model) {
        Optional<Giocatore> esistente = giocatoreService.findById(id);
        if (bindingResult.hasErrors()) {
            giocatore.setId(id);
            esistente.map(Giocatore::getSquadra)
                    .ifPresent(s -> model.addAttribute("squadra", s));
            return "admin/giocatori/form";
        }
        giocatoreService.findById(id).ifPresent(g -> {
            giocatore.setId(id);
            giocatore.setSquadra(g.getSquadra());
            giocatoreService.save(giocatore);
        });
        return "redirect:/squadre/" + giocatoreService.findById(id)
                .map(g -> g.getSquadra().getId().toString())
                .orElse("");
    }

    // elimina giocatore
    @PostMapping("/admin/giocatori/{id}/elimina")
    public String eliminaGiocatore(@PathVariable Long id) {
        Long squadraId = giocatoreService.findById(id)
                .map(g -> g.getSquadra().getId())
                .orElse(null);
        giocatoreService.deleteById(id);
        return "redirect:/squadre/" + (squadraId != null ? squadraId : "");
    }
}
