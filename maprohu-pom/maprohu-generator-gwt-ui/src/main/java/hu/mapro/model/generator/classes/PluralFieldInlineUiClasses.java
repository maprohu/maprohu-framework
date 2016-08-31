package hu.mapro.model.generator.classes;

import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurator;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.generator.classes.ComplexUiClasses.InlineEditorGridColumnSelect;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;


public class PluralFieldInlineUiClasses extends FieldUiContext {
	
	InterfaceGeneration editorGridConfigurator = new InterfaceGeneration("editorGridConfigurator") {
		void init(JDefinedClass inlineEditorGridConfigurator) {
			inlineEditorGridConfigurator._extends(cm.ref(InlineEditorGridConfigurator.class).narrow(clientElementType));
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					implementation._extends(valueClasses.defaultInlineEditorGridConfigurator.get());

					builder.injectSuper(
							editorGridColumnSelector.get(),
							valueClasses.fieldConstructors.get(),
							globalClasses.defaultMessages.get()
					);
				}
			};
		}
	};
	
	public InterfaceGeneration editorGridColumnSelector = new InterfaceGeneration("editorGridColumnSelector") {
		void init(JDefinedClass inlineEditorGridColumnSelector) {
			inlineEditorGridColumnSelector._implements(valueClasses.inlineEditorGridColumnSelector.get());
			
			new DefaultImplementation() {
				void init(JDefinedClass implementation) {
					final InlineEditorGridColumnSelect method = valueClasses.new InlineEditorGridColumnSelect(implementation);
					
					valueClasses.inlineEditorGridConfigurating.new FieldProcessor() {
						@Override
						void processField(FieldUiClasses fieldClasses) {
							method.method.body().add(
									JExpr.invoke(
											method.processFieldParam,
											fieldClasses.propertyName
									)
							);
						}
						
						boolean canProcess(FieldUiClasses fieldClasses) {
							return 
									fieldInfo.getInverseField()==null
									||
									!fieldClasses.fieldInfo.getName().equals(fieldInfo.getInverseField().getName());
						}
					};
					
					
				}
			};
		}
		
	};
	
	
	public PluralFieldInlineUiClasses(FieldUiClasses fieldUiClasses) {
		super(fieldUiClasses.complexUiClasses, fieldUiClasses.fieldInfo);
		this.fieldUiClasses = fieldUiClasses;
		
		this.valueClasses = fieldUiClasses.globalUiClasses.typeClasses.get((ComplexTypeInfo)fieldInfo.getValueType());
	}
	
	final FieldUiClasses fieldUiClasses;
	final ComplexUiClasses valueClasses;	

	@Override
	public String getFieldClassName(
			String typeName,
			String fieldName,
			String generatedClassName
	) {
		return typeName + fieldName + "PluralInline" + generatedClassName;
	}
	
}