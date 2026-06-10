package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Commento;
import it.uniroma3.siw.torneo_calcio.model.Partita;
import it.uniroma3.siw.torneo_calcio.model.Utente;
import it.uniroma3.siw.torneo_calcio.repository.UtenteRepository;
import it.uniroma3.siw.torneo_calcio.service.CommentoService;
import it.uniroma3.siw.torneo_calcio.service.PartitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class CommentoController {

    private CommentoService commentoService;
    private PartitaService partitaService;
    private UtenteRepository utenteRepository;

   
    public CommentoController(CommentoService commentoService,
                               PartitaService partitaService,
                               UtenteRepository utenteRepository) {
        this.commentoService = commentoService;
        this.partitaService = partitaService;
        this.utenteRepository = utenteRepository;
    }

    // pagina dettaglio partita con commenti
    @GetMapping("/partite/{id}")
    public String dettaglioPartita(@PathVariable Long id, Model model) {
        partitaService.findById(id).ifPresent(p -> {
            model.addAttribute("partita", p);
            model.addAttribute("commenti", commentoService.findByPartitaId(id));
            model.addAttribute("nuovoCommento", new Commento());
        });
        return "partite/dettaglio";
    }

    // aggiungi commento
    @PostMapping("/commenti/{partitaId}/nuovo")
    public String aggiungiCommento(@PathVariable Long partitaId,
                                    @Valid @ModelAttribute Commento commento,
                                    BindingResult bindingResult,
                                    Authentication auth) {
        if (bindingResult.hasErrors()) {
            return "redirect:/partite/" + partitaId;
        }
        Optional<Partita> partita = partitaService.findById(partitaId);
        Optional<Utente> utente = utenteRepository.findByUsername(auth.getName());

        if (partita.isPresent() && utente.isPresent()) {
            commento.setPartita(partita.get());
            commento.setUtente(utente.get());
            commento.setDataCreazione(LocalDateTime.now());
            commentoService.save(commento);
        }
        return "redirect:/partite/" + partitaId;
    }

    // modifica commento
    @GetMapping("/commenti/{id}/modifica")
    public String modificaCommentoForm(@PathVariable Long id,
                                        Model model,
                                        Authentication auth) {
        commentoService.findById(id).ifPresent(c -> {
            // solo il proprietario può modificare
            if (c.getUtente().getUsername().equals(auth.getName())) {
                model.addAttribute("commento", c);
            }
        });
        return "commenti/form";
    }

    // salva modifica commento
    @PostMapping("/commenti/{id}/modifica")
    public String salvaModificaCommento(@PathVariable Long id,
                                         @Valid @ModelAttribute Commento commentoModificato,
                                         BindingResult bindingResult,
                                         Authentication auth) {
        if (bindingResult.hasErrors()) {
            return "commenti/form";
        }
        commentoService.findById(id).ifPresent(c -> {
            if (c.getUtente().getUsername().equals(auth.getName())) {
                c.setTesto(commentoModificato.getTesto());
                commentoService.save(c);
            }
        });
        Long partitaId = commentoService.findById(id)
                .map(c -> c.getPartita().getId())
                .orElse(null);
        return "redirect:/partite/" + partitaId;
    }
}
