package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Prodotto;
import hibernate.Sottocategoria;
import hibernate.Supermercato;
import hibernate.Utente;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
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
	private ServletContext context;
	
	public void setServletContext(ServletContext context){
		this.context=context;
	}
	
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
		inserzioneForm.setDataInizio("dd/MM/yyyy");
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
	
	@RequestMapping(value="/inserzione/getSupermercati",method=RequestMethod.GET)
	public @ResponseBody ArrayNode getSupermercati(String lat,String lng){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ArrayNode results = factory.arrayNode();
		ObjectNode obj;
		
		for(Supermercato s : dati.getSupermercati()){
			if(distFrom(Float.parseFloat(lat), Float.parseFloat(lng), s.getLatitudine().floatValue(), s.getLongitudine().floatValue())<50){
				obj=factory.objectNode();
				obj.put("nome", s.getNome());
				obj.put("lat", s.getLatitudine());
				obj.put("lng", s.getLongitudine());
				results.add(obj);
			}
		}
		
		return results;
	}
	
	@RequestMapping(value="/inserzione/getSuggerimenti/{name}",method= RequestMethod.GET)
	public @ResponseBody ArrayNode getSuggerimenti(@PathVariable String name,String term){
		JsonNodeFactory factory = JsonNodeFactory.instance;
		
		ArrayNode results = factory.arrayNode();
		ObjectNode obj;
		if(name.equals("prodotti")){
			for(Prodotto prodotto : dati.getProdotti()){
				if(prodotto.getDescrizione().contains(term)){
					obj=factory.objectNode();
					obj.put(Long.toString(prodotto.getCodiceBarre()),prodotto.getDescrizione());
					results.add(obj);
				}
			}
		}
		if(name.equals("supermercatiNames")){
			for(Supermercato s : dati.getSupermercati()){
				if(s.getNome().contains(term)){	
					results.add(s.getNome());
				}
			}
		}
		if(name.equals("supermercatiLoc")){
			
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
				Map model = new HashMap<String, Object>();
				
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
				return new ModelAndView("inserzione",model);
			}
			String path = "";
			if(!inserzioneForm.getFoto().equals("")){
				URL url = new URL(inserzioneForm.getFoto());
				BufferedImage image = ImageIO.read(url);
				path = context.getRealPath("/")+"resources\\images"+File.separator+inserzioneForm.getCodiceBarre()+".png";
				File file = new File(path);
				File parent = file.getParentFile();
			    ImageIO.write(image, "png", file);
			    
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
							
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),path);
							inserimentoInserzione=true;
						}else{
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), path);
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
							
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),path);
							inserimentoInserzione=true;
						}else{
							idInsererzione=dati.insertInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), path);
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
	
	
	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 6371;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;


	    return new Float(dist).floatValue();
	 }
	

}
