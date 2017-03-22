package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Database;
import model.DatabaseException;
import model.Shipment;

public class ShipmentPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField[] textFields;
	private static final int NBR_FIELDS = 3;
	private static final int ORDER_ID = 0;
	private static final int PALLET_ID = 1;
	private static final int DELIVERY_DATE = 2;

	public ShipmentPane(Database db) {
		super(db);
	}

	public JComponent createMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		text = new JTextArea();
		text.setEditable(false);
		JScrollPane scroll = new JScrollPane(text);
		panel.add(scroll);
		return panel;
	}

	public JComponent createTopPanel() {
		textFields = new JTextField[NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(NBR_FIELDS + 1, 1));

		JPanel order_id = new JPanel(new GridLayout(1, 2));
		JLabel orderLabel = new JLabel("Order id");
		textFields[ORDER_ID] = new JTextField();
		order_id.add(orderLabel);
		order_id.add(textFields[ORDER_ID]);
		panel.add(order_id);

		JPanel pallet_id = new JPanel(new GridLayout(1, 2));
		JLabel palletLabel = new JLabel("Pallet id");
		textFields[PALLET_ID] = new JTextField();
		pallet_id.add(palletLabel);
		pallet_id.add(textFields[PALLET_ID]);
		panel.add(pallet_id);

		JPanel delivery = new JPanel(new GridLayout(1, 2));
		JLabel deliveryLabel = new JLabel("Delivery date");
		textFields[DELIVERY_DATE] = new JTextField();
		delivery.add(deliveryLabel);
		delivery.add(textFields[DELIVERY_DATE]);
		panel.add(delivery);

		JButton button = new JButton("Add Shipment");
		ActionHandler actHand = new ActionHandler();
		button.addActionListener(actHand);
		panel.add(button);
		return panel;
	}

	public JComponent createBottomPanel() {
		JPanel panel = new JPanel();
		panel.add(messageLabel);
		return panel;
	}

	public void entryActions() {
		text.setText("");
		List<Shipment> shipments = db.getShipments();
		text.append(String.format("%-8s\t %-21s\t %4s\n", "Order Id", "Pallet Id", "Delivery Date"));
		text.append("\n");
		for (Shipment s : shipments) {
			text.append(String.format("%-8s\t %-21s\t %4s\n", s.order_id, s.pallet_id, s.date_of_delivery));
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String order = textFields[ORDER_ID].getText();
			String pallet = textFields[PALLET_ID].getText();
			String delivery = textFields[DELIVERY_DATE].getText();

			try {
				db.insertShipment(order, pallet, delivery);
				displayMessage("Shipment successfully added!");
				entryActions();
				clearFields();
			} catch (DatabaseException e1) {
				displayMessage(e1.getMessage());
			}
		}

		private void clearFields() {
			for (int i = 0; i < textFields.length; i++) {
				textFields[i].setText("");
			}
		}
	}
}
