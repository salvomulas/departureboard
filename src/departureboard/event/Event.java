package departureboard.event;
import departureboard.model.*;

public abstract class Event {
	
	Train train;
	String time;
	String direction;
	String track;
	String trainNr;
	String via;
	String status;
	
	public Event (Train train) {
		this.train = train;
		this.time = train.getTime();
		this.direction = train.getDirection();
		this.track = train.getTrack();
		this.trainNr = train.getTrainNr();
		this.via = train.getVia();
		this.status = train.getStatus();
	}
	
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
