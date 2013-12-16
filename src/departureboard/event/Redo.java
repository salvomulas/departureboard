package departureboard.event;
import departureboard.model.*;

public class Redo extends Event {

	public Redo(Train train) {
		super(train);
	}

	public Undo getUndo() {
		return new Undo(train);
	}
	
}
