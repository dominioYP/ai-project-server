package ai.supermarket.controller;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import hibernate.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dati.Dati;
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		
		
		
		Set<Utente> utenti = dati.getUtenti();
		Utente utente = null;
		for(Utente u : utenti){
			
			if(u.getMail().equals(arg0)&&u.getConfermato()==true){
				utente = u;
				break;
			}
			
		}
		if(utente == null)
			throw new UsernameNotFoundException("User "+arg0+" not found");
		
		HashSet<GrantedAuthority> aut = new HashSet<GrantedAuthority>();
		aut.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new User(utente.getNickname(), utente.getPassword(), true, true, true, true, aut);
		
		// TODO Auto-generated method stub
		
	}

}
