package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Inserzione;
import hibernate.Prodotto;
import hibernate.Sottocategoria;
import hibernate.Supermercato;
import hibernate.Utente;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.InserzioneForm;
import dati.InserzioneValidation;

@Controller
public class InserzioneController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		
		this.dati=dati;
		
	}
	
	@Autowired
	private InserzioneValidation inserzioneValidator;
	
	public void setInserzioneValidator(InserzioneValidation inserzioneValidator){
		this.inserzioneValidator=inserzioneValidator;
	}
	
	@RequestMapping(value="/inserzione")
	public String showForm(Map model){
		InserzioneForm inserzioneForm = new InserzioneForm();
		model.put("inserzioneForm", inserzioneForm);
		
		Set<String> categorie = new HashSet<String>();
		
		for(Categoria c : dati.getCategorie()){
			categorie.add(c.getNome());
		}
		
		model.put("categorie", categorie);
		
		Set<String> argomenti = new HashSet<String>();
		
		for(Argomenti a : dati.getArgomenti()){
			
			argomenti.add(a.getArg1());
			
		}
		model.put("argomenti", argomenti);
		return "inserzione";
	}
	
	@RequestMapping(value="/inserzione/sottocategorie/{name}",method = RequestMethod.GET,consumes="application/json")
	public @ResponseBody Set<String> getSottoCategorie(@PathVariable String name){
		
		Set<String> sottocategorie = new HashSet<String>();
		for(Categoria c : dati.getCategorie()){
			if(c.getNome().equals(name)){
				for(Sottocategoria s : (Set<Sottocategoria>) c.getSottocategorias()){
					sottocategorie.add(s.getNome());
				}
				break;
			}
		}
		
		return sottocategorie;
		
	}
	
	@RequestMapping(value="/inserzione/getSuggerimenti/{name}",method= RequestMethod.GET)
	public @ResponseBody Set<String> getSuggerimenti(@PathVariable String name,String term){
		
		Set<String> results = new HashSet<String>();
		if(name.equals("prodotti")){
			for(Prodotto prodotto : dati.getProdotti()){
				if(prodotto.getDescrizione().contains(term)){
					results.add(prodotto.getDescrizione());
				}
			}
		}
		if(name.equals("supermercati")){
			for(Supermercato s : dati.getSupermercati()){
				if(s.getNome().contains(term)){
					results.add(s.getNome());
				}
			}
		}
		return results;
		
	}
	
	@RequestMapping(value="/inserzione",method= RequestMethod.POST)
	public ModelAndView processInserzione(@Valid InserzioneForm inserzioneForm,BindingResult result,Principal principal){
		
		boolean inserimentoSupermercato=false;
		boolean inserimentoInserzione=false;
		boolean inserimentoProdotto=false;
		Utente utente=null;
		Supermercato supermercato = null;
		int idInsererzione=-1;
		int idProdotto = -1;
		int idSupermercato = -1;
		try{	
			inserzioneValidator.validate(inserzioneForm, result,principal);
			if(result.hasErrors()){
				return new ModelAndView("inserzione");
			}
			
			
			
		
			for(Supermercato s : dati.getSupermercati()){
				if(s.getNome().equals(inserzioneForm.getSupermercato())){
					supermercato=s;
					break;
				}
			}
			
			if(supermercato==null){
				idSupermercato=dati.insertSupermercato(inserzioneForm.getSupermercato(), inserzioneForm.getLat(), inserzioneForm.getLng());
				inserimentoSupermercato=true;
				for(Supermercato s : dati.getSupermercati()){
					if(s.getIdSupermercato().equals(idSupermercato)){
						supermercato=s;
						break;
					}
				}
			}
			for(Utente u : dati.getUtenti()){
				if(u.getMail().equals(principal.getName())){
					utente=u;
					break;
				}
			}
			boolean trovato = false;
			for(Prodotto p : dati.getProdotti()){
				if(p.getCodiceBarre()==inserzioneForm.getCodiceBarre()){
					trovato=true;		
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){
							
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),null);
							inserimentoInserzione=true;
						}else{
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), null);
							inserimentoInserzione=true;
						}
				}
			}
		
			if(!trovato){
				Sottocategoria sottocategoria=null;
				for(Sottocategoria s : dati.getSottocategorie()){
					if(s.getNome().equals(inserzioneForm.getSottoCategoria())){
						sottocategoria=s;
						break;
					}
				}
				idProdotto=dati.insertProdotto(sottocategoria, inserzioneForm.getCodiceBarre(), inserzioneForm.getDescrizione());
				inserimentoProdotto=true;
				for(Prodotto p : dati.getProdotti()){
					if(p.getIdProdotto().equals(idProdotto)){
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){
							
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),null);
							inserimentoInserzione=true;
						}else{
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), null);
							inserimentoInserzione=true;
						}
						break;
					}
				}
			}
		}catch(Exception e){
			System.out.println(inserimentoInserzione+" "+inserimentoProdotto+" "+inserimentoSupermercato);
			if(inserimentoInserzione){
				dati.deleteInserzione(idInsererzione);
			}
			if(inserimentoProdotto){
				dati.deleteProdotto(idProdotto);
			}
			if(inserimentoSupermercato){
				dati.deleteSupermercato(idSupermercato);
			}
			e.printStackTrace();
			return new ModelAndView("inserzione","error","errore nell'immissione del form");
		}
		
		return new ModelAndView("inserzionesuccess","inserzione",inserzioneForm);
	}
	
	
	
	

}
