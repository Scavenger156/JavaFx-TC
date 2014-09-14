package eu.thecreator.validation.base;

/**
 * Auf Valdierungen horchen.
 * 
 * @author Scavenger156
 * 
 */
public interface ValidationListener {
	/**
	 * 
	 * @param result
	 *            Ergebniss der Validierung
	 */
	void validated(ValidationResult result);

}
