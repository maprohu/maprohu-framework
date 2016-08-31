package hu.mapro.model.analyzer.test.flangdomain;

import hu.mapro.model.LongId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class UserPrivilege implements LongId {
	 
	@ManyToOne(fetch=FetchType.LAZY)
	User user;
	
	Privilege privilege;

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

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

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}

	public void setVersion(Integer version) {
		// TODO Auto-generated method stub
		
	}
	
}
