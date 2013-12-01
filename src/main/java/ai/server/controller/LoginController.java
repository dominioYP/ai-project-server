package ai.server.controller;

import java.security.Principal;
import java.util.HashMap;

import org.omg.CORBA.Request;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class LoginController {
	
	 @RequestMapping(value="/welcome",method = RequestMethod.GET)
	 public ModelAndView printWelcome(Principal principal){
		 System.out.println(principal);
		 String name = principal.getName();
		 HashMap<String, String> map = new HashMap<String, String>();
		 map.put("name", name);
		 map.put("message", "Spring Security Custom Form example");
		 return new ModelAndView("hello",map);
	 }
	 
	 @RequestMapping(value="/login",method = RequestMethod.GET)
	 public String login(Principal principal,RedirectAttributes attributes){
		 if(principal != null){
			 attributes.addFlashAttribute("error", "you're already logged");
			 return "redirect:/";
		 }else{
		 	return "login";
		 }
	 }
	 
	 @RequestMapping(value="/loginfailed",method = RequestMethod.GET)
	 public ModelAndView loginerror(){
		 return new ModelAndView("login","error","true");
	 }
	 
	 @RequestMapping(value="/logout",method = RequestMethod.GET)
	 public ModelAndView logout(){
		 return new ModelAndView("login");
	 }

}
