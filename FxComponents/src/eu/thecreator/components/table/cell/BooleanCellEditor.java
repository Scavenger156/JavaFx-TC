/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.components.table.cell;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.input.MouseEvent;

/**
 * Editor mit einer Checkbox
 * 
 * @author Scavenger156
 * 
 * @param <S>
 */
public class BooleanCellEditor<S extends Object> extends TableCell<S, Boolean> {

	private CheckBox checkBox;

	public BooleanCellEditor() {
		checkBox = new CheckBox();

		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (isEditing()) {
					commitEdit(newValue == null ? false : newValue);
				}
			}
		});
		checkBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				updateItem(checkBox.isSelected(), false);
				commitEdit(checkBox.isSelected());
				TablePosition<S, Boolean> pos = new TablePosition<>(getTableView(), getIndex(), getTableColumn());
				@SuppressWarnings("rawtypes")
				EventType<CellEditEvent> evt = new EventType<>();
				TableColumn.CellEditEvent<S, Boolean> event = new CellEditEvent<>(getTableView(), pos, evt, checkBox.isSelected());
				// Somit landet die Änderung auch im Property
				getTableColumn().getOnEditCommit().handle(event);
			}
		});
		this.setGraphic(checkBox);
		this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		this.setEditable(true);
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (isEmpty()) {
			return;
		}
		checkBox.setDisable(false);
		checkBox.requestFocus();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				checkBox.setSelected(!checkBox.isSelected());
			}
		});
	}

	@Override
	public void updateItem(Boolean item, boolean empty) {
		super.updateItem(item, empty);
		if (!isEmpty()) {
			checkBox.setSelected(item);
		} else {
			setContentDisplay(ContentDisplay.TEXT_ONLY);
			setEditable(false);
		}
	}
}
