package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import model.Order;

public class OrderPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField[] topTextFields;
	private static final int TOP_NBR_FIELDS = 3;
	private static final int CUSTOMER = 0;
	private static final int ORDER = 1;
	private static final int DATE = 2;

	private JTextField[] bottomTextFields;
	private static final int BOTTOM_NBR_FIELDS = 2;
	private static final int FROM = 0;
	private static final int TO = 1;

	public OrderPane(Database db) {
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
		topTextFields = new JTextField[TOP_NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(TOP_NBR_FIELDS + 1, 1));

		JPanel customer = new JPanel(new GridLayout(1, 2));
		JLabel customerLabel = new JLabel("Customer ");
		topTextFields[CUSTOMER] = new JTextField();
		customer.add(customerLabel);
		customer.add(topTextFields[CUSTOMER]);
		panel.add(customer);

		JPanel order = new JPanel(new GridLayout(1, 2));
		JLabel orderLabel = new JLabel("Order ");
		topTextFields[ORDER] = new JTextField();
		order.add(orderLabel);
		order.add(topTextFields[ORDER]);
		panel.add(order);

		JPanel date = new JPanel(new GridLayout(1, 2));
		JLabel dateLabel = new JLabel("Deliver By Date ");
		topTextFields[DATE] = new JTextField();
		date.add(dateLabel);
		date.add(topTextFields[DATE]);
		panel.add(date);

		JButton button = new JButton("Add Order");
		OrderHandler actHand = new OrderHandler();
		button.addActionListener(actHand);
		panel.add(button);
		return panel;
	}

	public JComponent createBottomPanel() {
		bottomTextFields = new JTextField[BOTTOM_NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(BOTTOM_NBR_FIELDS + 2, 1));

		JPanel from = new JPanel(new GridLayout(1, 2));
		JLabel fromLabel = new JLabel("Date from ");
		bottomTextFields[FROM] = new JTextField();
		from.add(fromLabel);
		from.add(bottomTextFields[FROM]);
		panel.add(from);

		JPanel to = new JPanel(new GridLayout(1, 2));
		JLabel toLabel = new JLabel("Date to ");
		bottomTextFields[TO] = new JTextField();
		to.add(toLabel);
		to.add(bottomTextFields[TO]);
		panel.add(to);

		JButton button = new JButton("Search");
		SearchHandler actHand = new SearchHandler();
		button.addActionListener(actHand);
		JButton showButton = new JButton("Show all");
		ShowHandler showHand = new ShowHandler();
		showButton.addActionListener(showHand);
		JPanel p1 = new JPanel(new GridLayout(1, 2));
		p1.add(showButton);
		p1.add(button);
		panel.add(p1);

		panel.add(messageLabel);
		return panel;
	}

	public void entryActions() {
		text.setText("");
		List<Order> orders = db.getOrders();
		text.append(String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", "Order ID", "Recipe", "Amount", "Customer",
				"Deliver By"));
		text.append("\n");
		for (Order o : orders) {
			text.append(
					String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", o.id, o.recipe, o.amount, o.customer, o.date));
		}
	}

	class OrderHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String customer = topTextFields[CUSTOMER].getText();
			String order = topTextFields[ORDER].getText();
			String date = topTextFields[DATE].getText();

			try {
				db.addOrder(customer, order, date);
				displayMessage("Successfully added new order!");
				clearFields();
			} catch (DatabaseException e1) {
				displayMessage(e1.getMessage());
			}
		}

		private void clearFields() {
			for (int i = 0; i < topTextFields.length; i++) {
				topTextFields[i].setText("");
			}
		}
	}

	class SearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String from = bottomTextFields[FROM].getText();
			String to = bottomTextFields[TO].getText();

			try {
				List<Order> orders = db.getOrders(from, to);
				text.setText("");
				text.append(String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", "Order ID", "Recipe", "Amount",
						"Customer", "Deliver By"));
				text.append("\n");
				for (Order o : orders) {
					text.append(String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", o.id, o.recipe, o.amount,
							o.customer, o.date));
				}
				displayMessage("Search yielded " + orders.size() +" orders!");
				clearFields();
			} catch (DatabaseException e1) {
				displayMessage(e1.getMessage());
			}
		}

		private void clearFields() {
			for (int i = 0; i < bottomTextFields.length; i++) {
				bottomTextFields[i].setText("");
			}
		}
	}

	class ShowHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			entryActions();
			displayMessage("");
		}
	}
}