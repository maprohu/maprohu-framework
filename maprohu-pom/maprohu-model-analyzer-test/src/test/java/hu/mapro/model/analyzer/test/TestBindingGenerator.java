package hu.mapro.model.analyzer.test;

import hu.mapro.model.analyzer.binding.SingleBindingBean;
import hu.mapro.model.analyzer.test.binding.autobeans.ClassA;
import hu.mapro.model.analyzer.test.binding.autobeans.ClassB;
import hu.mapro.model.analyzer.test.binding.custom.CustomClassA;
import hu.mapro.model.analyzer.test.binding.custom.CustomClassB;
import hu.mapro.model.generator.binding.BindingGenerator;

import java.io.File;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class TestBindingGenerator {

	@Test
	public void test1() throws Exception {
		JCodeModel cm = new JCodeModel();
		
		File file = new File("./target/test-output-binding");
		file.mkdirs();
		
		JDefinedClass targetClass = cm._class("hu.mapro.model.generator.binding.test.output.Binding");
		
		
		BindingGenerator.generate(
				cm, targetClass, 
				ImmutableList.of(
						new SingleBindingBean(cm.ref(ClassA.class), cm.ref(CustomClassA.class)),
						new SingleBindingBean(cm.ref(ClassB.class), cm.ref(CustomClassB.class))
				),
				true
		);
		
		cm.build(file);
		
		
	}	

	
}
