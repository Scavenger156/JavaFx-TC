package eu.thecreator.components.table.cell;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

/**
 * Ein Überarbeiteter Editor/Renderer für Zellen.
 * 
 * @author Scavenger156
 * 
 * @param <S>
 *            VO
 * @param <T>
 *            Typ
 */
public class TextFieldCellEditor<S, T> extends TableCell<S, T> {

	private StringConverter<T> converter;
	private TextField textField;

	private boolean replaceOnEdit = false;
	private boolean listenerActive = false;
	/**
	 * Dieser Händler aktiviert das Editieren wenn mit der Maus geklickt wurde.
	 */
	private EventHandler<MouseEvent> haendlerMouse = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (!getTableView().getSelectionModel().getSelectedCells().isEmpty()) {
				@SuppressWarnings("unchecked")
				TablePosition<S, Object> selected = getTableView().getSelectionModel().getSelectedCells().get(0);
				TablePosition<S, T> pos = new TablePosition<>(getTableView(), getIndex(), getTableColumn());
				// Nur bei Änderungen
				if (pos.equals(selected) && !pos.equals(getTableView().getEditingCell())) {
					getTableView().edit(getIndex(), getTableColumn());
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// Wenn das Editieren per Klick ausgelöst wird
							textField.selectAll();
						}
					});
				}
			}
		}

	};
	/**
	 * Wenn mit der Tastatur eingegeben wird auch den Editiermodus nutzen
	 */
	private EventHandler<KeyEvent> haendlerKeys = new EventHandler<KeyEvent>() {
		@Override
		public void handle(final KeyEvent event) {
			TablePosition<S, T> pos = new TablePosition<>(getTableView(), getIndex(), getTableColumn());
			boolean eventAccepted = !"".equals(event.getText()) || event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE;
			if (eventAccepted && canEdit() && pos.equals(getTableView().getEditingCell())) {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
							// Bei Entfernen den Text löschen
							textField.setText("");
						} else if (replaceOnEdit) {
							textField.setText(event.getText());
						} else {
							textField.setText(textField.getText() + event.getText());
						}

						int pos = textField.getText().length();
						textField.selectRange(pos, pos);
					}
				});
			}
		}
	};

	/**
	 * 
	 * Konstruktor.
	 * 
	 * @param converter
	 *            Stringconverter
	 */
	public TextFieldCellEditor(StringConverter<T> converter) {
		super();
		this.converter = converter;
		setEditable(true);
		if (converter == null) {
			throw new IllegalArgumentException("converter is null");
		}
	}

	@Override
	public void updateIndex(int i) {
		super.updateIndex(i);
		// Listener hinzufügen/entfernen
		if (i == -1) {
			// Bei -1 ist die Zelle nicht mehr gültig
			getTableView().removeEventHandler(KeyEvent.KEY_PRESSED, haendlerKeys);
			getTableView().removeEventHandler(MouseEvent.MOUSE_CLICKED, haendlerMouse);
			listenerActive = false;
		} else if (!listenerActive) {
			getTableView().addEventHandler(KeyEvent.KEY_PRESSED, haendlerKeys);
			getTableView().addEventHandler(MouseEvent.MOUSE_CLICKED, haendlerMouse);
			listenerActive = true;
		}
	}

	/**
	 * 
	 * @return Editieren erlaubt
	 */
	protected boolean canEdit() {
		if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
			return false;
		}
		return true;
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (!canEdit()) {
			cancelEdit();
			return;
		}
		if (textField == null) {
			// Lazy
			buildEditComponent();
		}
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		textField.setText(converter.toString(getItem()));
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				textField.requestFocus();
			}
		});

	}

	/**
	 * Edtierungsfeld erstellen
	 */
	protected void buildEditComponent() {
		textField = new TextField();

		setGraphic(textField);
		// Auf Enter & ESC reagieren
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {

					commitEdit();
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});
		// Wenn der Fokus verschwindet so wurde eine andere Zelle selektiert und
		// somit das Editieren beenden
		textField.focusedProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				// Per Runnable da sonst zu geprüft wird wenn der Fokus noch
				// nicht weg ist
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// Textfeld nicht ausreichend daher von der Tabelle
						// "isFocused" checken
						if (!getTableView().getFocusModel().isFocused(getIndex(), getTableColumn())) {
							commitEdit();
						}

					}
				});
			}
		});
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		// Textzurücksetzen
		setText(converter.toString(getItem()));
		// Textfeld ausblenden
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void commitEdit(T newValue) {
		super.commitEdit(newValue);
		setContentDisplay(ContentDisplay.TEXT_ONLY);
		updateItem(newValue, false);
		// Event feueren damit es auch im Property landet!
		TablePosition<S, T> pos = new TablePosition<>(getTableView(), getIndex(), getTableColumn());
		@SuppressWarnings("rawtypes")
		EventType<CellEditEvent> evt = new EventType<>();
		TableColumn.CellEditEvent<S, T> event = new CellEditEvent<>(getTableView(), pos, evt, newValue);

		getTableColumn().getOnEditCommit().handle(event);
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(converter.toString(item));
		}
	}

	/**
	 * Hilfsmethode zum commit
	 */
	protected void commitEdit() {
		T value = converter.fromString(textField.getText());
		updateItem(value, false);
		commitEdit(value);
	}
}
