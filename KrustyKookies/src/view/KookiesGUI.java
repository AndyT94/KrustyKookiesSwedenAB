package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Database;

public class KookiesGUI {
	private Database db;
    private JTabbedPane tabbedPane;
	
	public KookiesGUI(Database db) {
		this.db = db;
		
        JFrame frame = new JFrame("Krusty Kookies Sweden AB");
        tabbedPane = new JTabbedPane();
		
        OrderPane orderPane = new OrderPane(db);
        tabbedPane.addTab("Order", null, orderPane, "The orders");
        
        PalletPane palletPane = new PalletPane(db);
        tabbedPane.addTab("Pallet", null, palletPane, "The pallets");
        
        StoragePane storagePane = new StoragePane(db);
        tabbedPane.addTab("Storage", null, storagePane, "The raw material storage");
        
        DeliveryPane deliveryPane = new DeliveryPane(db);
        tabbedPane.addTab("Delivery", null, deliveryPane, "Raw material deliveries");
        
        ShipmentPane shipmentPane = new ShipmentPane(db);
        tabbedPane.addTab("Shipment", null, shipmentPane, "Shipments");
        
        tabbedPane.setSelectedIndex(-1);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(new ChangeHandler());
        frame.addWindowListener(new WindowHandler());

        frame.setSize(600, 400);
        frame.setVisible(true);
        
		db.openConnection("krusty.db");
	}
	
    /**
     * ChangeHandler is a listener class, called when the user switches panes.
     */
    class ChangeHandler implements ChangeListener {
        /**
         * Called when the user switches panes. The entry actions of the new
         * pane are performed.
         * 
         * @param e
         *            The change event (not used).
         */
        public void stateChanged(ChangeEvent e) {
            BasicPane selectedPane = (BasicPane) tabbedPane
                .getSelectedComponent();
            selectedPane.entryActions();
        }
    }

    /**
     * WindowHandler is a listener class, called when the user exits the
     * application.
     */
    class WindowHandler extends WindowAdapter {
        /**
         * Called when the user exits the application. Closes the connection to
         * the database.
         * 
         * @param e
         *            The window event (not used).
         */
        public void windowClosing(WindowEvent e) {
            db.closeConnection();
            System.exit(0);
        }
    }
}
