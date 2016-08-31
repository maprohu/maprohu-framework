package hu.mapro.model.analyzer;

import hu.mapro.meta.EditorType;
import hu.mapro.model.meta.ComplexType;

import java.util.Collection;

public interface ComplexTypeInfo extends ComplexType<Object>, DefinedTypeInfo, HierarchicTypeInfo {

	HierarchicTypeInfo getSuperType();
	
	Collection<FieldInfo> getFields();
	
	boolean generateServer();
	
	boolean isUncached();
	
	Collection<FieldInfo> getDelegates();

	EditorType getEditorType();
	
	
}
