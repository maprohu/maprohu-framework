package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.UncachedFullTextComplexField;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;
import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;
import hu.mapro.jpa.model.domain.client.FilterConfigProxies;
import hu.mapro.jpa.model.domain.client.ListConfigBuilder;
import hu.mapro.jpa.model.domain.shared.FilterRepository.FilterItem;
import hu.mapro.jpa.model.domain.shared.FullTextFilterType;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class GxtUncachedFullTextComplexField implements UncachedFullTextComplexField, IsWidget {

	final private SimpleContainer container = new SimpleContainer() {
		{
			cacheSizes = false;
		}
	};

	@Override
	public Widget asWidget() {
		return container;
	}

	
	
	final DefaultUiMessages defaultUiMessages;
	final WidgetContext widgetContext;
	
	public GxtUncachedFullTextComplexField(DefaultUiMessages defaultUiMessages,
			WidgetContext widgetContext) {
		super();
		this.defaultUiMessages = defaultUiMessages;
		this.widgetContext = widgetContext;
	}


	@Override
	public <V> HandlerRegistration bind(ObservableValue<V> value,
			ComplexEditing editing, UncachedClientStore<V> clientStore,
			ProvidesKey<? super V> modelKeyProvider,
			Function<? super V, String> labelProvider,
			Function<? super V, String> queryStringProvider,
			FilterItem<? extends FullTextFilterType> filter
	) {
		GxtUncachedComplexFreeTextField<V> impl = new GxtUncachedComplexFreeTextField<V>(value, editing, clientStore, modelKeyProvider, labelProvider, queryStringProvider, filter);
		HandlerRegistration hr = impl.createWidget();
		container.setWidget(impl.combo);
		GxtUtil.forceLayout(container);
		return hr;
	}
	
	private static final String ITEM_ID_PREFIX = "ITEM_";
	private static final String MORE_ID = "MORE";

	
	class GxtUncachedComplexFreeTextField<V> {

		final ObservableValue<V> value;
		final ComplexEditing editing; 
		final UncachedClientStore<V> clientStore;
		final ModelKeyProvider<? super V> modelKeyProvider;
		final Function<? super V, String> labelProvider;
		final Function<? super V, String> queryStringProvider;
		final FilterItem<? extends FullTextFilterType> filter;
		final int maxResults = 10;
		private ComboBox<Item> combo;
		
		public GxtUncachedComplexFreeTextField(ObservableValue<V> value,
				ComplexEditing editing, UncachedClientStore<V> clientStore,
				ProvidesKey<? super V> modelKeyProvider,
				Function<? super V, String> labelProvider,
				Function<? super V, String> queryStringProvider,
				FilterItem<? extends FullTextFilterType> filter) {
			super();
			this.value = value;
			this.editing = editing;
			this.clientStore = clientStore;
			this.modelKeyProvider = ModelKeyProviders.from(modelKeyProvider);
			this.labelProvider = labelProvider;
			this.queryStringProvider = queryStringProvider;
			this.filter = filter;
		}

		public class HackCell extends ComboBoxCell<Item> {
			HackCell(ListStore<Item> store,
					LabelProvider<Item> labelProvider,
					ListView<Item, ?> view) {
				super(store, labelProvider, view);
			}
			
			public void doQuery(com.google.gwt.cell.client.Cell.Context context, com.sencha.gxt.core.client.dom.XElement parent, com.google.gwt.cell.client.ValueUpdater<GxtUncachedComplexFreeTextField<V>.Item> updater, GxtUncachedComplexFreeTextField<V>.Item value, String query, boolean force) {
				clearCache();
				super.doQuery(context, parent, updater, value, query, force);
			}
			
			private void clearCache() {
				lastQuery = null;
			}
			
		}

		abstract class Item {
			
			String id;
			String label;
			String query;
			
			public Item(String id, String label, String query) {
				super();
				this.id = id;
				this.label = label;
				this.query = query;
			}

			void setValue() {
			}

			public void onClick(BeforeSelectionEvent<Item> event) {
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public boolean equals(Object obj) {
				return 
						obj!=null
						&& 
						(obj instanceof GxtUncachedFullTextComplexField.GxtUncachedComplexFreeTextField.Item)
						&&
						Objects.equal(id, ((Item)obj).id);
			}
			
			@Override
			public int hashCode() {
				return Objects.hashCode(id);
			}
		}
		
		class ValueItem extends Item {
			
			V item;
			
			ValueItem(
					V item
			) {
				super(
						ITEM_ID_PREFIX + modelKeyProvider.getKey(item),
						labelProvider.apply(item),
						queryStringProvider.apply(item)
				);
				
				this.item = item;
			}

			@Override
			void setValue() {
				value.set(item);
			}
			
			
		}
		
		class MoreItem extends Item {

			MoreItem() {
				super(MORE_ID, defaultUiMessages.moreItems(), "");
			}

			@Override
			public void onClick(BeforeSelectionEvent<Item> event) {
				event.cancel();
				// TODO selector here
			}
			
		}
		
		HandlerRegistration createWidget() {
			final ListStore<Item> store = new ListStore<Item>(new ModelKeyProvider<Item>() {
				@Override
				public String getKey(Item item) {
					return item.id;
				}
			});
					
			ListView<Item, String> view = new ListView<Item, String>(store, new ValueProvider<Item, String>() {

				@Override
				public String getValue(Item object) {
					return object.label;
				}

				@Override
				public void setValue(Item object, String value) {
					throw new RuntimeException("not implemented");
				}

				@Override
				public String getPath() {
					return "_path";
				}
			});
			view.setCell(new TextCell());
			
			ComboBoxCell<Item> comboBoxCell = new HackCell(store, new LabelProvider<Item>() {
				@Override
				public String getLabel(Item item) {
					return item.query;
				}
			}, view);
			
			combo = new ComboBox<Item>(comboBoxCell);
			
			combo.setLoader(
					new PagingLoader<PagingLoadConfig, PagingLoadResult<Item>>(new DataProxy<PagingLoadConfig, PagingLoadResult<Item>>() {
						@Override
						public void load(
								PagingLoadConfig loadConfig,
								final com.google.gwt.core.client.Callback<PagingLoadResult<Item>, Throwable> callback) {

							clientStore.list(new ListConfigBuilder() {
								@Override
								public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
									listConfigProxy.setFirstResult(null);
									listConfigProxy.setMaxResults(maxResults+1);
									
									listConfigProxy.getFilterConfigs().add(
											FilterConfigProxies.fullText(factory, filter, combo.getText())
									);
								}
							}, new AbstractReceiver<List<V>>() {
								@Override
								public void onSuccess(List<V> response) {
									boolean hasMore = false;
									
									if (response.size()>maxResults) {
										hasMore = true;
										response = response.subList(0, maxResults);
									}

									List<Item> items = Lists.newArrayList();
									for (V item : response) {
										items.add(new ValueItem(item));
									}
									
									if (hasMore) {
										items.add(new MoreItem());
									}
									
									store.replaceAll(items);
									
									callback.onSuccess(new PagingLoadResultBean<Item>(items, items.size(), 0));
								}
							});
							
						}
					})
			);
			
			combo.setMinChars(0);
			
			combo.addBeforeSelectionHandler(new BeforeSelectionHandler<Item>() {

				@Override
				public void onBeforeSelection(BeforeSelectionEvent<Item> event) {
					Item f = combo.getListView().getSelectionModel()
							.getSelectedItem();
					
					if (f!=null) {
						f.onClick(event);
					}
				}
			});			
			
			ObservableValue<Item> observableItem = new ObservableValue<Item>() {
				@Override
				public void set(Item item) {
					// TODO item should never be null, no?
					if (item!=null) {
						item.setValue();
					}
				}
				@Override
				public Item get() {
					V item = value.get();
					return item == null ? null : new ValueItem(item);
				}
				@Override
				public HandlerRegistration register(Action action) {
					return value.register(action);
				}
				@Override
				public boolean isReadOnly() {
					return value.isReadOnly();
				}
				@Override
				public List<String> getValidationErrors() {
					return value.getValidationErrors();
				}
				@Override
				public HandlerRegistration addValidationStatusChangeHandler(
						Action action) {
					return value.addValidationStatusChangeHandler(action);
				}
				@Override
				public HandlerRegistration addFocusRequestHandler(
						Action action) {
					return value.addFocusRequestHandler(action);
				}
			};
			return HandlerRegistrations.of(GxtFactory.registerAll(
					combo, 
					GxtFactory.valueBaseFieldSupplier(combo),
					observableItem, 
					editing,
					widgetContext
			), GxtFactory.registerFieldHasSelectionHandlers(
					editing, 
					combo, 
					combo, 
					observableItem
			));
			
		}
		
		
	}

}
