package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Database;
import model.DatabaseException;
import model.Pallet;

public class ProductionPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> productionListModel;
	private JList<String> productionList;
	private static final int LOCATION = 0;
	private static final int RECIPE_NAME = 1;
	private static final int NBR_FIELDS = 2;
	
	private JTextField[] fields;
	private JTextField recipe;
	
	public ProductionPane(Database db) {
		super(db);
	}
	
	public JComponent createLeftPanel() {
		productionListModel = new DefaultListModel<String>();

		productionList = new JList<String>(productionListModel);
		productionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		productionList.setPrototypeCellValue("123456789012");
		productionList.addListSelectionListener(new ProductionSelectionListener());
		JScrollPane p1 = new JScrollPane(productionList);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 1));
		p.add(p1);
		return p;
	}
	
	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[LOCATION] = "Location";
		texts[RECIPE_NAME] = "Recipe name";

		fields = new JTextField[NBR_FIELDS];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField(20);
			fields[i].setEditable(false);
		}

		JPanel input = new InputPanel(texts, fields);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(input);
		return p;
	}
	
	public JComponent createBottomPanel() {
		JPanel p = new JPanel(new GridLayout(2,1));
		JPanel panel = new JPanel(new GridLayout(1, 3));
		panel.add(new JLabel("Recipe"));
		recipe = new JTextField();
		panel.add(recipe);
		JButton button = new JButton("Create pallet");
		button.addActionListener(new ActionHandler());
		panel.add(button);
		p.add(panel);
		p.add(messageLabel);
		return p;
	}
	
	public JComponent createMiddlePanel() {
		JPanel p = new JPanel(new FlowLayout());
		JButton ramp = new JButton("To ramp");
		JButton block = new JButton("Block pallet");
		ramp.addActionListener(new RampHandler());
		block.addActionListener(new BlockHandler());
		
		p.add(ramp);
		p.add(block);
		return p;
	}
	
	public void entryActions() {
		clearMessage();
		fillProductionList();
		clearFields();
	}
	
	private void fillProductionList() {
		productionListModel.removeAllElements();
		List<Pallet> pallets = db.getPalletsInProduction();
		for (Pallet p : pallets) {
			productionListModel.addElement(Integer.toString(p.pallet_id));
		}
	}
	
	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}
	
	class ProductionSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (productionList.isSelectionEmpty()) {
				return;
			}
			String pallet_id = productionList.getSelectedValue();
			clearFields();
			
			Pallet p = db.getPallet(pallet_id);
			fields[LOCATION].setText(p.location);
			fields[RECIPE_NAME].setText(p.recipe_name);
		}
	}
	
	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String rec = recipe.getText();
			try {
				db.createPallet(rec);
				entryActions();
				displayMessage("Pallet of " + rec + " successfully created!");
				recipe.setText("");
			} catch (DatabaseException e1) {
				displayMessage(e1.getMessage());
			}
		}
	}
	
	class RampHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String pallet_id = productionList.getSelectedValue();
			db.palletToRamp(pallet_id);
			entryActions();
			displayMessage("Pallet " + pallet_id + " in ramp!");
		}
	}
	
	class BlockHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String pallet_id = productionList.getSelectedValue();
			db.blockPallet(pallet_id);
			entryActions();
			displayMessage("Pallet " + pallet_id + " is blocked!");
		}
	}
}
