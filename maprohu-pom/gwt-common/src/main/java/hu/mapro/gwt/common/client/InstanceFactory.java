package hu.mapro.gwt.common.client;


public interface InstanceFactory<T> {

	T create(ClassDataFactory classDataFactory);
	
}
