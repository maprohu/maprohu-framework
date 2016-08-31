package hu.mapro.model;


public interface LongId extends LongIdGetter {

	@MaproModelExclude
	void setId(Long id);
	
	@MaproModelExclude
	void setVersion(Integer version);
	
}
