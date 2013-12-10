package dati;




import hibernate.Argomenti;
import hibernate.Categoria;
import hibernate.Inserzione;
import hibernate.ListaDesideri;
import hibernate.ListaDesideriProdotti;
import hibernate.ListaDesideriProdottiId;
import hibernate.ListaSpesa;
import hibernate.ListaSpesaProdotti;
import hibernate.ListaSpesaProdottiId;
import hibernate.Prodotto;
import hibernate.Profilo;
import hibernate.Sottocategoria;
import hibernate.Supermercato;
import hibernate.Utente;
import hibernate.ValutazioneInserzione;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;






import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service("dati")
@Scope("singleton")
public class Dati {

	private static volatile Dati istanza;

	private volatile Set<Utente> setUtente = new CopyOnWriteArraySet<Utente>();
	private volatile Set<Categoria> setCategoria = new CopyOnWriteArraySet<Categoria>();
	private volatile Set<ListaSpesaProdotti> setListaSpesaProdotti = new CopyOnWriteArraySet<ListaSpesaProdotti>();
	private volatile Set<ListaDesideriProdotti> setListaDesideriProdotti = new CopyOnWriteArraySet<ListaDesideriProdotti>();
	private volatile Set<Argomenti> setArgomenti = new CopyOnWriteArraySet<Argomenti>();
	private volatile Set<Inserzione> setInserzione = new CopyOnWriteArraySet<Inserzione>();
	private volatile Set<ListaDesideri> setListaDesideri = new CopyOnWriteArraySet<ListaDesideri>();
	private volatile Set<ListaSpesa> setListaSpesa = new CopyOnWriteArraySet<ListaSpesa>();
	private volatile Set<Prodotto> setProdotto = new CopyOnWriteArraySet<Prodotto>();
	private volatile Set<Profilo> setProfilo = new CopyOnWriteArraySet<Profilo>();
	private volatile Set<Sottocategoria> setSottocategoria = new CopyOnWriteArraySet<Sottocategoria>();
	private volatile Set<Supermercato> setSupermercato = new CopyOnWriteArraySet<Supermercato>();
	private volatile Set<ValutazioneInserzione> setValutazioneInserzione = new CopyOnWriteArraySet<ValutazioneInserzione>();

	private static SessionFactory factory;

