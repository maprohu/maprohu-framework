package hu.mapro.model.generator.classes;

import hu.mapro.jpa.model.domain.shared.FilterRepository.FullTextFilterItem;
import hu.mapro.jpa.model.server.DefaultFullTextFilterBuilder;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.BuiltinTypeInfo;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.FullTextSelectMethod;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.generator.classes.GenerationBase.InterfaceGeneration.DefaultImplementation;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class FullTextClasses extends FullTextClassesSuper {


	DefaultImplementation defaultfullTextFilterSelector = complexUiClasses.fullTextFilterSelector.new DefaultImplementation() {
		
		private FullTextSelectMethod buildFields;

		void init(JDefinedClass implementation) {
			buildFields = complexUiClasses.new FullTextSelectMethod(implementation);
			
			query(complexUiClasses, buildFields.b);
			
		}
		
		void query(ComplexUiClasses complexUiClasses, final JExpression b) {
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				Void present(ComplexUiClasses superOutput) {
					query(superOutput, b);
					return null;
				}
			}.process(complexUiClasses.complexTypeInfo);
			
			complexUiClasses.fieldClasses.new PostProcessor() {
				@Override
				void postProcess(final FieldInfo fieldInfo, final FieldUiClasses fieldClasses) {
					if (fieldInfo.isSearch()) {
						
						fieldInfo.getValueType().visit(new AbstractTypeInfoVisitor<Void>() {
							public Void visit(ComplexTypeInfo type) {
								query(globalUiClasses.typeClasses.get(type), b.invoke(fieldClasses.propertyName));
								
								return null;
							}
							
							public Void visit(BuiltinTypeInfo type) {
//								if (!type.getBuiltinTypeCategory().getTypeClass().equals(String.class)) {
//									throw new RuntimeException("Invalid type for search field: " + type.getBuiltinTypeCategory());
//								}
								
								buildFields.method.body().add(JExpr.invoke(b, fieldClasses.propertyName));
								
								
								return null;
							}
							
							protected Void defaultVisit(TypeInfo type) {
								throw new RuntimeException("invalid type for search field: " + type);
							}
						});
						
					}
				}
			};
			
		}
		
		
	};
	
	
	public ClassGeneration fullTextFilterBuilder = new ClassGeneration("fullTextFilterBuilder", serverContainer) {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass fullTextFilterBuilder) {
			builder.singleton();
			
			JFieldVar selector = builder.inject(complexUiClasses.fullTextFilterSelector.get());
			
			final FullTextSelectMethod buildFields = complexUiClasses.new FullTextSelectMethod(fakeClass());
			
			
			Reflector<DefaultFullTextFilterBuilder> reflector = builder._extends(DefaultFullTextFilterBuilder.class, serverClass);
			
			reflector.override().buildFields(null);
			
			JVar root = reflector.param("root");

			JBlock body = reflector.method().body();
			body.invoke(selector, buildFields.method).arg(JExpr._new(complexUiClasses.fullTextFilterNode.get()).arg(root));
			
			
		}		
		
	};

	
	Generator<JFieldVar> fullTextFilterItem = new Generator<JFieldVar>() {
		@Override
		JFieldVar create() {
			return complexUiClasses.filterRepository.get().field(JMod.PUBLIC, cm.ref(FullTextFilterItem.class), "fullText", JExpr._new(cm.ref(FullTextFilterItem.class)));
		}
	};
	
//	public ClassGeneration fullTextQueryStringBuilder = new ClassGeneration("fullTextQueryStringBuilder") {
//		
//		@SuppressWarnings({ "rawtypes", "unchecked" })
//		void init(JDefinedClass fullTextQueryStringBuilder) {
//			builder.singleton();
//			
//			JFieldVar selector = builder.inject(fullTextFilterSelector.get());
//			
//			final FullTextSelectMethod buildFields = new FullTextSelectMethod(fakeClass());
//			
//			
//			Reflector<DefaultFullTextFilterBuilder> reflector = builder._extends(DefaultFullTextFilterBuilder.class, serverClass);
//			
//			reflector.override().buildFields(null);
//			
//			JVar root = reflector.param("root");
//
//			JBlock body = reflector.method().body();
//			body.invoke(selector, buildFields.method).arg(JExpr._new(complexUiClasses.fullTextFilterNode.get()).arg(root));
//			
//			
//		}		
//		
//	};
	
	
	
	public FullTextClasses(ComplexUiClasses complexUiClasses) {
		super(complexUiClasses);
	}
	
	public JExpression fullTextFilterItemRef(JExpression filterRepository) {
		return filterRepository.ref(fullTextFilterItem.get());
	}

}
