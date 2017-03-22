package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Database;
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
		panel.add(button);

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

			String msg = "";
			Map<String, String> cookies = null;
			if (date.isEmpty() || customer.isEmpty() || order.isEmpty()) {
				msg += "Please fill in all fields! ";
			} else {
				if (!isDate(date)) {
					msg += "Invalid date (Format: YYYY-MM-DD)! ";
				}

				if (!db.hasCustomer(customer)) {
					msg += "No such customer! ";
				}

				String[] amount = order.split(",");
				cookies = new HashMap<String, String>();
				for (int i = 0; i < amount.length; i++) {
					String[] data = amount[i].trim().split("\\s");
					String nbr = data[0];
					String recipe = "";
					for (int j = 1; j < data.length; j++) {
						recipe += data[j] + " ";
					}
					cookies.put(recipe.trim(), nbr);
				}
				for(String key : cookies.keySet()) {
					if(!db.hasRecipe(key)) {
						msg += "No such recipe! ";
						break;
					}
				}
			}

			if (msg.isEmpty()) {
				db.addOrder(customer, cookies, date);
				entryActions();
				displayMessage("");
			} else {
				displayMessage(msg);
			}
			clearFields();
		}

		private void clearFields() {
			for (int i = 0; i < topTextFields.length; i++) {
				topTextFields[i].setText("");
			}
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

	class SearchHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}
}
