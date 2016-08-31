package hu.mapro.model.analyzer;

import hu.mapro.model.meta.EntityType;


public interface EntityTypeInfo extends EntityType<Object>, ComplexTypeInfo {

	boolean isHistory();
	
}
