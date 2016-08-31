package hu.mapro.gwt.common.shared;

import hu.mapro.model.Setter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class ObservableObjectWrapper<T> {
	
	public static final String TAG_NAME = ObservableObjectWrapper.class.getName();
	
	T wrapped;
	
	protected ObservableObjectWrapper(T wrapped) {
		super();
		this.wrapped = wrapped;
	}
	
	public class SetValue<V> extends CollectionValue<V, Set<V>> implements ObservableSet<V> {
		final Set<V> immutable = new ForwardingSet<V>() {
			@Override
			protected Set<V> delegate() {
				return getter.apply(wrapped);
			}
			
			@Override
			public boolean add(V arg0) {
				throw new UnsupportedOperationException();
			};
			
			@Override
			public boolean addAll(Collection<? extends V> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void clear() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean remove(Object object) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean removeAll(Collection<?> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean retainAll(Collection<?> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public Iterator<V> iterator() {
				final Iterator<V> iterator = super.iterator();
				return new ForwardingIterator<V>() {
					@Override
					protected Iterator<V> delegate() {
						return iterator;
					}
					
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
			
			
		};

		public SetValue(Function<? super T, ? extends Set<V>> getter, ProvidesKey<? super V> modelKeyProvider) {
			super(getter, modelKeyProvider);
		}

		@Override
		public Set<V> get() {
			return immutable;
		}
		
		@Override
		public void add(final V item) {
			Object addKey = modelKeyProvider.getKey(item);
			
			Iterator<V> iterator = getter.apply(wrapped).iterator();
			
			while (iterator.hasNext()) {
				if (Objects.equal(addKey, modelKeyProvider.getKey(iterator.next()))) {
					return;
				}
			}

			getter.apply(wrapped).add(item);
			fireAdd(item);
		}
		
	}

	class Value<V> implements ObservableValue<V>, Validatable {

		final Function<? super T, V> getter;
		final Setter<? super T, V> setter;
		final Flag readOnlyFlag;
		final Predicate<V> setValueAccess;
		final List<String> validationErrors = Lists.newArrayList();
		
		public Value(Function<? super T, V> getter, Setter<? super T, V> setter) {
			this(getter, setter, Flag.FALSE);
		}

		public Value(Function<? super T, V> getter,
				Setter<? super T, V> setter, Flag readOnlyFlag) {
			this(getter, setter, readOnlyFlag, Predicates.<V>alwaysTrue());
		}

		public Value(Function<? super T, V> getter,
				Setter<? super T, V> setter, 
				Flag readOnlyFlag,
				Predicate<V> setValueAccess
		) {
			super();
			this.getter = getter;
			this.setter = setter;
			this.readOnlyFlag = readOnlyFlag;
			this.setValueAccess = setValueAccess;
		}

		@Override
		public void set(V object) {
			if (!setValueAccess.apply(object)) {
				throw new CannotSetValueException();
			}
			setter.set(wrapped, object);
			handlers.fire();
		}

		@Override
		public V get() {
			return getter.apply(wrapped);
		}

		@Override
		public HandlerRegistration register(Action action) {
			return handlers.add(action);
		}
		
		final Handlers handlers = Handlers.newInstance();

		@Override
		public boolean isReadOnly() {
			return readOnlyFlag.isSet();
		}

		final Handlers validationStatusChangeHandlers = Handlers.newInstance();
		final Handlers focusRequestHandlers = Handlers.newInstance();
		
//		final HandlerSupport<ValidationHandler> validationHandlers = HandlerSupports.of(); 
//		
//		@Override
//		public HandlerRegistration addValidationHandler(
//				ValidationHandler validationHandler) {
//			return validationHandlers.addHandler(validationHandler);
//		}
		
		public void markInvalid(final String message) {
			validationErrors.add(message);
			
			validationStatusChangeHandlers.fire();
			
//			validationHandlers.fire(new Callback<ValidationHandler>() {
//				@Override
//				public void onResponse(ValidationHandler value) {
//					value.markInvalid(message);
//				}
//			});
		}

		public void display() {
			ObservableObjectWrapper.this.focusRequestHandlers.fire();
			
			focusRequestHandlers.fire();
//			
//			validationHandlers.fire(new Callback<ValidationHandler>() {
//				@Override
//				public void onResponse(ValidationHandler value) {
//					value.display();
//				}
//			});
		}

		@Override
		public List<String> getValidationErrors() {
			return validationErrors;
		}

		@Override
		public HandlerRegistration addValidationStatusChangeHandler(
				Action action) {
			return validationStatusChangeHandlers.register(action);
		}

		@Override
		public HandlerRegistration addFocusRequestHandler(Action action) {
			return focusRequestHandlers.register(action);
		}

		@Override
		public void clearInvalid() {
			validationErrors.clear();
			validationStatusChangeHandlers.fire();
		}
		
	}
	
	final List<Value<?>> values = Lists.newArrayList();
	final Map<String, Value<?>> valueMap = Maps.newHashMap();
	final List<CollectionValue<?, ? extends Collection<?>>> collectionValues = Lists.newArrayList();
	final Handlers focusRequestHandlers = Handlers.newInstance();
	
	public <V> ObservableValue<V> value(
			final Function<? super T, V> getter,
			final Setter<? super T, V> setter
	) {
		return value(getter, setter, Flag.FALSE, Predicates.<V>alwaysTrue());
	}
	
//	public <V> ObservableValue<V> value(
//			final Function<? super T, V> getter,
//			final Setter<? super T, V> setter,
//			final Predicate<? super T> readOnlyPredicate
//	) {
//		return value(getter, setter, new Flag() {
//			@Override
//			public boolean isSet() {
//				return readOnlyPredicate.apply(wrapped);
//			}
//		}, Predicates.<V>alwaysTrue());
//	}

	public <V> ObservableValue<V> value(
			final String name,
			final Function<? super T, V> getter,
			final Setter<? super T, V> setter,
			final ObservableValueAccessControl<? super T, V> accessControl
	) {
		Value<V> value = value(getter, setter, new Flag() {
			@Override
			public boolean isSet() {
				return accessControl.isReadOnly(wrapped);
			}
		}, new Predicate<V>() {
			@Override
			public boolean apply(V input) {
				return accessControl.canSetValue(wrapped, input);
			}
		});
		valueMap.put(name, value);
		return value;
	}
	
	public <V> Value<V> value(
			final Function<? super T, V> getter,
			final Setter<? super T, V> setter,
			Flag readOnlyFlag,
			Predicate<V> setValueAccess
	) {
		Value<V> value = new Value<V>(getter, setter, readOnlyFlag, setValueAccess);
		values.add(value);
		return value;
	}
	
	public <V> ObservableValue<V> value(
			String name,
			final Function<? super T, V> getter
	) {
		Value<V> value = new Value<V>(getter, new Setter<T, V>() {
			@Override
			public void set(T object, V value) {
				throw new RuntimeException("read only");
			}
		});
		values.add(value);
		valueMap.put(name, value);
		return value;
	}
	
	abstract class CollectionValue<V, C extends Collection<V>> implements ObservableCollection<V, C> {
		
		final Function<? super T, ? extends C> getter;
		final ProvidesKey<? super V> modelKeyProvider;

		public CollectionValue(Function<? super T, ? extends C> getter,
				ProvidesKey<? super V> modelKeyProvider) {
			super();
			this.getter = getter;
			this.modelKeyProvider = modelKeyProvider;
		}

		final HandlerSupport<ObservableListHandler<V>> collectionHandlers = HandlerSupports.of();
		
		@Override
		public HandlerRegistration register(
				final ObservableCollectionHandler<V> handler) {
			return collectionHandlers.addHandler(new ObservableListHandler<V>() {

				@Override
				public void onAdd(V object) {
					handler.onAdd(object);
				}

				@Override
				public void onRemove(V object) {
					handler.onRemove(object);
				}

				@Override
				public void onInsert(V object, int index) {
					onAdd(object);
				}

				@Override
				public void onReplaceAll() {
					handler.onReplaceAll();
				}
			});
		}

		protected void fireAdd(final V item) {
			collectionHandlers.fire(new Callback<ObservableListHandler<V>>() {
				@Override
				public void onResponse(ObservableListHandler<V> handler) {
					handler.onAdd(item);
				}
			});
		}

		@Override
		public void remove(final V item) {
			Object removeKey = modelKeyProvider.getKey(item);
			
			Iterator<V> iterator = getter.apply(wrapped).iterator();
			
			while (iterator.hasNext()) {
				if (Objects.equal(removeKey, modelKeyProvider.getKey(iterator.next()))) {
					iterator.remove();
					fireRemove(item);
					return;
				}
			}
		}

		public void fireRemove(final V item) {
			collectionHandlers.fire(new Callback<ObservableListHandler<V>>() {
				@Override
				public void onResponse(ObservableListHandler<V> handler) {
					handler.onRemove(item);
				}
			});
		}

		@Override
		public void replaceAll(Collection<V> items) {
			getter.apply(wrapped).clear();
			getter.apply(wrapped).addAll(items);
			collectionHandlers.fire(new Callback<ObservableListHandler<V>>() {
				@Override
				public void onResponse(ObservableListHandler<V> handler) {
					handler.onReplaceAll();
				}
			});
		}
		
		public void swap(T oldWrapped) {
			Collection<V> oldCollection = getter.apply(oldWrapped);
			Collection<V> newCollection = getter.apply(wrapped);
			
			// TODO check why it can be null and what to do about it
			if (oldCollection==null || newCollection==null) return;

			ImmutableMap<Object, V> oldKeyMap = Maps.uniqueIndex(oldCollection, Functions.from(modelKeyProvider));
			ImmutableMap<Object, V> newKeyMap = Maps.uniqueIndex(newCollection, Functions.from(modelKeyProvider));
			
			for (V oldValue : oldCollection) {
				Object oldKey = modelKeyProvider.getKey(oldValue);
				if (!newKeyMap.containsKey(oldKey)) {
					fireRemove(oldValue);
				}
			}
			
			for (V newValue : newCollection) {
				if (!oldKeyMap.containsKey(modelKeyProvider.getKey(newValue))) {
					fireAdd(newValue);
				} else {
					V oldValue = oldKeyMap.get(modelKeyProvider.getKey(newValue));
					ObservableSwappable observableSwappable = (ObservableSwappable)AutoBeanUtils.getAutoBean(oldValue).getTag(TAG_NAME);
					if (observableSwappable!=null) {
						observableSwappable.swap(newValue);
					}
				}
			}
			
		}
		
	}
	
	public <V> ObservableSet<V> set(
			final Function<? super T, ? extends Set<V>> getter,
			ProvidesKey<? super V> modelKeyProvider
	) {
		SetValue<V> result = new SetValue<V>(getter, modelKeyProvider);
		collectionValues.add(result);
		return result;
	}
	
	class ListValue<V> extends CollectionValue<V, List<V>> implements ObservableList<V> {

		public ListValue(
				Function<? super T, ? extends List<V>> getter,
						ProvidesKey<? super V> modelKeyProvider
		) {
			super(getter, modelKeyProvider);
		}

		final List<V> immutable = new ForwardingList<V>() {
			@Override
			protected List<V> delegate() {
				return getter.apply(wrapped);
			}
			
			@Override
			public boolean add(V arg0) {
				throw new UnsupportedOperationException();
			};
			
			@Override
			public boolean addAll(Collection<? extends V> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void clear() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean remove(Object object) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean removeAll(Collection<?> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean retainAll(Collection<?> collection) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void add(int index, V element) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public boolean addAll(int index, java.util.Collection<? extends V> elements) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public Iterator<V> iterator() {
				final Iterator<V> iterator = super.iterator();
				return new ForwardingIterator<V>() {
					@Override
					protected Iterator<V> delegate() {
						return iterator;
					}
					
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
			
			
		};
		
		
		@Override
		public List<V> get() {
			return immutable;
		}

		@Override
		public HandlerRegistration register(ObservableListHandler<V> handler) {
			return collectionHandlers.addHandler(handler);
		}

		@Override
		public void insert(final V item, final int index) {
			getter.apply(wrapped).add(index, item);
			collectionHandlers.fire(new Callback<ObservableListHandler<V>>() {
				@Override
				public void onResponse(ObservableListHandler<V> handler) {
					handler.onInsert(item, index);
				}
			});
		}
		
		@Override
		public void add(final V item) {
			getter.apply(wrapped).add(item);
			fireAdd(item);
		}
		
		
	}

	public <V> ObservableList<V> list(
			final Function<? super T, ? extends List<V>> getter,
					ProvidesKey<? super V> modelKeyProvider
	) {
		ListValue<V> listValue = new ListValue<V>(getter, modelKeyProvider);
		collectionValues.add(listValue);
		return listValue;
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static <T> ObservableObjectWrapper<T> autobean(
//			T wrapped
//	) {
//		AutoBean<T> autobean = AutoBeanUtils.getAutoBean(wrapped);
//		
//		return (ObservableObjectWrapper) autobean.getTag(TAG_NAME);
//		
//	}

	@SuppressWarnings("unchecked")
	public static <T> ObservableObjectWrapper<T> autobean(
			T wrapped,
			Function<? super T, ? extends HasObservableObjectWrapper> wrapping
	) {
		return (ObservableObjectWrapper<T>) Autobeans.getOrCreateTag(wrapped, TAG_NAME, wrapping).getObservableObjectWrapper();
	}
	
	
//	@SuppressWarnings({ "unchecked"})
//	public static <T, W extends ObservableObjectWrapper<? extends T>> W autobean(
//			T wrapped,
//			Function<T, W> wrapping
//	) {
//		AutoBean<T> autobean = AutoBeanUtils.getAutoBean(wrapped);
//		
//		W wrapper = (W) autobean.getTag(TAG_NAME);
//		
//		if (wrapper==null) {
//			wrapper = wrapping.apply(wrapped);
//			autobean.setTag(TAG_NAME, wrapper);
//		}
//		
//		return wrapper;
//	}

	public static <T> ObservableObjectWrapper<T> of(T wrapped) {
		return new ObservableObjectWrapper<T>(wrapped);
	}

	@SuppressWarnings("unchecked")
	public void swap(Object newWrapped) {
		T oldWrapped = wrapped;
		wrapped = (T) newWrapped;

		// TODO recurse into properties
		
		for (Value<?> value : values) {
			Object oldValue = value.getter.apply(oldWrapped);
			Object newValue = value.getter.apply(wrapped);
			
			if (!Objects.equal(oldValue, newValue)) {
				value.handlers.fire();
			}
		}
		
		for (CollectionValue<?, ? extends Collection<?>> cv : collectionValues) {
			cv.swap(oldWrapped);
		}
		
		// TODO collection properties
		
		AutoBeanUtils.getAutoBean(newWrapped).setTag(TAG_NAME, AutoBeanUtils.getAutoBean(oldWrapped).getTag(TAG_NAME));
	}
	
//	public void markInvalid(
//			String path,
//			String message
//	) {
//		Value<?> value = valueMap.get(path);
//		
//		if (value!=null) {
//			value.markInvalid(message);
//		}
//	}
//	
//	public static <T> void markInvalid(
//			T object,
//			String propertyName,
//			String message
//	) {
//		getValidatable(object, propertyName).markInvalid(message);
//	}

	public static <T> Validatable getValidatable(
			T object,
			String propertyName
	) {
		ValidatableByName o = Autobeans.getTag(object, TAG_NAME);
		
		if (o!=null) {
			return o.getValidatable(propertyName);
		} else {
			return Validatable.FAKE;
		}
	}

	public Validatable getValidatable(
			String propertyName
	) {
		return valueMap.get(propertyName);
	}

	public Handlers getFocusRequestHandlers() {
		return focusRequestHandlers;
	}

	public T getWrapped() {
		return wrapped;
	}

	
}
