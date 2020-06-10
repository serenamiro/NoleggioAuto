package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	//CODA DEGLI EVENTI (la classe simulatore ha come struttura dati principale una coda prioritaria)
	private PriorityQueue<Event> queue = new PriorityQueue<>();
	
	// PARAMETRI DI SIMULAZIONE
	private int NC = 10; // number of cars
	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES); // intervallo tra i clienti
	// è meglio inizializzare queste variabili con dei valori di default (per evitare errori in esecuzione)
	// per questo es, servono due parametri di simulazione non controllabili dall'esterno (orario di apertura e chiusura)
	private final LocalTime oraApertura = LocalTime.of(8, 00);
	private final LocalTime oraChiusura = LocalTime.of(17, 00);
	
	// MODELLO DEL MONDO
	// rappresentato dal numero di auto ancora disponibili nel deposito (compreso tra 0 e NC)
	private int nAuto;
	
	// VALORI DA CALCOLARE 
	// numero di clienti arrivati e numero di clienti insoddisfatti
	private int clienti;
	private int insoddisfatti;
	
	// METODI PER IMPOSTARE I PARAMETRI 
	public void setNumCars(int N) {
		this.NC = N;
	}
	
	public void setClientFrequency(Duration d) {
		this.T_IN = d;
	}

	// METODI PER RESTITUIRE I RISULTATI
	public int getClienti() {
		return clienti;
	}

	public int getInsoddisfatti() {
		return insoddisfatti;
	}
	
	// SIMULAZIONE VERA E PROPRIA
	public void run() {
		// 1. preparazione iniziale (impostare variabili mondo + coda eventi)
		this.nAuto = this.NC;
		this.clienti = this.insoddisfatti = 0;
		
		this.queue.clear();
		LocalTime oraArrivoCliente = this.oraApertura;
		do {
			Event e = new Event(oraArrivoCliente, EventType.NEW_CLIENT);
			this.queue.add(e);
			oraArrivoCliente = oraArrivoCliente.plus(this.T_IN);
		} while(oraArrivoCliente.isBefore(this.oraChiusura));
		
		// 2. esecuzione del ciclo di simulazione
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			// il poll() garantisce che l'estrazione dell'evento avverrà in ordine di data
			System.out.println(e);
			processEvent(e);			
		}
		
	}
	
	public void processEvent(Event e) {
		switch(e.getType()) {
		case NEW_CLIENT:
			
			if(this.nAuto>0) {
				// cliente viene servito, auto noleggiata
				// 1. aggiornare il modello del mondo
				this.nAuto--;
				
				// 2. aggiornare i risultati
				this.clienti++;
				
				// 3. genera nuovi eventi (in questo caso, restituzione dell'auto)
				double num = Math.random(); // [0, 1)
				
				Duration travel;
				if(num<1.0/3.0)
					travel = Duration.of(1, ChronoUnit.HOURS);
				else if (num<2.0/3.0)
					travel = Duration.of(2, ChronoUnit.HOURS);
				else
					travel = Duration.of(3, ChronoUnit.HOURS);
				
				Event nuovo = new Event(e.getTime().plus(travel), EventType.CAR_RETURNED);
				this.queue.add(nuovo);
				
			} else {
				// cliente insoddisfatto
				this.clienti++;
				this.insoddisfatti++;
			}
			break;
			
		case CAR_RETURNED:
			this.nAuto++; 
			break;
		}
	}

}
