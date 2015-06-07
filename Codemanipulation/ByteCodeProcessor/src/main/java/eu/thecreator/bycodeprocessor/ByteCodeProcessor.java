package eu.thecreator.bycodeprocessor;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;
import eu.thecreator.bycodeprocessor.generator.IGenerator;
import eu.thecreator.bycodeprocessor.generator.ToStringDummyGenerator;

public class ByteCodeProcessor {
	private List<IGenerator> generatorList = new ArrayList<>();

	public ByteCodeProcessor() {
		generatorList.add(new ToStringDummyGenerator());
	}

	public boolean apply(CtClass classToTransform) {
		boolean changed = false;

		for (IGenerator iGenerator : generatorList) {
			if (iGenerator.apply(classToTransform)) {
				changed = true;
			}
		}

		return changed;
	}

}
