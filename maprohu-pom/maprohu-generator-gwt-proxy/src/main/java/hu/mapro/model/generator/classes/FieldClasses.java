package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.shared.Visitable;
import hu.mapro.model.Getter;
import hu.mapro.model.Setter;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.analyzer.ValueTypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.generator.classes.GlobalClasses.MessageGenerator;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.BuiltinTypes;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HasLabel;
import hu.mapro.model.meta.MetaUtils;
import hu.mapro.model.meta.ObjectType;
import hu.mapro.model.meta.Type;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;

public class FieldClasses extends ComplexContext {

	Generator<JDefinedClass> field = gen(new ClassGeneration("Field") {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(final JDefinedClass field)  {
//			complexClasses.globalClasses.typeClasses.new SubAction() {
//				@Override
//				void subclass(ComplexClasses subClasses) {
//					JMethod fieldVisitMethod = field.method(JMod.PUBLIC, complexClasses.globalClasses.cm.directClass("O"), "visit");
//					JTypeVar fieldVisitMethodGeneric = fieldVisitMethod.generify("O");
//					JVar fieldVisitMethodParam = fieldVisitMethod.param(subClasses.fieldVisitor.get().narrow(fieldVisitMethodGeneric), "visitor");
//					fieldVisitMethod.body()._return(fieldVisitMethodParam.invoke(fieldObject.getName()));
//				}
//			}.process(complexClasses.complexTypeInfo);
			
//			fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
//				public Void visit(TypeInfo type) {
//					return null;
//				}
//				
//				public Void visit(ComplexTypeInfo type) {
//					field._implements(t.fetchPlanNavigator(complexClasses.clientClass.get()));
//					
//					JMethod navigate = builder.override(cm.VOID, "navigate");
//					JVar path = navigate.param(t.fetchPlanFollower(complexClasses.clientClass.get()), "path");
//					navigate.body().invoke(path, "follow").arg(propertyName);
//					return null;
//				}
//			});
			
			
//			JDefinedClass fieldImplClass = field; 
			
			readable = fieldObject.isReadable();
			writable = fieldObject.isWritable();
			

			Reflector<Field> fieldReflector = builder._implements(Field.class, complexClasses.clientClass.get(), elementClass);
			fieldReflector.override().getName();
			fieldReflector.method().body()._return(JExpr.lit(fieldInfo.getName()));

			fieldReflector.override().isReadable();
			fieldReflector.method().body()._return(JExpr.lit(readable));
			
			fieldReflector.override().isWritable();
			fieldReflector.method().body()._return(JExpr.lit(writable));
			
			fieldReflector.override().getCardinality();
			fieldReflector.method().body()._return(cm.ref(Cardinality.class).staticRef(fieldObject.getCardinality().name()));

			Reflector<HasLabel> hasLabelReflector = builder._implements(HasLabel.class);
			hasLabelReflector.override().getLabel();
			hasLabelReflector.method().body()._return(
					messageGenerator.getLabel()
			);
			
//			field._implements(complexClasses.field.get());
			
			final JClass valueTypeType = cm.ref(Type.class).narrow(elementClass);
			
			JExpression valueTypeExpression = fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<JExpression>() {
				@Override
				public JExpression visit(BuiltinTypeInfo type) {
					return JExpr.cast(valueTypeType, cm.ref(BuiltinTypes.class).staticRef(type.getBuiltinTypeCategory().name()));
				}
				
				@Override
				public JExpression visit(ObjectTypeInfo type) {
					return cm.ref(ObjectType.class).staticRef("INSTANCE");
				}
				
				@Override
				public JExpression visit(ValueTypeInfo type) {
					return other(type);
				}

				@Override
				public JExpression visit(EntityTypeInfo type) {
					return other(type);
				}
				
				@Override
				public JExpression visit(EnumTypeInfo type) {
					return other(type);
				}
				
				protected JExpression other(DefinedTypeInfo type) {
					return globalClasses.getClasses(type).refTypeField();
				}
				
				@Override
				protected JExpression defaultVisit(TypeInfo type) {
					throw new RuntimeException("something went wrong");
				}
			});
			
			fieldReflector.override().getValueType();
			fieldReflector.method().body()._return(valueTypeExpression);
			
			if (readable) {
				Reflector<Function> functionReflector = builder._implements(Function.class, complexClasses.clientClass.get(), clientPropertyType);
				functionReflector.override().apply(null);
				JVar functionInput = functionReflector.param("input");
				functionReflector.method().body()._if(functionInput.eq(JExpr._null()))._then()._return(JExpr._null());
				functionReflector.method().body()._return(functionInput.invoke(fieldInfo.getReadMethod()));
				
				Reflector<Getter> getterReflector = builder._implements(Getter.class, complexClasses.clientClass.get(), clientPropertyType);
				getterReflector.override().get(null);
				getterReflector.method().body()._return(JExpr.invoke(functionReflector.method()).arg(getterReflector.param("object")));
			}
			
			if (writable) {
				Reflector<Setter> setterReflector = builder._implements(Setter.class, complexClasses.clientClass.get(), clientPropertyType);
				setterReflector.override().set(null, null);
				setterReflector.method().body().invoke(
						setterReflector.param("object"), 
						fieldInfo.getWriteMethod()
				).arg(
						setterReflector.param("value")
				);
			}
			

			
//			JMethod fieldTypeVisitMethod = fieldImplClass.method(JMod.PUBLIC, complexClasses.globalClasses.cm.directClass("O"), "visit");
//			JTypeVar fieldTypeVisitMethodGeneric = fieldTypeVisitMethod.generify("O");
//			JVar fieldTypeVisitMethodParam = fieldTypeVisitMethod.param(complexClasses.globalClasses.fieldVisitor.get().narrow(fieldTypeVisitMethodGeneric), "visitor");
//			JBlock fieldTypeVisitMethodBody = fieldTypeVisitMethod.body();
			
			
			if (MetaUtils.isPluralField(fieldObject)) {
				
//				String visitorPrefix = null;
//				
//				if (fieldObject.getCardinality()==Cardinality.LIST) {
//					visitorPrefix = "list";
//				} else if (fieldObject.getCardinality()==Cardinality.SET) {
//					visitorPrefix = "set";
//				}
				
				ComplexClasses elementClasses = complexClasses.globalClasses.getClasses((ComplexTypeInfo)fieldInfo.getValueType());
				
				
				
//				fieldTypeVisitMethodBody._return(JExpr.cast(fieldTypeVisitMethodGeneric, complexClasses.globalClasses.createDomainCollectionVisitorInvocation(
//						complexClasses,
//						fieldTypeVisitMethodParam,
//						visitorPrefix, 
//						elementClasses, 
//						fieldObject.getInverseField()
//				)));
				
//				fieldImplClass._implements(complexClasses.globalClasses.cm.ref(PluralField.class).narrow(
//						complexClasses.clientClass.get(), 
//						elementClasses.proxy.get()
//				));
				
				fieldReflector.override().getInverseField();
				if (fieldObject.getInverseField()!=null) {
					fieldReflector.method().body()._return(elementClasses.fields.get().staticRef(fieldObject.getInverseField().getName()));
				} else {
					fieldReflector.method().body()._return(JExpr._null());
				}
			} else {
//				fieldTypeVisitMethodBody._return(JExpr.cast(fieldTypeVisitMethodGeneric, complexClasses.globalClasses.createVisitorInvocation(
//						fieldTypeVisitMethodParam,
//						"scalar", 
//						clientName
//				)));
				
				fieldReflector.override().getInverseField();
				fieldReflector.method().body()._return(JExpr._null());
			}
			
			
		}
		
	});
	
	Field<?, ?> fieldObject;
	String name;
	String propertyName;
	FieldInfo fieldInfo;


	
	
	public FieldClasses(ComplexClasses complexClasses, FieldInfo fieldInfo) {
		super(complexClasses);
		this.fieldInfo = fieldInfo;
		this.fieldObject = fieldInfo;
		propertyName = fieldObject.getName();
		name = org.apache.commons.lang3.StringUtils.capitalize(propertyName);
		valueType = fieldInfo.getValueType();
		readableName = GlobalClasses.getReadableSymbolName(name);
		
		
		clientPropertyType = globalClasses.getClientPropertyType(fieldInfo);
		elementClass = clientPropertyType;
		if (fieldObject.getCardinality()==Cardinality.SET||fieldObject.getCardinality()==Cardinality.LIST) {
			elementClass = elementClass.getTypeParameters().get(0);
		}
		
		messageGenerator = globalClasses.new MessageGenerator(complexClasses.propertyName+name, readableName);
		
		complexPropertyVisitable = new Visitable<ComplexClasses>(
				valueType.visit(new AbstractTypeInfoVisitor<Optional<ComplexClasses>>() {
					@Override
					public Optional<ComplexClasses> visit(ComplexTypeInfo type) {
						return Optional.of(globalClasses.typeClasses.get(type));
					}

					protected com.google.common.base.Optional<ComplexClasses> defaultVisit(TypeInfo type) {
						return Optional.absent();
					}
				})
		);
	}

	public JExpression staticRef() {
		return complexClasses.fields.get().staticRef(propertyName);
	}


	//JMethod setter;
	boolean writable;
	JClass elementClass;
	boolean readable;
	JClass clientPropertyType;
	TypeInfo valueType;
	String readableName;
	final MessageGenerator messageGenerator;

	final Visitable<ComplexClasses> complexPropertyVisitable;  
	
	
	@Override
	String getTypeName(String name) {
		return complexClasses.name + FieldClasses.this.name + name;
	}	

	public boolean isEditable() {
		return 
				!propertyName.equals("id")&&!propertyName.equals("version")&&
				fieldObject.isWritable() && fieldObject.isReadable();
	}
	
	public JClass portableValueType() {
		JClass type = fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<JClass>() {
			private JClass type(JClass jDefinedClass) {
				return getPropertyType(fieldInfo.getCardinality(), jDefinedClass);
			}
			
			public JClass visit(ComplexTypeInfo type) {
				switch (fieldInfo.getCardinality()) {
				case LIST:
				case SET:
					return type(globalClasses.typeClasses.get(type).immutable.get().wildcard());
				default:
					return type(globalClasses.typeClasses.get(type).immutable.get());
				}
				
			}
			

			public JClass visit(hu.mapro.model.analyzer.TypeInfo type) {
				return type(globalClasses.getScalarClientPropertyType(fieldInfo));
			}
		});
		return type;
	}

	public JClass readableValueType() {
		JClass type = fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<JClass>() {
			private JClass type(JClass jDefinedClass) {
				return getPropertyType(fieldInfo.getCardinality(), jDefinedClass);
			}
			
			public JClass visit(ComplexTypeInfo type) {
				switch (fieldInfo.getCardinality()) {
				case LIST:
				case SET:
					return type(globalClasses.typeClasses.get(type).readable.get().wildcard());
				default:
					return type(globalClasses.typeClasses.get(type).readable.get());
				}
				
			}

			public JClass visit(hu.mapro.model.analyzer.TypeInfo type) {
				return type(globalClasses.getScalarClientPropertyType(fieldInfo));
			}
		});
		return type;
	}
	
	
}