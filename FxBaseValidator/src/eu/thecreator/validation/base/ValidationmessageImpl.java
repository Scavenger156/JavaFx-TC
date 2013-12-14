package eu.thecreator.validation.base;

import java.lang.reflect.Field;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ValidationmessageImpl extends Validationmessage {

	public static final String FX_VALIDATION_ERRORIMAGEPATH = "fx.validation.errorimagepath";
	public static final String FX_VALIDATION_WARNINGIMAGEPATH = "fx.validation.warningimagepath";
	public static final String FX_VALIDATION_INFOIMAGEPATH = "fx.validation.infoimagepath";
	private boolean customValidator = false;
	private ImageView imageViewForIcon;
	private static Image imageError;
	private static Image imageWarn;
	private static Image imageInfo;

	private synchronized void initImages() {
		if (imageError != null) {
			return;
		}
		String imagePath = System.getProperty(FX_VALIDATION_ERRORIMAGEPATH);
		if (imagePath == null) {
			imagePath = "error.png";
			System.setProperty(FX_VALIDATION_ERRORIMAGEPATH, imagePath);
		}
		imageError = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath));

		imagePath = System.getProperty(FX_VALIDATION_WARNINGIMAGEPATH);
		if (imagePath == null) {
			imagePath = "warn.png";
			System.setProperty(FX_VALIDATION_WARNINGIMAGEPATH, imagePath);
		}
		imageWarn = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath));

		imagePath = System.getProperty(FX_VALIDATION_INFOIMAGEPATH);
		if (imagePath == null) {
			imagePath = "info.png";
			System.setProperty(FX_VALIDATION_INFOIMAGEPATH, imagePath);
		}
		imageInfo = new Image(ValidationmessageImpl.class.getResourceAsStream(imagePath));

	}

	/**
	 * Konstruktor
	 * 
	 * @param controller
	 *            Objekt welches als Parent fungiert
	 * @param field
	 *            Reflectionfeld
	 * @param fxElement
	 *            UI
	 */
	public ValidationmessageImpl(Object controller, Field field, Control fxElement) {

		this.controller = controller;
		this.field = field;
		this.fxElement = fxElement;
		addIconManager();
	}

	public ValidationmessageImpl(Object controller, Field field, Property<Object> fxProperty) {
		this.controller = controller;
		this.field = field;
		this.fxProperty = fxProperty;
	}

	public ValidationmessageImpl(Object controller, Field field) {
		this.controller = controller;
		this.field = field;
	}

	public ValidationmessageImpl(Control fxElement) {
		this.fxElement = fxElement;
		customValidator = true;
		addIconManager();
	}

	public boolean isCustomValidator() {
		return customValidator;
	}

	@Override
	public void setValidationTyp(ValidationTyp validationTyp) {
		super.setValidationTyp(validationTyp);
		if (validationTyp == ValidationTyp.ERROR) {
			imageViewForIcon.setImage(imageError);
		} else if (validationTyp == ValidationTyp.WARNING) {
			imageViewForIcon.setImage(imageWarn);
		} else {
			imageViewForIcon.setImage(imageInfo);
		}
	}

	/**
	 * UI-Feedback einbauen
	 */
	private void addIconManager() {
		initImages();
		Image image = imageError;
		double width = image.getWidth();
		// Wir wollen nur ein 8*8

		double scale = 13 / width;

		imageViewForIcon = new ImageView();
		imageViewForIcon.setScaleX(scale);
		imageViewForIcon.setScaleY(scale);
		imageViewForIcon.setScaleZ(scale);

		final Label l = new Label(null, imageViewForIcon);
		// Nicht Managed da wir uns selber positionieren
		l.setManaged(false);
		// Am Anfang noch nicht sichtbar
		l.setVisible(false);
		Parent p = fxElement.getParent();
		// Element immer gleich positionieren
		l.layoutXProperty().bind(fxElement.layoutXProperty().add(fxElement.widthProperty()).add(image.getWidth() / 2));
		l.layoutYProperty().bind(fxElement.layoutYProperty());

		l.setAlignment(Pos.CENTER_RIGHT);

		// Tooltip anlegen und binden
		l.setTooltip(new Tooltip());
		l.getTooltip().textProperty().bind(validationMessage);
		// Ausblenden wenn nicht mehr sichtbar
		validationMessage.addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String t, final String t1) {
				l.setVisible(t1 != null && t1.length() > 0);
				l.getTooltip().hide();
			}
		});

		// Leider per Reflection in den Parent das Label indizieren

		@SuppressWarnings("unchecked")
		ObservableList<Node> list = (ObservableList<Node>) ReflectionUtil.findAndInvokeMethod(p, Parent.class, "getChildren", null, null);
		list.add(l);

		setValidationTyp(validationTyp);

	}
}
