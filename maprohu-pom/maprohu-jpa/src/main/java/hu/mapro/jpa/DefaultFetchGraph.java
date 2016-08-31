package hu.mapro.jpa;

import java.util.Collection;

import com.google.common.collect.Lists;

public class DefaultFetchGraph<T> implements FetchGraph<T> {

	final Class<T> entityClass;
	final Collection<DefaultManyToOneFetch<T, ?>> manyToOne = Lists.newArrayList();
	final Collection<DefaultOneToManyFetch<T, ?>> oneToMany = Lists.newArrayList();
	final Collection<DefaultSubFetch<T, ?>> sub = Lists.newArrayList();
	
	public DefaultFetchGraph(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public Collection<? extends ManyToOneFetch<T, ?>> getManyToOne() {
		return manyToOne;
	}

	@Override
	public Collection<? extends OneToManyFetch<T, ?>> getOneToMany() {
		return oneToMany;
	}

	@SuppressWarnings("unchecked")
	public <P> DefaultFetchGraph<P> addManyToOne(String propertyName, Class<P> clazz) {
		for (DefaultManyToOneFetch<T, ?> f : manyToOne) {
			if (f.getProperty().getName().equals(propertyName)) {
				return (DefaultFetchGraph<P>) f.fetchGraph;
			}
		}
		
		DefaultFetchGraph<P> fg = new DefaultFetchGraph<P>(clazz);
		DefaultManyToOneProperty<T, P> p = new DefaultManyToOneProperty<T, P>(propertyName);
		DefaultManyToOneFetch<T, P> f = new DefaultManyToOneFetch<T, P>(fg, p);
		manyToOne.add(f);
		return fg;
	}
	
	@SuppressWarnings("unchecked")
	public <P> DefaultFetchGraph<P> addOneToMany(String propertyName, String inversePropertyName, Class<P> clazz) {
		for (DefaultOneToManyFetch<T, ?> f : oneToMany) {
			if (f.getProperty().getName().equals(propertyName)) {
				return (DefaultFetchGraph<P>) f.fetchGraph;
			}
		}
		
		DefaultFetchGraph<P> fg = new DefaultFetchGraph<P>(clazz);
		DefaultManyToOneProperty<P, T> ip = new DefaultManyToOneProperty<P, T>(inversePropertyName);
		DefaultOneToManyProperty<T, P> p = new DefaultOneToManyProperty<T, P>(propertyName, ip);
		DefaultOneToManyFetch<T, P> f = new DefaultOneToManyFetch<T, P>(fg, p);
		oneToMany.add(f);
		return fg;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <P> DefaultFetchGraph<P> addSub(Class<P> subClass) {
		for (DefaultSubFetch<T, ?> f : sub) {
			if (f.getChild().getEntityClass().equals(subClass)) {
				return (DefaultFetchGraph<P>) f.getChild();
			}
		}
		
		DefaultFetchGraph<P> fg = new DefaultFetchGraph<P>(subClass);
		sub.add(new DefaultSubFetch(this, fg));
		return fg;
	}
	
	
}
