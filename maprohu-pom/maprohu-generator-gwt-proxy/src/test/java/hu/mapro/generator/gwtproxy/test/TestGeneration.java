package hu.mapro.generator.gwtproxy.test;

import hu.mapro.generator.test.TestBase;
import hu.mapro.model.analyzer.binding.BindingClassAnalyzer;
import hu.mapro.model.analyzer.test.binding.autobeans.ClassA;
import hu.mapro.model.analyzer.test.binding.custom.CustomClassA;
import hu.mapro.model.generator.binding.BindingGenerator;
import hu.mapro.model.generator.gwtproxy.GwtProxyGenerator;
import hu.mapro.model.generator.util.ClassesGenerator;

import java.io.File;

import org.junit.Test;

import com.sun.codemodel.JCodeModel;

public class TestGeneration extends TestBase {
	

	
	@Test
	public void testBinding() throws Exception {
		JCodeModel cm = new JCodeModel();
		
		BindingGenerator.generate(
				cm, 
				cm._class(ClassA.class.getPackage().getName()+".Binding"),
				BindingClassAnalyzer.analyze(
						CustomClassA.class.getClassLoader(),
						CustomClassA.class.getPackage().getName(),
						new String[] {ClassA.class.getPackage().getName()},
						cm
				),
				true
		);
		
		
		File file = new File("./target/test-output");
		file.mkdirs();
		cm.build(file);
		
	}

	@Override
	protected ClassesGenerator classesGenerator() {
		return new GwtProxyGenerator();
	}
	
}
