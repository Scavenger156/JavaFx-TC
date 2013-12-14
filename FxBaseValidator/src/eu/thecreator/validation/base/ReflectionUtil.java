package eu.thecreator.validation.base;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtil {
	private ReflectionUtil() {

	}

	public static Object findAndInvokeMethod(Object instanz, Class<?> controller, String method, Class<?>[] classparameter, Object[] callParameter) {
		Method callmethod;
		Class<?>[] cparameter = classparameter;
		Object[] parameter = callParameter;
		try {
			if (cparameter == null) {
				cparameter = new Class[0];
			}
			if (parameter == null) {
				parameter = new Object[0];
			}
			callmethod = controller.getDeclaredMethod(method, cparameter);
			boolean accessible = callmethod.isAccessible();
			try {
				callmethod.setAccessible(true);
				return callmethod.invoke(instanz, parameter);
			} finally {
				callmethod.setAccessible(accessible);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Relfectionfehler die Methode '" + method + "' konnte nicht gefunden werden!", e);
		}
	}

	public static Object getFromField(Field field, Object controller) {
		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		try {
			Object value = field.get(controller);

			return value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Relfectionfehler beim auslesen", e);
		} finally {
			field.setAccessible(accessible);
		}
	}
}
