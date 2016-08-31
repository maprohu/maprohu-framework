package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.meta.ComplexType;
import hu.mapro.model.meta.EnumType;
import hu.mapro.model.meta.JavaEntityType;
import hu.mapro.model.meta.JavaType;
import hu.mapro.model.meta.ObjectType;
import hu.mapro.model.meta.Type;
import hu.mapro.model.meta.Type.TypeCategory;
import hu.mapro.model.meta.ValueType;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;

abstract class DefinedClasses extends GlobalContext {
		
	Generator<JDefinedClass> type = gen(new ClassGeneration("Type") {

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		void init(final JDefinedClass type)  {
			
			Reflector<Type> typeReflector = builder._implements(Type.class, clientClass.get());
			
			typeReflector.override().visit(null);
			typeReflector.method().body()._return(JExpr.invoke(typeReflector.param("visitor"), "visit").arg(JExpr._this()));

//			JMethod typeVisitMethod = type.method(JMod.PUBLIC, DefinedClasses.this.globalClasses.cm.directClass("V"), "visit");
//			typeVisitMethod.annotate(Override.class);
//			JTypeVar typeVisitMethodV = typeVisitMethod.generify("V");
//			JVar typeVisitMethodVisitor = typeVisitMethod.param(DefinedClasses.this.globalClasses.cm.ref(TypeVisitor.class).narrow(typeVisitMethodV), "visitor");

			typeReflector.override().getTypeCategory();
			typeReflector.method().body()._return(cm.ref(TypeCategory.class).staticRef(definedTypeInfo.getTypeCategory().name()));
			
			definedTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				
				public Void visit(EnumTypeInfo enumType) {
					type._implements(cm.ref(EnumType.class).narrow(clientClass.get()));
					return null;
				}
				
				public Void visit(ComplexTypeInfo complexType) {
					final ComplexClasses classes = globalClasses.getClasses(complexType);
					
					Reflector<ComplexType> complexReflector = builder._implements(ComplexType.class, clientClass.get());
					complexReflector.override().getSuperType();
					
					final JMethod getSuperTypeMethod = complexReflector.method();
					
					complexType.getSuperType().visit(new AbstractTypeInfoVisitor<Void>() {
						@Override
						public Void visit(ObjectTypeInfo type) {
							getSuperTypeMethod.body()._return(cm.ref(ObjectType.class).staticRef("INSTANCE"));
							return null;
						}
						@Override
						public Void visit(ComplexTypeInfo type) {
							getSuperTypeMethod.body()._return(globalClasses.getClasses(type).refTypeField());
							return null;
						}
					});
					
					complexReflector.override().getFields();
					final JInvocation fieldsInvocation = cm.ref(ImmutableList.class).staticInvoke("of");
					classes.fieldClasses.new PostProcessor() {
						@Override
						void postProcess(FieldInfo input, FieldClasses output) {
							fieldsInvocation.arg(classes.fields.get().staticRef(input.getName()));
						}
					};
					complexReflector.method().body()._return(fieldsInvocation);
					
					complexReflector.override().isAbstract();
					complexReflector.method().body()._return(JExpr.lit(complexType.isAbstract()));
					
					
					
					complexReflector.override().getName();
					complexReflector.method().body()._return(JExpr.lit(complexType.getName()));
					
					return null;
				}	
				
				public Void visit(ValueTypeInfo valueType) {
					visit((ComplexTypeInfo)valueType);
					
					type._implements(cm.ref(ValueType.class).narrow(clientClass.get()));
					
					return null;
				}
				
				public Void visit(EntityTypeInfo entityType) {
					visit((ComplexTypeInfo)entityType);
					
					
					Reflector<JavaEntityType> reflector = builder._implements(JavaEntityType.class, clientClass.get());
					reflector.override().isPersistent();
					reflector.method().body()._return(JExpr.lit(entityType.isPersistent()));
					
					
					return null;
				}
				
			});
			
			Reflector<JavaType> javaTypeReflector = builder._implements(JavaType.class, clientClass.get());
			javaTypeReflector.override().getJavaType();
			javaTypeReflector.method().body()._return(clientClass.get().dotclass());
			
		}
		
		
	});
	
	String name;
	String propertyName;
	String readableName;
	Supplier<? extends JClass> clientClass;
	Supplier<? extends JClass> serverClass;

	final DefinedTypeInfo definedTypeInfo;

	public DefinedClasses(
			GlobalClasses autoBeanGeneration, 
			DefinedTypeInfo definedTypeInfo
	)  {
		super(autoBeanGeneration);
		this.definedTypeInfo = definedTypeInfo;
		this.name = definedTypeInfo.getName();
		this.propertyName = StringUtils.uncapitalize(name);
		this.readableName = GlobalClasses.getReadableSymbolName(name);
		
	}
	
	public void setClientClass(Supplier<? extends JClass> clientClass) {
		this.clientClass = clientClass;
	}

	public void setServerClass(Supplier<? extends JClass> serverClass) {
		this.serverClass = serverClass;
	}

	@Override
	String getTypeName(String name) {
		return this.name + super.getTypeName(name);
	}	

	JFieldRef refTypeField() {
		return globalClasses.types.get().staticRef(propertyName);
	}
	
	
}