package hu.mapro.jpa.builder;

import hu.mapro.model.generator.gwtproxy.GwtProxyGenerator;
import hu.mapro.model.generator.util.ClassPathGenerator;
import hu.mapro.model.generator.util.GeneratorUtil;

import java.io.File;
import java.io.FileInputStream;

public class ProxyBuilder {

	public static void main(String[] args) throws Exception {
		File _targetDirectory = new File("../maprohu-jpa-gwt-domain/target/generated-sources/maprohu-model");
		File _modelDescriptor = new File("../maprohu-jpa-gwt-domain/src/main/model/model.xml");

		
		_targetDirectory.mkdirs();
		
		GeneratorUtil.generate(
				GeneratorUtil.getModel(new FileInputStream(_modelDescriptor)),
				_targetDirectory,
				new ClassPathGenerator(ProxyBuilder.class.getClassLoader(), new GwtProxyGenerator())
		);
		
		
	}
	
}
