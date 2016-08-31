package hu.mapro.model.analyzer.test.domain.flang;

import hu.mapro.model.LongIdBean;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class User extends LongIdBean {

	String username;
	
    String password;
    
    String email;
    
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
    Set<UserPrivilege> privileges;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<UserPrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<UserPrivilege> privileges) {
		this.privileges = privileges;
	}


}
