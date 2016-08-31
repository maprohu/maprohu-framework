package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.AbstractObservableCollectionHandler;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.Callbacks;
import hu.mapro.gwt.common.shared.Executers;
import hu.mapro.gwt.common.shared.HandlerSupport;
import hu.mapro.gwt.common.shared.HandlerSupports;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.HasObservableObjectWrapper;
import hu.mapro.gwt.common.shared.ObservableCollection;
import hu.mapro.gwt.common.shared.ObservableList;
import hu.mapro.gwt.common.shared.ObservableObjectWrapper;
import hu.mapro.gwt.common.shared.ObservableSet;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.EditingPersistence;
import hu.mapro.gwt.data.client.EditingPersistenceContext;
import hu.mapro.gwt.data.client.ObjectSource;
import hu.mapro.gwt.data.client.ObjectSourceHandler;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurator;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.records.RecordsFactory;
import hu.mapro.gwtui.client.records.RecordsFactory.EditorLayout;
import hu.mapro.gwtui.client.records.RecordsFactory.EditorStarter;
import hu.mapro.gwtui.client.records.RecordsFactory.FormEditorStarter;
import hu.mapro.gwtui.client.records.RecordsFactory.FrameSupplier;
import hu.mapro.gwtui.client.records.RecordsFactory.NewButtonAdder;
import hu.mapro.gwtui.client.records.RecordsFactory.SelectionUpdater;
import hu.mapro.gwtui.client.records.RecordsFactory.SplitFormElements;
import hu.mapro.gwtui.client.records.RecordsFactory.SplitFormLayout;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGrid;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.ResizablePanel;
import hu.mapro.gwtui.client.uibuilder.ResizablePanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Split;
import hu.mapro.gwtui.client.uibuilder.SplitBuilder;
import hu.mapro.gwtui.client.uibuilder.Tabs;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.model.Setter;
import hu.mapro.model.Wrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.testing.FakeRequest;

public class EditorChildrenCustomizers {

