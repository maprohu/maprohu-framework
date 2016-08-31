package hu.mapro.model.maven.gwtui;

import hu.mapro.model.generator.binding.BindingGenerator;
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
 * @goal generate-binder
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class BinderGeneratorMojo extends AbstractMojo {

	/**
	 * @parameter default-value="${basedir}/src/main/model/binder.xml"
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
			
			BindingGenerator.generate(
					GeneratorUtil.getBinders(new FileInputStream(_modelDescriptor)),
					_targetDirectory,
					newLoader
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
