package hu.mapro.model.analyzer.test.domain.imdate;


import java.io.Serializable;


public class Sql2Filter extends UserFilter  implements Serializable{

	String mainSql;
	String cacheSql;
	public String getMainSql() {
		return mainSql;
	}
	public void setMainSql(String mainSql) {
		this.mainSql = mainSql;
	}
	public String getCacheSql() {
		return cacheSql;
	}
	public void setCacheSql(String cacheSql) {
		this.cacheSql = cacheSql;
	}
	
}
