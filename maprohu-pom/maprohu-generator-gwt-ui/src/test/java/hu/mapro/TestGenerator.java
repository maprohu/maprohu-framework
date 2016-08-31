package hu.mapro;

import hu.mapro.model.generator.util.GeneratorUtil;

import org.junit.Test;

public class TestGenerator {

	@Test
	public void test1() {
		GeneratorUtil.getModel(getClass().getResourceAsStream("/test-model.xml"));
	}
	
}
