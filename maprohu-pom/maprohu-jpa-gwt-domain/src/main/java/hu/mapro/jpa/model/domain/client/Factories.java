package hu.mapro.jpa.model.domain.client;


import com.google.common.base.Optional;


public class Factories {

	public static class ListConfigFactoryBuilder extends ListConfigFactory {
		
	}
	
	public static ListConfigFactoryBuilder listConfig() {
		return new ListConfigFactoryBuilder();
	}
	
}
