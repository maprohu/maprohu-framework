package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.ComplexTypeInfo;


public class PluralFieldUiClasses extends FieldUiContext {

	public PluralFieldUiClasses(final FieldUiClasses fieldUiClasses) {
		super(fieldUiClasses.complexUiClasses, fieldUiClasses.fieldInfo);
		this.fieldUiClasses = fieldUiClasses;
		
		this.valueClasses = fieldUiClasses.globalUiClasses.typeClasses.get((ComplexTypeInfo)fieldInfo.getValueType());
		
		this.editorTypeVisitable = new EditorTypeVisitable<
				Void, 
				PluralFieldInlineUiClasses, 
				PluralFieldFormUiClasses, 
				PluralFieldPageUiClasses
		>(valueClasses.complexTypeInfo.getEditorType()) {
			@Override
			protected PluralFieldInlineUiClasses getInline() {
				return new PluralFieldInlineUiClasses(fieldUiClasses);
			}
			
			@Override
			protected PluralFieldFormUiClasses getForm() {
				return new PluralFieldFormUiClasses(fieldUiClasses);
			}
			
			@Override
			protected PluralFieldPageUiClasses getPage() {
				return new PluralFieldPageUiClasses(fieldUiClasses);
			}
		};
	}
	
	final FieldUiClasses fieldUiClasses;
	final ComplexUiClasses valueClasses;
	
	final EditorTypeVisitable<
		Void, 
		PluralFieldInlineUiClasses, 
		PluralFieldFormUiClasses, 
		PluralFieldPageUiClasses
	> editorTypeVisitable;

	@Override
	public String getFieldClassName(
			String typeName,
			String fieldName,
			String generatedClassName
	) {
		return typeName + fieldName + "Plural" + generatedClassName;
	}

	
	
}