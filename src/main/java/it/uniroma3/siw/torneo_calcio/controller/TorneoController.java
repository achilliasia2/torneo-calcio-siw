package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Torneo;
import it.uniroma3.siw.torneo_calcio.repository.TorneoRepository;
import it.uniroma3.siw.torneo_calcio.service.PartitaService;
import it.uniroma3.siw.torneo_calcio.service.SquadraService;
import it.uniroma3.siw.torneo_calcio.service.TorneoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller

public class TorneoController {

    private final TorneoService torneoService;
    private final PartitaService partitaService;
  
    private SquadraService squadraService;
    
    private TorneoRepository torneoRepository;
    

   

	

	public TorneoController(TorneoService torneoService, PartitaService partitaService, SquadraService squadraService,
			TorneoRepository torneoRepository) {
		super();
		this.torneoService = torneoService;
		this.partitaService = partitaService;
		this.squadraService = squadraService;
		this.torneoRepository = torneoRepository;
	}

    // pagina principale - lista tornei (pubblica)
    @GetMapping("/tornei")
    public String listaTornei(@RequestParam(required = false) String q, Model model) {
        List<Torneo> tornei = (q != null && !q.isBlank())
                ? torneoService.cercaPerNome(q.trim())
                : torneoService.findAll();
        Map<Long, Long> numeroPartitePerTorneo = tornei.stream()
                .collect(Collectors.toMap(
                        Torneo::getId,
                        torneo -> partitaService.countByTorneoId(torneo.getId())
                ));
        model.addAttribute("tornei", tornei);
        model.addAttribute("numeroPartitePerTorneo", numeroPartitePerTorneo);
        model.addAttribute("q", q);
        return "tornei/lista";
    }

    // dettaglio singolo torneo (pubblica)
    @GetMapping("/tornei/{id}")
    public String dettaglioTorneo(@PathVariable Long id, Model model) {
    	torneoService.findByIdWithSquadre(id).ifPresent(t -> {
            model.addAttribute("torneo", t);
            model.addAttribute("squadre", t.getSquadre());
            model.addAttribute("partite", partitaService.findByTorneoId(id));
        });
        return "tornei/dettaglio";
    }
    // form per creare nuovo torneo (solo ADMIN)
    @GetMapping("/admin/tornei/nuovo")
    public String nuovoTorneoForm(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "admin/tornei/form";
    }

    // salva nuovo torneo (solo ADMIN)
    @PostMapping("/admin/tornei/nuovo")
    public String salvaTorneo(@Valid @ModelAttribute Torneo torneo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/tornei/form";
        }
        torneoService.save(torneo);
        return "redirect:/tornei";
    }

    // form per modificare torneo esistente (solo ADMIN)
    @GetMapping("/admin/tornei/{id}/modifica")
    public String modificaTorneoForm(@PathVariable Long id, Model model) {
        torneoService.findById(id).ifPresent(t -> model.addAttribute("torneo", t));
        return "admin/tornei/form";
    }

    // salva modifiche torneo (solo ADMIN)
    @PostMapping("/admin/tornei/{id}/modifica")
    public String aggiornaTorneo(@PathVariable Long id, @Valid @ModelAttribute Torneo torneo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            torneo.setId(id);
            return "admin/tornei/form";
        }
        torneo.setId(id);
        torneoService.save(torneo);
        return "redirect:/tornei";
    }

    @PostMapping("/admin/tornei/{id}/elimina")
    public String eliminaTorneo(@PathVariable Long id) {
        torneoService.deleteById(id);
        return "redirect:/tornei";
    }
    
  
    @GetMapping("/tornei/{id}/classifica")
    public String classifica(@PathVariable Long id, Model model) {
        torneoService.findById(id).ifPresent(t -> {
            model.addAttribute("torneo", t);
            model.addAttribute("classifica", torneoService.calcolaClassifica(id));
        });
        return "tornei/classifica";
    }
    
    @GetMapping("/react/classifica")
    public String classificaReact(@RequestParam Long torneoId, 
                                   HttpServletResponse response) throws IOException {
        response.sendRedirect("/react/classifica.html?torneoId=" + torneoId);
        return null;
    }
    
    @GetMapping("/admin/tornei/{id}/squadre")
    public String gestisciSquadre(@PathVariable Long id, Model model) {
        torneoService.findByIdWithSquadre(id).ifPresent(t -> {
            model.addAttribute("torneo", t);
            model.addAttribute("squadreIscritte", t.getSquadre());
            model.addAttribute("tutteLeSquadre", squadraService.findAll());
        });
        return "admin/tornei/squadre";
    }

    @PostMapping("/admin/tornei/{torneoId}/squadre/{squadraId}/aggiungi")
    public String aggiungiSquadra(@PathVariable Long torneoId,
                                   @PathVariable Long squadraId) {
        torneoService.aggiungiSquadra(torneoId, squadraId);
        return "redirect:/admin/tornei/" + torneoId + "/squadre";
    }

    @PostMapping("/admin/tornei/{torneoId}/squadre/{squadraId}/rimuovi")
    public String rimuoviSquadra(@PathVariable Long torneoId,
                                  @PathVariable Long squadraId) {
        torneoService.rimuoviSquadra(torneoId, squadraId);
        return "redirect:/admin/tornei/" + torneoId + "/squadre";
    }
}
