package view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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

public class PalletPane extends BasicPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	private JTextField[] fields;

	private DefaultListModel<String> palletListModel;

	private JList<String> palletList;

	private static final int LOCATION = 0;

	private static final int PRODUCTION_DATE = 1;

	private static final int BLOCKED = 2;

	private static final int RECIPE_NAME = 3;

	private static final int NBR_FIELDS = 4;

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
		texts[PRODUCTION_DATE] = "production date";
		texts[BLOCKED] = "Is blocked";
		texts[RECIPE_NAME] = "recipe name";

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

	public void entryActions() {
		clearMessage();
		fillPalletList();
		clearFields();
	}

	private void fillPalletList() {
		palletListModel.removeAllElements();
		/* --- insert own code here --- */
		ArrayList<Pallet> pallets = db.getPallets();
		for (Pallet p : pallets) {
			palletListModel.addElement(p.recipe_name);
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
			/* --- insert own code here --- */
			clearFields();
			Pallet p = db.getPallet(pallet_id);
			fields[LOCATION].setText(p.location);
			fields[PRODUCTION_DATE].setText(p.production_date.toString());
			fields[BLOCKED].setText(p.blocked.toString());
			fields[RECIPE_NAME].setText(p.recipe_name);
		}
	}

}
