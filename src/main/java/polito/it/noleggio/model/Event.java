package polito.it.noleggio.model;

import java.time.LocalTime;

public class Event  implements Comparable<Event>{
	
	// SERVE PER RAPPRESENTARE IL SINGOLO EVENTO DI SIMULAZIONE
	// Un evento è costituito almeno da due campi sempre presenti: il tempo e il tipo di evento
	// (eventualmente possono essere presenti anche altri campi in base alle specifiche del problema)
	
	// Nel nostro caso sono possibili due tipi di eventi -> ENUMERAZIONE (classe di Java)
	
	public enum EventType{
		NEW_CLIENT, CAR_RETURNED
		// sto definendo una microclasse di tipo enumerazione all'interno di event
		// new_client e car_returned sono due costanti che indicano i due tipi di eventi
	}
	
	// questi due attributi sono sempre presenti negli eventi:
	private LocalTime time;
	private EventType type;
	
	// bisogna definire il criterio di ordinamento perchè gli eventi finiscono in una coda prioritaria
	
	public Event(LocalTime time, EventType type) {
		this.time = time;
		this.type = type;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	public int compareTo(Event other) {
		// delega al comparatore di time
		return this.time.compareTo(other.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + "]";
	}
	
	
}
