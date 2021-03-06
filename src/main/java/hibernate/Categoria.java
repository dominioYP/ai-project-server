package hibernate;

// Generated 17-apr-2014 0.21.19 by Hibernate Tools 3.4.0.CR1

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
import javax.persistence.UniqueConstraint;

/**
 * Categoria generated by hbm2java
 */
@Entity
@Table(name = "categoria", catalog = "supermercati", uniqueConstraints = @UniqueConstraint(columnNames = "Nome"))
public class Categoria implements java.io.Serializable {

	private Integer idCategoria;
	private String nome;
	private Set sottocategorias = new HashSet(0);

	public Categoria() {
	}

	public Categoria(String nome, Set sottocategorias) {
		this.nome = nome;
		this.sottocategorias = sottocategorias;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_Categoria", unique = true, nullable = false)
	public Integer getIdCategoria() {
		return this.idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	@Column(name = "Nome", unique = true, length = 45)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "categoria")
	public Set getSottocategorias() {
		return this.sottocategorias;
	}

	public void setSottocategorias(Set sottocategorias) {
		this.sottocategorias = sottocategorias;
	}

}
