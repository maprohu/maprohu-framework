package hu.mapro.gwtui.client.grid;

import hu.mapro.jpa.model.domain.client.FetchPlanFollower;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import hu.mapro.jpa.model.domain.client.FetchPlanRoute;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;
import hu.mapro.model.meta.ComplexType;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class ColumnRoot<T> extends MultiColumn<T, T> {

	final ComplexType<T> complexType;
	final ColumnCollector<T> collumnCollector;

	public ColumnRoot(ComplexType<T> complexType,
			ColumnCollector<T> collumnCollector) {
		super();
		this.complexType = complexType;
		this.collumnCollector = collumnCollector;
	}

	final List<DisplayColumn<T, ?>> columns = Lists.newArrayList();
	//final List<SortingColumn<?>> sortings = Lists.newArrayList();
	final List<FetchPlanNavigator<T>> fetchPlanNavigators = Lists.newArrayList();
	final Set<?> stoppers = ImmutableSet.of();
	
	class SortingColumn<V> {
		DisplayColumn<T, V> column;
		SortingDirection sortingDirection;
		public SortingColumn(DisplayColumn<T, V> column,
				SortingDirection sortingDirection) {
			super();
			this.column = column;
			this.sortingDirection = sortingDirection;
		}
	}
	
	@Override
	Function<T, T> getGetter() {
		return Functions.identity();
	}

	@Override
	String getPath() {
		return null;
	}

	@Override
	String getLabel() {
		return null;
	}

	@Override
	FetchPlanRoute<T, T> getFetchPlanRoute() {
		return new FetchPlanRoute<T, T>() {
			@Override
			public FetchPlanFollower<T> navigateFrom(FetchPlanFollower<T> origin) {
				return origin;
			}
		};
	}
	
	@Override
	ColumnCollector<T> getColumnCollector() {
		return collumnCollector;
	}
	
//	@Override
//	public <V> void addOrder(DisplayColumn<T, V> display,
//			SortingDirection direction) {
//		sortings.add(new SortingColumn<V>(display, direction));
//		
//	}
	
//	@Override
//	public <V> GridColumnCustomizer<V> add(DisplayColumn<T, V> columnConfig) {
//		return columns.add(columnConfig);
//	}

	public static <T> ColumnRoot<T> create(ComplexType<T> complexType, ColumnCollector<T> columnCollector) {
		return new ColumnRoot<T>(complexType, columnCollector);
	}

	@Override
	Set<?> getStoppers() {
		return stoppers;
	}

	@Override
	boolean getStopped() {
		return false;
	}


//	@Override
//	public void add(FetchPlanNavigator<T> navigator) {
//		fetchPlanNavigators.add(navigator);
//	}
//
//	@Override
//	public FetchPlan createFetchPlan(Factory factory) {
//		FetchPlan fetchPlan = factory.fetchPlan();
//		processFetchPlan(fetchPlan, factory);
//		return fetchPlan;
//	}


	
}