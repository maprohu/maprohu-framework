package hu.mapro.gwtui.client.grid;


import hu.mapro.jpa.model.domain.client.FetchPlanRoute;
import hu.mapro.jpa.model.domain.client.FetchPlanRoutes;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HasLabel;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class ReferenceColumn<T, Q> extends MultiColumn<T, Q> {

	private static final Joiner PATH_JOINER = Joiner.on('.').skipNulls();
	private static final Joiner LABEL_JOINER = Joiner.on(" of ").skipNulls();

	
	Function<T, Q> getter;
	String path;
	String label;
	ColumnCollector<T> columnCollector;
	FetchPlanRoute<T, Q> fetchPlanRoute;
	boolean stopped;
	Set<?> stoppers;
	
	
	public <R> ReferenceColumn(
		final MultiColumn<T, R> root,
		Function<? super R, Q> getter,
		final String path,
		String label,
		Set<?> stoppers
	) {
		
		this.getter = Functions.compose(getter, root.getGetter());
		this.path = PATH_JOINER.join(root.getPath(), path);
		this.label = LABEL_JOINER.join(label, root.getLabel());
		this.columnCollector = root.getColumnCollector();
		this.fetchPlanRoute = FetchPlanRoutes.compose(root.getFetchPlanRoute(), path); 
		
		
		this.stopped = root.getStopped() || !Sets.intersection(stoppers, root.getStoppers()).isEmpty();
		this.stoppers = ImmutableSet.builder().addAll(root.getStoppers()).addAll(stoppers).build();
		
	}
	
	@Override
	Function<T, Q> getGetter() {
		return getter;
	}

	@Override
	String getPath() {
		return path;
	}

	@Override
	String getLabel() {
		return label;
	}

	@Override
	FetchPlanRoute<T, Q> getFetchPlanRoute() {
		return fetchPlanRoute;
	}
	
	@Override
	ColumnCollector<T> getColumnCollector() {
		return columnCollector;
	}
	
	public static <T, V, Q> ReferenceColumn<T, V> create(
			MultiColumn<T, Q> ref,
			Function<? super Q, V> getter,
			String path,
			String label,
			Set<?> stoppers
	) {
		return new ReferenceColumn<T, V>(ref, getter, path, label, stoppers);
	}
	
	public static <T, V, Q, F extends Field<? super Q, V>&Function<? super Q, V>&HasLabel> ReferenceColumn<T, V> create(
			MultiColumn<T, Q> ref,
			F field
	) {
		return create(ref, field, ImmutableSet.of(field));
	}

	public static <T, V, Q, F extends Field<? super Q, V>&Function<? super Q, V>&HasLabel> ReferenceColumn<T, V> create(
			MultiColumn<T, Q> ref,
			F field, 
			Set<?> stoppers
	) {
		return new ReferenceColumn<T, V>(ref, field, field.getName(), field.getLabel(), stoppers);
	}
	
	public void all(
			MultiColumnDelegator<Q> delegator
	) {
		if (!getStopped()) {
			delegator.delegate(this);
		}
	}

	@Override
	Set<?> getStoppers() {
		return stoppers;
	}

	@Override
	boolean getStopped() {
		return stopped;
	}
	
	
}