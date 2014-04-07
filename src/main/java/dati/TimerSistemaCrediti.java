package dati;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
			
			// TODO Sostituire (count(vi.Valutazione) -> t1.totaleVoti
			Query q1 = session.createSQLQuery("select vi.ID_Inserzione, vi.ID_UtenteInserzionista, sum(vi.Valutazione*p.Reputazione)/(count(vi.Valutazione)*100) as valutazioneFinale " +
											  "from valutazione_inserzione vi, profilo p, (select ID_Inserzione from inserzione i where i.DataFine = :data) as t1 " +
											  "where vi.ID_Inserzione = t1.ID_inserzione and vi.ID_UtenteValutatore = p.ID_Utente" +
											  "group by vi.ID_Inserzione");
			q1.setParameter("data", formato.format(cal.getTime()));
			List inserzioniDaValutareList = q1.list();
			
			 Query q2; // Update inserzionista
	         Query q3; // Update valutatore
	         
	         String q2Text = "";
	         String q3Text = "";
			
			for(int i=0; i<inserzioniDaValutareList.size(); i++) {
	            Object[] inserzioneObj = (Object[])inserzioniDaValutareList.get(i);
	            System.out.println("ID_Inserizione: " + inserzioneObj[0] + " - ID_UtenteInserzionista: " + inserzioneObj[1] + " - Calcolo Valutazioni: " + inserzioneObj[2]);
	            
	            // UPDATE INSERZIONISTA
	            q2Text = "update profilo p" +
				  		"set p.CreditiPendenti = p.CreditiPendenti - 10,";
	            
	            if ((Integer)inserzioneObj[2] > 0)
	            	q2Text += "p.CreditiAcquisiti = p.CreditiAcquisiti + 10" +
	            	          "p.NumeroInserzioniPositive = p.NumeroInserzioniPositive + 1," +
	            			  "p.NumeroInserzioniTotali = p.NumeroInserzioniTotali + 1," +
	            			  "p.Reputazione = (p.NumeroInserzioniPositive + 1 + p.NumeroValutazioniPositive) / (p.NumeroInserzioniTotali + 1 + p.NumeroValutazioniTotali)";
	            else
	            	q2Text += "p.CreditiAcquisiti = p.CreditiAcquisiti - 10" +
	            			  "p.NumeroInserzioniTotali = p.NumeroInserzioniTotali + 1," +
	            			  "p.Reputazione = (p.NumeroInserzioniPositive + p.NumeroValutazioniPositive) / (p.NumeroInserzioniTotali + 1 + p.NumeroValutazioniTotali)," +
	            			  "p.ContatoreInfrazioni = p.ContatoreInfrazioni + 1";
	            
	            q2Text += "where p.ID_Utente in (select i.ID_Utente" +
												"from inserzione i" +
												"where i.ID_Inserzione = :idInserzione)";
	            
	            q2 = session.createSQLQuery(q2Text);
	            q2.setParameter("idInserzione", inserzioneObj[0]);
	            q2.executeUpdate();
	            
	            // SELECT UTENTI VALUTATORI PER L'INSERZIONE CONSIDERATA
	            q2Text = "select vi.ID_UtenteValutatore, vi.Valutazione" +
	            		 "from valutazione_inserzione vi" +
	            		 "where vi.ID_Inserzione = :idInserzione";
	            q2 = session.createSQLQuery(q2Text);
	            q2.setParameter("idInserzione", inserzioneObj[0]);
	            List utentiValutatoriList = q2.list();
	            
	            for(int j=0; j<utentiValutatoriList.size(); j++) {
		            Object[] utenteObj = (Object[])utentiValutatoriList.get(i);
		            System.out.println("ID_Utente: " + utenteObj[0] + " - Valutazione: " + utenteObj[1]);
		            
		            // UPDATE VALUTATORE
		            q3Text = "update profilo p" +
					  		"set p.CreditiPendenti = p.CreditiPendenti - 2,";

		            if (((Integer)inserzioneObj[2] > 0 && (Integer)utenteObj[1] > 0) || ((Integer)inserzioneObj[2] < 0 && (Integer)utenteObj[1] < 0))
		            	q3Text += "p.CreditiAcquisiti = p.CreditiAcquisiti + 2" +
		            			  "p.NumeroValutazioniPositive = p.NumeroValutazioniPositive + 1," +
		            			  "p.NumeroValutazioniTotali = p.NumeroValutazioniTotali + 1," +
		            			  "p.Reputazione = (p.NumeroInserzioniPositive + p.NumeroValutazioniPositive + 1) / (p.NumeroInserzioniTotali + p.NumeroValutazioniTotali + 1)";
		            else
		            	q3Text += "p.CreditiAcquisiti = p.CreditiAcquisiti - 2" +
		            			  "p.NumeroValutazioniTotali = p.NumeroValutazioniTotali + 1," +
		            			  "p.Reputazione = (p.NumeroInserzioniPositive + p.NumeroValutazioniPositive) / (p.NumeroInserzioniTotali + p.NumeroValutazioniTotali + 1)";
		            
		            q3Text += "where p.ID_profilo = :idUtente";
		            q3 = session.createSQLQuery(q3Text);
		            q3.setParameter("idUtente", utenteObj[0]);
		            q3.executeUpdate();
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
