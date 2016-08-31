package hu.mapro.gwtui.gxt.client.edit;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Functions;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.browser.grid.TableWidthUnit;
import hu.mapro.gwtui.client.grid.GridConfigurating;
import hu.mapro.gwtui.gxt.client.data.ValueProviders;
import hu.mapro.jpa.model.domain.shared.enums.SortingDirection;
import hu.mapro.model.Setter;

import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent.StoreClearHandler;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public class GxtGridConfigurating<T> implements	GridConfigurating<T> {
	
	private final List<ColumnConfig<T, ?>> columnConfigs;
	private final ListStore<T> listStore;
	private final Handlers closeHandlers;

	public GxtGridConfigurating(List<ColumnConfig<T, ?>> columnConfigs,
			ListStore<T> listStore, Handlers closeHandlers) {
		super();
		this.columnConfigs = columnConfigs;
		this.listStore = listStore;
		this.closeHandlers = closeHandlers;
	}

	@Override
	public <V> GridColumnCustomizer<V> addColumn(
			final Function<? super T, ? extends ObservableValue<V>> getter,
			String path
	) {
		return createColumn(listStore, columnConfigs, closeHandlers, getter, path);
	}

	public static <T, V> GridColumnCustomizer<V> createColumn(
			final ListStore<T> listStore,
			final List<ColumnConfig<T, ?>> columnConfigs,
			final Handlers closeHandlers,
			final Function<? super T, ? extends ObservableValue<V>> getter, 
			String path
	) {
		ValueProvider<T, V> valueProvider = readOnlyValueProvider(getter, path);
		
		return addColumn(listStore, columnConfigs, closeHandlers, getter,
				valueProvider);
	}

	public static <T, V> GridColumnCustomizer<V> addColumn(
			final ListStore<T> listStore,
			final List<ColumnConfig<T, ?>> columnConfigs,
			final Handlers closeHandlers,
			final Function<? super T, ? extends ObservableValue<V>> getter,
			ValueProvider<T, V> valueProvider
	) {
		registerObservers(listStore, closeHandlers, getter);
		
		return createRegisteredColumn(
				listStore,
				columnConfigs,
				valueProvider
		);
	}

	public static <T, V> ValueProvider<T, V> readOnlyValueProvider(
			Function<? super T, ? extends ObservableValue<V>> getter,
			String path
	) {
		return ValueProviders.<T,V>from(
				com.google.common.base.Functions.compose(
						Functions.<V>getSupplier(), 
						getter
				), 
				path
		);
	}

	public static <T, V> ValueProvider<T, V> readWriteValueProvider(
			final Function<? super T, ? extends ObservableValue<V>> getter,
			String path
	) {
		return ValueProviders.<T,V>from(
				com.google.common.base.Functions.compose(
						Functions.<V>getSupplier(), 
						getter
				), 
				new Setter<T, V>() {
					@Override
					public void set(T object, V value) {
						getter.apply(object).set(value);
					}
				},
				path
		);
	}
	
	public static <T, V> void registerObservers(
			final ListStore<T> listStore,
			final Handlers closeHandlers,
			final Function<? super T, ? extends ObservableValue<V>> getter
	) {
		new Object() {
			
			final Map<String, HandlerRegistration> registrations = Maps.newHashMap();

			{
				listStore.addStoreAddHandler(new StoreAddHandler<T>() {
					@Override
					public void onAdd(StoreAddEvent<T> event) {
						addRegistrations(event.getItems());
					}
				});
				
				listStore.addStoreRemoveHandler(new StoreRemoveHandler<T>() {
					@Override
					public void onRemove(StoreRemoveEvent<T> event) {
						registrations.remove(listStore.getKeyProvider().getKey(event.getItem())).removeHandler();
					}
				});
				
				listStore.addStoreClearHandler(new StoreClearHandler<T>() {
					@Override
					public void onClear(StoreClearEvent<T> event) {
						clearRegistrations();
					}
				});
				
				listStore.addStoreDataChangeHandler(new StoreDataChangeHandler<T>() {
					@Override
					public void onDataChange(StoreDataChangeEvent<T> event) {
						clearRegistrations();
						addRegistrations(listStore.getAll());
					}
				});
				
			}

			public void clearRegistrations() {
				for (HandlerRegistration reg : registrations.values()) {
					reg.removeHandler();
				}
				registrations.clear();
			}

			public void addRegistrations(List<T> items) {
				for (final T t : items) {
					HandlerRegistration reg = getter.apply(t).register(new Action() {
						@Override
						public void perform() {
							listStore.update(t);
						}
					});
					
					String mk = listStore.getKeyProvider().getKey(t);
					
					if (registrations.containsKey(mk)) {
						registrations.get(mk).removeHandler();
					}
					
					registrations.put(mk, reg);
				}
			}
			
		};
	}

	protected static <T, V> GridColumnCustomizer<V> createRegisteredColumn(
			final ListStore<T> listStore,
			final List<ColumnConfig<T, ?>> columnConfigs,
			final ValueProvider<T, V> valueProvider
	) {
		final ColumnConfig<T, V> cc = createColumnConfig(valueProvider);
		return addColumnConfig(listStore, columnConfigs, valueProvider, cc);
	}

	public static <T, V> GridColumnCustomizer<V> addColumnConfig(
			final ListStore<T> listStore,
			final List<ColumnConfig<T, ?>> columnConfigs,
			final ValueProvider<T, V> valueProvider, 
			final ColumnConfig<T, V> cc
	) {
		columnConfigs.add(cc);
		return new GridColumnCustomizer<V>() {

			@Override
			public GridColumnCustomizer<V> setWidth(double value, TableWidthUnit unit) {
				switch (unit) {
				case PX:
					cc.setWidth((int) value);
					break;

				default:
					throw new RuntimeException("unimplemented width unit: " + unit);
				}
				return this;
			}

			@Override
			public GridColumnCustomizer<V> setCell(Cell<V> cell) {
				cc.setCell(cell);
				return this;
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public GridColumnCustomizer<V> sort(SortingDirection sortingDirection) {
				listStore.addSortInfo(
						new StoreSortInfo(
								valueProvider, 
								sortingDirection==SortingDirection.DESCENDING 
									? SortDir.DESC
									: SortDir.ASC
						)
				);
				return this;
			}

			@Override
			public GridColumnCustomizer<V> setLabel(String label) {
				cc.setHeader(label);
				return this;
			}

			@Override
			public GridColumnCustomizer<V> setVisible(boolean visible) {
				cc.setHidden(!visible);
				return this;
			}
		};
	}

	public static <T, V> ColumnConfig<T, V> createColumnConfig(
			final ValueProvider<T, V> valueProvider) {
		final ColumnConfig<T, V> cc = new ColumnConfig<T, V>(valueProvider);
		return cc;
	}

	@Override
	public Handlers closeHandlers() {
		return closeHandlers;
	}
}