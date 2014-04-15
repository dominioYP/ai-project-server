package dati;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class InserzioneForm {
	@NotEmpty
	@Size(min= 5,max=50)
	private String descrizione;
	
	private long codiceBarre;
	
	private CommonsMultipartFile file;	

	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	private String foto;
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@NotEmpty
	private String categoria;
	
	@NotEmpty
	private String sottoCategoria;
	
	private float prezzo;
	
	private List<String> argomenti;
	
	public List<String> getArgomenti() {
		return argomenti;
	}

	public void setArgomenti(List<String> argomenti) {
		this.argomenti = argomenti;
	}

	@NotEmpty
	private String indirizzo;
	
	private float lat,lng;
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	@NotEmpty
	private String supermercato;
	
	public String getSupermercato() {
		return supermercato;
	}

	public void setSupermercato(String supermercato) {
		this.supermercato = supermercato;
	}

	private String arg1_corpo;
	@NotEmpty
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String dataInizio;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private String dataFine;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public long getCodiceBarre() {
		return codiceBarre;
	}

	public void setCodiceBarre(long codiceBarre) {
		this.codiceBarre = codiceBarre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSottoCategoria() {
		return sottoCategoria;
	}

	public void setSottoCategoria(String sottoCategoria) {
		this.sottoCategoria = sottoCategoria;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}
	

	public String getArg1_corpo() {
		return arg1_corpo;
	}

	public void setArg1_corpo(String arg1_corpo) {
		this.arg1_corpo = arg1_corpo;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	
	
	
	

}
