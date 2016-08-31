package hu.mapro.model.analyzer.test.domain.flang;

import javax.persistence.Entity;

@Entity
public class UserView extends UserViewA {

    String password;
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
