package departureboard;
import departureboard.model.*;
import departureboard.event.*;
import departureboard.view.*;

/**
 * @author Salvatore Mulas
 * Default controller for the application
 */
public class Controller {
	
	/**
	 * Declaring model and view as final attributes
	 */
	private final Master model;
	private final View view;

	/**
	 * Setting the controller's constructor by passing the model
	 * @param model
	 */
	public Controller(Master model) {
		this.model = model;
		this.view = new View (this,model);
	}
	
	/**
	 * Loading the CSV file (static filename)
	 */
	public void loadData() {
		model.importCsvFile("res/olten.txt");	
	}
	
	/**
	 * Saving the CSV file (static filename)
	 */
	public void saveData() {
		model.exportCsvFile("res/olten.txt");
		System.out.println("Gespeichert!");
	}

	/**
	 * Initializes the view
	 */
	public void viewInit(){
		view.generateView();
	}
	
	// Setter methods for all attributes below this line
	
	public void setActiveTrain(int row) {
		if(row != -1) {
			model.setActiveTrain(model.getAllTrains().get(row));
		} else {
			model.setActiveTrain(null);
		}
	}
	
	public void setFirstEntry() {
		model.setFirstEntry(model.getActiveTrain());
	}

	public void setTime(String time) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainTime(time);
	}	
	
	public void setTrainNr(String trainNr) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainTrainNr(trainNr);
	}
	
	public void setDirection(String direction) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainDirection(direction);
	}
	
	public void setTrack(String track) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainTrack(track);
	}
	
	public void setVia(String via) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainVia(via);
	}
	
	public void setStatus(String status) {
		model.addUndo(new Undo(model.getActiveTrain()));
		model.clearRedo();
		model.setActiveTrainStatus(status);
	}
	
	/**
	 * Method to implement the undo function
	 * Reads the last undo and adds a new record to the opposite
	 */
	public void undo() {
		Undo undo = model.getLastUndo();
		model.addRedo(new Redo(undo.getTrain()));		
		model.setActiveTrain(undo.getTrain());
		model.setActiveTrainTime(undo.getTime());
		model.setActiveTrainTrainNr(undo.getTrainNr());
		model.setActiveTrainDirection(undo.getDirection());
		model.setActiveTrainTrack(undo.getTrack());
		model.setActiveTrainVia(undo.getVia());
		model.setActiveTrainStatus(undo.getStatus());
	}
	
	/**
	 * Method to implement the redo function
	 * Reads the last redo and adds a new record to the opposite
	 */
	public void redo() {
		Redo redo = model.getLastRedo();
		model.addUndo(new Undo(redo.getTrain()));
		model.setActiveTrain(redo.getTrain());
		model.setActiveTrainTime(redo.getTime());
		model.setActiveTrainTrainNr(redo.getTrainNr());
		model.setActiveTrainDirection(redo.getDirection());
		model.setActiveTrainTrack(redo.getTrack());
		model.setActiveTrainVia(redo.getVia());
		model.setActiveTrainStatus(redo.getStatus());		
	}
	
}
