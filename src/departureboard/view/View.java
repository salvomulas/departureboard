package departureboard.view;

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
import javax.swing.JMenuBar;
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
import departureboard.*;

/**
 * @author Salvatore Mulas
 * View class which draws the entire user interface
 */

public class View extends JFrame {

	// Providing a serial version to avoid warnings
	private static final long serialVersionUID = 1L;
	
	// Model and controller
	private final Master model;
	private final Controller controller;
	
	// Menubar
	private JMenuBar nav;
	
	// Parent elements
    private JDialog boardDialog;
	private DepartureBoard board;
	private Train previousFirstEntry;
	private int firstEntryIndex;
	private int lastEntryIndex;
	
	// Buttons
	private JToggleButton btnToggleBoard;
	private JButton btnArrival;
	private JButton btnDeparture;
	private JButton btnBegin;
	
	// Text fields and areas
	private JTextField textFieldTime;
	private JTextField textFieldDirection;
	private JTextField textFieldTrainNr;
	private JTextField textFieldTrack;
	private JTextField textFieldFilter;
	private JTextArea textAreaVia;
	
	// Labels
	private JLabel labelTime;
	private JLabel labelDirection;
	private JLabel labelTrainNr;
	private JLabel labelTrack;
	private JLabel labelFilter;
	private JLabel labelVia;
	
	// Table and scrollpane
	private JTable table;
	private JScrollPane jscrollpane;
    private TableRowSorter <TableModel> sorter;

	/**
	 * Class constructor
	 * @param model Loads the given model reference
	 * @param controller Loads the given controller reference
	 */
	public View(Controller controller, Master model) {
		super("SMulas: DepartureBoard");
		this.controller = controller;
		this.model = model;
	}

	/**
	 * Initializes all elements
	 */
	private void initElements() {
		
		// Buttons
		btnToggleBoard = new JToggleButton(new ImageIcon("res/on.png"));
		btnArrival = new JButton("Fährt ein");
		btnArrival.setEnabled(false);
		btnDeparture = new JButton("Fährt aus");
		btnDeparture.setEnabled(false);
		btnBegin = new JButton("erster Eintrag auf Abfahrtstafel");
		btnBegin.setEnabled(false);
		
		// Text elements
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

		// Labels
		labelTime = new JLabel("Uhrzeit");
		labelDirection = new JLabel("in Richtung");
		labelTrainNr = new JLabel("Fahrt");
		labelTrack = new JLabel("Gleis");
		labelVia = new JLabel("Über");
		labelFilter = new JLabel("Suche: ");
		
		// Table
		final TableModel model = new TableAdapter();
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Table Sort
		sorter = new TableRowSorter<TableModel>(model);
	    table.setRowSorter(sorter);	
		jscrollpane = new JScrollPane(table);	
		
		// Departure Board
		boardDialog = new JDialog(View.this, "Graphical Departure Board");
		boardDialog.setResizable(false);
		board = new DepartureBoard();
		
		// JMenuBar
		nav = Navigation.createMenuBar();
		
	}
	
	/**
	 * Creates the window
	 */
	public void generateView() {	
		initElements();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // window will be closed using windowListener
		JPanel contents = layoutComponents();
		addEvents();
		add(contents);
		pack();
	}

