package departureboard;

public interface Observable {
	
	void addObserver(Observer observer);
	void removeObserver(Observer observer);
	
}