package ai.server.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class HomeController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}

	@RequestMapping(value="/")
	public ModelAndView home(HttpServletRequest request,@ModelAttribute("error")String error) throws IOException{
		
		if(error!=null){
			Map <String,Object> map = new HashMap<String, Object>();
			map.put("dati",dati);
			map.put("error", error);
			return new ModelAndView("home",map);
		}else{
			System.out.println("fatto2");
			return new ModelAndView("home","dati",dati);
		}
	}
}
