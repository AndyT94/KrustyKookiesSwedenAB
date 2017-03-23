package view;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Database;

public class RecipePane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private static final int QUANTITY = 0;
	private static final int NBR_FIELDS = 1;
	
	private JTextField[] fields;
	
	public RecipePane(Database db) {
		super(db);
	}

	public JComponent createTopPanel() {
		String[] texts = new String[NBR_FIELDS];
		texts[QUANTITY] = "Quantity";

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
}