	public static <V extends BaseProxy, P, C extends Collection<V>> EditorChildrenCustomizer<V, C> childrenInline(
			final EditorFieldsCollecting editorFieldsCollecting,
			final P parent,
			final ObservableCollection<V, C> values,
			final Setter<? super V, P> parentSetter,
			final Class<V> childClass,
			final ProvidesKey<? super V> modelKeyProvider,
			final String header,
			final ComplexEditorPageAccessControl<V> complexEditorAccessControl,
			final InlineEditorGridConfigurator<V> editorGridConfigurator,
			final DefaultUiMessages defaultUiMessages,
			final MessageInterface messageInterface
	) {
		final CollectionPropertyObjectSource<V> objectSource = collectionPropertyObjectSource(values);
		
		final EditorFieldCustomizer<V> field = EditorFieldCustomizers.tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(Panel o) {
				ChildrenAccessControl<V> accessControl = new ChildrenAccessControl<V>(complexEditorAccessControl, editorFieldsCollecting);
				final InlineEditorGrid<V> editorGrid = RecordsFactory.inlineGrid(
						modelKeyProvider, 
						editorGridConfigurator, 
						accessControl, 
						new InstanceFactory<V>() {
							@Override
							public V create(ClassDataFactory classDataFactory) {
								V instance = classDataFactory.create(childClass);
								parentSetter.set(instance, parent);
								return instance;
							}
						}, 
						defaultUiMessages, 
						childPersistence(editorFieldsCollecting, objectSource), 
						o, 
						Callbacks.<V>newInstance(),
						Callbacks.<V>newInstance()
				);
				editorGrid.setIsTop(false);
				
				Handlers destroyHandlers = Handlers.newInstance();
				
				RecordsFactory.deleteButton(
						editorGrid, 
						defaultUiMessages, 
						Callbacks.<List<V>>newInstance(), 
						accessControl, 
						Executers.APPROVE, 
						childPersistence(editorFieldsCollecting, objectSource), 
						messageInterface, 
						new Action() {
							@Override
							public void perform() {
								editorGrid.stopEditing(Action.NONE);
							}
						}, 
						destroyHandlers
				);
				
				RecordsFactory.bindObjects(editorFieldsCollecting, editorGrid, destroyHandlers, objectSource);
				RecordsFactory.performOnDestroy(o.asWidgetContext(), destroyHandlers);
			}
		});
		
		return EditorChildrenCustomizers.<V,C>of(field);
		
	}

	public static <V extends BaseProxy, P, C extends Collection<V>> EditorChildrenCustomizer<V, C> childrenPage(
			final EditorFieldsCollecting editorFieldsCollecting,
			final P parent,
			final ObservableCollection<V, C> values,
			final Setter<? super V, P> parentSetter,
			final Class<V> childClass,
			final ProvidesKey<? super V> modelKeyProvider,
			final String header,
			final ComplexEditorPageAccessControl<V> complexEditorAccessControl,
			final EditorGridConfigurator<V> editorGridConfigurator,
			final ComplexEditorConfigurator<V> complexEditorConfigurator,
			final ComplexEditorBuilder<V> complexEditorBuilder,		
			final Tabs tabs,
			final DefaultUiMessages defaultUiMessages,
			final hu.mapro.gwtui.client.workspace.MessageInterface messageInterface
	) {
		final CollectionPropertyObjectSource<V> objectSource = collectionPropertyObjectSource(values);
		final ChildrenAccessControl<V> accessControl = new ChildrenAccessControl<V>(complexEditorAccessControl, editorFieldsCollecting);
		
		final EditorFieldCustomizer<V> field = EditorFieldCustomizers.tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(Panel o) {
				final EditorGrid<V> editorGrid = RecordsFactory.pageEditorGrid(
						o,
						modelKeyProvider, 
						accessControl, 
						defaultUiMessages, 
						editorGridConfigurator,
						tabs,
						Callbacks.<EditorGrid<V>>none(),
						childrenNewButtonAdder(editorFieldsCollecting, accessControl, complexEditorConfigurator, complexEditorBuilder, defaultUiMessages, messageInterface, objectSource),
						childrenEditorStarter(editorFieldsCollecting, complexEditorBuilder, defaultUiMessages, messageInterface)
				);
				
				Handlers destroyHandlers = Handlers.newInstance();
				
				RecordsFactory.deleteButton(
						editorGrid, 
						defaultUiMessages, 
						Callbacks.<List<V>>newInstance(), 
						accessControl, 
						Executers.APPROVE, 
						childPersistence(editorFieldsCollecting, objectSource), 
						messageInterface, 
						Action.NONE, 
						destroyHandlers
				);
				
				RecordsFactory.bindObjects(editorFieldsCollecting, editorGrid, destroyHandlers, objectSource);
				RecordsFactory.performOnDestroy(o.asWidgetContext(), destroyHandlers);
			}
		});
		
		return EditorChildrenCustomizers.<V,C>of(field);
		
	}

	public static <V extends BaseProxy, P, C extends Collection<V>> EditorChildrenCustomizer<V, C> childrenForm(
			final EditorFieldsCollecting editorFieldsCollecting,
			final P parent,
			final ObservableCollection<V, C> values,
			final Setter<? super V, P> parentSetter,
			final Class<V> childClass,
			final Function<? super V, ? extends HasObservableObjectWrapper> wrapperFunction,
			final ProvidesKey<? super V> modelKeyProvider,
			final String header,
			final ComplexEditorPageAccessControl<V> complexEditorAccessControl,
			final EditorGridConfigurator<V> editorGridConfigurator,
			final ComplexEditorConfigurator<V> complexEditorConfigurator,
			final ComplexEditorBuilder<V> complexEditorBuilder,		
			final DefaultUiMessages defaultUiMessages,
			final hu.mapro.gwtui.client.workspace.MessageInterface messageInterface
	) {
		final CollectionPropertyObjectSource<V> objectSource = collectionPropertyObjectSource(values);
		final ChildrenAccessControl<V> accessControl = new ChildrenAccessControl<V>(complexEditorAccessControl, editorFieldsCollecting);
		
		final EditorFieldCustomizer<V> field = EditorFieldCustomizers.tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(Panel o) {
				Wrapper<Action> forceClose = Wrapper.of(Action.NONE);
				final EditorGrid<V> editorGrid = RecordsFactory.formEditorGrid(
						o,
						o.asWidgetContext(),
						modelKeyProvider, 
						accessControl, 
						messageInterface,
						defaultUiMessages, 
						editorGridConfigurator,
						new SplitFormLayout() {
							@Override
							public <Q> SplitFormElements<Q> layout(Panel panel,
									final EditorGridBuilder<Q> editorGridBuilder) {
								final SplitFormElements<Q> result = new SplitFormElements<Q>();
								
								panel.split(new SplitBuilder() {
									
									@Override
									public void build(Split split) {
										
										result.formPanel = split.center(null);

										split.west(new ResizablePanelBuilder() {
											@Override
											public void build(ResizablePanel resizablePanel) {
												resizablePanel.setSize(500);
												result.editorGrid = resizablePanel.asPanel().border(null).asPanel().editorGrid(editorGridBuilder); 
											}
										}).asPanel();
										
									}
								});
								
								return result;
							}
						},
						forceClose,
						childrenNewButtonAdder(
								editorFieldsCollecting,
								accessControl,
								complexEditorConfigurator,
								complexEditorBuilder, 
								defaultUiMessages,
								messageInterface, 
								objectSource
						),
						childrenEditorStarter(
								editorFieldsCollecting,
								complexEditorBuilder, 
								defaultUiMessages,
								messageInterface
						)
				);
				
				Handlers destroyHandlers = Handlers.newInstance();
				
				RecordsFactory.deleteButton(
						editorGrid, 
						defaultUiMessages, 
						Callbacks.<List<V>>newInstance(), 
						accessControl, 
						Executers.APPROVE, 
						childPersistence(editorFieldsCollecting, objectSource), 
						messageInterface, 
						forceClose.get(), 
						destroyHandlers
				);
				
				RecordsFactory.bindObjects(editorFieldsCollecting, editorGrid, destroyHandlers, objectSource);
				RecordsFactory.performOnDestroy(o.asWidgetContext(), destroyHandlers);

				final Map<Object, HandlerRegistration> focusRegistrations = Maps.newHashMap(); 
				
				final Action removeAllFocusRequestHandlers = new Action() {
					@Override
					public void perform() {
						for (HandlerRegistration reg : focusRegistrations.values()) {
							reg.removeHandler();
						}
						focusRegistrations.clear();
					}
				};
				destroyHandlers.register(removeAllFocusRequestHandlers);
				
				values.register(new AbstractObservableCollectionHandler<V>() {
					
					@Override
					public void onAdd(V object) {
						registerFocusRequestHandler(wrapperFunction,
								modelKeyProvider, editorGrid, focusRegistrations,
								object);
					}
					
					@Override
					public void onRemove(V object) {
						Object key = modelKeyProvider.getKey(object);
						focusRegistrations.remove(key).removeHandler();
					}

					@Override
					public void onReplaceAll() {
						removeAllFocusRequestHandlers.perform();
						registerFocusRequestHandlers(values, wrapperFunction,
								modelKeyProvider, editorGrid, focusRegistrations);
					}
				});
				
			}
		});
		
		return EditorChildrenCustomizers.<V,C>of(field);
		
	}
	
	
	public static <V> EditingPersistence<V> childPersistence(
			final ClassDataFactory classDataFactory,
			final CollectionPropertyObjectSource<V> objectSource
	) {
		return new EditingPersistence<V>() {

			final EditingPersistenceContext<V> context = childPersistenceContext(classDataFactory, objectSource);
			
			@Override
			public EditingPersistenceContext<V> newEditingContext() {
				return context;
			}
			
		};
	}
	
	public static <V, P, T> void childSetUpdater(
			final P parent,
			final Function<? super P, Set<V>> values,
			final Setter<? super V, P> parentSetter,
			final Callbacks<V> onAdd,
			final Callbacks<V> onRemove,
			final Handlers closeHandlers
	) {
		final Set<V> set = values.apply(parent);
		
		closeHandlers.add(Actions.removeHandler(onAdd.add(new Callback<V>() {
			@Override
			public void onResponse(V value) {
				parentSetter.set(value, parent);
				set.add(value);
			}
		})));
		
		closeHandlers.add(Actions.removeHandler(onRemove.add(new Callback<V>() {
			@Override
			public void onResponse(V value) {
				parentSetter.set(value, null);
				set.remove(value);
			}
		})));
	}

	public static <V> CollectionPropertyObjectSource<V> collectionPropertyObjectSource(
			ObservableCollection<V, ? extends Collection<V>> collection
	) {
		return new CollectionPropertyObjectSource<V>(collection);
//		AutoBean<Collection<V>> autoBean = AutoBeanUtils.getAutoBean(collection);
//		CollectionPropertyObjectSource<V> source = autoBean.getTag(CollectionPropertyObjectSource.class.getName());
//		
//		if (source==null) {
//			autoBean.setTag(CollectionPropertyObjectSource.class.getName(), source);
//		}
//		
//		return source;
	}
	
	public static <V> EditingPersistenceContext<V> childPersistenceContext(
			final ClassDataFactory classDataFactory,
			final CollectionPropertyObjectSource<V> objectSource
	) {
		return new EditingPersistenceContext<V>() {

			@Override
			public <O extends BaseProxy> O edit(O object) {
				return object;
			}

			@Override
			public Request<?> persist(final V object, final Receiver<V> receiver) {
				return new FakeRequest<Object>() {
					@Override
					public void fire() {
						objectSource.add(object);
						receiver.onSuccess(object);
					}
				};
			}

			@Override
			public Request<?> merge(final V object, final Receiver<V> receiver) {
				return new FakeRequest<Object>() {
					@Override
					public void fire() {
						objectSource.update(object);
						receiver.onSuccess(object);
					}
				};
			}

			@Override
			public void delete(List<V> object,
					Receiver<Void> receiver) {
				for (V item : object) {
					objectSource.remove(item);
				}
				receiver.onSuccess(null);
			}

			@Override
			public <C extends BaseProxy> C create(Class<C> clazz) {
				return classDataFactory.create(clazz);
			}
		};
	}

	public static <V> NewButtonAdder<V> childrenNewButtonAdder(
			final EditorFieldsCollecting editorFieldsCollecting,
			final ComplexEditorPageAccessControl<V> complexEditorAccessControl,
			final ComplexEditorConfigurator<V> complexEditorConfigurator,
			final ComplexEditorBuilder<V> complexEditorBuilder,
			final DefaultUiMessages defaultUiMessages,
			final hu.mapro.gwtui.client.workspace.MessageInterface messageInterface,
			final CollectionPropertyObjectSource<V> objectSource
	) {
		return new NewButtonAdder<V>() {

			@Override
			public void addNewButton(
					EditorGrid<V> editorGrid,
					final FrameSupplier<V> frameSupplier,
					final SelectionUpdater<V> selectionUpdater,
					final EditorLayout childrenFieldsLayout
			) {
				RecordsFactory.addFormNewButton(
						editorGrid, 
						complexEditorAccessControl,
						complexEditorConfigurator, 
						defaultUiMessages, 
						new FormEditorStarter<V>() {

							@Override
							public void start(
									final Function<ClassDataFactory, V> editingObjectProvider
							) {
								RecordsFactory.startChildrenFormEditing(
										frameSupplier, 
										complexEditorBuilder, 
										messageInterface, 
										defaultUiMessages, 
										childrenFieldsLayout, 
										true, 
										editorFieldsCollecting, 
										new Supplier<V>() {
											@Override
											public V get() {
												V editingObject = editingObjectProvider.apply(editorFieldsCollecting);
												
												objectSource.add(editingObject);
												
												selectionUpdater.select(editingObject);
												
												return editingObject;
											}
										},
										editorFieldsCollecting.isReadOnly()
								);
							}
							
						}
				);
			}

			
		};
	}

	public static <V> EditorStarter<V> childrenEditorStarter(
			final EditorFieldsCollecting editorFieldsCollecting,
			final ComplexEditorBuilder<V> complexEditorBuilder,
			final DefaultUiMessages defaultUiMessages,
			final hu.mapro.gwtui.client.workspace.MessageInterface messageInterface
	) {
		return new EditorStarter<V>() {

			@Override
			public void startEditor(
					V editingObject,
					FrameSupplier<V> frameSupplier,
					EditorLayout childrenFieldsLayout
			) {
				
				RecordsFactory.startChildrenFormEditing(
						frameSupplier, 
						complexEditorBuilder, 
						messageInterface, 
						defaultUiMessages, 
						childrenFieldsLayout, 
						false, 
						editorFieldsCollecting, 
						Suppliers.ofInstance(editingObject),
						editorFieldsCollecting.isReadOnly()
				);
				
			}
			
		};
	}

	private static final class ChildrenAccessControl<V> extends
			ForwardingComplexEditorPageAccessControl<V> {
		private final EditorFieldsCollecting editorFieldsCollecting;

		private ChildrenAccessControl(
				ComplexEditorPageAccessControl<V> delegate,
				EditorFieldsCollecting editorFieldsCollecting) {
			super(delegate);
			this.editorFieldsCollecting = editorFieldsCollecting;
		}

		@Override
		public boolean deleteButton() {
			return super.deleteButton() && !editorFieldsCollecting.isReadOnly();
		}

		@Override
		public boolean newButton() {
			return super.newButton() && !editorFieldsCollecting.isReadOnly();
		}

		@Override
		public boolean edit(
				Supplier<? extends V> editingObject) {
			return super.edit(editingObject) && !editorFieldsCollecting.isReadOnly();
		}
	}

	public static class CollectionPropertyObjectSource<V> implements ObjectSource<V> {

		final ObservableCollection<V, ? extends Collection<V>> collection;

		public CollectionPropertyObjectSource(ObservableCollection<V, ? extends Collection<V>> values) {
			super();
			this.collection = values;
		}

		final HandlerSupport<ObjectSourceHandler<V>> handlers = HandlerSupports.of();
		
		@Override
		public HandlerRegistration addObjectSourceHandler(
				final ObjectSourceHandler<V> handler
		) {
			return handlers.addHandler(handler);
		}
			
		@Override
		public void load(
				Callback<Iterable<V>> loaded,
				Callback<Iterable<V>> cached
		) {
			cached.onResponse(collection.get());
		}
		
		public void add(final V item) {
			collection.add(item);
			handlers.fire(new Callback<ObjectSourceHandler<V>>() {
				@Override
				public void onResponse(ObjectSourceHandler<V> handler) {
					handler.onAdd(item);
				}
			});
		}
		
		public void remove(final V item) {
			collection.remove(item);
			handlers.fire(new Callback<ObjectSourceHandler<V>>() {
				@Override
				public void onResponse(ObjectSourceHandler<V> handler) {
					handler.onRemove(item);
				}
			});
		}
		
		public void update(final V item) {
			handlers.fire(new Callback<ObjectSourceHandler<V>>() {
				@Override
				public void onResponse(ObjectSourceHandler<V> handler) {
					handler.onUpdate(item);
				}
			});
		}
		
	}


	public static <V extends BaseProxy, P, C extends Collection<V>> EditorChildrenCustomizer<V, C> manyToManySet(
			final EditorFieldsCollecting editorFieldsCollecting,
			final ObservableSet<V> values,
			final ProvidesKey<? super V> modelKeyProvider,
			final ClientStore<V> clientStore,
			final Function<V, String> labelProvider, 
			final String header
	) {
		
		final EditorFieldCustomizer<V> field = EditorFieldCustomizers.tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(final Panel o) {
				clientStore.register(new ClientStoreReader<V, Void>() {

					@Override
					public Void cached(CachedClientStore<V> store) {
						o.cachedComplexSetField(null).bind(
								values, 
								editorFieldsCollecting, 
								store, 
								modelKeyProvider, 
								labelProvider
						);
						return null;
					}

					@Override
					public Void uncached(UncachedClientStore<V> store) {
						throw new RuntimeException("not yet implemented");
					}
				});
			}
		});
		
		return EditorChildrenCustomizers.<V,C>of(field);
		
	}	

	public static <V extends BaseProxy, P, C extends Collection<V>> EditorChildrenCustomizer<V, C> manyToManyList(
			final EditorFieldsCollecting editorFieldsCollecting,
			final ObservableList<V> values,
			final ProvidesKey<? super V> modelKeyProvider,
			final ClientStore<V> clientStore,
			final Function<V, String> labelProvider, 
			final String header
	) {
		
		final EditorFieldCustomizer<V> field = EditorFieldCustomizers.tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(final Panel o) {
				clientStore.register(new ClientStoreReader<V, Void>() {
					
					@Override
					public Void cached(CachedClientStore<V> store) {
						o.cachedComplexListField(null).bind(
								values, 
								editorFieldsCollecting, 
								store, 
								modelKeyProvider, 
								labelProvider
								);
						return null;
					}
					
					@Override
					public Void uncached(UncachedClientStore<V> store) {
						throw new RuntimeException("not yet implemented");
					}
				});
			}
		});
		
		return EditorChildrenCustomizers.<V,C>of(field);
		
	}	
	
	public static <V, C extends Collection<V>> EditorChildrenCustomizer<V, C> of(final EditorFieldCustomizer<V> field) {
		return new EditorChildrenCustomizer<V, C>() {
			@Override
			public void setNotNull(boolean notNull) {
				field.setNotNull(notNull);
			}

			@Override
			public void setLabel(String text) {
				field.setLabel(text);
			}

			@Override
			public void setFill(boolean fill) {
				field.setFill(fill);
			}
		};
	}

	protected static <V> void registerFocusRequestHandler(
			final Function<? super V, ? extends HasObservableObjectWrapper> wrapperFunction,
			final ProvidesKey<? super V> modelKeyProvider,
			final EditorGrid<V> editorGrid,
			final Map<Object, HandlerRegistration> focusRegistrations,
			final V item) {
		final ObservableObjectWrapper<V> wrapper = ObservableObjectWrapper.autobean(item, wrapperFunction);
		final Object key = modelKeyProvider.getKey(wrapper.getWrapped());
		HandlerRegistration registration = wrapper.getFocusRequestHandlers().add(new Action() {
			@Override
			public void perform() {
				List<V> currentSelection = editorGrid.getSelection();
				
				if (
						currentSelection.size()!=1 
						|| 
						!Objects.equal(
								modelKeyProvider.getKey(currentSelection.get(0)),
								key
						)
				) {
					editorGrid.select(wrapper.getWrapped());
				}
				
			}
		});
		focusRegistrations.put(key, registration);
	}

	protected static <V, C extends Collection<V>> void registerFocusRequestHandlers(
			final ObservableCollection<V, C> values,
			final Function<? super V, ? extends HasObservableObjectWrapper> wrapperFunction,
			final ProvidesKey<? super V> modelKeyProvider,
			final EditorGrid<V> editorGrid,
			final Map<Object, HandlerRegistration> focusRegistrations) {
		for (final V item : values.get()) {
			registerFocusRequestHandler(wrapperFunction,
					modelKeyProvider, editorGrid, focusRegistrations,
					item);
		}
	}
	
}
