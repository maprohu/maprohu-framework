package hu.mapro.model.analyzer.test.imdatedomain;



import java.io.Serializable;


public class SqlFilter extends UserFilter implements Serializable {

	String sql;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}
