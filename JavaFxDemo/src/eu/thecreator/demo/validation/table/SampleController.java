/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.thecreator.demo.validation.table;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import eu.thecreator.components.table.TableEditHelper;
import eu.thecreator.demo.common.vo.TestVO;
import eu.thecreator.validation.base.ValidationTyp;
import eu.thecreator.validation.base.Violation;
import eu.thecreator.validation.base.table.cell.TextFieldCellEditorIconFeedback;
import eu.thecreator.validation.base.table.cell.TextFieldCellEditorIconFeedback.CellFeedback;

/**
 * Demo für das Feedback in einer Tabellenzelle.
 * 
 * @author Scavenger156
 * 
 */
public class SampleController implements Initializable {
	@FXML
	private TableView<TestVO> mytableView;

	@FXML
	private TableColumn<TestVO, String> colFeedback;
	@FXML
	private TableColumn<TestVO, String> colText;
	private ObservableList<TestVO> data;

	/**
	 * 
	 * Konstruktor.
	 */
	public SampleController() {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		colFeedback.setCellValueFactory(new PropertyValueFactory<TestVO, String>("zeichenkette"));
		colText.setCellValueFactory(new PropertyValueFactory<TestVO, String>("zeichenkette"));

		final MyCellFeedback feedback = new MyCellFeedback();

		colFeedback.setCellFactory(new Callback<TableColumn<TestVO, String>, TableCell<TestVO, String>>() {

			@Override
			public TableCell<TestVO, String> call(TableColumn<TestVO, String> param) {
				TextFieldCellEditorIconFeedback<TestVO, String> tfe = new TextFieldCellEditorIconFeedback<>(new DefaultStringConverter());
				tfe.setFeedback(feedback);

				return tfe;

			}
		});

		TableEditHelper.install(mytableView);

		data = FXCollections.observableArrayList();
		data.add(new TestVO("123"));
		data.add(new TestVO("schön"));
		mytableView.setItems(data);

	}

	/**
	 * Internes Fedback zu Tabellenzellen.
	 * 
	 * @author Scavenger156
	 * 
	 */
	private class MyCellFeedback implements CellFeedback<TestVO, String> {
		/**
		 * 
		 * Konstruktor.
		 */
		public MyCellFeedback() {
		}

		@Override
		public Violation validateValue(String value, TableCell<TestVO, String> cell) {

			if (!"Schön".equalsIgnoreCase(value)) {
				return new Violation("Bitte geben sie schön ein", ValidationTyp.ERROR);
			}
			return null;
		}

	}
}
