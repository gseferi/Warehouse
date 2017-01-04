package serobot.pcinterface;

import javax.swing.table.DefaultTableModel;

/**
 * Extends the table model to make the cells non-editable in the table.
 * @author Aaquib Naved
 *
 */
public class NonEditableTableModel extends DefaultTableModel {
	
	public NonEditableTableModel() {
		super();
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
       //all cells false
       return false;
    }
}
