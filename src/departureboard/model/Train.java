package departureboard.model;

// This class is used for the train objects

public class Train {

	// Variables
	private String time;
	private String trainNr;
	private String direction;
	private String via;
	private String track;
	private String status;

	// Constructor
	public Train (String time, String trainNr, String direction, String via, String track) {
		this.time = time;
		this.trainNr = trainNr;
		this.direction = direction;
		this.via = via;
		this.track = track;
		this.status = "hat Einfahrt";
	}

	// Getters & Setters
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTrainNr() {
		return trainNr;
	}

	public void setTrainNr(String trainNr) {
		this.trainNr = trainNr;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
	
}
