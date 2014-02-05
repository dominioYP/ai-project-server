package ai.server.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;



import hibernate.Utente;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		Utente utente = dati.getUtenti().get(arg0);
		if(utente != null){
			if(!utente.getConfermato())
				utente = null;
		}
		if(utente == null)
			throw new UsernameNotFoundException("User "+arg0+" not found");
		
		HashSet<GrantedAuthority> aut = new HashSet<GrantedAuthority>();
		aut.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new User(utente.getMail(), utente.getPassword(), true, true, true, true, aut);
		
		// TODO Auto-generated method stub
		
	}

}
