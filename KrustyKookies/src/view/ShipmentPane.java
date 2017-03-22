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
import model.Shipment;

public class ShipmentPane extends BasicPane{
	
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField[] textFields;
	private static final int NBR_FIELDS = 3;
	private static final int ORDER_ID = 0; //DATE
	private static final int PALLET_ID = 1; //MATERIAL
	private static final int DELIVERY_DATE = 2;   //AMOUNT

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

		JPanel order_id = new JPanel(new BorderLayout());
		JLabel orderLabel = new JLabel("Order id");
		textFields[ORDER_ID] = new JTextField();
		order_id.add(orderLabel, BorderLayout.WEST);
		order_id.add(textFields[ORDER_ID], BorderLayout.CENTER);
		panel.add(order_id);

		JPanel pallet_id = new JPanel(new BorderLayout());
		JLabel palletLabel = new JLabel("Pallet id");
		textFields[PALLET_ID] = new JTextField();
		pallet_id.add(palletLabel, BorderLayout.WEST);
		pallet_id.add(textFields[PALLET_ID], BorderLayout.CENTER);
		panel.add(pallet_id);

		JPanel delivery = new JPanel(new BorderLayout());
		JLabel deliveryLabel = new JLabel("Delivery date");
		textFields[DELIVERY_DATE] = new JTextField();
		delivery.add(deliveryLabel, BorderLayout.WEST);
		delivery.add(textFields[DELIVERY_DATE], BorderLayout.CENTER);
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

			String msg = "";

			if (order.isEmpty() || pallet.isEmpty() || delivery.isEmpty()) {
				msg += "Please fill in all fields! ";
			} else {
				if (!isDate(delivery)) {
					msg += "Invalid date (Format: YYYY-MM-DD)! ";
				}

				if (!db.hasPallet(pallet)) {
					msg += "No such pallet id! ";
				}

				if(!db.hasOrder(order)){
					msg += "No such order id";
				}
				if(db.isBlocked(pallet)){
					msg += "The pallet has been blocked!";
				}
			}

			if(msg.isEmpty()) {
				db.insertShipment(order, pallet, delivery);
				displayMessage("Shipment Added!");
				entryActions();
			} else {
				displayMessage(msg);
			}
			clearFields();
		}

		private void clearFields() {
			for(int i = 0; i < textFields.length; i++) {
				textFields[i].setText("");
			}
		}

		private boolean isDate(String date) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			try {
				format.parse(date);
			} catch (ParseException e) {
				return false;
			}
			return true;
		}
	}
}
