package departureboard.event;
import departureboard.model.*;

/**
 * Abstract class to define all events
 * @author Salvatore Mulas
 */
public abstract class Event {
	
	Train train;
	String time, direction, track, trainNr, via, status;
	
	/**
	 * Class constructor
	 * @param train Passes all attributes of the train to the instance
	 */
	public Event (Train train) {
		this.train = train;
		this.time = train.getTime();
		this.direction = train.getDirection();
		this.track = train.getTrack();
		this.trainNr = train.getTrainNr();
		this.via = train.getVia();
		this.status = train.getStatus();
	}
	
	// Eclipse auto-generated getters and setters below this line
	
	public Train getTrain() {
		return train;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public String getTrack() {
		return track;
	}
	
	public String getTrainNr() {
		return trainNr;
	}
	
	public String getVia() {
		return via;
	}
	
	public String getStatus() {
		return status;
	}
	
}
