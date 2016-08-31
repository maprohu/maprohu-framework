package hu.mapro.jpa.model.domain.server;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class ConstantItems extends Items<ConstantValue<?>> {

	<T> T constant(int index, Class<T> clazz) {
		return constant(items.get(index), clazz);
	}
	
	private static <T> T constant(ConstantValue<?> value, Class<T> clazz) {
		return clazz.cast(value.getValue());
	}
	
	private static <T> Iterable<T> constants(List<? extends ConstantValue<?>> value, final Class<T> clazz) {
		return Iterables.transform(value, new Function<ConstantValue<?>, T>() {
			@Override
			public T apply(ConstantValue<?> input) {
				return constant(input, clazz);
			}
		});
	}

	public List<? extends ConstantValue<?>> getConstants() {
		return getItems();
	}

	public void setConstants(List<? extends ConstantValue<?>> items) {
		setItems(items);
	}

}
