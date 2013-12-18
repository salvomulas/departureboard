package departureboard;

public interface Observable {
	
	void removeObserver(Observer observer);
	void addObserver(Observer observer);
	
}