package departureboard.event;
import departureboard.model.*;

/**
 * Child-class of Event - defines the redo type
 * @author Salvatore Mulas
 */
public class Redo extends Event {

	/**
	 * Class constructor which inherits from parent
	 * @param train Reads all attributes of the given train
	 */
	public Redo(Train train) {
		super(train);
	}

	/**
	 * Returns a new event
	 * @return train
	 */
	public Undo getUndo() {
		return new Undo(train);
	}
	
}
