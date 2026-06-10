package it.uniroma3.siw.torneo_calcio.controller;

import it.uniroma3.siw.torneo_calcio.model.Utente;
import it.uniroma3.siw.torneo_calcio.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller

public class AuthController {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder ;
    
    

    public AuthController(UtenteService utenteService, PasswordEncoder passwordEncoder) {
		super();
		this.utenteService = utenteService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registrazione")
    public String registrazioneForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "auth/registrazione";
    }

    @PostMapping("/registrazione")
    public String registraUtente(@ModelAttribute Utente utente, BindingResult bindingResult) {
        if (utente.getUsername() == null || utente.getUsername().isBlank()) {
            bindingResult.rejectValue("username", "required", "Username obbligatorio");
        }
        if (utente.getPassword() == null || utente.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "required", "Password obbligatoria");
        }
        if (bindingResult.hasErrors()) {
            return "auth/registrazione";
        }
        if (utenteService.existsByUsername(utente.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "Username gia in uso");
            return "auth/registrazione";
        }
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo("USER");
        utenteService.save(utente);
        return "redirect:/login";
    }
}
