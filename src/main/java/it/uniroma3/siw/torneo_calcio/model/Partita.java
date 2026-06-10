package it.uniroma3.siw.torneo_calcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity

public class Partita {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataOra() {
		return dataOra;
	}

	public void setDataOra(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public Integer getGoalsHome() {
		return goalsHome;
	}

	public void setGoalsHome(Integer goalsHome) {
		this.goalsHome = goalsHome;
	}

	public Integer getGoalsAway() {
		return goalsAway;
	}

	public void setGoalsAway(Integer goalsAway) {
		this.goalsAway = goalsAway;
	}

	public StatoPartita getStato() {
		return stato;
	}

	public void setStato(StatoPartita stato) {
		this.stato = stato;
	}

	public Torneo getTorneo() {
		return torneo;
	}

	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}

	public Squadra getSquadraHome() {
		return squadraHome;
	}

	public void setSquadraHome(Squadra squadraHome) {
		this.squadraHome = squadraHome;
	}

	public Squadra getSquadraAway() {
		return squadraAway;
	}

	public void setSquadraAway(Squadra squadraAway) {
		this.squadraAway = squadraAway;
	}

	public Arbitro getArbitro() {
		return arbitro;
	}

	public void setArbitro(Arbitro arbitro) {
		this.arbitro = arbitro;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime dataOra;

    @NotBlank
    private String luogo;

    private Integer goalsHome;
    private Integer goalsAway;

    @Enumerated(EnumType.STRING)
    private StatoPartita stato;

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "squadra_home_id")
    private Squadra squadraHome;

    @ManyToOne
    @JoinColumn(name = "squadra_away_id")
    private Squadra squadraAway;

    @ManyToOne
    @JoinColumn(name = "arbitro_id")
    private Arbitro arbitro;
}