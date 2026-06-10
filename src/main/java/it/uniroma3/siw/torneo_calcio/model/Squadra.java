package it.uniroma3.siw.torneo_calcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity

public class Squadra {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getAnnoFondazione() {
		return annoFondazione;
	}

	public void setAnnoFondazione(Integer annoFondazione) {
		this.annoFondazione = annoFondazione;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public List<Torneo> getTornei() {
		return tornei;
	}

	public void setTornei(List<Torneo> tornei) {
		this.tornei = tornei;
	}

	public List<Giocatore> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(List<Giocatore> giocatori) {
		this.giocatori = giocatori;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private Integer annoFondazione;

    @NotBlank
    private String citta;

    @ManyToMany(mappedBy = "squadre")
    private List<Torneo> tornei;

    @OneToMany(mappedBy = "squadra", cascade = CascadeType.ALL)
    private List<Giocatore> giocatori;
}