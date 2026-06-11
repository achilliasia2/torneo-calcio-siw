package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Squadra;
import it.uniroma3.siw.torneo_calcio.service.SquadraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class SquadraController {

    private final SquadraService squadraService;
    
    

    public SquadraController(SquadraService squadraService) {
		super();
		this.squadraService = squadraService;
	}

	@GetMapping("/squadre")
    public String listaSquadre(Model model) {
        List<Squadra> squadre = squadraService.findAll();
        model.addAttribute("squadre", squadre);
        return "squadre/lista";
    }

	@GetMapping("/squadre/{id}")
	public String dettaglioSquadra(@PathVariable Long id, Model model) {
	    squadraService.findByIdWithGiocatori(id)
	                  .ifPresent(s -> {
	                      model.addAttribute("squadra", s);
	                      model.addAttribute("giocatori", s.getGiocatori());
	                  });
	    return "squadre/dettaglio";
	}

    @GetMapping("/admin/squadre/nuova")
    public String nuovaSquadraForm(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/squadre/form";
    }

    @PostMapping("/admin/squadre/nuova")
    public String salvaSquadra(@Valid @ModelAttribute Squadra squadra, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/squadre/form";
        }
        squadraService.save(squadra);
        return "redirect:/squadre";
    }

    @GetMapping("/admin/squadre/{id}/modifica")
    public String modificaSquadraForm(@PathVariable Long id, Model model) {
        squadraService.findById(id).ifPresent(s -> model.addAttribute("squadra", s));
        return "admin/squadre/form";
    }

    @PostMapping("/admin/squadre/{id}/modifica")
    public String aggiornaSquadra(@PathVariable Long id, @Valid @ModelAttribute Squadra squadra, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            squadra.setId(id);
            return "admin/squadre/form";
        }
        squadra.setId(id);
        squadraService.save(squadra);
        return "redirect:/squadre";
    }

    @PostMapping("/admin/squadre/{id}/elimina")
    public String eliminaSquadra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean eliminata = squadraService.deleteIfNotUsedInPartite(id);
        if (!eliminata) {
            redirectAttributes.addFlashAttribute("erroreEliminazione", "La squadra non puo essere eliminata perche e gia presente in una o piu partite.");
            return "redirect:/squadre/" + id;
        }
        return "redirect:/squadre";
    }
}
