package hu.mapro.model.analyzer.test.imdatedomain;


import java.io.Serializable;


public class User  implements Serializable{

	String username;
	String name;
	String surname;
	String password;
	String organization;
	String country; 
	String whereMain;
	String whereCache;
	
	Role role;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWhereMain() {
		return whereMain;
	}
	public void setWhereMain(String whereMain) {
		this.whereMain = whereMain;
	}
	public String getWhereCache() {
		return whereCache;
	}
	public void setWhereCache(String whereCache) {
		this.whereCache = whereCache;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
