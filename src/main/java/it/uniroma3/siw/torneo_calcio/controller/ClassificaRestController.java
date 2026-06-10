package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClassificaRestController {

    private TorneoService torneoService;

    
    public ClassificaRestController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @GetMapping("/tornei/{id}/classifica")
    public ResponseEntity<Map<String, Object>> getClassifica(@PathVariable Long id) {
        return torneoService.findById(id).map(torneo -> {
            Map<String, Object> risposta = new HashMap<>();
            risposta.put("torneo", torneo.getNome());
            risposta.put("classifica", torneoService.calcolaClassifica(id));
            return ResponseEntity.ok(risposta);
        }).orElse(ResponseEntity.notFound().build());
    }
}