package eu.thecreator.bycodeprocessor.generator;

import javassist.CtClass;

public interface IGenerator {
	public boolean apply(CtClass classToTransform);
}
