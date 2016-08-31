package hu.mapro.jpa.model.server;

import hu.mapro.jpa.model.domain.server.FilterUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public abstract class DefaultFullTextFilterBuilder<T> implements FullTextFilterBuilder<T> {

	@Override
	public Predicate buildFullText(From<?, T> from, CriteriaBuilder criteriaBuilder, String queryString) {
		FullTextBuilderNode<T> root = FullTextBuilderNode.root(from);
		buildFields(root);
		return FilterUtil.fullText(criteriaBuilder, queryString, root.getSearchFields());
	}

	abstract public void buildFields(FullTextBuilderNode<T> root);

	
}
