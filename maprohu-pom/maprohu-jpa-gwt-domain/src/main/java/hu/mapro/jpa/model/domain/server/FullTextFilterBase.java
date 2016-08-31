package hu.mapro.jpa.model.domain.server;

import hu.mapro.gwt.common.shared.FullTextSearch;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import com.google.common.collect.Lists;

abstract public class FullTextFilterBase<R> implements FilterBase<R> {

	String queryString;
	
	List<? extends ExpressionBase<R, String>> fields = Lists.newArrayList();
	
	@Override
	public Predicate createPredicate(
			From<?, R> from,
			CriteriaBuilder criteriaBuilder
	) {
		String[] parts = queryString.split("[\\s"+FullTextSearch.SEPARATOR+"]+");
		
		boolean matchBeginning = Character.isWhitespace(queryString.charAt(0));
		
		List<Predicate> predicates = Lists.newArrayList();
		
		for (String part : parts) {
			
			List<Predicate> partPredicates = Lists.newArrayList();
			
			for (ExpressionBase<R, String> vp : fields) {
				
				Expression<String> fieldExpression = criteriaBuilder.lower(
						vp.createExpression(from)
				);
				
				partPredicates.add(
						criteriaBuilder.like(
								fieldExpression,
								(matchBeginning?"":"%")+part.toLowerCase()+"%"
						)
				);
				
				matchBeginning = false;
				
			}
			
			predicates.add(criteriaBuilder.or(partPredicates.toArray(new Predicate[0])));
			
		}
		
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<? extends ExpressionBase<R, String>> getFields() {
		return fields;
	}

	public void setFields(List<? extends ExpressionBase<R, String>> fields) {
		this.fields = fields;
	}
	
}
