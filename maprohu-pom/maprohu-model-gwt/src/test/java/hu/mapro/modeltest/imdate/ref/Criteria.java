package hu.mapro.modeltest.imdate.ref;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Criteria implements Serializable {

	String main;
	String cache;
	public String getMain() {
		return main;
	}
	public void setMain(String main) {
		this.main = main;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	
}
