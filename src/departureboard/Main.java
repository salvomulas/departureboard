package departureboard;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import departureboard.model.*;

/**
 * @author Salvatore Mulas
 * Main class to execute the application
 */
public class Main {

	public static void main(String [] args) {
		
		// Init model and controller
		final Master model = new Master();	
		final Controller controller = new Controller (model);
		controller.loadData();
		
		// Init GUI and load Nimbus LAF
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					controller.viewInit();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			}
		});	
	}
	
}
