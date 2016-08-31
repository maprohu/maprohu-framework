package hu.mapro.jpa.model.domain.server;

import hu.mapro.gwt.common.shared.FullTextSearch;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class FilterUtil {

	
	public static <R> Predicate trueFilter(
			From<?, R> from,
			CriteriaBuilder criteriaBuilder
	) {
		return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
	}

	
//	public static <R> Predicate fullText(
//			From<?, R> from,
//			CriteriaBuilder criteriaBuilder,
//			String queryString,
//			List<? extends ExpressionBase<R, String>> fields
//	) {
//		String[] parts = queryString.split("[\\s"+FullTextSearch.SEPARATOR+"]+");
//		
//		boolean matchBeginning = Character.isWhitespace(queryString.charAt(0));
//		
//		List<Predicate> predicates = Lists.newArrayList();
//		
//		for (String part : parts) {
//			
//			List<Predicate> partPredicates = Lists.newArrayList();
//			
//			for (ExpressionBase<R, String> vp : fields) {
//				
//				Expression<String> fieldExpression = criteriaBuilder.lower(
//						vp.createExpression(from)
//				);
//				
//				partPredicates.add(
//						criteriaBuilder.like(
//								fieldExpression,
//								(matchBeginning?"":"%")+part.toLowerCase()+"%"
//						)
//				);
//				
//				matchBeginning = false;
//				
//			}
//			
//			predicates.add(criteriaBuilder.or(partPredicates.toArray(new Predicate[0])));
//			
//		}
//		
//		
//		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//	}

	public static <R> Predicate fullText(
			CriteriaBuilder criteriaBuilder,
			String queryString,
			Collection<Expression<String>> fields
	) {
		String[] parts = queryString.split("[\\s"+Pattern.quote(FullTextSearch.SEPARATOR)+"]+");
		
		boolean matchBeginning = Strings.isNullOrEmpty(queryString) || !Character.isWhitespace(queryString.charAt(0));
		
		List<Predicate> predicates = Lists.newArrayList();
		
		for (String part : parts) {
			
			List<Predicate> partPredicates = Lists.newArrayList();
			
			for (Expression<String> fieldExpression : fields) {
				
				partPredicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(fieldExpression),
								(matchBeginning?"":"%")+part.toLowerCase()+"%"
						)
				);
				
				matchBeginning = false;
				
			}
			
			predicates.add(criteriaBuilder.or(partPredicates.toArray(new Predicate[0])));
			
		}
		
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
	
}
