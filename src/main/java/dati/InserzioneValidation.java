package dati;

import java.security.Principal;
import java.text.SimpleDateFormat;

import hibernate.Inserzione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component(value="InserzioneFormValidator")
public class InserzioneValidation {
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	
	
	public void validate(Object target, Errors errors,Principal principal){
		
		InserzioneForm inserzione = (InserzioneForm)target;
		
		if(inserzione.getDescrizione().length()<10){
			errors.rejectValue("descrizione", 
					"lengthOfDescrizione.InserzioneForm.descrizione", 
					"Descrizione troppo corta, deve essere almeno di 10 caratteri");
		}
		
		if(Integer.toString(inserzione.getCodiceBarre()).length()<5){
			errors.rejectValue("codiceBarre", 
					"lengthOfCodiceBarre.InserzioneForm.codiceBarre", 
					"Codice a barre troppo corto, deve essere almeno di 5 caratteri");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			for(Inserzione i : dati.getInserzioni() ){
				System.out.println(i.getProdotto().getCodiceBarre()+" "+inserzione.getCodiceBarre());
				if(i.getProdotto().getCodiceBarre()==inserzione.getCodiceBarre()&&
						i.getDataInizio().equals(sdf.parse(inserzione.getDataInizio()))&&
						i.getSupermercato().getLatitudine().intValue()==(int)inserzione.getLat()&&
						i.getSupermercato().getLongitudine().intValue()==(int)inserzione.getLng()&&
						i.getUtente().getMail().equals(principal.getName())){
					errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
							"è gia presente una tua inserzione con la stessa data");
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
