package hu.mapro.generator.test;

import hu.mapro.model.analyzer.test.domain.flang.User;
import hu.mapro.model.descriptor.Generate;
import hu.mapro.model.descriptor.MetaPackages;
import hu.mapro.model.descriptor.Model;
import hu.mapro.model.generator.UpdateCodeWriter;
import hu.mapro.model.generator.util.ClassPathGenerator;
import hu.mapro.model.generator.util.ClassesGenerator;
import hu.mapro.model.generator.util.GeneratorUtil;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.sun.codemodel.JCodeModel;

public abstract class TestBase {
	
	private JCodeModel cm;

	@Before
	public void init() {
		cm = new JCodeModel();
	}
	
	protected void write(String idx) throws IOException {
		File file = mkdir(idx);
		cm.build(new UpdateCodeWriter(file));
	}

	final protected File mkdir(int idx) {
		return mkdir(Integer.toString(idx));
	}
	protected File mkdir(String idx) {
		String pathname = outputPath(idx);
		File file = new File("./target/" + pathname);
		file.mkdirs();
		return file;
	}

	protected String outputPath(String idx) {
		String pathname = "test-output_"+idx;
		return pathname;
	}
	
	abstract protected ClassesGenerator classesGenerator();
	
//	protected void generate(BeanModelAnalyzer analyzer, int idx) throws JClassAlreadyExistsException, IOException {
//		generate(analyzer, Integer.toString(idx));
//	}
//	
//	protected void generate(BeanModelAnalyzer analyzer, String idx)
//			throws JClassAlreadyExistsException, IOException {
//		classesGenerator().generateFields(
//				cm._class(packageName(idx) + ".Autobeans"),
//				cm._class(packageName(idx) + ".Services"),
//				analyzer.getDefinedTypes()
//				);
//		
//		write(idx);
//	}

	final protected String packageName(int idx) {
		return packageName(Integer.toString(idx));
	}
	protected String packageName(String idx) {
		String packageName = "test_"+idx;
		return packageName;
	}

	protected void generate(Model model, int idx) {
		GeneratorUtil.generate(
				model, 
				mkdir(idx), 
				new ClassPathGenerator(
						User.class.getClassLoader(),
						classesGenerator()
				)
		);
	}
	
//	@Test
//	public void testAutobeansGeneration() throws Exception {
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				SampleBean.class, 
//				SampleBeanExtended.class
//		);
//
//		generate(analyzer, 1);
//	}
//
//
//
//
//	@Test
//	public void testAutobeansGeneration2() throws Exception {
//		
//		BeanModelAnalyzer analyzer = new BeanModelAnalyzer(
//				Book.class, 
//				User.class,
//				UserView.class,
//				UserPrivilege.class,
//				Privilege.class,
//				SomeValue.class
//		);
//		
//		generate(analyzer, 2);
//	}
//
//	
//	@Test
//	public void testAutobeansGeneration3() throws Exception {
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
//		generate(analyzer, 3);
//		
//	}
	
	@Test
	public void testAutobeansGeneration4() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass(packageName(4)+".TestAutoBeans");
		model.setServerClass(packageName(4)+".ServiceClass");
		model.setSharedClass(packageName(4)+".SharedClass");
		
		generate.getSourcePackage().add("hu.mapro.model.analyzer.test.imdate");
		
		generate(model, 4);
	}

	
	@Test
	public void testAutobeansGeneration5() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass(packageName(5)+".TestAutoBeansOne");
		model.setServerClass(packageName(5)+".ServiceClass");
		model.setSharedClass(packageName(5)+".SharedClass");
		
		generate.getSourcePackage().add("hu.mapro.model.analyzer.test.one");
		
		generate(model, 5);
		
	}
	
	@Test
	public void testAutobeansGenerationDraft() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass(packageName("draft")+".TestAutoBeansDraft");
		model.setServerClass(packageName("draft")+".ServiceClass");
		model.setSharedClass(packageName("draft")+".SharedClass");
		
		generate.getSourcePackage().add("hu.mapro.model.analyzer.test.domain.draft");
		
		generate(model, 5);
		
	}

	@Test
	public void testAutobeansGenerationMeta1() {
		Model model = new Model();
		Generate generate = new Generate();
		model.setGenerate(generate);
		
		model.setClientClass(packageName("meta1")+".TestAutoBeansMeta1");
		model.setServerClass(packageName("meta1")+".ServiceClass");
		model.setSharedClass(packageName("meta1")+".SharedClass");
		
		MetaPackages mp = new MetaPackages();
		mp.getEntity().add("hu.mapro.model.analyzer.test.meta.test1.entity");
		mp.getView().add("hu.mapro.model.analyzer.test.meta.test1.view");
		
		generate.getMetaPackages().add(mp);
		
		generate(model, 6);
		
	}
	
	
}
