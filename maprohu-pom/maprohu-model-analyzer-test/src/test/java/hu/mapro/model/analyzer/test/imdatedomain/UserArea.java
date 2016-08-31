package hu.mapro.model.analyzer.test.imdatedomain;



import java.io.Serializable;

public class UserArea   implements Serializable{
	
	String username;
	
	UserFilter filter;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserFilter getFilter() {
		return filter;
	}

	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}

}
