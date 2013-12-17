package departureboard.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

/**
 * @author Salvatore Mulas
 * Navigation class for drawing the JMenuBar - declared abstract with static attributes
 */
public abstract class Navigation {

	private static JMenuBar menuBar;
	private static JMenu file, edit;
	protected static JMenuItem exitItem, undoItem, redoItem;
	private static JCheckBoxMenuItem cbMenuItem;
	
	private static ImageIcon undo, redo;
	
	public static JMenuBar createMenuBar() {
		
		// Creates the menu icons
		ImageIcon undo = new ImageIcon("res/undo-icon.png");
		ImageIcon redo = new ImageIcon("res/redo-icon.png");

		// Creates the menu bar.
		menuBar = new JMenuBar();

		// Builds the file menu.
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_A);
		menuBar.add(file);
		
		exitItem = new JMenuItem("Exit", KeyEvent.VK_T);
		file.add(exitItem);

		// Builds the edit menu
		edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_N);
		menuBar.add(edit);
		
		undoItem = new JMenuItem("Undo", undo);
		redoItem = new JMenuItem("Redo", redo);
		undoItem.setEnabled(false);;
		redoItem.setEnabled(false);

		edit.add(undoItem);
		edit.add(redoItem);
		
		return menuBar;
		
	}

}
