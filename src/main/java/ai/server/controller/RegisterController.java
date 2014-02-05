package ai.server.controller;

import hibernate.Utente;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.Registration;

@Controller
public class RegisterController {
	@Autowired
	private RegistrationValidation registrationValidation;
	
	
	public void setRegistrationValidation(RegistrationValidation registrationValidation){
		
		this.registrationValidation = registrationValidation;

	}
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/register",method = RequestMethod.GET)
	public String showForm(Map model){
		Registration registration = new Registration();
		model.put("registration",registration);	
		
		return "register";
	}
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ModelAndView processRegistration(@Valid Registration registration,BindingResult result,HttpServletRequest request){
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	
		registrationValidation.validate(registration, result);
		String numerocasuale = java.util.UUID.randomUUID().toString();
		if(result.hasErrors()){
			return new ModelAndView("register");
		}
		try{
			dati.inserisciUtente(registration.getEmail(), registration.getUserName(), registration.getPassword(),new Date(),numerocasuale);
		}catch(Exception e){
			
			return new ModelAndView("register","error",e.toString());
		}
		
		Mail mail = (Mail)context.getBean("mail");

		String [] temp = request.getRequestURL().toString().split("/");
		String url = temp[0]+"//"+temp[1]+temp[2]+"/"+temp[3]+"/";
		
		mail.sendMail("giorgio.ciacchella@gmail.com", registration.getEmail(), "Registration Confirmation", "Click the link above to confirm your registration\n\n\n"+"<a href='"+url+"confirmregistration?numeroCasuale="+numerocasuale+"&email="+registration.getEmail()+"' />");
		return new ModelAndView("registersuccess","registration",registration);
	}
	
	@RequestMapping(value="/confirmregistration",method=RequestMethod.GET)
	public ModelAndView confirmRegistration(String numeroCasuale,String email){
		
	
		boolean trovato = false;
		Utente utente = dati.getUtenti().get(email);
		if(utente == null)
			return new ModelAndView("confirmregistration","error","Si è verificato un errore");
		
		if(utente.getNumeroCasuale().equals(numeroCasuale)){
			dati.modificaUtente(utente.getIdUtente(), utente.getMail(), utente.getNickname(), utente.getPassword(), utente.getDataRegistrazione(), true, "-1");
			trovato = true;			
		}
		
		
		return new ModelAndView("confirmregistration");
		
	}

}
