package hu.mapro.generator.test;

import hu.mapro.model.generator.gwtproxy.GwtProxyGenerator;
import hu.mapro.model.generator.util.ClassesGenerator;

public class TestProxy extends TestBase {

	@Override
	protected ClassesGenerator classesGenerator() {
		return new GwtProxyGenerator();
	}
	
	@Override
	protected String packageName(String idx) {
		return "testproxy"+idx;
	}
	
	@Override
	protected String outputPath(String idx) {
		return "test-output-proxy"+idx;
	}

}
