package hu.mapro.model.analyzer.test.domain.flang;

import hu.mapro.model.LongIdBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class UserPrivilege extends LongIdBean {
	 
	@ManyToOne(fetch=FetchType.LAZY)
	User user;
	
	Privilege privilege;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	
}
