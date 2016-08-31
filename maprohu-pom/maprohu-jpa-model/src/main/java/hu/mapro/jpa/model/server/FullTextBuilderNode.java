package hu.mapro.jpa.model.server;

import java.util.Collection;
import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FullTextBuilderNode<T> {

	final From<?, T> from;
	final Collection<Expression<String>> searchFields;
	final Map<String, FullTextBuilderNode<?>> cache = Maps.newHashMap();
	
	public FullTextBuilderNode(From<?, T> from,
			Collection<Expression<String>> searchFields) {
		super();
		this.from = from;
		this.searchFields = searchFields;
	}

	@SuppressWarnings("unchecked")
	public <P> FullTextBuilderNode<P> join(String path) {
		FullTextBuilderNode<P> target = (FullTextBuilderNode<P>) cache.get(path);
		if (target==null) {
			target = new FullTextBuilderNode<P>(from.<T, P>join(path, JoinType.LEFT), searchFields);
			cache.put(path, target);
		}
		return target;
	}
	
	public void search(String field) {
		searchFields.add(from.<String>get(field));
	}
	
	public void searchCast(String field) {
		searchFields.add(from.get(field).as(String.class));
	}
	
	public Collection<Expression<String>> getSearchFields() {
		return searchFields;
	}
	
	public static <T> FullTextBuilderNode<T> root(From<?, T> from) {
		return new FullTextBuilderNode<T>(from, Lists.<Expression<String>>newArrayList());
	}
	
}
