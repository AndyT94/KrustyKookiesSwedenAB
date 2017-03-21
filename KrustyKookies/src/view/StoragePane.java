package view;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.Database;
import model.RawMaterial;

public class StoragePane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private JTextArea text;

	public StoragePane(Database db) {
		super(db);
	}

	public JComponent createMiddlePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		text = new JTextArea();
		panel.add(text);
		return panel;
	}

	public void entryActions() {
		text.setText("");
		List<RawMaterial> materials = db.getRawMaterials();
		Collections.sort(materials);
		text.append(String.format("%-21s\t %-5s\t %4s\n", "Raw material", "Amount", "Unit"));
		text.append("\n");
		for (RawMaterial r : materials) {
			text.append(String.format("%-21s\t %-5s\t %4s\n", r.name, r.amount, r.unit));
		}
	}
}
