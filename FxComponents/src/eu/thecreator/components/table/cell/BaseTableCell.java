package eu.thecreator.components.table.cell;

import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

/**
 * Basiszelle für Tabellen
 * 
 * @author Scavenger156
 * 
 * @param <S>
 * @param <T>
 */
public class BaseTableCell<S, T> extends TableCell<S, T> {
	/**
	 * 
	 * Konstruktor.
	 */
	public BaseTableCell() {

	}

	@Override
	public void updateIndex(int i) {
		super.updateIndex(i);
		// Listener hinzufügen/entfernen
		if (i != -1 && i < getTableView().getItems().size()) {
			ObservableStringValue tip = getTooltip(getTableView().getItems().get(i));
			if (tip != null) {
				bindTooltip(tip);
			}
		}
	}

	/**
	 * Tooltip binden
	 * 
	 * @param tip
	 *            Tooltip
	 */
	protected void bindTooltip(ObservableStringValue tip) {
		if (getTooltip() == null) {
			setTooltip(new Tooltip());
		}
		getTooltip().textProperty().bind(tip);
	}

	/**
	 * Von einem VO den Tooltip laden
	 * 
	 * @param vo
	 *            aktuelle Zeile
	 * @return Property eines Tooltips
	 */
	public ObservableStringValue getTooltip(S vo) {
		return null;
	}
}
