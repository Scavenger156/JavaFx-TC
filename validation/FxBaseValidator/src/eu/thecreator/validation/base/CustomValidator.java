package eu.thecreator.validation.base;

/**
 * Ein eigener Validator um komplexe Validierungen selber durchzuf�hren.
 * 
 * @author Scavenger156
 * 
 */
public interface CustomValidator {
	/**
	 * Validierung durchf�hren
	 * 
	 * @param validationResultCustom
	 *            Das Ergebniss der Validierung
	 */
	void validate(ValidationResult validationResultCustom);

}
