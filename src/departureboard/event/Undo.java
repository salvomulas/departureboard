package departureboard.event;
import departureboard.model.*;

/**
 * Child-class of Event - defines the undo type
 * @author Salvatore Mulas
 */
public class Undo extends Event {

	/**
	 * Class constructor which inherits from parent
	 * @param train Reads all attributes of the given train
	 */
	public Undo (Train train) {
		super(train);
	}
	
	/**
	 * Returns a new event
	 * @return train
	 */
	public Redo getRedo() {
		return new Redo(train);
	}
	
}