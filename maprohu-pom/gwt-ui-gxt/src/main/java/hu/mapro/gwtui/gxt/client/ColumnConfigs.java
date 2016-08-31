package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.gxt.client.data.ValueProviders;
import hu.mapro.model.Setter;
import hu.mapro.model.meta.Field;
import hu.mapro.model.meta.HasLabel;

import com.google.common.base.Function;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public class ColumnConfigs {

	public static <T, F extends Field<T, V>&Function<? super T, V>&HasLabel, V> ColumnConfig<T, V> readOnly(final F field) {
		return readOnly(field, field.getName(), field.getLabel());
	}
	
	public static <T, F extends Field<T, V>&Function<? super T, V>&Setter<? super T, V>&HasLabel, V> ColumnConfig<T, V> readWrite(final F field) {
		return readWrite(field, field, field.getName(), field.getLabel());
	}
	
	public static <M, N> ColumnConfig<M, N> readWrite(
			Function<? super M, N> getter,
			Setter<? super M, N> setter,
			String path,
			String label
	) {
		ColumnConfig<M, N> columnConfig = new ColumnConfig<M, N>(
				ValueProviders.from(getter, setter, path)
		);
		columnConfig.setHeader(label);
		return columnConfig;
	}
	
	public static <M, N> ColumnConfig<M, N> readOnly(
			Function<? super M, N> getter,
			String path,
			String label
	) {
		ColumnConfig<M, N> columnConfig = new ColumnConfig<M, N>(
				ValueProviders.from(getter, path)
		);
		columnConfig.setHeader(label);
		return columnConfig;
	}
	
}
