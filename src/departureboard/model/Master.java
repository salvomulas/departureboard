package departureboard.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Stack;

import departureboard.*;
import departureboard.event.*;

public class Master implements Observable {

	// Variables
	private Train activeTrain;
	private Train firstEntry;
	private ArrayList<Train> allTrains;
	
	private Stack<Undo> undos;
	private Stack<Redo> redos;
	
	private final Set<Observer> observers = new HashSet<>();
	
	
	// Constructor
	public Master () {
		allTrains = new ArrayList<Train>();
		undos = new Stack<Undo>();
		redos = new Stack<Redo>();
	}
	
	// Getters & Setters
	public ArrayList<Train> getAllTrains() {
		return allTrains;
	}

	public Train getActiveTrain() {
		return activeTrain;
	}
	
	public Train getFirstEntry() {
		return firstEntry;
	}
	
	public void setActiveTrain(Train activeTrain) {
		// Update and notify only when object is not the same
		if (this.activeTrain != activeTrain) {
			this.activeTrain = activeTrain;
			notifyObservers();
		}
	}
	
	public void setActiveTrainTime(String time) {
		// Update and notify only when value is not the same
		if (!this.activeTrain.getTime().equals(time)) {
			activeTrain.setTime(time);
			notifyObservers();
		}
	}	

	public void setActiveTrainTrainNr(String trainNr) {
		if (!this.activeTrain.getTrainNr().equals(trainNr)) {
			activeTrain.setTrainNr(trainNr);
			notifyObservers();		
		}
	}

	public void setActiveTrainDirection(String direction) {
		if (!this.activeTrain.getDirection().equals(direction)) {
			activeTrain.setDirection(direction);
			notifyObservers();	
		}
	}

	public void setActiveTrainTrack(String track) {
		if (!this.activeTrain.getTrack().equals(track)) {
			activeTrain.setTrack(track);
			notifyObservers();		
		}
	}

	public void setActiveTrainVia(String via) {
		if (!this.activeTrain.getVia().equals(via)) {
			activeTrain.setVia(via);
			notifyObservers();
		}
	}

	public void setActiveTrainStatus(String status) {
		if (!this.activeTrain.getStatus().equals(status)) {
			activeTrain.setStatus(status);
			notifyObservers();
		}
	}		
	
	public void setFirstEntry(Train firstEntry) {
		// Update and notify only when object is not the same
		if (this.firstEntry != firstEntry) {
			this.firstEntry = firstEntry;
			notifyObservers();
		}
	}
	
	// Methods for Undo / Redo
	public void addUndo(Undo undo) {
		undos.push(undo);
	}
	
	public void addRedo(Redo redo) {
		redos.push(redo);
	}
	
	public Undo getLastUndo() {
		return undos.pop();
	}
	
	public Redo getLastRedo() {
		return redos.pop();
	}
	
	public boolean hasUndo() {
		return !undos.empty();
	}
	
	public boolean hasRedo() {
		return !redos.empty();
	}

	public void clearRedo() {
		redos.clear();
	}
	
	// Methods for CSV file import / export
	public void importCsvFile(String filepath) {
	    try
	    {		
	      FileInputStream fis = new FileInputStream(filepath);
	      BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
	      br.readLine(); // Jump over the first line
	      String stringRead = br.readLine();
	      
	      while( stringRead != null )
	      {
	          String[] elements = stringRead.split(";", -1);

//	          if(elements.length < 5) {
//	            throw new RuntimeException("line too short"); //handle missing entries
//	          }

	  		String time = elements[0];
			String trainNr = elements[1];
			String direction = elements[2];
			String via = elements[3];
			String track = elements[4];	           
			
	          // Create new train object
	          allTrains.add(new Train(time, trainNr, direction, via, track));

	          // read the next line
	          stringRead = br.readLine();
	      }
	      
	      br.close();
	      
	    }
	    catch(IOException ioe)
	    {
	    	System.out.println("IO EXCEPTION!!!");
	    	ioe.printStackTrace();
	    }
	}
	
	public void exportCsvFile(String filepath) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filepath);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.append("Uhrzeit;Fahrt;in Richtung;über;Gleis;\r\n");
			for(Train t: allTrains) {
				bw.append(t.getTime() + ";" + t.getTrainNr() + ";" + t.getDirection() + ";" + t.getVia() + ";"+ t.getTrack() + ";\r\n");
			}
			bw.flush();
			bw.close();			
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			System.out.println("IO EXCEPTION!!!");
			ioe.printStackTrace();
		}
	}
	
	// Methods from interface Observable
	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	private void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(this);
		}
	}
	
}
