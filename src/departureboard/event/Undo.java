package departureboard.event;
import departureboard.model.*;

public class Undo extends Event {

	public Undo (Train train) {
		super(train);
	}
	
	public Redo getRedo() {
		return new Redo(train);
	}
	
}