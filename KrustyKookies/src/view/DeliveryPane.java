package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Database;
import model.RawMaterial;
import model.RawMaterialDelivery;

import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeliveryPane extends BasicPane {
	private JTextArea text;
	
	
	public DeliveryPane(Database db) {
		super(db);
	}
	
	public JComponent createMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		text = new JTextArea();
		panel.add(text);
		return panel;
	}
	
	@PostConstruct
	public JComponent createBottomPanel() {
		JPanel panel = new JPanel();
		JButton button = new JButton("Add Delivery");
		return panel;
	}
	
	public void entryActions() {
		text.setText("");
		List<RawMaterialDelivery> deliveries = db.getRawMaterials();
		text.append(String.format("%-21s\t %-5s\t %4s\n", "Date", "Raw material", "Amount"));
		text.append("\n");
		for (RawMaterialDelivery r : deliveries) {
			text.append(String.format("%-21s\t %-5s\t %4s\n", r.date, r.material, r.amount));
		}
	}
}
