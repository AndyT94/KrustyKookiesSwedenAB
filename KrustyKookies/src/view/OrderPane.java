package view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Database;
import model.Order;

public class OrderPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;
	
	
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
	
	public void entryActions() {
		text.setText("");
		List<Order> orders = db.getOrders();
		text.append(String.format("%-8s\t %-21s\t %-5s\t %-15s\t %4s\n", "Order ID", "Recipe", "Amount", "Customer", "Deliver By"));
		text.append("\n");
		for (Order o : orders) {
			text.append(String.format("%-8s\t %-21s\t %-5s\t %-15s\t %4s\n", o.id, o.recipe, o.amount, o.customer, o.date));
		}
	}
}
