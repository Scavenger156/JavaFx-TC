package eu.thecreator.validation.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javafx.beans.property.Property;
import javafx.scene.control.Control;

/**
 * Implementierung für Annontationbasiertes Validieren.
 * 
 * @author Scavenger156
 * 
 */
public class ValidationmessageAnnontations extends ValidationmessageImpl {
	private List<Annotation> validationAnnontations;

	/**
	 * 
	 * Konstruktor.
	 * 
	 * @param fxElement
	 *            FX-Control Element
	 * @param validationAnnontations
	 *            Validerungsannontation
	 */
	public ValidationmessageAnnontations(Control fxElement, List<Annotation> validationAnnontations) {
		super(fxElement);
		this.validationAnnontations = validationAnnontations;
	}

	public ValidationmessageAnnontations(Object controller, Field field, Control fxElement, List<Annotation> validationAnnontations) {
		super(controller, field, fxElement);
		this.validationAnnontations = validationAnnontations;
	}

	public ValidationmessageAnnontations(Object controller, Field field, Property<Object> fxProperty, List<Annotation> validationAnnontations) {
		super(controller, field, fxProperty);
		this.validationAnnontations = validationAnnontations;
	}

	public ValidationmessageAnnontations(Object controller, Field field, List<Annotation> validationAnnontations) {
		super(controller, field);
		this.validationAnnontations = validationAnnontations;
	}

	/**
	 * 
	 * @return Annontations für die Validierung
	 */
	public List<Annotation> getValidationAnnontations() {
		return validationAnnontations;
	}

}
