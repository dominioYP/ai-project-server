package dati;

import hibernate.Profilo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/***
 * @author Ignazio
 * 
 * Questa classe estende TimerTask per implementare il controllo relativo al nostro sistema a crediti.
 * 
 * In pratica viene realizzato da un timer, che è stato definito e inizializzato nella classe Dati. Quando il suddetto timer
 * scade, viene creato un thread secondario che esegue il metodo "run" di cui è stato fatto l'override.
 * 
 * Il metodo run:
 * 		- ricerca tutte le inserzioni che scadono nel singolo giorno passato;
 * 		- di ognuna di queste calcola se è stata valutata positivamente o negativamente;
 * 		- aggiorna di conseguenza i crediti (pendenti e acquisiti) di inserzionista e valutatori.
 */
public class TimerSistemaCrediti extends TimerTask {

	@Override
	public void run() {

		Session session = null;
		Transaction tx = null;

		session = Dati.factory.openSession();
		try {
			tx=session.beginTransaction();
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = new GregorianCalendar();
			
			/***
			 * Questa prima query ha lo scopo di selezionare tutte le inserizioni in scadenza nel giorno che stiamo considerando.
			 * 
			 * Oltre a ritornare l'id dell'inserione e dell'utente inserionista, viene ritornato un valore, chiamato "valutazioneFinale",
			 * che viene calcolato con la nostra formula per verificare se l'inserzione è giudicata positivamente o negativamente.
			 * 
			 * Il valore di "valutazioneFinale" varia tra -1 e 1. Se è < 0 allora la valutazione è negativa, se > 0 è positiva.
			 */
			Query q1 = session.createSQLQuery("select vi.ID_Inserzione, vi.ID_UtenteInserzionista, t1.ID_Profilo, sum(vi.Valutazione*p.Reputazione)/(count(vi.Valutazione)*100) as valutazioneFinale " +
											  "from valutazione_inserzione vi, profilo p, (select ID_Inserzione, ID_Profilo from inserzione i, profilo p1 where i.DataFine = :data and i.ID_Utente = p1.ID_Utente) as t1 " +
											  "where vi.ID_Inserzione = t1.ID_inserzione and vi.ID_UtenteValutatore = p.ID_Utente" +
											  "group by vi.ID_Inserzione");
			q1.setParameter("data", formato.format(cal.getTime()));
			List inserzioniDaValutareList = q1.list();
			
			 Query q2;
	         String q2Text = "";
	         
	         // Acquisisco la mappa dei profili
	         Map<Integer,Profilo> mappaProfili = Dati.getInstance().getProfili();
	         Profilo p;
			
	         /***
	          *  Per ogni inserzione ritornata dalla query precedente, è necessario aggiornare i crediti (pendenti e acquisiti) relativi agli utenti valutatori e anche dell'inserzionista.
	          */
	         for(int i=0; i<inserzioniDaValutareList.size(); i++) {
	            Object[] inserzioneObj = (Object[])inserzioniDaValutareList.get(i);
	            System.out.println("ID_Inserizione: " + inserzioneObj[0] + " - ID_UtenteInserzionista: " + inserzioneObj[1] + " - ID_Profilo: " + inserzioneObj[2] + " - Calcolo Valutazioni: " + inserzioneObj[3]);
	            
	            // UPDATE INSERZIONISTA	            
	            p = mappaProfili.get(inserzioneObj[2]);
	            System.out.println("Mappa - ID_Profilo: " + p.getIdProfilo() + " - Reputazione: " + p.getReputazione());
	            
	            if ((Integer)inserzioneObj[3] > 0)
	            	Dati.getInstance().modificaProfilo(p.getIdProfilo(),
								            			p.getCreditiAcquisiti() + 10,
								            			p.getCreditiPendenti() - 10,
								            			(p.getNumeroInserzioniPositive() + 1 + p.getNumeroValutazioniPositive()) / (p.getNumeroInserzioniTotali() + 1 + p.getNumeroValutazioniTotali()),
								            			p.getPremium(),
								            			p.getContatoreInfrazioni(),
								            			p.getNumeroInserzioniPositive() + 1,
								            			p.getNumeroInserzioniTotali() + 1,
								            			p.getNumeroValutazioniPositive(),
								            			p.getNumeroValutazioniTotali());
	            else
	            	Dati.getInstance().modificaProfilo(p.getIdProfilo(),
								            			p.getCreditiAcquisiti() - 10,
								            			p.getCreditiPendenti() - 10,
								            			(p.getNumeroInserzioniPositive() + p.getNumeroValutazioniPositive()) / (p.getNumeroInserzioniTotali() + 1 + p.getNumeroValutazioniTotali()),
								            			p.getPremium(),
								            			p.getContatoreInfrazioni() + 1,
								            			p.getNumeroInserzioniPositive(),
								            			p.getNumeroInserzioniTotali() + 1,
								            			p.getNumeroValutazioniPositive(),
								            			p.getNumeroValutazioniTotali());
	            
	            // SELECT UTENTI VALUTATORI PER L'INSERZIONE CONSIDERATA
	            q2Text = "select vi.ID_UtenteValutatore, vi.Valutazione, p.ID_profilo" +
	            		 "from valutazione_inserzione vi, profilo p" +
	            		 "where vi.ID_Inserzione = :idInserzione and vi.ID_UtenteValutatore = p.ID_Utente";
	            q2 = session.createSQLQuery(q2Text);
	            q2.setParameter("idInserzione", inserzioneObj[0]);
	            List utentiValutatoriList = q2.list();
	            
	            for(int j=0; j<utentiValutatoriList.size(); j++) {
		            Object[] utenteObj = (Object[])utentiValutatoriList.get(i);
		            System.out.println("Query - ID_Utente: " + utenteObj[0] + " - Valutazione: " + utenteObj[1] + " - Profilo: " + utenteObj[2]);
		            
		            // UPDATE VALUTATORE
		            p = mappaProfili.get(utenteObj[2]);
		            System.out.println("Mappa - ID_Profilo: " + p.getIdProfilo() + " - Reputazione: " + p.getReputazione());

		            if (((Integer)inserzioneObj[3] > 0 && (Integer)utenteObj[1] > 0) || ((Integer)inserzioneObj[3] < 0 && (Integer)utenteObj[1] < 0))
		            	Dati.getInstance().modificaProfilo(p.getIdProfilo(),
									            			p.getCreditiAcquisiti() + 2,
									            			p.getCreditiPendenti() - 2,
									            			(p.getNumeroInserzioniPositive() + p.getNumeroValutazioniPositive() + 1) / (p.getNumeroInserzioniTotali() + p.getNumeroValutazioniTotali() + 1),
									            			p.getPremium(),
									            			p.getContatoreInfrazioni(),
									            			p.getNumeroInserzioniPositive(),
									            			p.getNumeroInserzioniTotali(),
									            			p.getNumeroValutazioniPositive() + 1,
									            			p.getNumeroValutazioniTotali() + 1);
		            else
		            	Dati.getInstance().modificaProfilo(p.getIdProfilo(),
									            			p.getCreditiAcquisiti() - 2,
									            			p.getCreditiPendenti() - 2,
									            			(p.getNumeroInserzioniPositive() + p.getNumeroValutazioniPositive()) / (p.getNumeroInserzioniTotali() + p.getNumeroValutazioniTotali() + 1),
									            			p.getPremium(),
									            			p.getContatoreInfrazioni(),
									            			p.getNumeroInserzioniPositive(),
									            			p.getNumeroInserzioniTotali(),
									            			p.getNumeroValutazioniPositive(),
									            			p.getNumeroValutazioniTotali() + 1);
	            }
	         }
			
			tx.commit();
		} catch(RuntimeException e) {
			if(tx!=null)
				tx.rollback();
			throw e;
		} finally {
			if(session!=null && session.isOpen())
				session.close();
		}
	}
}
