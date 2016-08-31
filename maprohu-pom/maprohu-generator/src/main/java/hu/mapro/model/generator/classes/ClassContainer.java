package hu.mapro.model.generator.classes;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JDefinedClass;

public class ClassContainer {

	private final JClassContainer dc;
	
	public ClassContainer(JClassContainer dc) {
		super();
		checkNotNull(dc);
		this.dc = dc;
	}

	public JDefinedClass _class(int mods, String name) {
		try {
			return dc._class(mods, name);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException("class already exists: " + name, e);
		}
	}

	public JDefinedClass _class(String name) {
		try {
			return dc._class(name);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException("class already exists: " + name, e);
		}
	}

	public JDefinedClass _class(int mods, String name, ClassType kind) {
		try {
			return dc._class(mods, name, kind);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException("class already exists: " + name, e);
		}
	}

	public JDefinedClass _interface(int mods, String name) {
		try {
			return dc._interface(mods, name);
		} catch (JClassAlreadyExistsException e) {
			System.out.println(e.getExistingClass().fullName());
			throw new RuntimeException("class already exists: " + name, e);
		}
	}

	public JDefinedClass _interface(String name) {
		try {
			return dc._interface(name);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException("class already exists: " + name, e);
		}
	}
	
	public boolean isPackage() {
		return dc.isPackage();
	}

	public boolean isClass() {
		return dc.isClass();
	}
	
}