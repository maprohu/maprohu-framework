package hu.mapro.generator.gwtui.test;

import hu.mapro.model.analyzer.binding.BindingClassAnalyzer;
import hu.mapro.model.analyzer.test.binding.autobeans.ClassA;
import hu.mapro.model.analyzer.test.binding.custom.CustomClassA;
import hu.mapro.model.analyzer.test.domain.flang.User;
import hu.mapro.model.descriptor.Generate;
import hu.mapro.model.descriptor.Model;
import hu.mapro.model.generator.binding.BindingGenerator;
import hu.mapro.model.generator.gwtui.GwtUiGenerator;
import hu.mapro.model.generator.util.ClassPathGenerator;
import hu.mapro.model.generator.util.GeneratorUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.google.common.io.NullOutputStream;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;

public class TestGeneration {

	private static final CodeWriter outputWriter = new CodeWriter() {
		@Override
		public OutputStream openBinary(JPackage pkg, String fileName)
				throws IOException {
			return new NullOutputStream();
		}
		
		@Override
		public void close() throws IOException {
		}
	};
	
//	@Test
//	public void testAutobeansGeneration() throws Exception {
//		JCodeModel cm = new JCodeModel();
//		GwtUiGenerator generator = new GwtUiGenerator();
//		
//		File file = new File("./target/test-output");
//		file.mkdirs();
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				SampleBean.class, 
//				SampleBeanExtended.class
//		);
//		
//		
//		
//		generator.generateFields(
//				cm._class("test.Autobeans"),
//				cm._class("test.Services"),
//				analyzer.getDefinedTypes()
//		);
//		cm.build(outputWriter);
//		
//	}
//
//	@Test
//	public void testAutobeansGeneration2() throws Exception {
//		JCodeModel cm = new JCodeModel();
//		GwtUiGenerator generator = new GwtUiGenerator();
//		
//		File file = new File("./target/test-output");
//		file.mkdirs();
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				Book.class, 
//				User.class,
//				UserPrivilege.class,
//				Privilege.class,
//				SomeValue.class
//		);
//		
//		generator.generateFields(
//				cm._class("test.Autobeans"),
//				cm._class("test.Services"),
//				analyzer.getDefinedTypes()
//		);
//		cm.build(outputWriter);
//		
//	}
//
//	
//	@Test
//	public void testAutobeansGeneration3() throws Exception {
//		JCodeModel cm = new JCodeModel();
//		GwtUiGenerator generator = new GwtUiGenerator();
//		
//		File file = new File("./target/test-output");
//		file.mkdirs();
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				AndFilter.class,
//				Area.class,
//				AreaFilter.class,
//				BoundingBox.class,
//				Composite.class,
//				Coordinate.class,
//				Criteria.class,
//				MultiFilter.class,
//				NotFilter.class,
//				OrFilter.class,
//				Polygon.class,
//				Sql2Filter.class,
//				SqlFilter.class,
//				hu.mapro.model.analyzer.test.domain.imdate.User.class,
//				UserArea.class,
//				UserData.class,
//				UserFilter.class,
//				Role.class
//		);
//		
//		generator.generateFields(
//				cm._class("test.Autobeans"),
//				cm._class("test.Services"),
//				analyzer.getDefinedTypes()
//		);
//		cm.build(outputWriter);
//		
//	}
	
	@Test
	public void testAutobeansGeneration4() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass("hu.mapro.test.imdate.util.TestAutoBeans");
		model.setServerClass("hu.mapro.test.imdate.util.ServiceClass");
		model.setSharedClass("hu.mapro.test.imdate.util.SharedClass");
		
		generate.getSourcePackage().add("hu.mapro.model.analyzer.test.imdate");
		
		File file = new File("./target/test-output");
		file.mkdirs();
		
		GeneratorUtil.generate(
				model, 
				file, 
				new ClassPathGenerator(
						User.class.getClassLoader(),
						new GwtUiGenerator()
				)
		);
		
	}
	
	@Test
	public void testAutobeansGeneration5() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass("hu.mapro.test.imdate.util.TestAutoBeansOne");
		model.setServerClass("hu.mapro.test.imdate.util.ServiceClass");
		model.setSharedClass("hu.mapro.test.imdate.util.SharedClass");
		
		generate.getSourcePackage().add("hu.mapro.model.analyzer.test.one");
		
		File file = new File("./target/test-output");
		file.mkdirs();
		
		GeneratorUtil.generate(model, file, new ClassPathGenerator(User.class.getClassLoader(), new GwtUiGenerator()));
		
	}
	

	
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
	
}
