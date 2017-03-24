package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import view.OrderPane.SearchHandler;
import view.ProductionPane.ActionHandler;

public class PalletPane extends BasicPane {
	private static final long serialVersionUID = 1;
	private JTextField[] fields;
	private DefaultListModel<String> palletListModel;
	private JList<String> palletList;
	private static final int LOCATION = 0;
	private static final int PRODUCTION_DATE = 1;
	private static final int BLOCKED = 2;
	private static final int RECIPE_NAME = 3;
	private static final int NBR_FIELDS = 4;
	private JButton[] buttons;
	
	
	private JTextField[] middleTextFields;
	private static final int MIDDLE_NBR_FIELDS = 4;
	private static final int RECIPE = 0;
	private static final int PALLET_ID = 1;
	private static final int FROM = 2;
	private static final int TO = 3;

	PalletPane(Database db) {
		super(db);
	}

	public JComponent createLeftPanel() {
		palletListModel = new DefaultListModel<String>();

		palletList = new JList<String>(palletListModel);
		palletList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		palletList.setPrototypeCellValue("123456789012");
		palletList.addListSelectionListener(new PalletSelectionListener());
		JScrollPane p1 = new JScrollPane(palletList);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		return p;
	}

	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[LOCATION] = "Location";
		texts[PRODUCTION_DATE] = "Production date";
		texts[BLOCKED] = "Blocked";
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
		buttons = new JButton[2];
		buttons[0] = new JButton("Show All Blocked Pallets");
		buttons[1] = new JButton("Show All Pallets");

		return new ButtonAndMessagePanel(buttons, messageLabel, new ActionHandler());
	}
	
	public JComponent createMiddlePanel(){
		middleTextFields = new JTextField[ MIDDLE_NBR_FIELDS];
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		
		JPanel recipe = new JPanel(new GridLayout(1, 3));
		JLabel recipeLabel = new JLabel("Recipe");
		middleTextFields[RECIPE] = new JTextField();
		recipe.add(recipeLabel);
		recipe.add(middleTextFields[RECIPE]);
		panel.add(recipe);
		JButton recipeButton = new JButton("Search");
		recipeHandler actHand = new recipeHandler();
		recipeButton.addActionListener(actHand);
		panel.add(recipeButton);
		
		
		JPanel palletId = new JPanel(new GridLayout(1, 3));
		JLabel palletIdLabel = new JLabel("Pallet Id");
		middleTextFields[PALLET_ID] = new JTextField();
		palletId.add(palletIdLabel);
		palletId.add(middleTextFields[PALLET_ID]);
		panel.add(palletId);
		JButton palletIdButton = new JButton("Search");
		idHandler actHand1 = new idHandler();
		palletIdButton.addActionListener(actHand1);
		panel.add(palletIdButton);
		
		
		JPanel date = new JPanel(new GridLayout(1, 5));
		JLabel fromLabel = new JLabel("Date from");
		middleTextFields[FROM] = new JTextField();
		date.add(fromLabel);
		date.add(middleTextFields[FROM]);
		JLabel toLabel = new JLabel("Date to");
		middleTextFields[TO] = new JTextField();
		date.add(toLabel);
		date.add(middleTextFields[TO]);
		
		panel.add(date);
		JButton dateButton = new JButton("Search");
		dateHandler actHand2 = new dateHandler();
		dateButton.addActionListener(actHand2);
		panel.add(dateButton);
	
		return panel;
	}

	public void entryActions() {
		clearMessage();
		fillPalletList();
		clearFields();
	}

	private void fillPalletList() {
		palletListModel.removeAllElements();

		ArrayList<Pallet> pallets = db.getPallets();
		for (Pallet p : pallets) {
			palletListModel.addElement(Integer.toString(p.pallet_id));
		}
	}

	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}

	/**
	 * A class that listens for clicks in the date list.
	 */
	class PalletSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the date list. Fetches
		 * performance data from the database and displays it in the text
		 * fields.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (palletList.isSelectionEmpty()) {
				return;
			}
			String pallet_id = palletList.getSelectedValue();
		
			clearFields();
			Pallet p = db.getPallet(pallet_id);
			fields[LOCATION].setText(p.location);
			fields[PRODUCTION_DATE].setText(p.production_date.toString());
			fields[BLOCKED].setText(p.blocked.toString());
			fields[RECIPE_NAME].setText(p.recipe_name);
		}
	}

	/**
	 * A class that listens for button clicks.
	 */
	class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals(buttons[0].getText())) {
				palletListModel.removeAllElements();
				ArrayList<Pallet> pallets = db.getAllBlockedPallets();
				for (Pallet p : pallets) {
					palletListModel.addElement(Integer.toString(p.pallet_id));
				}
				displayMessage("This is all the blocked pallets!");
			} else if (e.getActionCommand().equals(buttons[1].getText())) {
				fillPalletList();
				displayMessage("");
			}
		}
	}
	
	class recipeHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String recipe = middleTextFields[RECIPE].getText();
			palletListModel.removeAllElements();
			List<Pallet> pallets = db.searchRecipe(recipe);
			for(Pallet p: pallets){
				palletListModel.addElement(Integer.toString(p.pallet_id));
			}
			middleTextFields[RECIPE].setText("");
			
		}
		
		
		
	}
	
	class idHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String pallet_id = middleTextFields[PALLET_ID].getText();
			palletListModel.removeAllElements();
			List<Pallet> pallets = db.searchPalletId(pallet_id);
			for(Pallet p: pallets){
				palletListModel.addElement(Integer.toString(p.pallet_id));
			}
			middleTextFields[PALLET_ID].setText("");
			
		}
		
		
		
	}
	
	class dateHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String from = middleTextFields[FROM].getText();
			String to = middleTextFields[TO].getText();
			palletListModel.removeAllElements();
			List<Pallet> pallets = null;
			try {
				pallets = db.searchDate(from,to);
			} catch (DatabaseException e1) {
				displayMessage(e1.getMessage());
			}
			for(Pallet p: pallets){
				palletListModel.addElement(Integer.toString(p.pallet_id));
			}
			middleTextFields[FROM].setText("");
			middleTextFields[TO].setText("");
			
			
		}
		
		
		
	}
}
