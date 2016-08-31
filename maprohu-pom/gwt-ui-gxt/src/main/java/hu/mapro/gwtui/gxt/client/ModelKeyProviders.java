package hu.mapro.gwtui.gxt.client;

import hu.mapro.model.LongIdGetter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.data.shared.ModelKeyProvider;

public class ModelKeyProviders {

	public static final ModelKeyProvider<String> IDENTITY = new ModelKeyProvider<String>() {
		@Override
		public String getKey(String item) {
			return item;
		}
	};

	public static <T> ModelKeyProvider<T> from(final Function<T, String> function) {
		return new ModelKeyProvider<T>() {
			@Override
			public String getKey(T item) {
				return function.apply(item);
			}
		};
	}
	
	public static <T extends LongIdGetter> ModelKeyProvider<T> longId() {
		return new ModelKeyProvider<T>() {
			@Override
			public String getKey(T item) {
				return item.getId().toString();
			}
		};
	}

	
	public static <T> ModelKeyProvider<T> from(final ProvidesKey<T> providesKey) {
		final Map<Object, String> map = Maps.newHashMap();
		
		return new ModelKeyProvider<T>() {
			
			int counter = 0;
			
			@Override
			public String getKey(T item) {
				Object key = providesKey.getKey(item);
				
				if (!map.containsKey(key)) {
					map.put(key, Integer.toString(counter++));
				}
				
				return map.get(key);
			}
		};
	}
	
	public static <T> ModelKeyProvider<T> toStringProvider() {
		return new ModelKeyProvider<T>() {
			@Override
			public String getKey(T item) {
				if (item==null) return null;
				return item.toString();
			}
		};
	}
	
}
