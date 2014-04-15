package ai.server.controller;

import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Inserzione;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
		
		for(Map.Entry<Integer,Categoria> c : dati.getCategorie().entrySet()){
			categorie.add(c.getValue().getNome());
		}
		
		model.put("categorie", categorie);
		
		Set<String> argomenti = new HashSet<String>();
		
		for(Map.Entry<Integer, Argomenti> a : dati.getArgomenti().entrySet()){
			
			argomenti.add(a.getValue().getArg1());
			
		}
		model.put("argomenti", argomenti);
		return "inserzione";
	}
	
	@RequestMapping(value="/inserzione/sottocategorie/{name}",method = RequestMethod.GET,consumes="application/json")
	public @ResponseBody Set<String> getSottoCategorie(@PathVariable String name){
		
		Set<String> sottocategorie = new HashSet<String>();
		for(Map.Entry<Integer, Categoria> c : dati.getCategorie().entrySet()){
			if(c.getValue().getNome().equals(name)){
				for(Sottocategoria s : (Set<Sottocategoria>) c.getValue().getSottocategorias()){
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
		
		for(Map.Entry<String, Supermercato> s : dati.getSupermercati().entrySet()){
			if(distFrom(Float.parseFloat(lat), Float.parseFloat(lng), s.getValue().getLatitudine().floatValue(), s.getValue().getLongitudine().floatValue())<3){
				obj=factory.objectNode();
				obj.put("nome", s.getValue().getNome());
				obj.put("lat", s.getValue().getLatitudine());
				obj.put("lng", s.getValue().getLongitudine());
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
			for(Map.Entry<Long, Prodotto> prodotto : dati.getProdotti().entrySet()){
				if(prodotto.getValue().getDescrizione().toLowerCase().contains(term.toLowerCase())){
					obj=factory.objectNode();
					obj.put(Long.toString(prodotto.getValue().getCodiceBarre()),prodotto.getValue().getDescrizione());
					results.add(obj);
				}
			}
		}
		if(name.equals("supermercati")){
			for(Map.Entry<String, Supermercato> s : dati.getSupermercati().entrySet()){
				if(s.getValue().getNome().toLowerCase().contains(term.toLowerCase())){	
					results.add(s.getValue().getNome());
				}
			}
		}
		return results;
		
	}
	
	@RequestMapping(value="/inserzione",method= RequestMethod.POST)
	public ModelAndView processInserzione(InserzioneForm inserzioneForm,BindingResult result,Principal principal){
		boolean inserimentoSupermercato=false;
		boolean inserimentoInserzione=false;
		boolean inserimentoProdotto=false;
		Utente utente=null;
		Supermercato supermercato = null;
		int idInsererzione=-1;
		int idProdotto = -1;
		int idSupermercato = -1;
		try{	
			if(result.hasErrors())
				System.out.println(result.getAllErrors().get(0).toString());
			inserzioneValidator.validate(inserzioneForm, result,principal);
			if(result.hasErrors()){
				Map model = new HashMap<String, Object>();		
				if(inserzioneForm.getFile() != null)
					inserzioneForm.setFile(null);
				inserzioneForm.setSupermercato(inserzioneForm.getSupermercato().split(" - ")[0]);
				model.put("inserzioneForm", inserzioneForm);				
				Set<String> categorie = new HashSet<String>();
				
				for(Map.Entry<Integer,Categoria> c : dati.getCategorie().entrySet()){
					categorie.add(c.getValue().getNome());
				}
				
				model.put("categorie", categorie);				
				Set<String> argomenti = new HashSet<String>();
				
				for(Map.Entry<Integer,Argomenti> a : dati.getArgomenti().entrySet()){					
					argomenti.add(a.getValue().getArg1());					
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
			if(inserzioneForm.getFile()!=null){
				path = context.getRealPath("/")+"resources\\images"+File.separator+inserzioneForm.getCodiceBarre()+".png";
				File file = new File(path);
				FileUtils.writeByteArrayToFile(file, inserzioneForm.getFile().getBytes());
				System.out.println("file salvato in : "+path);
			}
		
			supermercato = dati.getSupermercati().get(inserzioneForm.getSupermercato());
			
			if(supermercato == null){
				idSupermercato=dati.inserisciSupermercato(inserzioneForm.getSupermercato(), inserzioneForm.getLat(), inserzioneForm.getLng());
				inserimentoSupermercato = true;
				supermercato = dati.getSupermercati().get(inserzioneForm.getSupermercato());				
			}
			
			utente = dati.getUtenti().get(principal.getName());
			
			boolean trovato = false;
			Prodotto prodotto = dati.getProdotti().get(inserzioneForm.getCodiceBarre());
			//TODO Bisogna ancora inserire gli argomenti usati
			
			if(prodotto != null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				trovato = true;
				if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){				
					idInsererzione=dati.inserisciInserzione(utente, supermercato, prodotto, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),path,new HashSet<Argomenti>());
					inserimentoInserzione=true;
				}else{
					idInsererzione=dati.inserisciInserzione(utente, supermercato, prodotto, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), path,new HashSet<Argomenti>());
					inserimentoInserzione=true;
				}
			}
			
		
			if(!trovato){
				Sottocategoria sottocategoria=null;
				sottocategoria = dati.getSottocategorie().get(inserzioneForm.getSottoCategoria());
				idProdotto=dati.inserisciProdotto(sottocategoria, inserzioneForm.getCodiceBarre(), inserzioneForm.getDescrizione());
				inserimentoProdotto = true;
				Prodotto p = dati.getProdotti().get(inserzioneForm.getCodiceBarre());
				//Si devono inserire gli argomenti usati
				if(p.getIdProdotto().equals(idProdotto)){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					if(inserzioneForm.getDataFine()!=null&&!(inserzioneForm.getDataFine().equals(""))){						
						idInsererzione=dati.inserisciInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), sdf.parse(inserzioneForm.getDataFine()), inserzioneForm.getDescrizione(),path, new HashSet<Argomenti>());
						inserimentoInserzione=true;
					}else{
						idInsererzione=dati.inserisciInserzione(utente, supermercato, p, inserzioneForm.getPrezzo(), sdf.parse(inserzioneForm.getDataInizio()), null, inserzioneForm.getDescrizione(), path, new HashSet<Argomenti>());
						inserimentoInserzione=true;
					}
				}
				
			}
		}catch(Exception e){
			System.out.println(inserimentoInserzione+" "+inserimentoProdotto+" "+inserimentoSupermercato);
			if(inserimentoInserzione){
				dati.eliminaInserzione(idInsererzione);
			}
			if(inserimentoProdotto){
				dati.eliminaProdotto(idProdotto);
			}
			if(inserimentoSupermercato){
				dati.eliminaSupermercato(idSupermercato);
			}
			e.printStackTrace();
			
			Map model = new HashMap<String, Object>();				
			model.put("inserzioneForm", inserzioneForm);				
			Set<String> categorie = new HashSet<String>();
			
			for(Map.Entry<Integer,Categoria> c : dati.getCategorie().entrySet()){
				categorie.add(c.getValue().getNome());
			}
			
			model.put("categorie", categorie);				
			Set<String> argomenti = new HashSet<String>();
			
			for(Map.Entry<Integer,Argomenti> a : dati.getArgomenti().entrySet()){					
				argomenti.add(a.getValue().getArg1());					
			}
			model.put("argomenti", argomenti);
			model.put("error","errore nell'immissione del form");
			return new ModelAndView("inserzione",model);
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
