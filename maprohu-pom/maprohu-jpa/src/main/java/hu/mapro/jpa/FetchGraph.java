package hu.mapro.jpa;

import java.util.Collection;

public interface FetchGraph<T> {
	
	Class<T> getEntityClass();
	
	Collection<? extends ManyToOneFetch<T, ?>> getManyToOne();
	
	Collection<? extends OneToManyFetch<T, ?>> getOneToMany();
	
}
