package departureboard.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

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
	private static JMenu file, edit, view;
	protected static JMenuItem exitItem, undoItem, redoItem, saveItem;
	static JCheckBoxMenuItem dBoard;
	
	private static ImageIcon undo, redo;
	
	protected Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = resizedImg.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    return resizedImg;
	}
	
	public static JMenuBar createMenuBar() {
		
		// Creates the menu icons and resizes them
		ImageIcon undoSrc = new ImageIcon("res/undo-icon.png");
		Image image = undoSrc.getImage();
		Image newimg = image.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH);
		undo = new ImageIcon(newimg);
		
		ImageIcon redoSrc = new ImageIcon("res/redo-icon.png");
		Image image2 = redoSrc.getImage();
		Image newimg2 = image2.getScaledInstance(24, 24,  java.awt.Image.SCALE_SMOOTH);
		redo = new ImageIcon(newimg2);

		// Creates the menu bar.
		menuBar = new JMenuBar();

		// Builds the file menu.
		file = new JMenu("Datei");
		file.setMnemonic(KeyEvent.VK_A);
		menuBar.add(file);
		
		saveItem = new JMenuItem("Speichern", KeyEvent.VK_S);
		file.add(saveItem);
		exitItem = new JMenuItem("Beenden", KeyEvent.VK_W);
		file.add(exitItem);

		// Builds the edit menu
		edit = new JMenu("Bearbeiten");
		edit.setMnemonic(KeyEvent.VK_N);
		menuBar.add(edit);
		
		undoItem = new JMenuItem("Rückgängig", undo);
		redoItem = new JMenuItem("Wiederholen", redo);
		undoItem.setEnabled(false);;
		redoItem.setEnabled(false);

		edit.add(undoItem);
		edit.add(redoItem);
		
		// Builds the view menu
		view = new JMenu("Ansicht");
		view.setMnemonic(KeyEvent.VK_N);
		menuBar.add(view);
		
		dBoard = new JCheckBoxMenuItem("Zeige Abfahrtstafel");
		dBoard.setSelected(false);
		view.add(dBoard);
		
		return menuBar;
		
	}

}
