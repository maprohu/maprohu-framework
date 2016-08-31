package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.gxt.client.data.ValueProviders;
import hu.mapro.model.Getter;
import hu.mapro.model.meta.Field;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

public class StoreSortInfos {

	public static <T, F extends Field<T, V>&Getter<? super T, V>, V extends Comparable<V>> StoreSortInfo<T> fromField(
			final F field
	) {
		return fromField(field, SortDir.ASC);
	}
	
	public static <T, F extends Field<T, V>&Getter<? super T, V>, V extends Comparable<V>> StoreSortInfo<T> fromField(
			final F field,
			SortDir sortDir
	) {
		return new StoreSortInfo<T>(
				ValueProviders.fieldValueProvider(field),
				sortDir
		);
	}	
	
}
