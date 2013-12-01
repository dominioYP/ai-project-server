package hibernate;

// Generated 21-nov-2013 16.24.11 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Utente generated by hbm2java
 */
@Entity
@Table(name = "utente", catalog = "supermercati", uniqueConstraints = {
		@UniqueConstraint(columnNames = "Mail"),
		@UniqueConstraint(columnNames = "Nickname") })
public class Utente implements java.io.Serializable {

	private Integer idUtente;
	private String mail;
	private String nickname;
	private String password;
	private Date dataRegistrazione;
	private Boolean confermato;
	private String numeroCasuale;
	private Set valutazioneInserzionesForIdUtenteInserzionista = new HashSet(0);
	private Set valutazioneInserzionesForIdUtenteValutatore = new HashSet(0);
	private Set listaSpesas = new HashSet(0);
	private Set inserziones = new HashSet(0);
	private Set profilos = new HashSet(0);
	private Set listaDesideris = new HashSet(0);

	public Utente() {
	}

	public Utente(String mail, String nickname, String password,
			Date dataRegistrazione, Boolean confermato, String numeroCasuale,
			Set valutazioneInserzionesForIdUtenteInserzionista,
			Set valutazioneInserzionesForIdUtenteValutatore, Set listaSpesas,
			Set inserziones, Set profilos, Set listaDesideris) {
		this.mail = mail;
		this.nickname = nickname;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
		this.confermato = confermato;
		this.numeroCasuale = numeroCasuale;
		this.valutazioneInserzionesForIdUtenteInserzionista = valutazioneInserzionesForIdUtenteInserzionista;
		this.valutazioneInserzionesForIdUtenteValutatore = valutazioneInserzionesForIdUtenteValutatore;
		this.listaSpesas = listaSpesas;
		this.inserziones = inserziones;
		this.profilos = profilos;
		this.listaDesideris = listaDesideris;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Utente", unique = true, nullable = false)
	public Integer getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(Integer idUtente) {
		this.idUtente = idUtente;
	}

	@Column(name = "Mail", unique = true, length = 45)
	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Column(name = "Nickname", unique = true, length = 45)
	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "Password", length = 45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DataRegistrazione", length = 10)
	public Date getDataRegistrazione() {
		return this.dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	@Column(name = "Confermato")
	public Boolean getConfermato() {
		return this.confermato;
	}

	public void setConfermato(Boolean confermato) {
		this.confermato = confermato;
	}

	@Column(name = "NumeroCasuale", length = 45)
	public String getNumeroCasuale() {
		return this.numeroCasuale;
	}

	public void setNumeroCasuale(String numeroCasuale) {
		this.numeroCasuale = numeroCasuale;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utenteByIdUtenteInserzionista")
	public Set getValutazioneInserzionesForIdUtenteInserzionista() {
		return this.valutazioneInserzionesForIdUtenteInserzionista;
	}

	public void setValutazioneInserzionesForIdUtenteInserzionista(
			Set valutazioneInserzionesForIdUtenteInserzionista) {
		this.valutazioneInserzionesForIdUtenteInserzionista = valutazioneInserzionesForIdUtenteInserzionista;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utenteByIdUtenteValutatore")
	public Set getValutazioneInserzionesForIdUtenteValutatore() {
		return this.valutazioneInserzionesForIdUtenteValutatore;
	}

	public void setValutazioneInserzionesForIdUtenteValutatore(
			Set valutazioneInserzionesForIdUtenteValutatore) {
		this.valutazioneInserzionesForIdUtenteValutatore = valutazioneInserzionesForIdUtenteValutatore;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utente")
	public Set getListaSpesas() {
		return this.listaSpesas;
	}

	public void setListaSpesas(Set listaSpesas) {
		this.listaSpesas = listaSpesas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utente")
	public Set getInserziones() {
		return this.inserziones;
	}

	public void setInserziones(Set inserziones) {
		this.inserziones = inserziones;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "utente")
	public Set getProfilos() {
		return this.profilos;
	}

	public void setProfilos(Set profilos) {
		this.profilos = profilos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "utente")
	public Set getListaDesideris() {
		return this.listaDesideris;
	}

	public void setListaDesideris(Set listaDesideris) {
		this.listaDesideris = listaDesideris;
	}

}
