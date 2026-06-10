package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Arbitro;
import it.uniroma3.siw.torneo_calcio.service.ArbitroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArbitroController {

    private ArbitroService arbitroService;

   
    public ArbitroController(ArbitroService arbitroService) {
        this.arbitroService = arbitroService;
    }

    @GetMapping("/admin/arbitri")
    public String listaArbitri(Model model) {
        model.addAttribute("arbitri", arbitroService.findAll());
        return "admin/arbitri/lista";
    }

    @GetMapping("/admin/arbitri/nuovo")
    public String nuovoArbitroForm(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "admin/arbitri/form";
    }

    @PostMapping("/admin/arbitri/nuovo")
    public String salvaArbitro(@Valid @ModelAttribute Arbitro arbitro, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/arbitri/form";
        }
        arbitroService.save(arbitro);
        return "redirect:/admin/arbitri";
    }

    @GetMapping("/admin/arbitri/{id}/modifica")
    public String modificaArbitroForm(@PathVariable Long id, Model model) {
        arbitroService.findById(id).ifPresent(a -> model.addAttribute("arbitro", a));
        return "admin/arbitri/form";
    }

    @PostMapping("/admin/arbitri/{id}/modifica")
    public String aggiornaArbitro(@PathVariable Long id, @Valid @ModelAttribute Arbitro arbitro, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            arbitro.setId(id);
            return "admin/arbitri/form";
        }
        arbitro.setId(id);
        arbitroService.save(arbitro);
        return "redirect:/admin/arbitri";
    }
}
