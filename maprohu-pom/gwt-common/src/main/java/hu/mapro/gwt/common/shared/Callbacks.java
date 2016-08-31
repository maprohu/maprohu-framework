package hu.mapro.gwt.common.shared;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.model.Consumer;
import hu.mapro.model.Setter;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;

public class Callbacks<T> {

	List<Callback<T>> handlers = Lists.newArrayList();
	
	public HandlerRegistration add(Callback<T> action) {
		return HandlerRegistrations.addItem(handlers, action);
	}
	
	public boolean remove(Callback<T> o) {
		return handlers.remove(o);
	}

	public void fire(T t) {
		for (Callback<T> a : ImmutableList.copyOf(handlers)) {
			a.onResponse(t);
		}
	}
	
	public static <T> Callbacks<T> newInstance() {
		return new Callbacks<T>();
	}
	
	public void clear() {
		handlers.clear();
	}
	
	final Callback<T> callback = new Callback<T>() {
		@Override
		public void onResponse(T value) {
			fire(value);
		}
	};
	
	public Callback<T> callback() {
		return callback;
	}

	public static <T> Callback<T> of(final Consumer<T> wrapper) {
		return new Callback<T>() {
			@Override
			public void onResponse(T value) {
				wrapper.set(value);
			}
		};
	}
	
	public static <T> Callback<T> none() {
		return new Callback<T>() {
			@Override
			public void onResponse(T value) {
			}
		};
	}

	public static <T> Callback<T> of(
			final Callback<T> c1,
			final Callback<T> c2
	) {
		return new Callback<T>() {
			@Override
			public void onResponse(T value) {
				c1.onResponse(value);
				c2.onResponse(value);
			}
		};
	}
	
	public static <T, V> Callback<T> setter(
			final Setter<? super T, V> setter,
			final V value
	) {
		return new Callback<T>() {
			@Override
			public void onResponse(T object) {
				setter.set(object, value);
			}
		};
	}
	
}