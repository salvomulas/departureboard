package departureboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import departureboard.board.DepartureBoard;
import departureboard.model.*;

public class View extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Define buttons
	private JToggleButton buttonDisplayPane;
	private JButton buttonUndo;
	private JButton buttonRedo;
	private JButton buttonSave;
	private JButton buttonTrainEnters;
	private JButton buttonTrainLeaves;
	private JButton buttonFirstEntry;
	
	// Define text fields and text areas
	private JTextField textFieldTime;
	private JTextField textFieldDirection;
	private JTextField textFieldTrainNr;
	private JTextField textFieldTrack;
	private JTextArea textAreaVia;
	private JTextField textFieldFilter;
	
	// Define labels
	private JLabel labelTime;
	private JLabel labelDirection;
	private JLabel labelTrainNr;
	private JLabel labelTrack;
	private JLabel labelVia;
	private JLabel labelFilter;
	
	// Define table
	private JTable table;
	private JScrollPane jscrollpane;
    TableRowSorter<TableModel> sorter;
    
    // Define departure board
    JDialog boardDialog;
	DepartureBoard board;
	Train previousFirstEntry;
	int firstEntryIndex;
	int lastEntryIndex;

    // Define model and controller
	private final Master model;
	private final Controller controller;

	public View(Master model, Controller controller) {
		super("Departure Board!");
		this.model = model;
		this.controller = controller;
	}

	public void createAndShow() {	
		initializeComponents();
		JPanel contents = layoutComponents();
		addEvents();

		add(contents);
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // window will be closed using windowListener
		setVisible(true);
	}

	private void initializeComponents() {
		// Initialize buttons
		buttonDisplayPane = new JToggleButton(new ImageIcon("resources/on.png"));
		buttonUndo = new JButton(new ImageIcon("resources/undo-icon.png"));
		buttonUndo.setEnabled(false);
		buttonRedo = new JButton(new ImageIcon("resources/redo-icon.png"));
		buttonRedo.setEnabled(false);
		buttonSave = new JButton(new ImageIcon("resources/save-icon.png"));
		buttonTrainEnters = new JButton("Fährt ein");
		buttonTrainEnters.setEnabled(false);
		buttonTrainLeaves = new JButton("Fährt aus");
		buttonTrainLeaves.setEnabled(false);
		buttonFirstEntry = new JButton("erster Eintrag auf Abfahrtstafel");
		buttonFirstEntry.setEnabled(false);
		
		// Initalize text fields and text areas
		textFieldTime = new JTextField(20);
		textFieldTime.setEnabled(false);
		textFieldDirection = new JTextField(20);
		textFieldDirection.setEnabled(false);
		textFieldTrainNr = new JTextField(20);
		textFieldTrainNr.setEnabled(false);
		textFieldTrack = new JTextField(20);
		textFieldTrack.setEnabled(false);
		textAreaVia = new JTextArea(5, 20);
		textAreaVia.setEnabled(false);
		textFieldFilter = new JTextField(20);

		// Initialize labels
		labelTime = new JLabel("Uhrzeit");
		labelDirection = new JLabel("in Richtung");
		labelTrainNr = new JLabel("Fahrt");
		labelTrack = new JLabel("Gleis");
		labelVia = new JLabel("über");
		labelFilter = new JLabel("Suche: ");
		
		// Initialize table
		final TableModel model = new TableAdapter();
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Table Sorter used to sort and filter table
		sorter = new TableRowSorter<TableModel>(model);
	    table.setRowSorter(sorter);	
		jscrollpane = new JScrollPane(table);	
		
		// Initialize departure board
		boardDialog = new JDialog(View.this, "Departure Board");
		board = new DepartureBoard();
		
	}

	private JPanel layoutComponents() {
		// Create total panel
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		// Create toolbar
		JToolBar toolbar = new JToolBar();

		// Create split pane
		JSplitPane splitpane = new JSplitPane();
		splitpane.setContinuousLayout(true);
		
		// Create GridBag panel
		JPanel gridbag = new JPanel();
		gridbag.setLayout(new GridBagLayout());
		gridbag.setMinimumSize(new Dimension(310, 300));
		gridbag.setPreferredSize(new Dimension(310, 300));
		GridBagConstraints c = new GridBagConstraints();
		
		// Configure jscrollpane
		jscrollpane.setMinimumSize(new Dimension(400, 300));
		jscrollpane.setPreferredSize(new Dimension(400, 300));
		
		
		// Add stuff to toolbar
		toolbar.add(buttonDisplayPane);
		toolbar.addSeparator(new Dimension(10, 40));
		toolbar.add(buttonUndo);
		toolbar.add(buttonRedo);
		toolbar.addSeparator(new Dimension(10, 40));
		toolbar.add(buttonSave);
		toolbar.addSeparator(new Dimension(10, 40));
		toolbar.add(labelFilter);
		toolbar.add(textFieldFilter);
		
		// Add stuff to split plane
		splitpane.setLeftComponent(jscrollpane);
		splitpane.setRightComponent(gridbag);
		
		// Add stuff to GridBag panel
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;		
		c.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTime, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;		
		gridbag.add(textFieldTime, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;		
		c.anchor = GridBagConstraints.WEST;
		gridbag.add(labelDirection, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;		
		gridbag.add(textFieldDirection, c);

		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 0;		
		c.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTrainNr, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;		
		gridbag.add(textFieldTrainNr, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.weighty = 0;		
		c.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTrack, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 0;		
		gridbag.add(textFieldTrack, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;
		c.weighty = 1;		
		gridbag.add(labelVia, c);
		
		c.fill = GridBagConstraints.BOTH; 
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 2;
		c.weightx = 1;
		c.weighty = 1;
		textAreaVia.setLineWrap(true);
		textAreaVia.setWrapStyleWord(true);
		gridbag.add(textAreaVia, c);
		
		c.fill = GridBagConstraints.NONE; 
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		gridbag.add(buttonTrainEnters, c);
		
		c.fill = GridBagConstraints.NONE; 
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 0;
		gridbag.add(buttonTrainLeaves, c);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridx = 0;
		c.gridy = 6;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 3;
		c.weightx = 1;
		c.weighty = 0;
		c.ipadx = 60;
		gridbag.add(buttonFirstEntry, c);		
		
		// Add departure board
		boardDialog.add(board);
		boardDialog.pack();
		
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(splitpane, BorderLayout.CENTER);
		
		return panel;
	}

	private void addEvents() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(
						View.this,
						"Programm wirklich beenden?",
						"Confirm",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE
				);
				if (answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		boardDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {	
				buttonDisplayPane.setSelected(false);
			}
		});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			    boolean isAdjusting = e.getValueIsAdjusting();
			    if(isAdjusting == false) {
			    	int rowIndexView = lsm.getMinSelectionIndex(); // get row index in the current view
			    	int rowIndexModel;
			    	if (rowIndexView != -1) {
			    		rowIndexModel = table.convertRowIndexToModel(rowIndexView); // convert to row index of model
			    	} else {
			    		rowIndexModel = -1;
			    	}
					System.out.println("Clicked on line " + rowIndexModel);
					controller.setActiveTrain(rowIndexModel);
			    }
			}			
		});
		textFieldTime.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.setTime(textFieldTime.getText());				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textFieldDirection.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.setDirection(textFieldDirection.getText());				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textFieldTrainNr.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.setTrainNr(textFieldTrainNr.getText());				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textFieldTrack.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.setTrack(textFieldTrack.getText());				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textAreaVia.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.setVia(textAreaVia.getText());				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textFieldFilter.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {
				String text = textFieldFilter.getText();
				System.out.println("Typed " + textFieldFilter.getText());
				// Set table filter to null if text field is empty
				if (text.length() == 0) {
					sorter.setRowFilter(null);
				// Set table filter to search string of text field
			    } else {
			    	try {
			    		sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
			    		System.out.println("Setting filter to " + text);
			    	} catch (PatternSyntaxException pse) {
			    		System.err.println("Bad regex pattern");
			    	}
			    }			
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		buttonDisplayPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(boardDialog.isVisible() == false) {
					boardDialog.setVisible(true);
				} else {
					boardDialog.setVisible(false);
				}
			}
		});
		buttonUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		buttonRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.saveData();
			}			
		});
		buttonTrainEnters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setStatus("im Bahnhof");
			}			
		});
		buttonTrainLeaves.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setStatus("abgefahren");
			}			
		});
		buttonFirstEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setFirstEntry();
			}
		});
		
		// Add Observer for model
		model.addObserver(new Observer() {
			@Override
			public void update(Observable m) {
				// Get model
				Master myModel = (Master) m;
				// Get index of active item
				ArrayList<Train> allTrains = model.getAllTrains();				
				int activeIndex = allTrains.indexOf(model.getActiveTrain());
				System.out.println("Active index: " + activeIndex);
				// Get table staff
				TableAdapter ta = (TableAdapter) table.getModel();
				int rowIndex;
				if(activeIndex != -1) {
					rowIndex = table.convertRowIndexToView(activeIndex);
					// Reset the table view filter, if the selected row is not included in the displayed rows (mainly used for undo / redo)
					if(rowIndex == -1) {
						sorter.setRowFilter(null);
						rowIndex = table.convertRowIndexToView(activeIndex);
					}
					int colIndex = table.getSelectedColumn();					
					table.changeSelection(rowIndex, colIndex, false, false); // change selection to active row and col
				} else {
					rowIndex = 0;
					table.clearSelection();
				}
				System.out.println("Selected: " + rowIndex);
				System.out.println("MODEL CHANGED!");
				// Get staff for departure board
				boolean updateDepartureBoard = false;		
				
				
				// Update text fields and table only when values has changed!
				if(myModel.getActiveTrain() == null) {
					textFieldTime.setEnabled(false);
					textFieldTime.setText("");
				} else if(textFieldTime.getText() != myModel.getActiveTrain().getTime()) {
					textFieldTime.setEnabled(true);
					textFieldTime.setText(myModel.getActiveTrain().getTime());
					ta.fireTableCellUpdated(rowIndex, 0); // update table if value has changed'
					// Update departure board only if these lines are visible on it
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}
				}
				if(myModel.getActiveTrain() == null) {
					textFieldTrainNr.setEnabled(false);
					textFieldTrainNr.setText("");
				} else if(textFieldTrainNr.getText() != myModel.getActiveTrain().getTrainNr()) {
					textFieldTrainNr.setEnabled(true);
					textFieldTrainNr.setText(myModel.getActiveTrain().getTrainNr());
					ta.fireTableCellUpdated(rowIndex, 1); // update table if value has changed
					// Update departure board only if these lines are visible on it
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}					
				}
				if(myModel.getActiveTrain() == null) {
					textFieldDirection.setEnabled(false);
					textFieldDirection.setText("");
				} else if(textFieldDirection.getText() != myModel.getActiveTrain().getDirection()) {
					textFieldDirection.setEnabled(true);
					textFieldDirection.setText(myModel.getActiveTrain().getDirection());
					ta.fireTableCellUpdated(rowIndex, 2); // update table if value has changed
					// Update departure board only if these lines are visible on it
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}				
				}				
				if(myModel.getActiveTrain() == null) {
					textFieldTrack.setEnabled(false);
					textFieldTrack.setText("");
				} else if(textFieldTrack.getText() != myModel.getActiveTrain().getTrack()) {
					textFieldTrack.setEnabled(true);
					textFieldTrack.setText(myModel.getActiveTrain().getTrack());
					// Update departure board only if these lines are visible on it
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}				
				}
				if(myModel.getActiveTrain() == null) {
					textAreaVia.setEnabled(false);
					textAreaVia.setText("");
				} else if(textAreaVia.getText() != myModel.getActiveTrain().getVia()) {
					textAreaVia.setEnabled(true);
					textAreaVia.setText(myModel.getActiveTrain().getVia());
					// Update departure board only if these lines are visible on it
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}					
				}
				// Enable / disable train buttons
				if(myModel.getActiveTrain() == null) {
					buttonTrainEnters.setEnabled(false);
					buttonTrainLeaves.setEnabled(false);
					buttonFirstEntry.setEnabled(false);
				} else if(myModel.getActiveTrain().getStatus().equals("im Bahnhof")) {
					buttonTrainEnters.setEnabled(false);
					buttonTrainLeaves.setEnabled(true);
					buttonFirstEntry.setEnabled(true);					
					ta.fireTableCellUpdated(rowIndex, 3); // update table if value has changed
				} else if (myModel.getActiveTrain().getStatus().equals("abgefahren")) {
					buttonTrainEnters.setEnabled(false);
					buttonTrainLeaves.setEnabled(false);
					buttonFirstEntry.setEnabled(true);					
					ta.fireTableCellUpdated(rowIndex, 3); // update table if value has changed
					
				} else {
					buttonTrainEnters.setEnabled(true);
					buttonTrainLeaves.setEnabled(true);
					buttonFirstEntry.setEnabled(true);
				}
				// Enable / disable undo / redo buttons
				if(myModel.hasUndo()) {
					buttonUndo.setEnabled(true);
				} else {
					buttonUndo.setEnabled(false);
				}
				if(myModel.hasRedo()) {
					buttonRedo.setEnabled(true);
				} else {
					buttonRedo.setEnabled(false);
				}

				// Update only if another first entry has been set
				if(model.getFirstEntry() != previousFirstEntry) {
					updateDepartureBoard = true;
				}
				
				// Update departure board if needed
				// IDEA:
				// The departure board is only updated if first entry has changed or 
				// if any attributes of the displayed trains on the departure board have changed.
				// If a line which is not displayed on the departure board has changed, do not update the departure board 
				if(activeIndex != -1 && updateDepartureBoard == true) {
					System.out.println("Updating departure board!");
					previousFirstEntry = model.getFirstEntry();
					int rowCount = 0;
					int activeFirstIndex = allTrains.indexOf(model.getFirstEntry());
					ArrayList<Train> boardTrains = new ArrayList<Train>();
					// Get 5 valid trains for departure board (not leaved yet)
					for(int index = activeFirstIndex; index < allTrains.size(); index++) {
						String status = allTrains.get(index).getStatus();
						if(!status.equals("abgefahren")) {
							boardTrains.add(allTrains.get(index));
							// Leave loop after 5 lines
							if(rowCount < 4) {
								rowCount++;							
							} else {
								break;
							}
						}
					}
					// Save first and last entry index for later (to update only if needed)
					firstEntryIndex = allTrains.indexOf(model.getFirstEntry());
					lastEntryIndex = allTrains.indexOf(boardTrains.get(boardTrains.size() - 1));
					// Add trains to departure board
					for(int index = 0; index < 5; index++) {
						String status;
						String hour;
						String minute;
						String direction;
						String track;
						String info;
						if(index < boardTrains.size()) {
							// Get train status
							status = boardTrains.get(index).getStatus();
							String time = boardTrains.get(index).getTime();
							hour = time.split(":")[0];
							minute = time.split(":")[1];
							direction = boardTrains.get(index).getDirection();
							direction = direction.replace("ü", "ue");
							direction = direction.replace("ä", "ae");
							direction = direction.replace("ö", "oe");
							track = boardTrains.get(index).getTrack();
							info = boardTrains.get(index).getTrainNr() + ": " + boardTrains.get(index).getVia();
						} else {
							status = "";
							hour = "";
							minute = "";
							direction = "";
							track = "";
							info = "";
						}
						// Set line on departure board
						if(status.equals("im Bahnhof")) {
							board.getRows().get(index).setBlinking(true);
						} else {
							board.getRows().get(index).setBlinking(false);
						}
						board.getRows().get(index).setHour(hour);
						board.getRows().get(index).setMinute(minute);
						board.getRows().get(index).setDestination(direction);
						board.getRows().get(index).setTrack(track);
						board.getRows().get(index).setInfo(info);			
					}
				}
				
			}
		});
	}		

	private class TableAdapter extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
		
		@Override
		public void setValueAt(Object o, int rowIndex, int columnIndex) {		
			switch (columnIndex) {
			case 0:
				controller.setTime(o.toString());
				break;
			case 1:
				controller.setTrainNr(o.toString());
				break;
			case 2:
				controller.setDirection(o.toString());
				break;
			case 3:
				controller.setStatus(o.toString());
				break;
			}				
			fireTableCellUpdated(rowIndex, columnIndex);
		}
		
		@Override
		public int getRowCount() {
			return model.getAllTrains().size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int column) {
			String columnName = "";
			
			switch (column) {
				case 0:
					columnName = "Uhrzeit";
					break;
				case 1:
					columnName = "Fahrt";
					break;
				case 2:
					columnName = "in Richtung";
					break;
				case 3:
					columnName = "Status";
					break;
			}
			
			return columnName;
		}

		@Override
		public Object getValueAt(int row, int column) {
			String value = "";
			
			switch (column) {
			case 0:
				value = model.getAllTrains().get(row).getTime();
				break;
			case 1:
				value = model.getAllTrains().get(row).getTrainNr();
				break;
			case 2:
				value = model.getAllTrains().get(row).getDirection();
				break;
			case 3:
				value = model.getAllTrains().get(row).getStatus();
				break;
			}			
			
			return value;
			
		}

	}
	
}
