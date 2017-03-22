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
import model.Order;
import view.DeliveryPane.ActionHandler;

public class OrderPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	private JTextField[] textFields;
	private static final int NBR_FIELDS = 3;
	private static final int CUSTOMER = 0;
	private static final int ORDER = 1;
	private static final int DATE = 2;
	
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
		textFields = new JTextField[NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(NBR_FIELDS + 1, 1));

		JPanel customer = new JPanel(new BorderLayout());
		JLabel customerLabel = new JLabel("Customer ");
		textFields[CUSTOMER] = new JTextField();
		customer.add(customerLabel, BorderLayout.WEST);
		customer.add(textFields[CUSTOMER], BorderLayout.CENTER);
		panel.add(customer);

		JPanel order = new JPanel(new BorderLayout());
		JLabel orderLabel = new JLabel("Order ");
		textFields[ORDER] = new JTextField();
		order.add(orderLabel, BorderLayout.WEST);
		order.add(textFields[ORDER], BorderLayout.CENTER);
		panel.add(order);

		JPanel amount = new JPanel(new BorderLayout());
		JLabel amountLabel = new JLabel("Deliver By Date ");
		textFields[DATE] = new JTextField();
		amount.add(amountLabel, BorderLayout.WEST);
		amount.add(textFields[DATE], BorderLayout.CENTER);
		panel.add(amount);

		JButton button = new JButton("Add Order");
		ActionHandler actHand = new ActionHandler();
		button.addActionListener(actHand);
		panel.add(button);
		return panel;
	}
	
	public void entryActions() {
		text.setText("");
		List<Order> orders = db.getOrders();
		text.append(String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", "Order ID", "Recipe", "Amount", "Customer", "Deliver By"));
		text.append("\n");
		for (Order o : orders) {
			text.append(String.format("%-1s\t %-15s\t %-1s\t %-10s\t %4s\n", o.id, o.recipe, o.amount, o.customer, o.date));
		}
	}
	
	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		}
	}
}
