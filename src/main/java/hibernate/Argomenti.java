package hibernate;

// Generated 7-apr-2014 22.47.58 by Hibernate Tools 3.4.0.CR1

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

/**
 * Argomenti generated by hbm2java
 */
@Entity
@Table(name = "argomenti", catalog = "supermercati")
public class Argomenti implements java.io.Serializable {

	private Integer idArgomenti;
	private String arg1;
	private Set argomentiInserziones = new HashSet(0);

	public Argomenti() {
	}

	public Argomenti(String arg1, Set argomentiInserziones) {
		this.arg1 = arg1;
		this.argomentiInserziones = argomentiInserziones;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Argomenti", unique = true, nullable = false)
	public Integer getIdArgomenti() {
		return this.idArgomenti;
	}

	public void setIdArgomenti(Integer idArgomenti) {
		this.idArgomenti = idArgomenti;
	}

	@Column(name = "arg1", length = 30)
	public String getArg1() {
		return this.arg1;
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "argomenti")
	public Set getArgomentiInserziones() {
		return this.argomentiInserziones;
	}

	public void setArgomentiInserziones(Set argomentiInserziones) {
		this.argomentiInserziones = argomentiInserziones;
	}

}
