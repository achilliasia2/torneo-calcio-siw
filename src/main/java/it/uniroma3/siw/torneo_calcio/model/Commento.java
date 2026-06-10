package it.uniroma3.siw.torneo_calcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String testo;

    private LocalDateTime dataCreazione;

    @ManyToOne
    @JoinColumn(name = "partita_id")
    private Partita partita;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    // getter e setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }
    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
    public Partita getPartita() { return partita; }
    public void setPartita(Partita partita) { this.partita = partita; }
    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
}