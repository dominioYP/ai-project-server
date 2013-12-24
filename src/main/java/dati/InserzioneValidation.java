package dati;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component(value="InserzioneFormValidator")
public class InserzioneValidation {
	
	public void validate(Object target, Errors errors){
		
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
		
	}

}
