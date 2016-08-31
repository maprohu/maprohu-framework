package hu.mapro.model.analyzer.test.meta.test1.entity;


public interface UserAccount {
	
	//@Column(unique=true)
	String username();
	
	String password();
	
	Boolean isAdmin();
	
	Boolean isCoordinator();
	
	Agent agent();
	
	Boolean isManager();
	
	Boolean isDisabled();
	
}
