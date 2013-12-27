package eu.thecreator.validation.base.table.cell;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import eu.thecreator.components.table.cell.TextFieldCellEditor;
import eu.thecreator.validation.base.ValidationTyp;
import eu.thecreator.validation.base.ValidationmessageImpl;
import eu.thecreator.validation.base.Violation;

/**
 * Renderer/Editor mit UI-Feedback mit Tooltips und Icons
 * 
 * @author Scavenger156
 * 
 * @param <S>
 * @param <T>
 */
public class TextFieldCellEditorIconFeedback<S, T> extends TextFieldCellEditor<S, T> {

	private ImageView imageViewForIcon;
	private static Image imageError;
	private static Image imageWarn;
	private static Image imageInfo;
	private StringProperty validationMessage = new SimpleStringProperty();
	private BorderPane uiForEdit;
	private Label errorIcon;

	private CellFeedback<S, T> validator;

	/**
	 * 
	 * Konstruktor.
	 * 
	 * @param converter
	 *            Stringconverter
	 */
	public TextFieldCellEditorIconFeedback(StringConverter<T> converter) {
		super(converter);
		initImages();

		imageViewForIcon = new ImageView();

		errorIcon = new Label(null, imageViewForIcon);

		errorIcon.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		// Am Anfang noch nicht sichtbar
		errorIcon.setVisible(false);

		// Tooltip anlegen und binden
		errorIcon.setTooltip(new Tooltip());
		errorIcon.getTooltip().textProperty().bind(validationMessage);
		// Ausblenden wenn nicht mehr sichtbar
		validationMessage.addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String t, final String t1) {
				errorIcon.setVisible(t1 != null && t1.length() > 0);
				errorIcon.getTooltip().hide();
			}
		});

		setValidationTyp(ValidationTyp.ERROR);
	}

	/**
	 * Die Bilder initalisieren
	 */
	private synchronized void initImages() {
		if (imageError != null) {
			return;
		}
		String imagePath = System.getProperty(ValidationmessageImpl.FX_VALIDATION_ERRORIMAGEPATH);
		if (imagePath == null) {
			imagePath = "error.png";
			System.setProperty(ValidationmessageImpl.FX_VALIDATION_ERRORIMAGEPATH, imagePath);
		}
		imageError = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath), 13, 13, true, true);

		imagePath = System.getProperty(ValidationmessageImpl.FX_VALIDATION_WARNINGIMAGEPATH);
		if (imagePath == null) {
			imagePath = "warn.png";
			System.setProperty(ValidationmessageImpl.FX_VALIDATION_WARNINGIMAGEPATH, imagePath);
		}
		imageWarn = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath), 13, 13, true, true);

		imagePath = System.getProperty(ValidationmessageImpl.FX_VALIDATION_INFOIMAGEPATH);
		if (imagePath == null) {
			imagePath = "info.png";
			System.setProperty(ValidationmessageImpl.FX_VALIDATION_INFOIMAGEPATH, imagePath);
		}
		imageInfo = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath), 13, 13, true, true);

	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (getIndex() != -1 && getIndex() < getTableView().getItems().size()) {
			setContentDisplay(ContentDisplay.RIGHT);
			setGraphic(errorIcon);
			validate(false);
		}
	}

	/**
	 * Erroricon festlegen
	 * 
	 * @param validationTyp
	 *            Typ des Fehlers
	 */
	protected final void setValidationTyp(ValidationTyp validationTyp) {
		if (validationTyp == ValidationTyp.ERROR) {
			imageViewForIcon.setImage(imageError);
		} else if (validationTyp == ValidationTyp.WARNING) {
			imageViewForIcon.setImage(imageWarn);
		} else {
			imageViewForIcon.setImage(imageInfo);
		}
	}

	@Override
	protected void finishEdit() {
		super.finishEdit();
		// UI zurücksetzen
		uiForEdit.setRight(null);
		setContentDisplay(ContentDisplay.RIGHT);
		setGraphic(errorIcon);
		validate(false);
	}

	@Override
	public void startEdit() {
		if (canEdit()) {

			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			super.startEdit();
			setGraphic(uiForEdit);
			if (textField != null) {
				uiForEdit.setRight(errorIcon);
				textField.setMinWidth(this.getWidth() - errorIcon.getWidth() - getGraphicTextGap());
			}
		}

	}

	@Override
	protected void buildEditComponent() {
		super.buildEditComponent();
		uiForEdit = new BorderPane();
		uiForEdit.setCenter(textField);
		textField.textProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				validate(true);
			}
		});
	}

	private void validate(boolean editor) {
		T value;
		if (editor) {
			value = converter.fromString(textField.getText());
		} else {
			value = getItem();
		}
		Violation v = validateValue(value);
		if (v != null) {
			validationMessage.set(v.getMessage());
			setValidationTyp(v.getValidationTyp());
		} else {
			validationMessage.set("");
		}
	}

	/**
	 * Wert Validieren
	 * 
	 * @param value
	 *            aktueller Wert
	 * @return Violation oder null wenn kein Feedback
	 */
	protected Violation validateValue(T value) {
		return validator != null ? validator.validateValue(value, this) : null;
	}

	/**
	 * 
	 * @param feedback
	 *            Feedback festlegen
	 */
	public void setFeedback(CellFeedback<S, T> feedback) {
		this.validator = feedback;
	}

	/**
	 * Feedback
	 * 
	 * @author Scavenger156
	 * 
	 * @param <S>
	 * @param <T>
	 */
	public interface CellFeedback<S, T> {
		Violation validateValue(T value, TableCell<S, T> cell);
	}
}
