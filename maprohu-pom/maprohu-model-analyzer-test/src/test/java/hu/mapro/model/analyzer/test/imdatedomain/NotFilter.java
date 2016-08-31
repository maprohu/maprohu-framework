package hu.mapro.model.analyzer.test.imdatedomain;



import java.io.Serializable;


public class NotFilter extends UserFilter  implements Serializable{

	UserFilter filter;

	public UserFilter getFilter() {
		return filter;
	}

	public void setFilter(UserFilter filter) {
		this.filter = filter;
	}
	
}
