package hu.mapro.model.maven.gwtproxy;

import hu.mapro.model.generator.gwtproxy.GwtProxyGenerator;
import hu.mapro.model.generator.util.ClassPathGenerator;
import hu.mapro.model.generator.util.GeneratorUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;

/**
 * 
 * @goal generate-client
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class ModelGeneratorMojo extends AbstractMojo {

	/**
	 * @parameter default-value="${basedir}/src/main/model/model.xml"
	 */
	private File _modelDescriptor;
	
	/**
	 * @parameter default-value="${project.build.directory}/generated-sources/maprohu-model"
	 */
	private File _targetDirectory;
	
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    /**
     * The plugin descriptor
     * 
     * @parameter default-value="${descriptor}"
     */
    private PluginDescriptor descriptor;

    
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		try {
			_targetDirectory.mkdirs();
			project.addCompileSourceRoot(_targetDirectory.getAbsolutePath());
			
			List<String> runtimeClasspathElements = project.getRuntimeClasspathElements();
			URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
			for (int i = 0; i < runtimeClasspathElements.size(); i++) {
			  String element = (String) runtimeClasspathElements.get(i);
			  runtimeUrls[i] = new File(element).toURI().toURL();
			}
			URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
			  descriptor.getClassRealm());
			
			GeneratorUtil.generate(
					GeneratorUtil.getModel(new FileInputStream(_modelDescriptor)),
					_targetDirectory,
					new ClassPathGenerator(newLoader, new GwtProxyGenerator())
			);
		} catch (FileNotFoundException e) {
			throw new MojoExecutionException(e, e.getMessage(), e.getMessage());
		} catch (MalformedURLException e) {
			throw new MojoExecutionException(e, e.getMessage(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException(e, e.getMessage(), e.getMessage());
		}
		
	}

	public void setModelDescriptor(File modelDescriptor) {
		this._modelDescriptor = modelDescriptor;
	}

	public void setTargetDirectory(File _targetDirectory) {
		this._targetDirectory = _targetDirectory;
	}

	public File getModelDescriptor() {
		return _modelDescriptor;
	}

	public File getTargetDirectory() {
		return _targetDirectory;
	}
	
	

}
