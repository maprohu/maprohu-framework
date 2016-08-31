package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.meta.DefinedType;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Supplier;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

abstract class DefinedUiContext extends GlobalUiContext {
		
	String serverClassFullName;
	String name;
	String propertyName;
	String readableName;
	JClass serverClass;
	Supplier<? extends JClass> clientClass;

	//JDefinedClass typeClass;
	DefinedType<?> definedType;
	DefinedTypeInfo definedTypeInfo;
	private DefinedClasses definedClasses;


	public DefinedUiContext(
			GlobalUiClasses autoBeanGeneration, 
			DefinedTypeInfo definedTypeInfo,
			JClass serverClass
	)  {
		super(autoBeanGeneration);
		this.definedTypeInfo = definedTypeInfo;
		
		definedClasses = autoBeanGeneration.globalClasses.getClasses(definedTypeInfo);
		this.clientClass = definedClasses.clientClass;
		this.serverClass = serverClass;
		
		this.definedType = definedTypeInfo;
		this.serverClassFullName = serverClass.fullName();
		this.name = definedClasses.name;
		this.propertyName = StringUtils.uncapitalize(name);
		this.readableName = GlobalClasses.getReadableSymbolName(name);
	}
	
	JDefinedClass clazz(String suffix)  {
		return clazz(clientContainer, suffix);
	}
	
	JDefinedClass clazz(ClassContainer cc, String suffix)  {
		return cc._class(
				JMod.STATIC|JMod.PUBLIC, 
				name+suffix
		);
	}
	
	JDefinedClass iface(String suffix)  {
		return iface(clientContainer, suffix);
	}
	
	JDefinedClass iface(ClassContainer cc, String suffix)  {
		return cc._interface(
				JMod.PUBLIC, 
				name+suffix
		);
	}
	
	@Override
	String getTypeName(String name) {
		return this.name + super.getTypeName(name);
	}	
	
	class MessageGenerator extends GlobalClasses.MessageGenerator {

		public MessageGenerator(
				String code,
				String label
		) {
			globalClasses.super(propertyName+StringUtils.capitalize(code), label);
		}
	}
	
}