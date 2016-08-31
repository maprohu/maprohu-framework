package hu.mapro.model.generator.classes;

import hu.mapro.model.generator.classes.DefinedClassBuilder.FieldPropertyBuilder;
import hu.mapro.model.impl.Cardinality;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

public class GenerationBase {

	final protected Inits inits;
	final protected ClassContainer clientContainer;
	final protected ClassContainer serverContainer;
	final protected ClassContainer sharedContainer;
	final protected JCodeModel cm;
	
	public GenerationBase(GenerationBase c) {
		this(c.cm, c.clientContainer, c.serverContainer, c.sharedContainer, c.inits);
	}
	
	public GenerationBase(
			JCodeModel cm, 
			ClassContainer clientContainer,
			ClassContainer serverContainer,
			ClassContainer sharedContainer,
			Inits inits
	) {
		super();
		this.cm = cm;
		this.clientContainer = clientContainer;
		this.serverContainer = serverContainer;
		this.sharedContainer = sharedContainer;
		this.inits = inits;
	}

//	private static int getModifiers(ClassType classType) {
//		return classType==ClassType.CLASS ? JMod.PUBLIC|JMod.STATIC : JMod.PUBLIC;
//	}
	
	String getTypeName(String name) {
		return name;
	}

	ClassContainer getDefaultClassContainer() {
		return clientContainer;
	}
	
	interface ClassDetails {
		
	}
	
	abstract class DefinedClassGeneration extends Generator<JDefinedClass> {

//		ClassDetails classDetails = new ClassDetails() {
//			
//		};
		
		
		final String name;
		final ClassContainer classContainer;
		String javadoc;

		private void javadoc() {
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			
			Class<? extends DefinedClassGeneration> thisClass = this.getClass();
			String thisClassName = thisClass.getName();
			
			int idx = 0;
			
			while (!trace[idx].getClassName().equals(thisClassName) || !trace[idx].getMethodName().equals("<init>")) {
				idx++;
			}
			
			String className = trace[idx+1].getClassName();

			while (true) {
				int dollarIdx = className.lastIndexOf('$');
				
				if (dollarIdx == -1) break;
				
				if (Character.isDigit(className.charAt(dollarIdx+1))) {
					className = className.substring(0, dollarIdx);
				} else {
					break;
				}
			}
			
			javadoc = "@see " + className+"#"+StringUtils.uncapitalize(name);
		}
		
		public DefinedClassGeneration(String name, ClassContainer classContainer) {
			super();
			this.name = StringUtils.capitalize(name);
			this.classContainer = classContainer;
			javadoc();
		}

		public DefinedClassGeneration(String name) {
			super();
			this.name = StringUtils.capitalize(name);
			this.classContainer = getClassContainer();
			javadoc();
		}

		public ClassContainer getClassContainer() {
			return getDefaultClassContainer();
		}

		final DefinedClassBuilder builder = new DefinedClassBuilder(cm, this);
		
		@Override
		JDefinedClass create()  {
			ClassType classType = getClassType();
			JDefinedClass definedClass = classContainer._class(getModifiers(), getName(), classType);
			if (javadoc!=null) {
				definedClass.javadoc().add(javadoc);
			}
			return definedClass;
		}

		int getModifiers() {
			return getModifiers(getClassType(), classContainer);
		}
		
		int getModifiers(ClassType classType, ClassContainer classContainer) {
			int modifiers = JMod.PUBLIC;
			if (classType==ClassType.CLASS && isAbstract()) {
				modifiers |= JMod.ABSTRACT;
			}
			if (classType==ClassType.CLASS && classContainer.isClass()) {
				modifiers |= JMod.STATIC;
			}
			return modifiers;
		}
		
		String getName() {
			return getTypeName(name);
		}
		
		abstract ClassType getClassType();

		boolean isAbstract() {
			return false;
		}
		
		
	}
	
	class ClassGeneration extends DefinedClassGeneration {
		
		public ClassGeneration(String name, ClassContainer classContainer) {
			super(name, classContainer);
		}

		public ClassGeneration(String name) {
			super(name);
		}

		@Override
		ClassType getClassType() {
			return ClassType.CLASS;
		}
		
	}
	
	class InterfaceGeneration extends DefinedClassGeneration {

		public InterfaceGeneration(String name, ClassContainer classContainer) {
			super(name, classContainer);
		}

		public InterfaceGeneration(String name) {
			super(name);
		}

		@Override
		ClassType getClassType() {
			return ClassType.INTERFACE;
		}
		
		//DefaultImplementation defaultImplementation = null;
		
		class Implementation extends ClassGeneration {
			
			@Override
			public ClassContainer getClassContainer() {
				return InterfaceGeneration.this.getClassContainer();
			}
			
			public Implementation() {
				super(InterfaceGeneration.this.name);
				builder.definedClass.get()._implements(InterfaceGeneration.this.get());
			}
			
			@Override
			// only exists to rename the param - so that Eclispe generates an override with 
			// a more meaningful param name
			void init(JDefinedClass implementation) {
				super.init(implementation);
			}
			
			public void injectImplements() {
				builder.injectImplements(InterfaceGeneration.this.get());
			}
			
		}

		
		class DefaultImplementation extends Implementation {
			
			@Override
			public ClassContainer getClassContainer() {
				return InterfaceGeneration.this.getClassContainer();
			}
			
			public DefaultImplementation() {
				//defaultImplementation = this;
				injectImplements();
			}