	/**
	 * Provides all elements to the window
	 * @return panel JPanel which is implemented into the JFrame
	 */
	private JPanel layoutComponents() {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		setJMenuBar(nav);
		
		JToolBar toolbar = new JToolBar();

		JSplitPane splitpane = new JSplitPane();
		splitpane.setContinuousLayout(true);
		
		JPanel gridbag = new JPanel();
		gridbag.setLayout(new GridBagLayout());
		gridbag.setMinimumSize(new Dimension(310, 300));
		gridbag.setPreferredSize(new Dimension(310, 300));
		GridBagConstraints g = new GridBagConstraints();
		
		jscrollpane.setMinimumSize(new Dimension(400, 300));
		jscrollpane.setPreferredSize(new Dimension(400, 300));
		
		
		toolbar.add(btnToggleBoard);
		toolbar.addSeparator(new Dimension(10, 40));
		toolbar.add(labelFilter);
		toolbar.add(textFieldFilter);
		
		splitpane.setLeftComponent(jscrollpane);
		splitpane.setRightComponent(gridbag);
		
		// Draw the entire GridBag
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;		
		g.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTime, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 1;
		g.gridy = 0;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 2;
		g.weightx = 1;
		g.weighty = 0;		
		gridbag.add(textFieldTime, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0;
		g.weighty = 0;		
		g.anchor = GridBagConstraints.WEST;
		gridbag.add(labelDirection, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 1;
		g.gridy = 1;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 2;
		g.weightx = 1;
		g.weighty = 0;		
		gridbag.add(textFieldDirection, g);

		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 2;
		g.weightx = 0;
		g.weighty = 0;		
		g.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTrainNr, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 1;
		g.gridy = 2;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 2;
		g.weightx = 1;
		g.weighty = 0;		
		gridbag.add(textFieldTrainNr, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 3;
		g.weightx = 0;
		g.weighty = 0;		
		g.anchor = GridBagConstraints.WEST;
		gridbag.add(labelTrack, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 1;
		g.gridy = 3;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 2;
		g.weightx = 1;
		g.weighty = 0;		
		gridbag.add(textFieldTrack, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 4;
		g.anchor = GridBagConstraints.WEST;
		g.weightx = 0;
		g.weighty = 1;		
		gridbag.add(labelVia, g);
		
		g.fill = GridBagConstraints.BOTH; 
		g.gridx = 1;
		g.gridy = 4;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 2;
		g.weightx = 1;
		g.weighty = 1;
		textAreaVia.setLineWrap(true);
		textAreaVia.setWrapStyleWord(true);
		gridbag.add(textAreaVia, g);
		
		g.fill = GridBagConstraints.NONE; 
		g.gridx = 0;
		g.gridy = 5;
		g.anchor = GridBagConstraints.WEST;
		g.gridwidth = 1;
		g.weightx = 1;
		g.weighty = 0;
		gridbag.add(btnArrival, g);
		
		g.fill = GridBagConstraints.NONE; 
		g.gridx = 2;
		g.gridy = 5;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 1;
		g.weightx = 1;
		g.weighty = 0;
		gridbag.add(btnDeparture, g);
		
		g.fill = GridBagConstraints.HORIZONTAL; 
		g.gridx = 0;
		g.gridy = 6;
		g.anchor = GridBagConstraints.EAST;
		g.gridwidth = 3;
		g.weightx = 1;
		g.weighty = 0;
		g.ipadx = 60;
		gridbag.add(btnBegin, g);		
		
		boardDialog.add(board);
		boardDialog.pack();
		
		panel.add(toolbar, BorderLayout.SOUTH);
		panel.add(splitpane, BorderLayout.CENTER);
		
		return panel;
	}

	/**
	 * Adding all events and actions to the GUI elements
	 */
	private void addEvents() {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(View.this,"Applikation wirklich beenden?","Beenden",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
			
		boardDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {	
				btnToggleBoard.setSelected(false);
				Navigation.dBoard.setSelected(false);
			}
		});
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			    boolean isAdjusting = e.getValueIsAdjusting();
			    if(isAdjusting == false) {
			    	int rowIndexView = lsm.getMinSelectionIndex();
			    	int rowIndexModel;
			    	if (rowIndexView != -1) {
			    		rowIndexModel = table.convertRowIndexToModel(rowIndexView);
			    	} else {
			    		rowIndexModel = -1;
			    	}
					System.out.println("Focused dataset " + rowIndexModel);
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
				System.out.println("Textinput " + textFieldFilter.getText());
				if (text.length() == 0) {
					sorter.setRowFilter(null);
			    } else {
			    	try {
			    		sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
			    		System.out.println("Filter has been applied to " + text);
			    	} catch (PatternSyntaxException pse) {
			    		System.err.println("Regex error");
			    	}
			    }			
			}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		
		btnToggleBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(boardDialog.isVisible() == false) {
					boardDialog.setVisible(true);
					Navigation.dBoard.setSelected(true);
				} else {
					boardDialog.setVisible(false);
					Navigation.dBoard.setSelected(false);
				}
			}
		});
		
		Navigation.dBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(boardDialog.isVisible() == false) {
					boardDialog.setVisible(true);
					btnToggleBoard.setSelected(true);
					Navigation.dBoard.setSelected(true);
				} else {
					boardDialog.setVisible(false);
					btnToggleBoard.setSelected(false);
					Navigation.dBoard.setSelected(false);
				}
			}
		});
		
		Navigation.exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int answer = JOptionPane.showConfirmDialog(View.this,"Applikation wirklich beenden?","Beenden",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				if (answer == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		
		Navigation.undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		Navigation.redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		Navigation.saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.saveData();
			}			
		});
		btnArrival.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setStatus("angekommen");
			}			
		});
		btnDeparture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setStatus("abgefahren");
			}			
		});
		btnBegin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setFirstEntry();
			}
		});
		
		// Adding the Observer
		model.addObserver(new Observer() {
			@Override
			public void update(Observable m) {

				Master myModel = (Master) m;
				
				// Active item
				ArrayList<Train> allTrains = model.getAllTrains();				
				int activeIndex = allTrains.indexOf(model.getActiveTrain());
				System.out.println("Active index: " + activeIndex);

				TableAdapter ta = (TableAdapter) table.getModel();
				int rowIndex;
				if(activeIndex != -1) {
					rowIndex = table.convertRowIndexToView(activeIndex);
					if(rowIndex == -1) {
						sorter.setRowFilter(null);
						rowIndex = table.convertRowIndexToView(activeIndex);
					}
					int colIndex = table.getSelectedColumn();					
					table.changeSelection(rowIndex, colIndex, false, false);
				} else {
					rowIndex = 0;
					table.clearSelection();
				}
				System.out.println("Selection: " + rowIndex);
				System.out.println("Applied!");
				boolean updateDepartureBoard = false;		
				
				if(myModel.getActiveTrain() == null) {
					textFieldTime.setEnabled(false);
					textFieldTime.setText("");
				} else if(textFieldTime.getText() != myModel.getActiveTrain().getTime()) {
					textFieldTime.setEnabled(true);
					textFieldTime.setText(myModel.getActiveTrain().getTime());
					ta.fireTableCellUpdated(rowIndex, 0);
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
					ta.fireTableCellUpdated(rowIndex, 1);
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
					ta.fireTableCellUpdated(rowIndex, 2);
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
					if(activeIndex >= firstEntryIndex && activeIndex <= lastEntryIndex) {
						updateDepartureBoard = true;
					}					
				}
				if(myModel.getActiveTrain() == null) {
					btnArrival.setEnabled(false);
					btnDeparture.setEnabled(false);
					btnBegin.setEnabled(false);
				} else if(myModel.getActiveTrain().getStatus().equals("angekommen")) {
					btnArrival.setEnabled(false);
					btnDeparture.setEnabled(true);
					btnBegin.setEnabled(true);					
					ta.fireTableCellUpdated(rowIndex, 3); // update table if value has changed
				} else if (myModel.getActiveTrain().getStatus().equals("abgefahren")) {
					btnArrival.setEnabled(false);
					btnDeparture.setEnabled(false);
					btnBegin.setEnabled(true);					
					ta.fireTableCellUpdated(rowIndex, 3); // update table if value has changed
					
				} else {
					btnArrival.setEnabled(true);
					btnDeparture.setEnabled(true);
					btnBegin.setEnabled(true);
				}

				if(myModel.hasUndo()) {
					Navigation.undoItem.setEnabled(true);
				} else {
					Navigation.undoItem.setEnabled(false);
				}
				if(myModel.hasRedo()) {
					Navigation.redoItem.setEnabled(true);
				} else {
					Navigation.redoItem.setEnabled(false);
				}

				if(model.getFirstEntry() != previousFirstEntry) {
					updateDepartureBoard = true;
				}
				
				// Force update of the board if necessary
				if(activeIndex != -1 && updateDepartureBoard == true) {
					System.out.println("Updating departure board!");
					previousFirstEntry = model.getFirstEntry();
					int rowCount = 0;
					int activeFirstIndex = allTrains.indexOf(model.getFirstEntry());
					ArrayList<Train> boardTrains = new ArrayList<Train>();
					for(int index = activeFirstIndex; index < allTrains.size(); index++) {
						String status = allTrains.get(index).getStatus();
						if(!status.equals("abgefahren")) {
							boardTrains.add(allTrains.get(index));
							if(rowCount < 4) {
								rowCount++;							
							} else {
								break;
							}
						}
					}
					firstEntryIndex = allTrains.indexOf(model.getFirstEntry());
					lastEntryIndex = allTrains.indexOf(boardTrains.get(boardTrains.size() - 1));
					for(int index = 0; index < 5; index++) {
						String status;
						String hour;
						String minute;
						String direction;
						String track;
						String info;
						if(index < boardTrains.size()) {
							status = boardTrains.get(index).getStatus();
							String time = boardTrains.get(index).getTime();
							hour = time.split(":")[0];
							minute = time.split(":")[1];
							direction = boardTrains.get(index).getDirection();
							// Makes sure that ä,ö,ü get replaced to avoid infinite loop on the board
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
						if(status.equals("angekommen")) {
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