	public static SessionFactory buildSessionFactory(){
		try{
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");		
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);
		}catch(Throwable ex){
			System.err.println("Errore nell'inizializzazione della sessionfactory"+ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private Dati(){
		factory = buildSessionFactory();
		Session session = factory.openSession();
		Transaction tx = null;

		session = factory.openSession();
		try{
			tx=session.beginTransaction();

			List<Utente> result = session.createQuery("from Utente").list();
			setUtente.addAll(result);
			
			setCategoria.addAll(session.createQuery("from Categoria").list());
			setListaSpesaProdotti.addAll(session.createQuery("from ListaSpesaProdotti").list());
			setListaDesideriProdotti.addAll(session.createQuery("from ListaDesideriProdotti").list());
			setArgomenti.addAll(session.createQuery("from Argomenti").list());
			setInserzione.addAll(session.createQuery("from Inserzione").list());
			setListaDesideri.addAll(session.createQuery("from ListaDesideri").list());
			setListaSpesa.addAll(session.createQuery("from ListaSpesa").list());
			setProdotto.addAll(session.createQuery("from Prodotto").list());
			setProfilo.addAll(session.createQuery("from Profilo").list());
			setSottocategoria.addAll(session.createQuery("from Sottocategoria").list());
			setSupermercato.addAll(session.createQuery("from Supermercato").list());
			setValutazioneInserzione.addAll(session.createQuery("from ValutazioneInserzione").list());
			
			
			tx.commit();
		}catch(RuntimeException e){
			if(tx!=null)
				tx.rollback();
			throw e;
		}finally{
			if(session!=null && session.isOpen())
				session.close();
		}
	}

	public static Dati getInstance()
	{
		if (istanza == null)
			istanza = new Dati();
		return istanza; 
	}
	
	public Set<Argomenti> getArgomenti(){
		HashSet<Argomenti> argomenti = new HashSet<Argomenti>();
		for(Argomenti a : setArgomenti){
			argomenti.add(a);
		}
		return argomenti;
	}
	
	public void insertArgomenti(String arg1){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			if(arg1==null )
				throw new RuntimeException("tutti gli argomenti devono essere non nulli");
			Argomenti arg = new Argomenti(arg1);
			
			session.save(arg);
			
			session.persist(arg);
			
			
				setArgomenti.add(arg);
			
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public Argomenti getArgomenti(int IdArgomenti){
		
		
		Argomenti argomenti = null;
		
		Iterator<Argomenti> it = setArgomenti.iterator();
		
		for(;it.hasNext();){
			Argomenti a = it.next();
			if(a.getIdArgomenti().equals(IdArgomenti)){
				argomenti=a;
				break;
			}
		}
		if(argomenti == null)
			throw new RuntimeException("Elemento non trovato");
		return argomenti;
	}
	// si deve fornire l'oggetto settando l'id del vecchio oggetto
	public void modifyArgomenti(int idargomenti,String arg1){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		if(idargomenti <= 0 || arg1 == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		Argomenti argomenti = new Argomenti(arg1);
		argomenti.setIdArgomenti(idargomenti);
		boolean trovato = false;
		try{
			tx=session.beginTransaction();
			
			Iterator<Argomenti> it = setArgomenti.iterator();
			
			for(;it.hasNext();){
				Argomenti a = it.next();
				if(a.getIdArgomenti().equals(idargomenti)){
					it.remove();
					setArgomenti.add(argomenti);
					trovato= true;
					break;
				}
			}
			
			if(!trovato)
				throw new RuntimeException("elemento non trovato");
			session.update(argomenti);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
		
	}
	
	
	public void insertCategoria(String nome,Set<Sottocategoria> sottocategorie,Set<Prodotto> prodotti){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			if(nome == null || sottocategorie == null)
				throw new RuntimeException("tutti gli argomenti devono essere non nulli");
			Categoria categoria = new Categoria(nome, sottocategorie);
			
			session.save(categoria);
			
			session.persist(categoria);
			
			
			setCategoria.add(categoria);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
		
	}
	
	public void insertInserzione(Utente utente,Supermercato supermercato,Prodotto prodotto,float prezzo,Date dataInizio,Date dataFine,String descrizione,String foto){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		try{
			tx=session.beginTransaction();
			if(utente == null || supermercato == null || prodotto == null || prezzo<=0 || dataInizio == null || dataFine == null || foto == null)
				throw new RuntimeException("errore nell'immissione dei parametri");
			Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto);
			
			session.save(inserzione);
			
			session.persist(inserzione);
			
			
			setInserzione.add(inserzione);
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getInserziones().add(inserzione);
					break;
				}
			}
			
			for(Supermercato s : setSupermercato){
				if(s.equals(supermercato)){
					s.getInserziones().add(inserzione);
					break;
				}
			}
			
			for(Prodotto p : setProdotto){
				if(p.equals(prodotto)){
					p.getInserziones().add(inserzione);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	//in ingresso inserzione modificata con id dell'inserzione vecchia
	public void modifyInserzione(int idinserzione,Utente utente,Supermercato supermercato,Prodotto prodotto,float prezzo,Date dataInizio,Date dataFine,String descrizione,String foto){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean trovato = false;
		if(idinserzione <= 0 || utente == null || supermercato == null || prodotto == null || prezzo <= 0 || dataInizio == null || dataFine == null || descrizione == null || foto == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		Inserzione inserzione = new Inserzione(utente, supermercato, prodotto, prezzo, dataInizio, dataFine, descrizione, foto);
		inserzione.setIdInserzione(idinserzione);
		try{
			tx=session.beginTransaction();
			
			Iterator<Inserzione> it = setInserzione.iterator();
			
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(idinserzione)){
				//	it.remove();
			//		setInserzione.add(inserzione);
					trovato= true;
					break;
				}
			}
	
			if(!trovato)
				throw new RuntimeException("elemento non trovato");
			
			session.update(inserzione);
			
		
			it.remove();
			setInserzione.add(inserzione);
			
			
			
			it = inserzione.getUtente().getInserziones().iterator();
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(idinserzione)){
					it.remove();
					setInserzione.add(inserzione);
					break;
				}
			}
		
			
			it = inserzione.getSupermercato().getInserziones().iterator();
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(idinserzione)){
					it.remove();
					setInserzione.add(inserzione);
					break;
				}
			}
		
			
			
			it = inserzione.getProdotto().getInserziones().iterator();
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(idinserzione)){
					it.remove();
					setInserzione.add(inserzione);
					break;
				}
			}
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void deleteInserzione(int IdInserzione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Inserzione inserzione = getInserzione(IdInserzione);
		boolean trovato = false;
		try{
			tx=session.beginTransaction();
			
			Iterator<Inserzione> it = inserzione.getUtente().getInserziones().iterator();
			session.delete(inserzione);
			
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(inserzione.getIdInserzione())){
					it.remove();
					trovato = true;
					break;
				}
			}
			if(!trovato)
				throw new RuntimeException("elemento non trovato nell'utente");
			trovato=false;
			
			it = inserzione.getSupermercato().getInserziones().iterator();
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(inserzione.getIdInserzione())){
					it.remove();
					trovato = true;
					break;
				}
			}
			
			if(!trovato)
				throw new RuntimeException("elemento non trovato nel supermercato");
			trovato=false;
		
			it = inserzione.getProdotto().getInserziones().iterator();
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(inserzione.getIdInserzione())){
					it.remove();
					trovato=true;
					break;
				}
			}
			
			if(!trovato)
				throw new RuntimeException("elemento non trovato nel prodotto");
			trovato=false;
			it = setInserzione.iterator();
			
			for(;it.hasNext();){
				Inserzione a = it.next();
				if(a.getIdInserzione().equals(inserzione.getIdInserzione())){
					it.remove();
					trovato= true;
					break;
				}
			}
			
			if(!trovato)
				throw new RuntimeException("elemento non trovato nelle inserzioni");
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}

	public Inserzione getInserzione(int IdInserzione){
		
		Inserzione inserzione = null;
		
		Iterator<Inserzione> it = setInserzione.iterator();
		
		for(;it.hasNext();){
			Inserzione a = it.next();
			if(a.getIdInserzione().equals(IdInserzione)){
				inserzione=a;
				break;
			}
		}
		
		if(inserzione == null)
			throw new RuntimeException("Elemento non trovato");
		return inserzione;
	}
	
	public void insertListaDesideri(Utente utente,Set<Prodotto> prodotti,String nomeListaDesideri,String descrizione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		try{
			tx=session.beginTransaction();
			int idListaDesideri=1;
			
			if(utente == null || prodotti == null || nomeListaDesideri == null || descrizione == null)
				throw new RuntimeException("tutti gli argomenti devono essere non nulli");
			
			ListaDesideri listadesideri = new ListaDesideri(utente, nomeListaDesideri, new HashSet<ListaDesideriProdotti>());
			
			for(Prodotto prodotto : prodotti){
				ListaDesideriProdottiId id = new ListaDesideriProdottiId(listadesideri.getIdListaDesideri(), prodotto.getIdProdotto(), prodotto.getDescrizione());
				ListaDesideriProdotti ldp = new ListaDesideriProdotti(id , prodotto, listadesideri);
				listadesideri.getListaDesideriProdottis().add(ldp);
		
			}
			session.save(listadesideri);
			
			
				
			for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>)listadesideri.getListaDesideriProdottis()){
				
				for(Prodotto p : setProdotto){
					if(p.equals(ldp.getProdotto())){
						p.getListaDesideriProdottis().add(ldp);
						break;
					}
				}
			}
				
		
				
		
			setListaDesideri.add(listadesideri);
			
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getListaDesideris().add(listadesideri);
					break;
				}
			}
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void modifyListaDesideri(int idlistadesideri,Utente utente,String nomeListaDesideri,Set<Prodotto> prodotti){
		Session session = factory.getCurrentSession();
		
		Transaction tx = null;
		if(idlistadesideri <= 0 || utente == null || nomeListaDesideri == null || prodotti == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		ListaDesideri listadesideri = new ListaDesideri(utente, nomeListaDesideri,new HashSet<ListaDesideriProdotti>());
		listadesideri.setIdListaDesideri(idlistadesideri);
		
		for(Prodotto prodotto : prodotti){
			ListaDesideriProdottiId id = new ListaDesideriProdottiId(listadesideri.getIdListaDesideri(), prodotto.getIdProdotto(), prodotto.getDescrizione());
			ListaDesideriProdotti ldp = new ListaDesideriProdotti(id , prodotto, listadesideri);
			listadesideri.getListaDesideriProdottis().add(ldp);
		
		}
		
		boolean trovato = false;
		
		try{
			tx=session.beginTransaction();
			
			
			
			session.update(listadesideri);
			
			
			
			ListaDesideri ldtemp = null;
			
			for(ListaDesideri ld : setListaDesideri){
				
				if(ld.getIdListaDesideri()==idlistadesideri){
				
					trovato = true;
					ldtemp = ld;
					//si cancellano i vari collegamenti inter object
					setListaDesideri.remove(ld);
					setListaDesideri.add(listadesideri);
				
					//si aggiungono i nuovi collegamenti inter-object
					
					break;
				}
			}
			
			
			if(!trovato){
				throw new RuntimeException("elemento non trovato");
			}else{
				
				for(ListaDesideriProdotti ldp :(Set<ListaDesideriProdotti>) ldtemp.getListaDesideriProdottis()){
				
					for(Prodotto p : setProdotto){
						if(p.equals(ldp.getProdotto())){
							p.getListaDesideriProdottis().remove(ldp);
							break;
						}
					}
				}
				
				for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>)listadesideri.getListaDesideriProdottis()){
					
					for(Prodotto p : setProdotto){
						if(p.equals(ldp.getProdotto())){
							p.getListaDesideriProdottis().add(ldp);
							break;
						}
					}
				}
					
				for(Utente u : setUtente){
					if(u.equals(ldtemp.getUtente())){
						u.getListaDesideris().remove(ldtemp);
						break;
					}
				}
				
				for(Utente u : setUtente){
					if(u.equals(listadesideri.getUtente())){
						u.getListaDesideris().add(listadesideri);
						break;
					}
				}
				
				
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void deleteListaDesideri(int idlistadesideri){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean trovato = false;
		try{
			tx=session.beginTransaction();
		
			if(idlistadesideri<=0)
				throw new RuntimeException("id non valido");
			ListaDesideri ld = null;
			
			for(ListaDesideri listadesideri : setListaDesideri){
				if(listadesideri.getIdListaDesideri()==idlistadesideri){
					trovato = true;
					ld=listadesideri;
					
					break;
				}
			}
		
			if(trovato){
				session.delete(ld);
				setListaDesideri.remove(ld);
			
				for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>)ld.getListaDesideriProdottis()){
					ldp.getProdotto().getListaDesideriProdottis().remove(ld);
					for(Prodotto p : setProdotto){
						if(p.equals(ldp.getProdotto())){
							p.getListaDesideriProdottis().remove(ld);
							break;
						}
					}
				}
				
				for(Utente u : setUtente){
					if(u.equals(ld.getUtente())){
						u.getListaDesideris().remove(ld);
						break;
					}
				}
				
			}else{
				throw new RuntimeException("elemento non trovato");
			}
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public ListaDesideri getListaDesideri(int idlistadesideri){
		Session session = factory.getCurrentSession();
		
		ListaDesideri ld = null;
		for(ListaDesideri listadesideri : setListaDesideri){
			if(listadesideri.getIdListaDesideri().equals(idlistadesideri)){
				ld=listadesideri;
				break;
			}
		}
		if(ld==null)
			throw new RuntimeException("elemento non trovato");
		return ld;
	}
	
	
	public void insertListaSpesa(Utente utente,Set<Prodotto> prodotti,String nomeListaSpesa){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		float prezzo = 0;
		Inserzione inserzione,ultima;
		ultima = null;
		
		try{
			tx=session.beginTransaction();
			
			if(utente == null || prodotti == null || nomeListaSpesa == null )
				throw new RuntimeException("tutti gli argomenti devono essere non nulli");
			Iterator <Inserzione> it;
			for(Prodotto prodotto : prodotti){
				
				it=prodotto.getInserziones().iterator();
				for(;it.hasNext();){
					if(ultima==null){
						ultima = it.next();
					}else{
						inserzione = it.next();
						if(ultima.getDataInizio().getTime()<inserzione.getDataInizio().getTime()){
							ultima=inserzione;
						}
					}
				}
				if(ultima!=null){
					prezzo+=ultima.getPrezzo();
				}
			}
			
			ListaSpesa listaspesa = new ListaSpesa(utente, nomeListaSpesa,prezzo, new HashSet<ListaSpesaProdotti>());
			
			for(Prodotto prodotto : prodotti){
				ListaSpesaProdottiId id = new ListaSpesaProdottiId(listaspesa.getIdSpesa(), prodotto.getIdProdotto(), prodotto.getDescrizione());
				ListaSpesaProdotti ldp = new ListaSpesaProdotti(id , prodotto, listaspesa);
				listaspesa.getListaSpesaProdottis().add(ldp);
			
			}
			session.save(listaspesa);
			
			for(ListaSpesaProdotti lsp : (Set<ListaSpesaProdotti>)listaspesa.getListaSpesaProdottis()){
				
				for(Prodotto p : setProdotto){
					if(p.equals(lsp.getProdotto())){
						p.getListaSpesaProdottis().add(lsp);
						break;
					}
				}
			}
				
			setListaSpesa.add(listaspesa);
				
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getListaSpesas().add(listaspesa);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
		
	public void modifyListaSpesa(int idspesa,Utente utente,String nomeListaSpesa,Set<Prodotto> prodotti){
		Session session = factory.getCurrentSession();
		
		Transaction tx = null;
		boolean trovato = false;
		Iterator <Inserzione> it;
		Inserzione ultima = null;
		Inserzione inserzione;
		float prezzo = 0;
		if(utente == null || prodotti == null || nomeListaSpesa == null )
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		for(Prodotto prodotto : prodotti){
			
			it=prodotto.getInserziones().iterator();
			for(;it.hasNext();){
				if(ultima==null){
					ultima = it.next();
				}else{
					inserzione = it.next();
					if(ultima.getDataInizio().getTime()<inserzione.getDataInizio().getTime()){
						ultima=inserzione;
					}
				}
			}
			if(ultima!=null){
				prezzo+=ultima.getPrezzo();
			}
		}
		
		ListaSpesa listaspesa = new ListaSpesa(utente, nomeListaSpesa,prezzo, new HashSet<ListaSpesaProdotti>());
		listaspesa.setIdSpesa(idspesa);
		for(Prodotto prodotto : prodotti){
			ListaSpesaProdottiId id = new ListaSpesaProdottiId(listaspesa.getIdSpesa(), prodotto.getIdProdotto(), prodotto.getDescrizione());
			ListaSpesaProdotti ldp = new ListaSpesaProdotti(id , prodotto, listaspesa);
			listaspesa.getListaSpesaProdottis().add(ldp);
		
		}
		
		try{
			tx=session.beginTransaction();
			
			session.update(listaspesa);
			
			ListaSpesa ldtemp = null;
			
			for(ListaSpesa ls : setListaSpesa){
				
				if(ls.getIdSpesa()==listaspesa.getIdSpesa()){
				
					trovato = true;
					ldtemp = ls;
					//si cancellano i vari collegamenti inter object
					setListaSpesa.remove(ls);
					setListaSpesa.add(listaspesa);
				
					//si aggiungono i nuovi collegamenti inter-object
					
					break;
				}
			}
			
			
			if(!trovato){
				throw new RuntimeException("elemento non trovato");
			}else{
				
				for(ListaSpesaProdotti lsp :(Set<ListaSpesaProdotti>) ldtemp.getListaSpesaProdottis()){
				
					for(Prodotto p : setProdotto){
						if(p.equals(lsp.getProdotto())){
							p.getListaSpesaProdottis().remove(lsp);
							break;
						}
					}
				}
				for(ListaSpesaProdotti lsp :(Set<ListaSpesaProdotti>)listaspesa.getListaSpesaProdottis()){
				
					for(Prodotto p : setProdotto){
						if(p.equals(lsp.getProdotto())){
							p.getListaSpesaProdottis().add(lsp);
							break;
						}
					}
					
				}
				
				for(Utente u : setUtente){
					if(u.equals(ldtemp.getUtente())){
						u.getListaSpesas().remove(ldtemp);
						break;
					}
				}
				for(Utente u : setUtente){
					if(u.equals(listaspesa.getUtente())){
						u.getListaSpesas().add(listaspesa);
					}
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void deleteListaSpesa(int idspesa){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean trovato = false;
		try{
			tx=session.beginTransaction();
		
			if(idspesa<=0)
				throw new RuntimeException("id non valido");
			ListaSpesa ls = null;
			
			for(ListaSpesa listaspesa : setListaSpesa){
				if(listaspesa.getIdSpesa()==idspesa){
					trovato = true;
					ls=listaspesa;
					
					break;
				}
			}
			
			if(trovato){
				session.delete(ls);
				setListaSpesa.remove(ls);
				
				
				for(ListaSpesaProdotti lsp : (Set<ListaSpesaProdotti>)ls.getListaSpesaProdottis()){
					
					for(Prodotto p : setProdotto){
						if(p.equals(lsp.getProdotto())){
							p.getListaSpesaProdottis().remove(ls);
							break;
						}
					}
				}
				
				for(Utente u : setUtente){
					if(u.equals(ls.getUtente())){
						u.getListaSpesas().remove(ls);
						break;
					}
				}
				
			}else{
				throw new RuntimeException("elemento non trovato");
			}
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public ListaSpesa getListaSpesa(int idspesa){
		ListaSpesa ls = null;
		for(ListaSpesa listaspesa : setListaSpesa){
			if(listaspesa.getIdSpesa().equals(idspesa)){
				ls=listaspesa;
				break;
			}
		}
		if(ls==null)
			throw new RuntimeException("elemento non trovato");
		return ls;
	}
	
	public void insertProdotto(Sottocategoria sottocategoria,int codicebarre,String descrizione,int idarg,int arg1,int arg2){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		try{
			tx=session.beginTransaction();
			
			if(codicebarre <=0 || descrizione == null || idarg<=0 || arg1 <= 0|| arg2 <= 0 )
				throw new RuntimeException("tutti gli argomenti devono essere non nulli");
				
			Prodotto prodotto = new Prodotto(sottocategoria,codicebarre, descrizione, idarg, arg1, arg2, new HashSet<Inserzione>(), new HashSet<ListaDesideriProdotti>(),new HashSet<ListaSpesaProdotti>());
			
			
			
			session.save(prodotto);
			
			session.persist(prodotto);
			
			
			setProdotto.add(prodotto);
			
			for(Sottocategoria s : setSottocategoria){
				if(s.equals(sottocategoria)){
					s.getProdottos().add(prodotto);
					break;
				}
			}
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void modifyProdotto(Sottocategoria sottocategoria,int idprodotto,int codicebarre,String descrizione,int idarg,int arg1,int arg2,Set<Inserzione> inserzioni,Set<ListaDesideriProdotti> desideriprodotti,Set<ListaSpesaProdotti> spesaprodotti){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		boolean trovato = false;
		
		
		if(idprodotto <= 0 || codicebarre<=0 || descrizione == null || idarg <= 0 || arg1<= 0 || arg2<=0 || inserzioni == null || desideriprodotti == null || spesaprodotti == null)
			throw new RuntimeException("tutti gli argomenti devono essere immessi");
		
		Prodotto prodotto = new Prodotto(sottocategoria,codicebarre, descrizione, idarg, arg1, arg2, new HashSet<Inserzione>(), new HashSet<ListaDesideriProdotti>(), new HashSet<ListaSpesaProdotti>());
		prodotto.setIdProdotto(idprodotto);
		
		for(Inserzione inserzione : inserzioni){
			prodotto.getInserziones().add(inserzione);
			//inserzione.setProdotto(prodotto);
		}
		for(ListaDesideriProdotti dp : desideriprodotti){
			prodotto.getListaDesideriProdottis().add(dp);
			//dp.setProdotto(prodotto);
		}
		for(ListaSpesaProdotti sp : spesaprodotti){
			prodotto.getListaSpesaProdottis().add(sp);
			//sp.setProdotto(prodotto);
		}
		
		try{
			tx=session.beginTransaction();
			
			session.update(prodotto);
			
			
			for(Prodotto p : setProdotto){
				
				if(p.getIdProdotto().equals(idprodotto)){
					trovato= true;
					
					for(Sottocategoria s : setSottocategoria){
						if(s.equals(sottocategoria)){
							s.getProdottos().remove(p);
							s.getProdottos().add(prodotto);
							break;
						}
					}
					
					setProdotto.remove(p);
					setProdotto.add(prodotto);
					break;
				}
				
			}				
			
			
				
			
			if(!trovato){
				throw new RuntimeException("elemento non trovato");
			}else{
				
				for(Inserzione inserzione : inserzioni){
					inserzione.setProdotto(prodotto);
				}
			
			
				for(ListaDesideriProdotti dp : desideriprodotti){
					dp.setProdotto(prodotto);
				}
				
				
				for(ListaSpesaProdotti sp : spesaprodotti){
					sp.setProdotto(prodotto);
				}
				
			}
			tx.commit();
			
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
		
	}
	
	public void deleteProdotto(int idprodotto){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		Prodotto prodotto = null;
		
		if(idprodotto<=0)
			throw new RuntimeException("idprodotto non valido");
		
		
		for(Prodotto p : setProdotto){
			if(p.getIdProdotto().equals(idprodotto)){
				prodotto = p;
				
				break;
			}
		}
		
		
		if(prodotto!=null){
			try{
				tx=session.beginTransaction();
				
				session.delete(prodotto);
				
				for(Inserzione inserzione : (Set<Inserzione>) prodotto.getInserziones()){
					inserzione.setProdotto(null);
				}
				
				for(ListaDesideriProdotti ldp : (Set<ListaDesideriProdotti>)prodotto.getListaDesideriProdottis()){
					ldp.setProdotto(null);
				}
				
			
				for(ListaSpesaProdotti lsp : (Set<ListaSpesaProdotti>)prodotto.getListaDesideriProdottis()){
					lsp.setProdotto(null);
				}
			
				tx.commit();
				
			}catch(Throwable ex){
				if(tx!=null)
					tx.rollback();
				throw new RuntimeException(ex);
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
				session=null;
			}
		}else{
			throw new RuntimeException("elemento non trovato");
		}
		
	}
	
	public Set<Prodotto> getProdotti(){
		
		HashSet<Prodotto> prodotti = new HashSet<Prodotto>();
		
		for(Prodotto p : setProdotto){
			prodotti.add(p);
		}
		
		return prodotti;
	}
	
	public Set<Profilo> getProfili(){
		HashSet<Profilo> profili = new HashSet<Profilo>();
		
		
			for(Profilo profilo : setProfilo){
				profili.add(profilo);
			}
		
		return profili;
		
	}
	
	public void insertProfilo(Utente utente,int creditiacquisiti,int creditipendenti,int reputazione,boolean premium,int contatoreinfrazioni){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(utente == null || creditiacquisiti<0 || creditipendenti<0  || contatoreinfrazioni<0)
			throw new RuntimeException("parametro/i non validi");
		
		Profilo profilo = new Profilo(utente, creditiacquisiti, creditipendenti, reputazione, premium, contatoreinfrazioni);
		
		try{
			tx=session.beginTransaction();
			
			session.save(profilo);
			
			session.persist(profilo);
			
			for(Utente u : setUtente){
				if(u.equals(profilo.getUtente())){
					u.getProfilos().add(profilo);
					break;
				}
			}
			setProfilo.add(profilo);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void modifyProfilo(int idprofilo,Utente utente,int creditiacquisiti,int creditipendenti,int reputazione,boolean premium,int contatoreinfrazioni){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo pold=null;
		
		if(idprofilo <0 || utente == null || creditiacquisiti<0 || creditipendenti<0 ||  contatoreinfrazioni<0)
			throw new RuntimeException("parametro/i non validi");
		
		for(Profilo p : setProfilo){
			if(p.getIdProfilo().equals(idprofilo)){
				pold=p;
				break;
			}
		}
		
		if(pold!=null){
			
			Profilo profilo = new Profilo(utente, creditiacquisiti, creditipendenti, reputazione, premium, contatoreinfrazioni);
			profilo.setIdProfilo(idprofilo);
			
			
			try{
				tx=session.beginTransaction();
				
				session.update(profilo);
				
				setProfilo.add(profilo);
				
				for(Utente u : setUtente){
					if(u.equals(pold.getUtente())){
						u.getProfilos().remove(pold);
						break;
					}
				}
				
				setProfilo.remove(pold);
				
				tx.commit();
			}catch(Throwable ex){
				if(tx!=null)
					tx.rollback();
				throw new RuntimeException(ex);
			}finally{
				if(session!=null && session.isOpen()){
					session.close();
				}
				session=null;
			}
		}else{
			throw new RuntimeException("elemento non trovato");
		}
	}
	
	public void deleteProfilo(int idprofilo){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Profilo pold=null;
		
		if(idprofilo<0)
			throw new RuntimeException("id non valido");
		
		for(Profilo p : setProfilo){
			if(p.getIdProfilo().equals(idprofilo)){
				pold=p;
				break;
			}
		}
		
		if(pold==null)
			throw new RuntimeException("elemento non trovato");
		
		try{
			tx=session.beginTransaction();
			
			session.delete(pold);
			
			for(Utente u : setUtente){
				if(u.equals(pold.getUtente())){
					u.getProfilos().remove(pold);
					break;
				}
			}
			
			setProfilo.remove(pold);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
			
	}
	
	public void insertSottocategoria(Categoria categoria,String nome,Set<Prodotto> prodotti){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(categoria == null || nome == null)
			throw new RuntimeException("tutti i parametri devono essere non nulli");
		
		Sottocategoria sottocategoria = new Sottocategoria(categoria, nome,prodotti);
		
		
		try{
			tx=session.beginTransaction();
			
			session.save(sottocategoria);
			
			session.persist(sottocategoria);
			
			for(Categoria c : setCategoria){
				
				if(c.equals(categoria)){
					c.getSottocategorias().add(sottocategoria);
					break;
				}
				
			}
			
			setSottocategoria.add(sottocategoria);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void modifySottocategoria(int idsottocategoria,Categoria categoria,String nome,Set<Prodotto> prodotti){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Sottocategoria scold = null;
		
		if(idsottocategoria <=0 || categoria == null || nome == null)
			throw new RuntimeException("parametri non corretti");
		
		for(Sottocategoria s : setSottocategoria){
			if(s.getIdSottocategoria().equals(idsottocategoria)){
				scold=s;
				break;
			}
		}
		
		Sottocategoria sottocategoria = new Sottocategoria(categoria, nome,prodotti);
		sottocategoria.setIdSottocategoria(idsottocategoria);
		
		try{
			tx=session.beginTransaction();
		
			session.update(sottocategoria);
			
			for(Categoria c : setCategoria){
				if(c.equals(scold.getCategoria())){
					c.getSottocategorias().remove(scold);
					break;
				}
			}
			
			setSottocategoria.remove(scold);
			
			setSottocategoria.add(sottocategoria);
			
			for(Categoria c : setCategoria){
				if(c.equals(categoria)){
					c.getSottocategorias().add(sottocategoria);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void deleteSottocategoria(int idsottocategoria){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Sottocategoria scold = null;
		
		if(idsottocategoria<=0)
			throw new RuntimeException("id non valido");
		
		for(Sottocategoria s : setSottocategoria){
			if(s.getIdSottocategoria().equals(idsottocategoria)){
				scold=s;
				break;
			}
		}
		
		if(scold==null)
			throw new RuntimeException("elemento non trovato");
		
		try{
			tx=session.beginTransaction();
		
			session.delete(scold);
			
			for(Categoria c : setCategoria){
				if(c.equals(scold.getCategoria())){
					c.getSottocategorias().remove(scold);
					break;
				}
			}
			
			setSottocategoria.remove(scold);

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public Set<Sottocategoria> getSottocategorie(){
		
		HashSet<Sottocategoria> sottocategorie = new HashSet<Sottocategoria>();
		
		for(Sottocategoria s : setSottocategoria){
			sottocategorie.add(s);
		}
		
		return sottocategorie;
		
	}
	
	public void insertSupermercato(String nome,BigDecimal latitudine,BigDecimal longitudine){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(nome == null || latitudine == null || longitudine == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		Supermercato supermercato = new Supermercato(nome, latitudine, longitudine, new HashSet<Inserzione>());
		
		
		try{
			tx=session.beginTransaction();
			
			session.save(supermercato);
			
			session.persist(supermercato);
			
			setSupermercato.add(supermercato);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public void modifySupermercato(int idsupermercato,String nome,BigDecimal latitudine,BigDecimal longitudine,Set<Inserzione> inserzioni){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato sold = null;
		
		if(idsupermercato<=0 || nome == null || latitudine == null || longitudine == null || inserzioni == null)
			throw new RuntimeException("tutti gli argomenti devono essere non nulli");
		
		for(Supermercato s : setSupermercato){
			if(s.getIdSupermercato().equals(idsupermercato)){
				sold=s;
				break;
			}
		}
		
		if(sold==null)
			throw new RuntimeException("elemento non trovato");
		
		Supermercato supermercato = new Supermercato(nome, latitudine, longitudine, new HashSet<Inserzione>());
		supermercato.setIdSupermercato(idsupermercato);
		
		
		try{
			tx=session.beginTransaction();
			
			session.update(supermercato);
			
			for(Inserzione i : (Set<Inserzione>) sold.getInserziones()){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setSupermercato(null);
						break;
					}
				}
			}
			
			setSupermercato.remove(sold);
			
			for(Inserzione i : (Set<Inserzione>) supermercato.getInserziones()){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setSupermercato(supermercato);
						break;
					}
				}
			}
			
			setSupermercato.add(supermercato);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
			
	}
	
	public void deleteSupermercato(int idsupermercato){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Supermercato sold = null;
		
		for(Supermercato s : setSupermercato){
			if(s.getIdSupermercato().equals(idsupermercato)){
				sold=s;
				break;
			}
		}
		
		if(sold==null)
			throw new RuntimeException("elemento non trovato");
		
		try{
			tx=session.beginTransaction();
		
			session.delete(sold);/*
			si vuole lasciare la traccia del supermercato eliminato?
			
			for(Inserzione i : (Set<Inserzione>) sold.getInserziones()){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setSupermercato(null);
						break;
					}
				}
			}*/
			
			setSupermercato.remove(sold);
			

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public Set<Supermercato> getSupermercati(){
		
		Set<Supermercato> supermercati = new HashSet<Supermercato>();
		
		for(Supermercato s : setSupermercato){
			supermercati.add(s);
		}
		return supermercati;
	}
	
	public void insertUtente(String mail,String nickname,String password,Date dataregistrazione,String numerocasuale){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(mail == null || nickname == null || password == null || dataregistrazione == null)
			throw new RuntimeException("i parametri devono essere non nulli");
		
		Utente utente = new Utente(mail, nickname, password, dataregistrazione,false,numerocasuale,new HashSet<ValutazioneInserzione>(), new HashSet<ValutazioneInserzione>(), new HashSet<ListaSpesa>(), new HashSet<Inserzione>(), new HashSet<Profilo>(), new HashSet<ListaDesideri>());
		Profilo profilo = new Profilo(utente, 0, 0, 0, false, 0);
		utente.getProfilos().add(profilo);
		try{
			tx=session.beginTransaction();
			
			session.save(utente);
			
			session.persist(utente);
			
			session.save(profilo);
			
			session.persist(profilo);
			
			
			setUtente.add(utente);
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
	}
	
	public void modifyUtente(int idutente,String mail,String nickname,String password,Date dataregistrazione,Set<ValutazioneInserzione> valutazioniinserzionista,Set<ValutazioneInserzione> valutazionivalutatore,Set<ListaSpesa> listaspesas,Set<Inserzione> inserzioni, Set<Profilo> profili,Set<ListaDesideri> listadesideris,boolean confermato,String numerocasuale){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Utente uold = null;
		
		for(Utente u : setUtente){
			if(u.getIdUtente().equals(idutente)){
				uold=u;
				break;
			}
		}
		
		if(uold==null)
			throw new RuntimeException("elemento non trovato");
		
		Utente utente = new Utente(mail, nickname, password, dataregistrazione,confermato,numerocasuale, valutazioniinserzionista,valutazionivalutatore, listaspesas, inserzioni, profili, listadesideris);
		utente.setIdUtente(idutente);
		
		
		
		try{
			tx=session.beginTransaction();
			
			session.update(utente);
			

			for(Inserzione i : (Set<Inserzione>)uold.getInserziones()){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setUtente(null);
						break;
					}
				}
			}
			
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>)uold.getValutazioneInserzionesForIdUtenteInserzionista()){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteInserzionista(null);
						break;
					}
				}
			}
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>)uold.getValutazioneInserzionesForIdUtenteValutatore()){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteValutatore(null);
						break;
					}
				}
			}
			
			for(ListaSpesa ls : (Set<ListaSpesa>)uold.getListaSpesas()){
				for(ListaSpesa lis : setListaSpesa){
					if(lis.equals(ls)){
						lis.setUtente(null);
						break;
					}
				}
			}
			
			for(ListaDesideri ld : (Set<ListaDesideri>)uold.getListaDesideris()){
				for(ListaDesideri ldes : setListaDesideri){
					if(ldes.equals(ld)){
						ldes.setUtente(null);
						break;
					}
				}
			}
			
			for(Profilo p :(Set<Profilo>)uold.getProfilos()){
				for(Profilo pro : setProfilo){
					if(pro.equals(p)){
						pro.setUtente(null);
						break;
					}
				}
			}
			
			setUtente.remove(uold);
			
			setUtente.add(utente);
			
			for(ValutazioneInserzione v : valutazioniinserzionista){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteInserzionista(utente);
						break;
					}
				}
			}
			
			for(ValutazioneInserzione v : valutazionivalutatore){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteValutatore(utente);
						break;
					}
				}
			}
			
			for(Inserzione i : inserzioni){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setUtente(utente);
						break;
					}
				}
			}
			
			for(ListaDesideri l : listadesideris){
				for(ListaDesideri ld : setListaDesideri){
					if(ld.equals(l)){
						ld.setUtente(utente);
						break;
					}
				}
			}
			
			for(ListaSpesa l : listaspesas){
				for(ListaSpesa ls : setListaSpesa){
					if(ls.equals(l)){
						ls.setUtente(utente);
						break;
					}
				}
			}
			
			for(Profilo p : profili){
				for(Profilo pro : setProfilo){
					if(pro.equals(p)){
						pro.setUtente(utente);
						break;
					}
				}
			}
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
			
	}
	
	
	public void deleteUtente(int idutente){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		Utente uold = null;
		
		for(Utente u : setUtente){
			if(u.getIdUtente().equals(idutente)){
				uold=u;
				break;
			}
		}
		
		if(uold==null)
			throw new RuntimeException("elemento non trovato");
		
		try{
			tx=session.beginTransaction();
			
			session.delete(uold);
			
			for(Inserzione i : (Set<Inserzione>)uold.getInserziones()){
				for(Inserzione ins : setInserzione){
					if(ins.equals(i)){
						ins.setUtente(null);
						break;
					}
				}
			}
			
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>)uold.getValutazioneInserzionesForIdUtenteInserzionista()){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteInserzionista(null);
						break;
					}
				}
			}
			for(ValutazioneInserzione v : (Set<ValutazioneInserzione>)uold.getValutazioneInserzionesForIdUtenteValutatore()){
				for(ValutazioneInserzione val : setValutazioneInserzione){
					if(val.equals(v)){
						val.setUtenteByIdUtenteValutatore(null);
						break;
					}
				}
			}
			
			for(ListaSpesa ls : (Set<ListaSpesa>)uold.getListaSpesas()){
				for(ListaSpesa lis : setListaSpesa){
					if(lis.equals(ls)){
						lis.setUtente(null);
						break;
					}
				}
			}
			
			for(ListaDesideri ld : (Set<ListaDesideri>)uold.getListaDesideris()){
				for(ListaDesideri ldes : setListaDesideri){
					if(ldes.equals(ld)){
						ldes.setUtente(null);
						break;
					}
				}
			}
			
			for(Profilo p :(Set<Profilo>)uold.getProfilos()){
				for(Profilo pro : setProfilo){
					if(pro.equals(p)){
						pro.setUtente(null);
						break;
					}
				}
			}
			
			setUtente.remove(uold);
			
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
			
	}
	
	public Set<Utente> getUtenti(){
		HashSet<Utente> utenti = new HashSet<Utente>();
		
		for(Utente u : setUtente){
			utenti.add(u);
		}
		
		return utenti;
	}
	
	public void insertValutazioneInserzioneValutatore(Utente utente,int valutazione,Date data){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(utente == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");
		
		ValutazioneInserzione valutazioneinserzione = new ValutazioneInserzione(utente, null, valutazione, data);
		
		
		
		try{
			tx=session.beginTransaction();
			
			session.save(valutazioneinserzione);
			
			session.persist(valutazioneinserzione);
			
			setUtente.add(utente);
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getValutazioneInserzionesForIdUtenteValutatore().add(valutazioneinserzione);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
		
	}
	
	public void modifyValutazioneInserzioneValutatore(int idvalutazione,Utente utente,Date data,int valutazione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione vold = null;
		
		if(idvalutazione <=0 || utente == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");
		
		for(ValutazioneInserzione v : setValutazioneInserzione){
			if(v.getIdValutazioneInserzione().equals(idvalutazione)){
				vold=v;
				break;
			}
		}
		
		if(vold==null)
			throw new RuntimeException("elemento non trovato");
		
		ValutazioneInserzione valutazioneinserzione = new ValutazioneInserzione(utente, null, valutazione, data);
		valutazioneinserzione.setIdValutazioneInserzione(idvalutazione);
		
		try{
			tx=session.beginTransaction();
			
			for(Utente u : setUtente){
				if(u.equals(vold.getUtenteByIdUtenteValutatore())){
					u.getValutazioneInserzionesForIdUtenteValutatore().remove(vold);
					break;
				}
			}
			
			setValutazioneInserzione.remove(vold);
			
			setValutazioneInserzione.add(valutazioneinserzione);
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getValutazioneInserzionesForIdUtenteValutatore().add(valutazioneinserzione);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public void deleteValutazioneInserzioneValutatore(int idvalutazione){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione vold = null;
		
		if(idvalutazione <=0 )
			throw new RuntimeException("id non valido");
		
		for(ValutazioneInserzione v : setValutazioneInserzione){
			if(v.getIdValutazioneInserzione().equals(idvalutazione)){
				vold=v;
				break;
			}
		}
		
		if(vold==null)
			throw new RuntimeException("elemento non trovato");
		

		try{
			tx=session.beginTransaction();
			
			session.delete(vold);
			
			for(Utente u : setUtente){
				if(u.equals(vold.getUtenteByIdUtenteValutatore())){
					u.getValutazioneInserzionesForIdUtenteValutatore().remove(vold);
					break;
				}
			}

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public void insertValutazioneInserzioneInserzionista(Utente utente,int valutazione,Date data){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		
		if(utente == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");
		
		ValutazioneInserzione valutazioneinserzione = new ValutazioneInserzione(null, utente, valutazione, data);
		
		
		
		try{
			tx=session.beginTransaction();
			
			session.save(valutazioneinserzione);
			
			session.persist(valutazioneinserzione);
			
			setUtente.add(utente);
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getValutazioneInserzionesForIdUtenteInserzionista().add(valutazioneinserzione);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
		
	}
	
	public void modifyValutazioneInserzioneInserzionista(int idvalutazione,Utente utente,Date data,int valutazione){
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione vold = null;
		
		if(idvalutazione <=0 || utente == null || valutazione<0 || data == null)
			throw new RuntimeException("parametri non corretti");
		
		for(ValutazioneInserzione v : setValutazioneInserzione){
			if(v.getIdValutazioneInserzione().equals(idvalutazione)){
				vold=v;
				break;
			}
		}
		
		if(vold==null)
			throw new RuntimeException("elemento non trovato");
		
		ValutazioneInserzione valutazioneinserzione = new ValutazioneInserzione(null , utente, valutazione, data);
		valutazioneinserzione.setIdValutazioneInserzione(idvalutazione);
		
		try{
			tx=session.beginTransaction();
			
			for(Utente u : setUtente){
				if(u.equals(vold.getUtenteByIdUtenteInserzionista())){
					u.getValutazioneInserzionesForIdUtenteInserzionista().remove(vold);
					break;
				}
			}
			
			setValutazioneInserzione.remove(vold);
			
			setValutazioneInserzione.add(valutazioneinserzione);
			
			for(Utente u : setUtente){
				if(u.equals(utente)){
					u.getValutazioneInserzionesForIdUtenteInserzionista().add(valutazioneinserzione);
					break;
				}
			}
			
			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public void deleteValutazioneInserzioneInserzionista(int idvalutazione){
		
		Session session = factory.getCurrentSession();
		Transaction tx = null;
		ValutazioneInserzione vold = null;
		
		if(idvalutazione <=0 )
			throw new RuntimeException("id non valido");
		
		for(ValutazioneInserzione v : setValutazioneInserzione){
			if(v.getIdValutazioneInserzione().equals(idvalutazione)){
				vold=v;
				break;
			}
		}
		
		if(vold==null)
			throw new RuntimeException("elemento non trovato");
		

		try{
			tx=session.beginTransaction();
			
			session.delete(vold);
			
			for(Utente u : setUtente){
				if(u.equals(vold.getUtenteByIdUtenteInserzionista())){
					u.getValutazioneInserzionesForIdUtenteInserzionista().remove(vold);
					break;
				}
			}

			tx.commit();
		}catch(Throwable ex){
			if(tx!=null)
				tx.rollback();
			throw new RuntimeException(ex);
		}finally{
			if(session!=null && session.isOpen()){
				session.close();
			}
			session=null;
		}
		
	}
	
	public Set<ValutazioneInserzione> getValutazioniInserzioni(){
		HashSet<ValutazioneInserzione> valutazioni = new HashSet<ValutazioneInserzione>();
		
		for(ValutazioneInserzione v : setValutazioneInserzione){
			valutazioni.add(v);
		}
		
		return valutazioni;
	}
}