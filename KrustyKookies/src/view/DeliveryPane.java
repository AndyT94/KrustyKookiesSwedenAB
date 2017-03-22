package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import model.RawMaterialDelivery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeliveryPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField[] textFields;
	private static final int NBR_FIELDS = 3;
	private static final int DATE = 0;
	private static final int MATERIAL = 1;
	private static final int AMOUNT = 2;

	public DeliveryPane(Database db) {
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

		JPanel date = new JPanel(new GridLayout(1, 2));
		JLabel dateLabel = new JLabel("Delivery date");
		textFields[DATE] = new JTextField();
		date.add(dateLabel);
		date.add(textFields[DATE]);
		panel.add(date);

		JPanel mat = new JPanel(new GridLayout(1, 2));
		JLabel matLabel = new JLabel("Raw material");
		textFields[MATERIAL] = new JTextField();
		mat.add(matLabel);
		mat.add(textFields[MATERIAL]);
		panel.add(mat);

		JPanel amount = new JPanel(new GridLayout(1, 2));
		JLabel amountLabel = new JLabel("Delivered amount");
		textFields[AMOUNT] = new JTextField();
		amount.add(amountLabel);
		amount.add(textFields[AMOUNT]);
		panel.add(amount);

		JButton button = new JButton("Add Delivery");
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
		List<RawMaterialDelivery> deliveries = db.getRawMaterialsDeliveries();
		text.append(String.format("%-8s\t %-21s\t %4s\n", "Date", "Raw material", "Amount"));
		text.append("\n");
		for (RawMaterialDelivery r : deliveries) {
			text.append(String.format("%-8s\t %-21s\t %4s\n", r.date, r.material, r.amount));
		}
	}

	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String date = textFields[DATE].getText();
			String material = textFields[MATERIAL].getText();
			String amount = textFields[AMOUNT].getText();

			try {
				db.insertDelivery(date, material, amount);
				displayMessage("Raw material inserted into storage!");
			} catch (DatabaseException exception) {
				displayMessage(exception.getMessage());
			}
			clearFields();
		}

		private void clearFields() {
			for (int i = 0; i < textFields.length; i++) {
				textFields[i].setText("");
			}
		}
	}
}
