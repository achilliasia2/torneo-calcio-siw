package it.uniroma3.siw.torneo_calcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Torneo {

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

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Squadra> getSquadre() {
		return squadre;
	}

	public void setSquadre(List<Squadra> squadre) {
		this.squadre = squadre;
	}

	public List<Partita> getPartite() {
		return partite;
	}

	public void setPartite(List<Partita> partite) {
		this.partite = partite;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private Integer anno;

    private String descrizione;

    @ManyToMany
    @JoinTable(
        name = "torneo_squadra",
        joinColumns = @JoinColumn(name = "torneo_id"),
        inverseJoinColumns = @JoinColumn(name = "squadra_id")
    )
    private List<Squadra> squadre = new ArrayList<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partita> partite = new ArrayList<>();
}
