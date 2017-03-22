package view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Database;
import model.Pallet;
import view.PalletPane.PalletSelectionListener;

public class ProductionPane extends BasicPane {
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> productionListModel;
	private JList<String> productionList;
	
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
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		return p;
	}
	
	public void entryActions() {
		clearMessage();
		fillProductionList();
	}
	
	private void fillProductionList() {
		productionListModel.removeAllElements();

		//List<Pallet> pallets = db.getPalletsInProduction();
//		for (Pallet p : pallets) {
//			productionListModel.addElement(Integer.toString(p.pallet_id));
//		}
	}
	
	class ProductionSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (productionList.isSelectionEmpty()) {
				return;
			}
			String pallet_id = productionList.getSelectedValue();
		}
	}
}
