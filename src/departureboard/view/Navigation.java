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

public abstract class Navigation {

	private static JMenuBar menuBar;
	private static JMenu menu, submenu;
	private static JMenuItem exitItem, undoItem, redoItem;
	private static JRadioButtonMenuItem rbMenuItem;
	private static JCheckBoxMenuItem cbMenuItem;
	
	private static ImageIcon undo, redo;
	
	public static JMenuBar createMenuBar() {
		
		// Creates the menu icons
		ImageIcon undo = new ImageIcon("res/undo-icon.png");
		ImageIcon redo = new ImageIcon("res/redo-icon.png");

		// Creates the menu bar.
		menuBar = new JMenuBar();

		// Builds the file menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		
		exitItem = new JMenuItem("Exit", KeyEvent.VK_T);
		menu.add(exitItem);

		// Builds the edit menu
		menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menu);
		
		undoItem = new JMenuItem("Undo", undo);
		redoItem = new JMenuItem("Redo", redo);

		menu.add(undoItem);
		menu.add(redoItem);
		
		return menuBar;
		
	}

}
