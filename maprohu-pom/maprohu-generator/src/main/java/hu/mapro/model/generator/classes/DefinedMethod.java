package hu.mapro.model.generator.classes;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public class DefinedMethod {
	
	final JMethod method;

	public DefinedMethod(JMethod method) {
		this.method = method;
	}
	
	public DefinedMethod(JDefinedClass definedClass, JType returnType, String name) {
		this(definedClass.method(getMethodModifiers(definedClass), returnType, name));
		if (isOverride(definedClass)) {
			override();
		}
	}
	
	
	final Supplier<JAnnotationArrayMember> suppressArray = Suppliers.memoize(new Supplier<JAnnotationArrayMember>() {
		@Override
		public JAnnotationArrayMember get() {
			return method.annotate(SuppressWarnings.class).paramArray("value");
		}
	});
	
	public void unchecked() {
		suppressArray.get().param("unchecked");
	}
	
	public void rawtypes() {
		suppressArray.get().param("rawtypes");
	}
	
	public void override() {
		override(method);
	}
	
	public static void override(JMethod method) {
		method.annotate(Override.class);
	}
	
	static int getMethodModifiers(JDefinedClass definedClass) {
		return definedClass.isInterface() ? JMod.NONE : JMod.PUBLIC;
	}

	JVar param(JType type, String name) {
		return method.param(type, name);
	}
	
	boolean isOverride(JDefinedClass definedClass) {
		return !definedClass.isInterface();
	}
	
	String name() {
		return method.name();
	}
	
}
