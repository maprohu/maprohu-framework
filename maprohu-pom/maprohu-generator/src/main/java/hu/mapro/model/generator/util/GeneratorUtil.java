package hu.mapro.model.generator.util;

import hu.mapro.meta.Abstract;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.descriptor.Binders;
import hu.mapro.model.descriptor.Model;
import hu.mapro.model.generator.UpdateCodeWriter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JCodeModel;

public class GeneratorUtil {

	public static Model getModel(InputStream is) {
		try {
			JAXBContext jc = JAXBContext.newInstance("hu.mapro.model.descriptor");
			Unmarshaller u = jc.createUnmarshaller();
			Model o = (Model) u.unmarshal(is);
			return o;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	public static Binders getBinders(InputStream is) {
		try {
			JAXBContext jc = JAXBContext.newInstance("hu.mapro.model.descriptor");
			Unmarshaller u = jc.createUnmarshaller();
			Binders o = (Binders) u.unmarshal(is);
			return o;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void generate(
			Model model, 
			File existingTargetDirectory,
			ModelGenerator generator
	) {
		try {
			JCodeModel cm = new JCodeModel();

			JClassContainer clientContainer;

			if (model.getClientClass() != null) {
				clientContainer = cm._class(model.getClientClass());
			} else {
				clientContainer = cm._package(model.getClientPackage());
			}

			JClassContainer serverContainer = null;
			
			if (model.getServerClass() != null) {
				serverContainer = cm._class(model.getServerClass());
			} else if (model.getServerPackage() != null) {
				serverContainer = cm._package(model.getServerPackage());
			}
			
			JClassContainer sharedContainer = null;
			
			if (model.getSharedClass() != null) {
				sharedContainer = cm._class(model.getSharedClass());
			} else if (model.getSharedPackage() != null) {
				sharedContainer = cm._package(model.getSharedPackage());
			}
			
			generator.generate(
					model.getGenerate(), 
					clientContainer, 
					serverContainer,
					sharedContainer
			);

			cm.build(new UpdateCodeWriter(existingTargetDirectory));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final Function<FieldInfo, String> FIELD_NAME = new Function<FieldInfo, String>() {
		@Override
		public String apply(FieldInfo input) {
			return input.getName();
		}
	};
	
	public static final void copyAnnotations(
			Annotation[] annotations,
			JAnnotatable target
	) {
		for (Annotation a : annotations) {
			Class<? extends Annotation> annotationType = a.annotationType();
			
			if (Objects.equal(annotationType.getPackage(), Abstract.class.getPackage())) continue;
			
			JAnnotationUse use = target.annotate(annotationType);
			Method[] methods = annotationType.getMethods();
			for (Method m : methods) {
				
				Class<?> declaringClass = m.getDeclaringClass();
				try {
					if (declaringClass.equals(Object.class) || declaringClass.equals(Annotation.class) || m.getParameterTypes().length!=0) continue;
					
					Object v = m.invoke(a);
					if (Objects.equal(m.getDefaultValue(), v)) continue;
					

					if (v instanceof Boolean) {
						use.param(m.getName(), (Boolean)v);
					} else if (v instanceof Byte) {
						use.param(m.getName(), (Byte)v);
					} else if (v instanceof Character) {
						use.param(m.getName(), (Character)v);
					} else if (v instanceof Double) {
						use.param(m.getName(), (Double)v);
					} else if (v instanceof Float) {
						use.param(m.getName(), (Float)v);
					} else if (v instanceof Long) {
						use.param(m.getName(), (Long)v);
					} else if (v instanceof Short) {
						use.param(m.getName(), (Short)v);
					} else if (v instanceof Integer) {
						use.param(m.getName(), (Integer)v);
					} else if (v instanceof String) {
						use.param(m.getName(), (String)v);
					} 
					
				} catch (Exception e) {
					throw Throwables.propagate(e);
				}
			}
			
			
		}
		
	}
	
}
