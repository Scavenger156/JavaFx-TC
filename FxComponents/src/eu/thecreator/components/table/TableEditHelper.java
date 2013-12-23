package eu.thecreator.components.table;

import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Hilfsklasse für Tabellen
 * 
 * @author Scavenger156
 * 
 * @param <S>
 *            VO Typ
 */
public class TableEditHelper<S> {

	private TableView<S> table;

	/**
	 * 
	 * Konstruktor.
	 * 
	 * @param table
	 *            die zu verbessernde Tabelle
	 */
	public TableEditHelper(TableView<S> table) {
		super();
		this.table = table;
	}

	/**
	 * Initalisiert das Feature für editieren per Tastendruck
	 */
	public void init() {
		table.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent event) {
				boolean eventAccepted = !"".equals(event.getText()) || event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE;
				if (eventAccepted && !table.getSelectionModel().getSelectedCells().isEmpty()) {
					@SuppressWarnings("unchecked")
					TablePosition<S, Object> selected = table.getSelectionModel().getSelectedCells().get(0);
					table.edit(selected.getRow(), selected.getTableColumn());
				}
			}
		});

	}
}
