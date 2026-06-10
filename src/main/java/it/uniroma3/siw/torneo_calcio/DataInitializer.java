package it.uniroma3.siw.torneo_calcio;

import it.uniroma3.siw.torneo_calcio.model.Utente;
import it.uniroma3.siw.torneo_calcio.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (utenteRepository.findByUsername("admin").isEmpty()) {
            Utente admin = new Utente();
            String adminPassword = System.getenv().getOrDefault("ADMIN_PASSWORD", "admin123");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRuolo("ADMIN");
            utenteRepository.save(admin);
            System.out.println(">>> Utente admin creato!");
        }
    }
}
