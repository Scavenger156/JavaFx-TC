package eu.thecreator.bycodeprocessor.generator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import eu.thecreator.bytecode.annontations.ToString;

/**
 * ToString dummycode
 * 
 * @author andre
 *
 */
public class ToStringDummyGenerator implements IGenerator {

	@Override
	public boolean apply(CtClass classToTransform) {
		try {
			ToString annontation = (ToString) classToTransform
					.getAnnotation(ToString.class);
			if (annontation == null) {
				return false;
			}
		} catch (ClassNotFoundException e1) {
			return false;
		}
		try {
			CtMethod toStringMethodToRemove = classToTransform
					.getDeclaredMethod("toString");
			classToTransform.removeMethod(toStringMethodToRemove);
		} catch (NotFoundException e) {
			// OK
		}
		try {
			CtMethod toStringMethodToRemove = classToTransform
					.getDeclaredMethod("test");
			classToTransform.removeMethod(toStringMethodToRemove);
		} catch (NotFoundException e) {
			// OK
		}

		try {

			CtMethod newToStringMethod = CtNewMethod
					.make("public String toString() { return \"toString() changed\"; }",
							classToTransform);

			classToTransform.addMethod(newToStringMethod);

			CtMethod newMethod = CtNewMethod.make(
					"public String test() { return \"changed\"; }",
					classToTransform);

			classToTransform.addMethod(newMethod);

			return true;
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}
}