			@Override
			String getName() {
				return getTypeName("Default"+name);
			}
			
		}
		
		
	}

	
	
//	class MethodDefinition {
//		
//		class Param {
//			final JType type;
//			final String name;
//			Param(JType type, String name) {
//				super();
//				this.type = type;
//				this.name = name;
//			}
//		}
//		
//		final JType returnType;
//		final String name;
//		final List<Param> params = Lists.newArrayList();
//		
//		public MethodDefinition(JType returnType, String name) {
//			super();
//			this.returnType = returnType;
//			this.name = name;
//		}
//		
//	}

	public <T> Generator<T> gen(final Generator<T> generation) {
		return generation;
	}
	
	
	public <T> Generator<T> gen(final Generation<T> generation) {
		return new Generator<T>() {
			@Override
			T create() {
				return generation.create();
			}
			
			void init(T object) {
				generation.init(object);
			}
		};
	}
	

	public abstract class Generator<T> implements Supplier<T> {

		private Condition condition = new Condition() {
			@Override
			public boolean evaulate() {
				return canGenerate();
			}
		};
		
		final Supplier<T> cache = Suppliers.memoize(new Supplier<T>() {
			@Override
			public T get() {
				return condition.evaulate() ? create() : null;
			}
		});
		
		public Generator() {
			inits.init(new Init() {
				@Override
				public void init() {
					if (condition.evaulate()) {
						Generator.this.init(cache.get());
					}
				}
			});
		}
		
		abstract T create() ;
		
		void init(T object)  {
		}
		
		@Override
		public final T get() {
			return cache.get();
		}
		
		public void setCondition(Condition condition) {
			this.condition = condition;
		}
		
		boolean canGenerate() {
			return baseCanGenerate();
		}
		
	}

	public interface Condition {
		boolean evaulate();
		
		Condition TRUE = new Condition() {
			@Override
			public boolean evaulate() {
				return true;
			}
		};
		
	}
	
	public JClass getPropertyType(Cardinality cardinality, JClass type)  {
		switch (cardinality) {
		case LIST:   return cm.ref(List.class).narrow(type);
		case SET:    return cm.ref(Set.class).narrow(type);
		case SCALAR: return type;
		}
		return null;
	}

	public static JClass narrowIfNeeded(JClass base, JClass... narrow) {
		if (narrow.length==0) return base;
		else return base.narrow(narrow);
	}

	class ProxyGeneration implements Transferable {

		InterfaceGeneration proxy; 

		ClassGeneration server;
		
		ProxyGeneration(String name) {
			this(name, false);
		}
		
		ProxyGeneration(String name, final boolean isAbstract) {
			proxy = new InterfaceGeneration(name+"Proxy") {
				

				@Override
				void init(JDefinedClass proxy) {
					proxy._implements(cm.ref(ValueProxy.class));
					if (!isAbstract) {
						JAnnotationUse proxyFor = proxy.annotate(ProxyFor.class);
						proxyFor.param("value", server.get());
					}
					initProxy(ProxyGeneration.this);
				}
				
			};
			
			server = new ClassGeneration(name) {
				@Override
				boolean isAbstract() {
					return isAbstract;
				}
				
				@Override
				void init(JDefinedClass object) {
					
					ProxyGeneration.this.init(object, proxy.get());
				}
				
				@Override
				public ClassContainer getClassContainer() {
					return serverContainer;
				}
			};
		}
		
		void superProxy(ProxyGeneration superProxy, JClass... narrow) {
			proxy.get()._implements(narrowIfNeeded(superProxy.proxy.get(), narrow));
		}
		
		void superClass(ProxyGeneration superClass) {
			superProxy(superClass);
			server.get()._extends(superClass.server.get());
		}
		
		void init(JDefinedClass serverType, JDefinedClass proxyType) {
		}
		
		void property(JClass clazz) {
			
		}
		
		void fieldProperty(Transferable type) {
			FieldPropertyBuilder p = server.builder.fieldProperty(type.getServerType());
			proxy.builder.property(type.getProxyType(), p.name);
		}

		@Override
		public JClass getProxyType() {
			return proxy.get();
		}

		@Override
		public JClass getServerType() {
			return server.get();
		}
		
		
	}

	interface Transferable {
		
		JClass getProxyType();
		JClass getServerType();
		
	}
	
	static Transferable transferable(final JClass clazz) {
		return new Transferable() {
			@Override
			public JClass getServerType() {
				return clazz;
			}
			
			@Override
			public JClass getProxyType() {
				return clazz;
			}
		};
	}
	
	Transferable list(Transferable type) {
		return narrow(cm.ref(List.class), type);
	}
	
	Transferable set(Transferable type) {
		return narrow(cm.ref(Set.class), type);
	}
	
	Transferable narrow(final JClass base, final Transferable type) {
		return new Transferable() {
			
			@Override
			public JClass getServerType() {
				return base.narrow(type.getServerType());
			}
			
			@Override
			public JClass getProxyType() {
				return base.narrow(type.getProxyType());
			}
		};
	}
	
	void initProxy(ProxyGeneration proxyGeneration) {
	}

	public static JDefinedClass fakeClass() {
		try {
			return new JCodeModel()._class("fake");
		} catch (JClassAlreadyExistsException e) {
			throw Throwables.propagate(e);
		}
	}
	
	public static DefinedClassBuilder fakeClassbuildBuilder() {
		return new DefinedClassBuilder(fakeClass());
	}


	boolean baseCanGenerate() {
		return true;
	}
	
}
