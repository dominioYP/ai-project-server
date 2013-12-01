package ai.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class InserzioneController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		
		this.dati=dati;
		
	}
	@RequestMapping(value="/inserzione")
	public ModelAndView doInserzione(){
		
		return new ModelAndView();
	}
	

}
