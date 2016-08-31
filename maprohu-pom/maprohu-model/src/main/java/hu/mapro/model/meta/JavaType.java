package hu.mapro.model.meta;

public interface JavaType<T> extends Type<T> {
	
	Class<T> getJavaType();
	
}
