package hu.mapro.jpa.model.server;

import hu.mapro.jpa.JpaUtils.CriteriaQuerySetup;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterConfigImmutable;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigImmutable;
import hu.mapro.jpa.model.domain.client.AutoBeans.SortingConfigImmutable;
import hu.mapro.jpa.model.domain.shared.FilterCollecting;
import hu.mapro.jpa.model.domain.shared.FilterRepository;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;

final public class ListConfigCriteriaQuerySetup<R> implements
		CriteriaQuerySetup<R> {
	
	final ListConfigImmutable listConfigImmutable;
	
	final Map<String, ExpressionBuilder<R, ?>> sortingExpressions;
	
	final FilterRepository filterRepository;
	
	final FullTextFilterBuilder<R> fullTextFilterBuilder;
	
	public ListConfigCriteriaQuerySetup(
			ListConfigImmutable listConfigImmutable,
			Map<String, ExpressionBuilder<R, ?>> sortingExpressions,
			FilterRepository filterRepository,
			FullTextFilterBuilder<R> fullTextFilterBuilder) {
		super();
		this.listConfigImmutable = listConfigImmutable;
		this.sortingExpressions = sortingExpressions;
		this.filterRepository = filterRepository;
		this.fullTextFilterBuilder = fullTextFilterBuilder;
	}
	
	@Override
	public void setup(
			final CriteriaBuilder criteriaBuilder,
			CriteriaQuery<R> query,
			final Root<R> root
	) {
		if (listConfigImmutable==null) return;
		
		
		List<Order> order = Lists.newArrayList();
		
		for (SortingConfigImmutable sortingConfig : listConfigImmutable.getSortingConfigs()) {
			
			Expression<?> exp = sortingExpressions.get(sortingConfig.getFieldId()).buildExpression(criteriaBuilder, root);
			
			order.add(
					sortingConfig.getDirection() == SortingDirection.ASCENDING 
					?
					criteriaBuilder.asc(exp)
					:
					criteriaBuilder.desc(exp)
			);
			
		}
		
		query.orderBy(order);

		// TODO add the filter
		
		final List<Predicate> predicates = Lists.newArrayList();
		
		FilterCollecting filterCollecting = new FilterCollecting() {

			@Override
			public void fullText(String queryString) {
				predicates.add(
						fullTextFilterBuilder.buildFullText(root, criteriaBuilder, queryString)
				);
			}
			
		};
		
		for (FilterConfigImmutable filterConfig : listConfigImmutable.getFilterConfigs()) {
			filterRepository.getFilter(filterConfig.getFilterId()).setupFilter(
					filterCollecting,
					filterConfig.getParameters()
			);
		}
		
		
		query.where(
				predicates.toArray(new Predicate[0])
		);
		
	}

	
}