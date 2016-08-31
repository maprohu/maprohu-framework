package hu.mapro.generator.test;

import hu.mapro.model.generator.gwtui.GwtUiGenerator;
import hu.mapro.model.generator.util.ClassesGenerator;

public class TestUi extends TestBase {

	@Override
	protected ClassesGenerator classesGenerator() {
		return new GwtUiGenerator();
	}
	
	@Override
	protected String packageName(String idx) {
		return "testui"+idx;
	}
	
	@Override
	protected String outputPath(String idx) {
		return "test-output-ui"+idx;
	}

}
