package hu.mapro.gwtui.client.grid;


import hu.mapro.jpa.model.domain.client.FetchPlanRoute;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HasLabel;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

public abstract class MultiColumn<R, T> {

	abstract Function<R, T> getGetter();
	abstract String getPath();
	abstract String getLabel();
	abstract FetchPlanRoute<R, T> getFetchPlanRoute();
	abstract ColumnCollector<R> getColumnCollector();
	abstract Set<?> getStoppers();
	abstract boolean getStopped();
	
	public <V> DisplayColumn<R, V> display(
			Function<? super T, V> getter,
			String path,
			String label,
			Set<?> stoppers
	) {
		return DisplayColumn.create(this, getter, path, label, stoppers);
	}
	
	public <V, Q, F extends Field<? super T, V>&Function<? super T, V>&HasLabel> DisplayColumn<R, V> display(
			F field
	) {
		return DisplayColumn.create(this, field);
	}

	public <V> ReferenceColumn<R, V> reference(
			Function<? super T, V> getter,
			String path,
			String label,
			Set<?> stoppers
	) {
		return ReferenceColumn.create(this, getter, path, label, stoppers);
	}
	
	public <V, Q, F extends Field<? super T, V>&Function<? super T, V>&HasLabel> ReferenceColumn<R, V> reference(
			F field
	) {
		return ReferenceColumn.create(this, field);
	}
	
	public <V, Q, F extends Field<? super T, V>&Function<? super T, V>&HasLabel> ReferenceColumn<R, V> referenceForce(
			F field
	) {
		return ReferenceColumn.create(this, field, ImmutableSet.of());
	}
	
	
	
}