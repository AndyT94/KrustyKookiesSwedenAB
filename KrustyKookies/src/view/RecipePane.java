package view;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Database;

public class RecipePane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> recipeListModel;
	private JList<String> recipeList;
	
	private DefaultListModel<String> materialListModel;
	private JList<String> materialList;
	
	private static final int QUANTITY = 0;
	private static final int NBR_FIELDS = 1;
	
	private JTextField[] fields;
	
	public RecipePane(Database db) {
		super(db);
	}

	public JComponent createLeftPanel() {
		recipeListModel = new DefaultListModel<String>();

		recipeList = new JList<String>(recipeListModel);
		recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		recipeList.setPrototypeCellValue("123456789012");
		recipeList.addListSelectionListener(new RecipeSelectionListener());
		JScrollPane p1 = new JScrollPane(recipeList);

		materialListModel = new DefaultListModel<String>();

		materialList = new JList<String>(materialListModel);
		materialList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		materialList.setPrototypeCellValue("123456789012");
		materialList.addListSelectionListener(new MaterialSelectionListener());
		JScrollPane p2 = new JScrollPane(materialList);
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		p.add(p2);
		return p;
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
	
	private void clearFields() {
		for (int i = 0; i < fields.length; i++) {
			fields[i].setText("");
		}
	}
	
	class RecipeSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (recipeList.isSelectionEmpty()) {
				return;
			}
			String recipe = recipeList.getSelectedValue();	
			materialListModel.removeAllElements();
		}
	}
	
	class MaterialSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (materialList.isSelectionEmpty()) {
				return;
			}
			String recipe = recipeList.getSelectedValue();	
			String material = materialList.getSelectedValue();
			clearFields();
		}
	}
}
