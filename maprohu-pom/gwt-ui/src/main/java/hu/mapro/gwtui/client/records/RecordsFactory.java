package hu.mapro.gwtui.client.records;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.client.Call;
import hu.mapro.gwt.common.client.Calls;
import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.client.EntityFetcher;
import hu.mapro.gwt.common.client.ForwardReceiver;
import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Autobeans;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.Callbacks;
import hu.mapro.gwt.common.shared.CannotSetValueException;
import hu.mapro.gwt.common.shared.Executer;
import hu.mapro.gwt.common.shared.Executers;
import hu.mapro.gwt.common.shared.Flag;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableObjectWrapper;
import hu.mapro.gwt.common.shared.ObservableSwappable;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ObservableValues;
import hu.mapro.gwt.common.shared.Validatable;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.EditingPersistence;
import hu.mapro.gwt.data.client.EditingPersistenceContext;
import hu.mapro.gwt.data.client.EditingPersistences;
import hu.mapro.gwt.data.client.ListResult;
import hu.mapro.gwt.data.client.ObjectSource;
import hu.mapro.gwt.data.client.ObjectSourceHandler;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.edit.AbstractComplexEditorBuilding;
import hu.mapro.gwtui.client.edit.CancelEvent;
import hu.mapro.gwtui.client.edit.CancelEvent.CancelHandler;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.ComplexEditingRegistration;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorConfigurating;
import hu.mapro.gwtui.client.edit.ComplexEditorConfigurator;
import hu.mapro.gwtui.client.edit.ComplexEditorPageAccessControl;
import hu.mapro.gwtui.client.edit.FormFieldsCollecting;
import hu.mapro.gwtui.client.edit.LabeledFieldCustomizer;
import hu.mapro.gwtui.client.edit.LabeledFieldCustomizers;
import hu.mapro.gwtui.client.edit.SaveEvent;
import hu.mapro.gwtui.client.edit.SaveEvent.SaveHandler;
import hu.mapro.gwtui.client.edit.ValidationError;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.gwtui.client.grid.FullTextFiltering;
import hu.mapro.gwtui.client.grid.GridConfigurating;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurating;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurator;
import hu.mapro.gwtui.client.grid.InlineEditorGridHandler;
import hu.mapro.gwtui.client.grid.InstanceEditingHandler;
import hu.mapro.gwtui.client.grid.InstanceSavingHandler;
import hu.mapro.gwtui.client.grid.Paging;
import hu.mapro.gwtui.client.grid.PagingControl;
import hu.mapro.gwtui.client.iface.AbstractWidgetContext;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetOperation;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.menu.SubButton;
import hu.mapro.gwtui.client.uibuilder.Builder;
import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.Connector;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenArea;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreaBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreaBuilding;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreas;
import hu.mapro.gwtui.client.uibuilder.EditorForm;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilding;
import hu.mapro.gwtui.client.uibuilder.Fields;
import hu.mapro.gwtui.client.uibuilder.Frame;
import hu.mapro.gwtui.client.uibuilder.HasHeader;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGrid;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilding;
import hu.mapro.gwtui.client.uibuilder.Margin;
import hu.mapro.gwtui.client.uibuilder.MarginBuilder;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.ResizablePanel;
import hu.mapro.gwtui.client.uibuilder.ResizablePanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Split;
import hu.mapro.gwtui.client.uibuilder.Tab;
import hu.mapro.gwtui.client.uibuilder.TabBuilder;
import hu.mapro.gwtui.client.uibuilder.Tabs;
import hu.mapro.gwtui.client.uibuilder.TabsBuilder;
import hu.mapro.gwtui.client.uibuilder.TabsBuilding;
import hu.mapro.gwtui.client.uibuilder.TabsBuilding.TabPosition;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupports;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.jpa.model.domain.client.ListConfigBuilder;
import hu.mapro.jpa.model.domain.client.ListConfigBuilders;
import hu.mapro.jpa.model.domain.client.Sorting;
import hu.mapro.jpa.model.domain.shared.FilterRepository.FilterItem;
import hu.mapro.jpa.model.domain.shared.FullTextFilterType;
import hu.mapro.model.Consumer;
import hu.mapro.model.Wrapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.validation.client.impl.Validation;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

public class RecordsFactory {

	interface EditorGridCreator<T> {
		EditorGrid<T> create(Panel panel);
	}
	
	interface EditorGridBinder<T> {
		HandlerRegistration bind(EditorGrid<T> grid);
	}
	
	public interface SelectionUpdater<T> {
		
		void select(T selection);
		void deselectAll();
		void select(List<T> selection);
		
	}
	
	public static <T> SelectionUpdater<T> fakeSelectionUpdater() {
		return new SelectionUpdater<T>() {

			@Override
			public void select(T selection) {
			}

			@Override
			public void deselectAll() {
			}

			@Override
			public void select(List<T> selection) {
			}
		};
	}
	
	public static <T> void widget(
			final Panel panel, 
			final WidgetContext context,
			final EditorGridCreator<T> editorGridCreator,
			final EditorGridBinder<T> binder
	) {
		
		
		EditorGrid<T> editorGrid = editorGridCreator.create(
				panel
		);
		
		bindAndRegister(editorGrid, context, binder);
	}

	public static <T> void bindAndRegister(
			EditorGrid<T> editorGrid,
			final WidgetContext context, 
			final EditorGridBinder<T> binder
	) {
		final HandlerRegistration handlerRegistration = binder.bind(editorGrid);
		
		removeOnDestroy(context, handlerRegistration);
	}

	public static void removeOnDestroy(final WidgetContext context,
			final HandlerRegistration handlerRegistration) {
		final Action action = Actions.removeHandler(handlerRegistration);
		performOnDestroy(context, action);
	}

	public static void performOnDestroy(final WidgetContext context,
			final Action action) {
		context.registerListener(new AbstractWidgetListener() {
			public void onDestroy(hu.mapro.gwtui.client.iface.WidgetOperation opertation) {
				opertation.approve(action);
			}
		});
	}

	public static <T> HandlerRegistration bind(
			final EditorGrid<T> editorGrid,
			ClientStore<T> store,
			final DefaultUiMessages defaultUiMessages,
			final Executer refreshExecuter,
			final Callbacks<T> onPersist,
			final Callbacks<T> onMerge,
			final Callbacks<List<T>> onDelete,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final Executer deleteExecuter,
			final EditingPersistence<T> editingPersistence,
			final MessageInterface messageInterface,
			final Action forceClose
	) {
		final Handlers closeHandlers = Handlers.newInstance();
		
		store.register(new ClientStoreReader<T, Void>() {
			
			@Override
			public Void cached(final CachedClientStore<T> cachedClientStore) {
				bindCache(
						editorGrid, 
						defaultUiMessages, 
						refreshExecuter,
						closeHandlers, 
						cachedClientStore
				);
				
				return null;
			}

			@Override
			public Void uncached(final UncachedClientStore<T> uncachedClientStore) {
				new Object() {
					Optional<T> lastSaved = Optional.absent();
					
					ListConfigBuilder fullText = ListConfigBuilder.NONE; 
					
					{
						final PagingControl pagingControl = editorGrid.setPaging(new Paging<T>() {
							
							@Override
							public void load(int offset, int limit,
									List<Sorting<T>> sorting,
									final Receiver<ListResult<T>> receiver) {
								
								editorGrid.showLoadMask();
								
								uncachedClientStore.listCount(
										ListConfigBuilders.multi(
												ListConfigBuilders.paging(offset, limit, sorting),
												fullText
										),
										new AbstractReceiver<ListResult<T>>() {
									@Override
									public void onSuccess(ListResult<T> response) {
										editorGrid.replaceValues(response.getList());
										editorGrid.hideLoadMask();
										
										if (lastSaved.isPresent()) {
											editorGrid.select(lastSaved.get());
											lastSaved = Optional.absent();
										}
										
										receiver.onSuccess(response);
									}
								});
								
							}

						});
						
						final FilterItem<? extends FullTextFilterType> fullTextFilter = uncachedClientStore.getDefaultFullTextFilterItem();
						if (fullTextFilter!=null) {
							editorGrid.setFullTextFiltering(new FullTextFiltering() {
								@Override
								public void filter(String queryString) {
									fullText = ListConfigBuilders.fullText(fullTextFilter, queryString);
									
									pagingControl.refresh();
								}
							});
						}
						
								
						pagingControl.refresh();
						
						Callback<T> cb = new Callback<T>() {
							@Override
							public void onResponse(T editingObject) {
								lastSaved = Optional.of(editingObject);
								pagingControl.refresh();
							}
						};
						onPersist.add(cb);
						onMerge.add(cb);
						onDelete.add(new Callback<List<T>>() {
							@Override
							public void onResponse(List<T> value) {
								pagingControl.refresh();
							}
						});
					}
					
				};
				
				return null;
			}
		});
		
		deleteButton(
				editorGrid, 
				defaultUiMessages, 
				onDelete,
				complexEditorAccessControl, 
				deleteExecuter, 
				editingPersistence,
				messageInterface, 
				forceClose, 
				closeHandlers
		);
		
		
		return HandlerRegistrations.perform(closeHandlers);
		
	}

	public static <T> void deleteButton(
			final EditorGrid<T> editorGrid,
			final DefaultUiMessages defaultUiMessages,
			final Callbacks<List<T>> onDelete,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final Executer deleteExecuter,
			final EditingPersistence<T> editingPersistence,
			final MessageInterface messageInterface, 
			final Action forceClose,
			final Handlers closeHandlers
	) {
		if (complexEditorAccessControl.deleteButton()) {
			final Button deleteButton = editorGrid.button();
			deleteButton.setLabel(defaultUiMessages.delete());
			closeHandlers.add(Actions.removeHandler(enableWhenSelectionNotEmpty(editorGrid, deleteButton)));
			deleteButton.addListener(new Action() {
				
				@Override
				public void perform() {
					
					deleteExecuter.execute(new Action() {
						
						@Override
						public void perform() {
							final List<T> selectedItems = editorGrid.getSelection();
							
							Action deleteAction = new Action() {

								@Override
								public void perform() {
									editingPersistence.newEditingContext().delete(selectedItems, new AbstractReceiver<Void>() {
										@Override
										public void onSuccess(Void response) {
											forceClose.perform();
											onDelete.fire(selectedItems);
										}
									});
								}
								
							};
							
							messageInterface.confirm(
									defaultUiMessages.delete(), 
									defaultUiMessages.deleteConfirm(selectedItems.size()), 
									deleteAction, 
									null
							);
						}
					}, Action.NONE);
					
				}
			});

		}
	}
	
	public static <T> HandlerRegistration enableWhenSelectionNotEmpty(
			final EditorGrid<T> editorGrid,
			final Button button
	) {
		Action action = new Action() {
			@Override
			public void perform() {
				button.setEnabled(!editorGrid.getSelection().isEmpty());
			}
		};
		action.perform();
		return editorGrid.addSelectionChangeHandler(action);
	}

	public static <T> EditorGrid<T> createEditorGrid(
			Panel o,
			final ProvidesKey<? super T> modelKeyProvider,
			final EditorGridConfigurator<T> editorGridConfigurator
	) {
		return o.editorGrid(createEditorGridBuilder(modelKeyProvider, editorGridConfigurator, o.asWidgetContext()));
	}

	public static <T> EditorGridBuilder<T> createEditorGridBuilder(
			final ProvidesKey<? super T> modelKeyProvider,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final WidgetContext widgetContext
	) {
		return new EditorGridBuilder<T>() {
			@Override
			public void build(final EditorGridBuilding<T> o) {
				final Handlers closeHandlers = destroyHandlers(widgetContext);
				
				o.setModelKeyProvider(modelKeyProvider);
				editorGridConfigurator.configure(new GridConfigurating<T>() {
					@Override
					public <V> GridColumnCustomizer<V> addColumn(
							Function<? super T, ? extends ObservableValue<V>> getter,
							String path) {
						return o.addColumn(getter, path);
					}

					@Override
					public Handlers closeHandlers() {
						return closeHandlers;
					}
				});
			}
		};
	}
	
	public interface FrameSupplier<T> {
		void open(Callback<FrameContext<T>> result);
	}

	interface FrameContext<T> extends HasHeader {
		WidgetContext context();
		Panel panel();
		void close();
		void associate(T editing);
	}

	public interface FormEditorStarter<T> {
	
		void start(Function<ClassDataFactory, T> editingObjectProvider);
		
	}

	public static <T extends BaseProxy> void addFormNewButton(
			final EditorGrid<T> editorGrid,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			ComplexEditorConfigurator<T> complexEditorConfigurator,
			final DefaultUiMessages defaultUiMessages,
			final FrameSupplier<T> frameSupplier,
			final EditingPersistence<T> editingPersistence,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final MessageInterface messageInterface,
			final Callback<T> onPersist,
			final Callback<T> onMerge,
			final SelectionUpdater<T> selectionUpdater,
			final EditorLayout editorLayout
	) {
		addFormNewButton(
				editorGrid, 
				complexEditorAccessControl, 
				complexEditorConfigurator, 
				defaultUiMessages, 
				new FormEditorStarter<T>() {

					@Override
					public void start(
							com.google.common.base.Function<ClassDataFactory,T> editingObjectProvider 
					) {

						final EditingPersistenceContext<T> ctx = editingPersistence.newEditingContext();
						final T editingObject = editingObjectProvider.apply(ctx);
						
						startFormEditing(
								frameSupplier,
								complexEditorBuilder,
								messageInterface, 
								defaultUiMessages,
								newObjectEditorFlow(
										defaultUiMessages,
										editingPersistence, 
										onPersist, 
										onMerge,
										selectionUpdater, 
										ctx, 
										editingObject
								),
								editorLayout,
								!complexEditorAccessControl.edit(Suppliers.ofInstance(editingObject))
						);
						
						
					}
					
				}
		);
		
	}
	
	
	public static <T> void addFormNewButton(
			final EditorGrid<T> editorGrid,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			ComplexEditorConfigurator<T> complexEditorConfigurator,
			final DefaultUiMessages defaultUiMessages,
			final FormEditorStarter<T> formEditorStarter
	) {
		if (complexEditorAccessControl.newButton()) {
			final MultiButton newButton = editorGrid.multiButton();
			newButton.setLabel(defaultUiMessages._new());
			newButton.setEnabled(false);
			final Wrapper<SubButton> defaultItem = Wrapper.create();
			
			newButton.addListener(new Action() {
				@Override
				public void perform() {
					defaultItem.get().fire();
				}
			});
			
			complexEditorConfigurator.configure(new ComplexEditorConfigurating<T>() {
				
				boolean first = true;
				
				@Override
				public void addNewObject(
						String label, 
						final InstanceFactory<? extends T> instanceFactory
				) {
					final SubButton subButton = newButton.subButton();
					if (first) {
						defaultItem.set(subButton);
						subButton.setHighlight(true);
					}
					first = false;
					newButton.setEnabled(true);
					
					subButton.setLabel(label);
					
					subButton.addListener(new Action() {
						@Override
						public void perform() {
							if (defaultItem.get()!=null) {
								defaultItem.get().setHighlight(false);
							}
							subButton.setHighlight(true);
							defaultItem.set(subButton);

							formEditorStarter.start(
									new Function<ClassDataFactory, T>() {
										@Override
										public T apply(
												ClassDataFactory ctx) {
											return instanceFactory.create(ctx);
										}
									}
							);
						}
					});
					
				}
			});
		}
		
	}

	public static <T> T getSingleSelection(
			EditorGrid<T> editorGrid) {
		return Iterables.getOnlyElement(editorGrid.getSelection());
	}

	public static <T> void addAndPerformSelectionChangeHandler(
			EditorGrid<T> editorGrid,
			Action selectionListener) {
		editorGrid.addSelectionChangeHandler(selectionListener);
		selectionListener.perform();
	}

	public static <T> void enableWhenSingleSelection(
			final EditorGrid<T> editorGrid, final Button button) {
		addAndPerformSelectionChangeHandler(editorGrid, new Action() {
			@Override
			public void perform() {
				button.setEnabled(isSingleSelection(editorGrid));
			}
		});
	}

	public interface EditorFlow<T> {
		Call<Void> init();
		void setSaveReceiver(Receiver<T> done);
		String getHeaderName(String entityName);
		T getSavedObject();
		T getEditingObject();
		void save();
		ClassDataFactory getClassDataFactory();
		boolean isNew();
	}
	
	public interface EditorLayout {
		
		FormFieldsCollecting layout(Panel panel);
		
	}
	
	public static FormFieldsCollecting childrenTabsLayout(
			Panel panel
	) {
		final Split editorSplit = panel.split(null);
		
		final Fields fields = editorSplit.center(null).scroll(null).asPanel().margin(new MarginBuilder() {
			@Override
			public void build(Margin o) {
				o.setWidth(10);
			}
		}).asPanel().fields(null);

		return new FormFieldsCollecting() {
			
			Supplier<Tabs> childTabs = Suppliers.memoize(new Supplier<Tabs>() {
				@Override
				public Tabs get() {
					return editorSplit.south(new ResizablePanelBuilder() {
						@Override
						public void build(ResizablePanel o) {
							o.setSize(400);
						}
					}).asPanel().margin(null).asPanel().tabs(new TabsBuilder() {
						@Override
						public void build(TabsBuilding o) {
							o.setTabPosition(TabPosition.TOP);
						}
					});
				}
			});
			
			@Override
			public EditorChildrenArea editorChildrenArea(
					final EditorChildrenAreaBuilder builder) {
				
				final Tab tab = childTabs.get().tab(new TabBuilder() {
					@Override
					public void build(Tab o) {
						o.setClosable(false);
					}
				});
				
				return new EditorChildrenArea() {
					
					{
						final EditorChildrenArea thisFinal = this;
						
						BuildingFactory.build(new EditorChildrenAreaBuilding() {
							
							@Override
							public boolean isFramed() {
								return true;
							}
							
							@Override
							public EditorChildrenArea getTarget() {
								return thisFinal;
							}
						}, builder);
					}
					
					@Override
					public WidgetContext asWidgetContext() {
						return tab.asWidgetContext();
					}
					
					@Override
					public Panel asPanel() {
						return tab.asPanel();
					}
					
					@Override
					public void setHeader(String header) {
						tab.setHeader(header);
					}

					@Override
					public void setFill(boolean fill) {
					}
				};
			}
			

			@Override
			public LabeledFieldCustomizer addEditorField(
					final String label,
					final boolean notNull,
					final PanelBuilder builder
			) {
				return LabeledFieldCustomizers.editorField(fields, label, notNull, builder);
			}


			@Override
			public Panel addFill(PanelBuilder builder) {
				return fields.fill(builder);
			}
		};
	}
	
	interface FormEmbedder<T> {
		void embed(
				FrameContext<T> frameContext,
				PanelBuilder panelBuilder,
				Handlers removeHandlers,
				ObservableValue<Boolean> validStatus,
				ObservableValue<Boolean> dirtyStatus,
				Handlers saveHandlers
		);
	}

	public static <T> void startFormEditing(
			final FrameSupplier<T> frameSupplier,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorFlow<T> editorFlow,
			final EditorLayout layout,
			boolean isReadOnly
	) {
		FormEmbedder<T> formEmbedder = topFormEmbedder(editorFlow, defaultUiMessages, isReadOnly);
		
		startFormEditing(
				frameSupplier, 
				complexEditorBuilder, 
				messageInterface,
				defaultUiMessages, 
				editorFlow, 
				layout,
				formEmbedder,
				isReadOnly
		);
		
	}

	public static <T> void startFormEditing(
			final FrameSupplier<T> frameSupplier,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorFlow<T> editorFlow, 
			final EditorLayout layout,
			FormEmbedder<T> formEmbedder,
			boolean isReadOnly
	) {
		final Call<Void> call = editorFlow.init();
		final Handlers saveHandlers = Handlers.newInstance();
		startFormEditing(
				frameSupplier, 
				complexEditorBuilder, 
				messageInterface, 
				defaultUiMessages, 
				layout, 
				formEmbedder, 
				call, 
				editorFlow.isNew(), 
				new ClassDataFactory() {
					@Override
					public <C extends BaseProxy> C create(Class<C> clazz) {
						return editorFlow.getClassDataFactory().create(clazz);
					}
				}, 
				new Supplier<T>() {
					@Override
					public T get() {
						return editorFlow.getEditingObject();
					}
				},
				isReadOnly,
				saveHandlers
		);
	}

	public static <T> FormEmbedder<T> topFormEmbedder(
			final EditorFlow<T> editorFlow,
			final DefaultUiMessages defaultUiMessages,
			final boolean isReadOnly
	) {
		return new FormEmbedder<T>() {
			
			@Override
			public void embed(
					final FrameContext<T> frameContext,
					PanelBuilder panelBuilder,
					final Handlers removeHandlers,
					final ObservableValue<Boolean> validStatus,
					final ObservableValue<Boolean> dirtyStatus,
					final Handlers saveHandlers
			) {
				frameContext.associate(editorFlow.getEditingObject());
				
				if (isReadOnly) {
					BuildingFactory.build(frameContext.panel(), panelBuilder);
				} else {

					final EditorForm editorForm = frameContext.panel().editorForm(null);

					BuildingFactory.build(editorForm.asPanel(), panelBuilder);
					
					Action updateSaveAction = new Action() {
						@Override
						public void perform() {
							editorForm.setInvalid(!validStatus.get());
						}
					};
					removeHandlers.add(Actions.removeHandler(validStatus.register(updateSaveAction)));
					updateSaveAction.perform();
					
					Action updateDirtyAction = new Action() {
						@Override
						public void perform() {
							editorForm.setDirty(dirtyStatus.get());
						}
					};
					removeHandlers.add(Actions.removeHandler(dirtyStatus.register(updateDirtyAction)));
					updateDirtyAction.perform();
					
					
					editorForm.addCancelHandler(new CancelHandler() {
						@Override
						public void onCancel(CancelEvent event) {
							removeHandlers.fire();
							frameContext.close();
						}
					});
					
					removeHandlers.add(Actions.removeHandler(frameContext.context().registerListener(new AbstractWidgetListener() {
						@Override
						public void onDestroy(WidgetOperation operation) {
							if (editorFlow.isNew() || !AutoBeanUtils.deepEquals(
									AutoBeanUtils.getAutoBean(editorFlow.getSavedObject()), 
									AutoBeanUtils.getAutoBean(editorFlow.getEditingObject()))
							) {
								operation.confirm(defaultUiMessages.closingDirtyEditorWarningMessage(), Action.NONE);
							} else {
								operation.approve(Action.NONE);
							}
						}
					})));
					
					final Wrapper<Action> clearInvalidAction = Wrapper.of(Action.NONE);
					
					editorFlow.setSaveReceiver(new AbstractReceiver<T>() {
						@Override
						public void onSuccess(T response) {
							editorForm.setSaving(false);
							
							saveHandlers.fire();
						}

						public void onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure error) {
							editorForm.setSaving(false);
							super.onFailure(error);
						};

						public void onConstraintViolation(java.util.Set<javax.validation.ConstraintViolation<?>> violations) {
							editorForm.setSaving(false);
							clearInvalidAction.set(markInvalid(editorForm, violations));
						};
					});
					
					editorForm.addSaveHandler(new SaveHandler() {
						@Override
						public void onSave(SaveEvent event) {
							editorForm.hideValidationErrors();
							clearInvalidAction.get().perform();
							
							Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
							java.util.Set<javax.validation.ConstraintViolation<T>> violations = validator.validate(editorFlow.getEditingObject());

							if (!violations.isEmpty()) {
								clearInvalidAction.set(markInvalid(editorForm, violations));
							} else {
								editorForm.setSaving(true);
								editorFlow.save();
							}
						}
					});
					
				}
				
				
				
			}
		};
	}

	public static <T> void startChildrenFormEditing(
			final FrameSupplier<T> frameSupplier,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorLayout layout,
			final boolean isNew,
			final ComplexEditing classDataFactory,
			final Supplier<T> editingObject,
			final boolean isReadOnly
	) {
		final Handlers saveHandlers = Handlers.newInstance();
		
		classDataFactory.register(new ComplexEditingListener() {
			
			@Override
			public void onValidate(ValidationErrors errors) {
			}
			
			@Override
			public void onSaved() {
				saveHandlers.fire();
			}
			
			@Override
			public void onFlush() {
			}
			
			@Override
			public boolean isValid() {
				return true;
			}
			
			@Override
			public void focus(Action nextFocus) {
			}
		});
		
		startFormEditing(
				frameSupplier, 
				complexEditorBuilder, 
				messageInterface, 
				defaultUiMessages, 
				layout, 
				RecordsFactory.<T>childrenFormEmbedder(classDataFactory, defaultUiMessages), 
				Calls.<Void>ofInstance(null), 
				isNew, 
				classDataFactory, 
				editingObject,
				isReadOnly,
				saveHandlers
		);
	}

	public static <T> FormEmbedder<T> childrenFormEmbedder(
			final ComplexEditing parentEditing, 
			final DefaultUiMessages defaultUiMessages) {
		return new FormEmbedder<T>() {
			@Override
			public void embed(
					FrameContext<T> frameContext,
					PanelBuilder panelBuilder, 
					Handlers removeHandlers,
					final ObservableValue<Boolean> validStatus,
					final ObservableValue<Boolean> dirtyStatus,
					Handlers saveHandlers
			) {
				BuildingFactory.build(frameContext.panel(), panelBuilder);
				
				final ComplexEditingRegistration complexRegistration = parentEditing.register(new ComplexEditingListener() {
					
					@Override
					public void onValidate(ValidationErrors errors) {
					}
					
					@Override
					public void onSaved() {
					}
					
					@Override
					public void onFlush() {
					}
					
					@Override
					public boolean isValid() {
						return true;
					}
					
					@Override
					public void focus(Action nextFocus) {
					}
				});
				
				removeHandlers.add(Actions.removeHandler(complexRegistration));
				
				HandlerRegistration dirtyRegistration = dirtyStatus.register(new Action() {
					@Override
					public void perform() {
						complexRegistration.setDirty(dirtyStatus.get());
					}
				});
				
				removeHandlers.add(Actions.removeHandler(dirtyRegistration));
				
				
//				frameContext.context().registerListener(new AbstractWidgetListener() {
//					@Override
//					public void onDestroy(WidgetOperation operation) {
//						if (!validStatus.get()) {
//							operation.confirm(defaultUiMessages.closingDirtyEditorWarningMessage(), Action.NONE);
//						} else {
//							operation.approve(Action.NONE);
//						}
//					}
//				});
			}
		};
	}
	
	public static <T> void startFormEditing(
			final FrameSupplier<T> frameSupplier,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorLayout layout,
			final FormEmbedder<T> formEmbedder,
			final Call<Void> call,
			final boolean isNew,
			final ClassDataFactory classDataFactory,
			final Supplier<T> editingObject,
			final boolean isReadOnly,
			final Handlers saveHandlers
	) {
		frameSupplier.open(
				new Callback<FrameContext<T>>() {
					public void onResponse(final FrameContext<T> editorFrame) {
						
						editorFrame.setHeader(defaultUiMessages.loading());

						Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
							@Override
							public void execute() {
								call.join(new Callback<Void>() {
									
									String entityName;
									
									@Override
									public void onResponse(Void value) {
										
										final Handlers removeHandlers = Handlers.newInstance();
										
										final ObservableValue<Boolean> valid = ObservableValues.of(true);
										final ObservableValue<Boolean> dirty = ObservableValues.of(false);

										//final Handlers saveHandlers = Handlers.newInstance();

										
										formEmbedder.embed(editorFrame, new PanelBuilder() {
											@Override
											public void build(Panel formPanel) {
												
												final Tabs entityTabs = formPanel.tabs(new TabsBuilder() {
													@Override
													public void build(TabsBuilding o) {
														o.setSingleTabVisible(false);
														o.setTabbedConnector(new Connector<Panel>() {
															@Override
															public void connect(Panel object, Builder<? super Panel> builder) {
																BuildingFactory.build(object.margin(null).asPanel(), builder);
															}
														});
													}
												});
												
												final Tab mainTab = entityTabs.tab(new TabBuilder() {
													@Override
													public void build(Tab o) {
														o.setClosable(false);
													}
												});
												
												final FormFieldsCollecting formFieldsCollecting = layout.layout(mainTab.asPanel());
												
												
												final AbstractComplexEditorBuilding building = new AbstractComplexEditorBuilding(classDataFactory, new Flag() {
													@Override
													public boolean isSet() {
														return isReadOnly;
													}
												}, messageInterface, defaultUiMessages) {
													
													
													@Override
													public void setEntityName(String name) {
														entityName = name;
														editorFrame.setHeader(isNew ? defaultUiMessages.editingNew(entityName) : editingLabel(entityName, defaultUiMessages, isReadOnly));
														mainTab.setHeader(name);
													}

													@Override
													public Tabs entityTabs() {
														return entityTabs;
													}

													@Override
													public EditorChildrenArea editorChildrenArea(
															EditorChildrenAreaBuilder builder) {
														return formFieldsCollecting.editorChildrenArea(builder);
													}

													@Override
													public LabeledFieldCustomizer addEditorField(
															String label,
															boolean notNull,
															PanelBuilder builder
													) {
														return formFieldsCollecting.addEditorField(label, notNull, builder);
													}

													@Override
													public Panel addFill(
															PanelBuilder builder) {
														return formFieldsCollecting.addFill(builder);
													}

												};
												
												saveHandlers.add(new Action() {
													@Override
													public void perform() {
														building.saved();
														
														editorFrame.setHeader(
																editingLabel(
																		entityName,
																		defaultUiMessages,
																		isReadOnly
																)
														);
													}
												});
												
												
												building.getDirtyHandlers().add(new Action() {
													@Override
													public void perform() {
														dirty.set(building.isDirty());
													}
												});
												
												T editigObjectInstance = editingObject.get();
												
												complexEditorBuilder.buildEditor(editigObjectInstance, building);
												
												editorFrame.associate(editigObjectInstance);
												
//												removeHandlers.add(Actions.removeHandler(editorFrame.context().registerListener(new AbstractWidgetListener() {
//													@Override
//													public void onDestroy(
//															WidgetOperation opertation) {
//														if (building.isDirty()) {
//															opertation.confirm(defaultUiMessages.closingDirtyEditorWarningMessage(), removeHandlers);
//														} else {
//															opertation.approve(removeHandlers);
//														}
//													}
//												})));
												
												
												
											}
										}, removeHandlers, valid, dirty, saveHandlers);
										
										
										
									}
								});
							}
						});
						
						
					}
				}
		);
	}


	public static <T> boolean isSingleSelection(final EditorGrid<T> editorGrid) {
		return editorGrid.getSelection().size()==1;
	}

	public static <T, K> FrameSupplier<T> createTabFrameSupplier(
			final Tabs tabs,
			final DefaultUiMessages defaultUiMessages,
			final Map<K, Tab> editors,
			final Function<? super T, K> modelKeyProvider
	) {
		return new FrameSupplier<T>() {

			@Override
			public void open(
					final Callback<FrameContext<T>> result
			) {
				final Tab tab = tabs.tab(new TabBuilder() {
					@Override
					public void build(Tab o) {
						o.setHeader(defaultUiMessages.loading());
						o.show();
					}
				});

				result.onResponse(new FrameContext<T>() {
					
					@Override
					public WidgetContext context() {
						return tab.asWidgetContext();
					}
					
					@Override
					public void close() {
						tab.remove();
					}

					@Override
					public Panel panel() {
						return tab.asPanel();
					}

					@Override
					public void setHeader(String header) {
						tab.setHeader(header);
					}

					@Override
					public void associate(T editing) {
						RecordsFactory.registerEditorTab(
								modelKeyProvider.apply(editing),
								tab,
								editors
						);
					}

				});
				
			}
			
		};
	}

	public static <T> void registerEditorTab(
			final T selectionKey, 
			Tab value,
			final Map<? super T, Tab> editors
	) {
		editors.put(selectionKey, value);
		value.asWidgetContext().registerListener(new AbstractWidgetListener() {
			public void onDestroy(WidgetOperation operation) {
				operation.approve(new Action() {
					@Override
					public void perform() {
						editors.remove(selectionKey);
					}
				});
			}
		});
	}

	public static <T extends BaseProxy> void page(
			final Panel panel, 
			final WidgetContext context,
			final ProvidesKey<? super T> modelKeyProvider,
			final ClientStore<T> clientStore,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final ComplexEditorConfigurator<T> complexEditorConfigurator,
			final EntityFetcher<T> entityFetcher,
			final ComplexEditorBuilder<T> complexEditorBuilder, 
			final Tabs tabs,
			final Callback<EditorGrid<T>> customizer
	) {
		final EditingPersistence<T> editingPersistence = EditingPersistences.from(clientStore);
		
		final Callbacks<T> onPersist = Callbacks.newInstance();
		final Callbacks<T> onMerge = Callbacks.newInstance();
		final Callbacks<List<T>> onDelete = Callbacks.newInstance();
		
		final EditorGridBinder<T> binder = new EditorGridBinder<T>() {
			@Override
			public HandlerRegistration bind(final EditorGrid<T> editorGrid) {
				return RecordsFactory.bind(
						editorGrid, 
						clientStore,
						defaultUiMessages,
						Executers.APPROVE,
						onPersist,
						onMerge,
						onDelete,
						complexEditorAccessControl,
						Executers.APPROVE,
						editingPersistence,
						messageInterface,
						Action.NONE
				);
			}
			
		};
		
		final EditorGrid<T> editorGrid = pageEditorGrid(
				panel, 
				modelKeyProvider,
				complexEditorAccessControl, 
				messageInterface,
				defaultUiMessages, 
				editorGridConfigurator,
				complexEditorConfigurator, 
				entityFetcher, 
				complexEditorBuilder,
				tabs, 
				customizer, 
				editingPersistence, 
				onPersist, 
				onMerge
		);
		
		RecordsFactory.bindAndRegister(editorGrid, context, binder);
	
		
	}

	public static <T extends BaseProxy> EditorGrid<T> pageEditorGrid(
			final Panel panel,
			final ProvidesKey<? super T> modelKeyProvider,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final ComplexEditorConfigurator<T> complexEditorConfigurator,
			final EntityFetcher<T> entityFetcher,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final Tabs tabs, 
			final Callback<EditorGrid<T>> customizer,
			final EditingPersistence<T> editingPersistence,
			final Callbacks<T> onPersist, 
			final Callbacks<T> onMerge
	) {
		
		return pageEditorGrid(
				panel, 
				modelKeyProvider, 
				complexEditorAccessControl, 
				defaultUiMessages, 
				editorGridConfigurator, 
				tabs, 
				customizer, 
				new NewButtonAdder<T>() {

					@Override
					public void addNewButton(
							EditorGrid<T> editorGrid,
							FrameSupplier<T> frameSupplier,
							SelectionUpdater<T> selectionUpdater,
							EditorLayout childrenLayout
					) {

						RecordsFactory.addFormNewButton(
								editorGrid, 
								complexEditorAccessControl, 
								complexEditorConfigurator, 
								defaultUiMessages, 
								frameSupplier, 
								editingPersistence, 
								complexEditorBuilder,
								messageInterface,
								new Callback<T>() {
									@Override
									public void onResponse(T value) {
										onPersist.fire(value);
										//RecordsFactory.registerEditorTab(modelKeyProvider.apply(value), tab.get(), editors);
									}
								},
								new Callback<T>() {
									@Override
									public void onResponse(T value) {
										onMerge.fire(value);
									}
								},
								selectionUpdater,
								childrenLayout
						);
						
					}
				},
				new EditorStarter<T>() {

					@Override
					public void startEditor(T editingObject,
							FrameSupplier<T> frameSupplier,
							EditorLayout editorLayout) {
						boolean isReadOnly = !complexEditorAccessControl.edit(Suppliers.ofInstance(editingObject));
						RecordsFactory.startFormEditing(
								frameSupplier,
								complexEditorBuilder,
								messageInterface, 
								defaultUiMessages,
								RecordsFactory.editExistingFlow(
										editingPersistence,
										editingObject,
										entityFetcher,
										defaultUiMessages,
										onMerge.callback(),
										isReadOnly
								),
								editorLayout,
								isReadOnly
						);											
					}
				}
		);
		
	}
	
	public static <T> EditorGrid<T> pageEditorGrid(
			final Panel panel,
			final ProvidesKey<? super T> modelKeyProvider,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final DefaultUiMessages defaultUiMessages,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final Tabs tabs, 
			final Callback<EditorGrid<T>> customizer,
			final NewButtonAdder<T> newButtonAdder,
			final EditorStarter<T> editorStarter
	) {
		final EditorGrid<T> editorGrid = RecordsFactory.createEditorGrid(
				panel, 
				modelKeyProvider, 
				editorGridConfigurator
		);
		
		final SelectionUpdater<T> selectionUpdater = new SelectionUpdater<T>() {
			@Override
			public void select(T selection) {
				editorGrid.select(selection);
			}

			@Override
			public void deselectAll() {
				editorGrid.deselectAll();
			}

			@Override
			public void select(List<T> selection) {
				editorGrid.select(selection);
			}
		};
		
		final Map<Object, Tab> editors = Maps.newHashMap();
		
		final FrameSupplier<T> frameSupplier = RecordsFactory.createTabFrameSupplier(
				tabs, 
				defaultUiMessages,
				editors,
				new Function<T, Object>() {
					@Override
					public Object apply(T input) {
						return modelKeyProvider.getKey(input);
					}
				}
		);
		
		final EditorLayout childrenTabsLayout = childrenTabsEditorLayout();
		
		newButtonAdder.addNewButton(
				editorGrid, 
				frameSupplier, 
				selectionUpdater, 
				childrenTabsLayout
		);
		

		Button openButton = editorGrid.button();
		openButton.setLabel(defaultUiMessages.open());
		Action openAction = new Action() {
			@Override
			public void perform() {
				if (!RecordsFactory.isSingleSelection(editorGrid)) return;
				
				final T selection = RecordsFactory.getSingleSelection(editorGrid);
				
				final Object selectionKey = modelKeyProvider.getKey(selection);
				
				if (editors.containsKey(selectionKey)) {
					editors.get(selectionKey).show();
				} else {
					editorStarter.startEditor(
							selection, 
							frameSupplier, 
							childrenTabsLayout
					);
				}

				
			}
		};
		openButton.addListener(openAction);
		RecordsFactory.enableWhenSingleSelection(editorGrid, openButton);
		
		editorGrid.addDoubleClickHandler(openAction);
		
		customizer.onResponse(editorGrid);
		return editorGrid;
	}

	public static EditorLayout childrenTabsEditorLayout() {
		return new EditorLayout() {
			@Override
			public FormFieldsCollecting layout(Panel panel) {
				return childrenTabsLayout(panel);
			}
		};
	}

	public static <T> void bindCache(
			final EditorGrid<T> editorGrid,
			final DefaultUiMessages defaultUiMessages,
			final Executer refreshExecuter, 
			final Handlers handlers,
			final CachedClientStore<T> cachedClientStore
	) {
		new Object() {
			Optional<List<T>> savedSelection = Optional.absent();

			{
				editorGrid.showLoadMask();
				
				handlers.add(Actions.removeHandler(hu.mapro.gwt.data.client.ObjectSource.HandlerRegistrations.of(
						cachedClientStore, 
						new ObjectSourceHandler<T>() {

							@Override
							public void onStartLoading() {
								editorGrid.showLoadMask();
							}

							@Override
							public void onLoad(Iterable<T> values) {
								editorGrid.deselectAll();
								
								// TODO check if is okay to abort any current editing
								editorGrid.replaceValues(Lists.newArrayList(values));
								
								editorGrid.hideLoadMask();
								
								if (savedSelection.isPresent()) {
									editorGrid.select(savedSelection.get());
									savedSelection = Optional.absent();
								}
							}

							@Override
							public void onAdd(T object) {
								editorGrid.addValue(object);
							}

							@Override
							public void onRemove(T object) {
								editorGrid.removeValue(object);
							}

							@Override
							public void onUpdate(T object) {
								editorGrid.updateValue(object);
							}
						}
				)));
						
				
				Button refreshButton = editorGrid.button();
				refreshButton.setLabel(defaultUiMessages.refresh());
				refreshButton.addListener(new Action() {
					@Override
					public void perform() {
						savedSelection = Optional.of(editorGrid.getSelection());
						
						refreshExecuter.execute(
								new Action() {
									@Override
									public void perform() {
										cachedClientStore.reload();
									}
								},
								new Action() {
									@Override
									public void perform() {
										savedSelection = Optional.absent();
									}
								}
						);
					}
				});
				
			}
		};
	}

	public static <T> void bindObjects(
			final ComplexEditing complexEditing,
			final EditorGrid<T> editorGrid,
			final Handlers removeHandlers,
			final ObjectSource<T> cachedClientStore
	) {
		editorGrid.showLoadMask();
		
		final ComplexEditingRegistration complexRegistration = complexEditing.register(new ComplexEditingListener() {
			
			@Override
			public void onValidate(ValidationErrors errors) {
			}
			
			@Override
			public void onSaved() {
			}
			
			@Override
			public void onFlush() {
			}
			
			@Override
			public boolean isValid() {
				return true;
			}
			
			@Override
			public void focus(Action nextFocus) {
			}
		});
		
		removeHandlers.add(Actions.removeHandler(complexRegistration));
		
		removeHandlers.add(Actions.removeHandler(hu.mapro.gwt.data.client.ObjectSource.HandlerRegistrations.of(
				cachedClientStore, 
				new ObjectSourceHandler<T>() {

					@Override
					public void onStartLoading() {
						editorGrid.showLoadMask();
					}

					@Override
					public void onLoad(Iterable<T> values) {
						// TODO check if is okay to abort any current editing
						editorGrid.replaceValues(Lists.newArrayList(values));
						
						editorGrid.hideLoadMask();
					}

					@Override
					public void onAdd(T object) {
						editorGrid.addValue(object);
						
						complexRegistration.setDirty(true);
					}

					@Override
					public void onRemove(T object) {
						editorGrid.removeValue(object);
						
						complexRegistration.setDirty(true);
					}

					@Override
					public void onUpdate(T object) {
						editorGrid.updateValue(object);
					}
				}
		)));
		
	}

	public static <T extends BaseProxy> void inline(
			final Panel panel, 
			final WidgetContext context,
			final ProvidesKey<? super T> modelKeyProvider,
			final ClientStore<T> clientStore,
			final InlineEditorGridConfigurator<T> editorGridConfigurator,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final InstanceFactory<T> instanceFactory,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages
	) {
		final EditingPersistence<T> editingPersistence = EditingPersistences.from(clientStore);

		new Object() {
			Callbacks<T> onPersist = Callbacks.newInstance();
			Callbacks<T> onMerge = Callbacks.newInstance();
			Callbacks<List<T>> onDelete = Callbacks.newInstance();
			
			{

				RecordsFactory.widget(
						panel,
						context,
						new EditorGridCreator<T>() {
							@Override
							public EditorGrid<T> create(Panel panel) {
								return inlineGrid(
										modelKeyProvider,
										editorGridConfigurator,
										complexEditorAccessControl,
										instanceFactory, 
										defaultUiMessages,
										editingPersistence, 
										panel,
										onPersist,
										onMerge
								);
							}
						},
						new EditorGridBinder<T>() {
							@Override
							public HandlerRegistration bind(final EditorGrid<T> editorGrid) {
								return RecordsFactory.bind(
										editorGrid, 
										clientStore,
										defaultUiMessages,
										Executers.APPROVE,
										onPersist,
										onMerge,
										onDelete,
										complexEditorAccessControl,
										Executers.APPROVE,
										editingPersistence,
										messageInterface,
										Action.NONE
								);
							}
							
						}
						
				);
				
				
			}
		};
		
	}

	public static <T extends BaseProxy> InlineEditorGrid<T> inlineGrid(
			final ProvidesKey<? super T> modelKeyProvider,
			final InlineEditorGridConfigurator<T> editorGridConfigurator,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final InstanceFactory<T> instanceFactory,
			final DefaultUiMessages defaultUiMessages,
			final EditingPersistence<T> editingPersistence, 
			final Panel panel,
			final Callbacks<T> onPersist,
			final Callbacks<T> onMerge
			
	) {
		final InlineEditorGrid<T> editorGrid = panel.inlineEditorGrid(new InlineEditorGridBuilder<T>() {
			@Override
			public void build(final InlineEditorGridBuilding<T> o) {
				o.setModelKeyProvider(modelKeyProvider);
				
				final Handlers closeHandlers = destroyHandlers(panel.asWidgetContext());
				
				editorGridConfigurator.configure(new InlineEditorGridConfigurating<T>() {
					@Override
					public <V> GridColumnCustomizer<V> addColumn(
							Function<? super T, ? extends ObservableValue<V>> getter,
							String path) {
						return o.addColumn(getter, path);
					}

					@Override
					public <V> GridColumnCustomizer<V> addEditableColumn(
							Function<? super T, ? extends ObservableValue<V>> getter, 
							FieldConstructor<V> fieldConstructor,
							String path
					) {
						return o.addEditableColumn(getter, fieldConstructor, path);
					}

					@Override
					public Handlers closeHandlers() {
						return closeHandlers;
					}
				});
				
				o.setInlineEditorGridHandler(new InlineEditorGridHandler<T>() {
					@Override
					public InstanceEditingHandler<T> startEdit(final T object) {
						final EditingPersistenceContext<T> ctx = editingPersistence.newEditingContext();
						
						final T editingObject = ctx.edit(object);
						
						return new InstanceEditingHandler<T>() {
							@Override
							public void onSave() {
								ctx.merge(editingObject, new AbstractReceiver<T>() {
									@Override
									public void onSuccess(T response) {
										onMerge.fire(response);
									}
								});
							}
							
							@Override
							public void onCancel() {
							}

							@Override
							public T getEditingObject() {
								return editingObject;
							}
						};
					}

					@Override
					public boolean canEdit(T object) {
						return complexEditorAccessControl.edit(Suppliers.ofInstance(object));
					}
				});
			}
		});
		
		if (complexEditorAccessControl.newButton()) {
			Button button = editorGrid.button();
			button.setLabel(defaultUiMessages._new());
			button.addListener(new Action() {
				@Override
				public void perform() {
					editorGrid.stopEditing(new Action() {
						@Override
						public void perform() {
							final EditingPersistenceContext<T> ctx = editingPersistence.newEditingContext();
							
							final T instance = instanceFactory.create(ctx);
							
							editorGrid.editNew(instance, new InstanceSavingHandler() {
								@Override
								public void onSave() {
									ctx.persist(instance, new AbstractReceiver<T>() {
										@Override
										public void onSuccess(T response) {
											editorGrid.select(response);
											onPersist.fire(response);
										}
									});
								}
								
								@Override
								public void onCancel() {
								}

							});
						}
					});
				}
			});
		}
		
		return editorGrid;
	}

	public static class SplitFormElements<T> {
		
		public EditorGrid<T> editorGrid;
		public Panel formPanel;
		public SplitFormElements(EditorGrid<T> editorGrid, Panel formPanel) {
			super();
			this.editorGrid = editorGrid;
			this.formPanel = formPanel;
		}
		public SplitFormElements() {
		}
		
	}
	
	public interface SplitFormLayout {
		
		<T> SplitFormElements<T> layout(Panel panel, EditorGridBuilder<T> editorGridBuilder);
		
	}
	
	public static <T extends BaseProxy> void form(
				final Panel panel, 
				final WidgetContext context,
				final ProvidesKey<? super T> modelKeyProvider,
				final ClientStore<T> clientStore,
				final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
				final MessageInterface messageInterface,
				final DefaultUiMessages defaultUiMessages,
				final EditorGridConfigurator<T> editorGridConfigurator,
				final ComplexEditorConfigurator<T> complexEditorConfigurator,
				final EntityFetcher<T> entityFetcher,
				final ComplexEditorBuilder<T> complexEditorBuilder,
				final Callback<EditorGrid<T>> customizer,
				final SplitFormLayout splitFormLayout
				
		) {
			final EditingPersistence<T> editingPersistence = EditingPersistences.from(clientStore);
			final Callbacks<T> onPersist = Callbacks.newInstance();
			final Callbacks<T> onMerge = Callbacks.newInstance();
			final Callbacks<List<T>> onDelete = Callbacks.newInstance();
			final Wrapper<Action> forceClose = Wrapper.of(Action.NONE);
		
			EditorGridBinder<T> binder = new EditorGridBinder<T>() {
				@Override
				public HandlerRegistration bind(final EditorGrid<T> editorGrid) {
					return RecordsFactory.bind(
							editorGrid, 
							clientStore,
							defaultUiMessages,
							Executers.APPROVE,
							onPersist,
							onMerge,
							onDelete,
							complexEditorAccessControl,
							Executers.APPROVE,
							editingPersistence,
							messageInterface,
							new Action() {
								@Override
								public void perform() {
									forceClose.get().perform();
								}
							}
					);
				}
				
			};
		
		
	
			final EditorGrid<T> editorGrid = formEditorGrid(
					panel, 
					context,
					modelKeyProvider, 
					complexEditorAccessControl,
					messageInterface, 
					defaultUiMessages,
					editorGridConfigurator, 
					complexEditorConfigurator,
					entityFetcher, 
					complexEditorBuilder, 
					splitFormLayout,
					editingPersistence, 
					onPersist, 
					onMerge, 
					forceClose
			);

			
			bindAndRegister(
					editorGrid, 
					context, 
					binder
			);
			
			customizer.onResponse(editorGrid);
			
		}

	
	public interface NewButtonAdder<T> {
		
		void addNewButton(
				EditorGrid<T> editorGrid,
				FrameSupplier<T> frameSupplier,
				SelectionUpdater<T> selectionUpdater,
				EditorLayout childrenFieldsLayout
				//Wrapper<Executer> startNewEditingApprover
		);
		
	}
	
	public interface EditorStarter<T> {
		void startEditor(T editingObject, FrameSupplier<T> frameSupplier,
				EditorLayout editorLayout);
	}

	
	public static <T extends BaseProxy> EditorGrid<T> formEditorGrid(
			final Panel panel,
			final WidgetContext context,
			final ProvidesKey<? super T> modelKeyProvider,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final ComplexEditorConfigurator<T> complexEditorConfigurator,
			final EntityFetcher<T> entityFetcher,
			final ComplexEditorBuilder<T> complexEditorBuilder,
			final SplitFormLayout splitFormLayout,
			final EditingPersistence<T> editingPersistence,
			final Callbacks<T> onPersist, 
			final Callbacks<T> onMerge,
			final Wrapper<Action> forceClose
	) {
		return formEditorGrid(
				panel, 
				context, 
				modelKeyProvider, 
				complexEditorAccessControl, 
				messageInterface, 
				defaultUiMessages, 
				editorGridConfigurator, 
				splitFormLayout, 
				forceClose, 
				new NewButtonAdder<T>() {

					@Override
					public void addNewButton(
							EditorGrid<T> editorGrid,
							FrameSupplier<T> frameSupplier,
							SelectionUpdater<T> selectionUpdater,
							EditorLayout childrenFieldsLayout
							//Wrapper<Executer> startNewEditingApprover
					) {
						addFormNewButton(
								editorGrid, 
								complexEditorAccessControl, 
								complexEditorConfigurator, 
								defaultUiMessages, 
								frameSupplier, 
								editingPersistence, 
								complexEditorBuilder,
								messageInterface,
								new Callback<T>() {
									@Override
									public void onResponse(T value) {
										onPersist.fire(value);
									}
								},
								new Callback<T>() {
									@Override
									public void onResponse(T value) {
										onMerge.fire(value);
									}
								},
								selectionUpdater,
								childrenFieldsLayout
						);
						
					}
					
				},
				new EditorStarter<T>() {

					@Override
					public void startEditor(
							T editingObject,
							FrameSupplier<T> frameSupplier,
							EditorLayout childrenFieldsLayout
					) {
						boolean isReadOnly = !complexEditorAccessControl.edit(Suppliers.ofInstance(editingObject));
						
						startFormEditing(
								frameSupplier,
								complexEditorBuilder,
								messageInterface, 
								defaultUiMessages,
								editExistingFlow(
										editingPersistence, 
										editingObject, 
										entityFetcher, 
										defaultUiMessages, 
										onMerge.callback(),
										isReadOnly
								),
								childrenFieldsLayout,
								isReadOnly
						);
					}
					
				}
		);
		
		
	}
	
	public static <T> EditorGrid<T> formEditorGrid(
			final Panel panel,
			final WidgetContext context,
			final ProvidesKey<? super T> modelKeyProvider,
			final ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			final MessageInterface messageInterface,
			final DefaultUiMessages defaultUiMessages,
			final EditorGridConfigurator<T> editorGridConfigurator,
			final SplitFormLayout splitFormLayout,
			final Wrapper<Action> forceClose,
			NewButtonAdder<T> newButtonAdder,
			final EditorStarter<T> editorStarter
	) {
		
		
		
		final Wrapper<Boolean> suppressSelectionHandler = Wrapper.of(false);
		final Wrapper<Executer> changeSelectionExecutor = Wrapper.of(Executers.APPROVE);
		
		SplitFormElements<T> elements = splitFormLayout.layout(
				panel.margin(null).asPanel(), 
				new EditorGridBuilder<T>() {
					@Override
					public void build(EditorGridBuilding<T> o) {
						BuildingFactory.build(o, createEditorGridBuilder(modelKeyProvider, editorGridConfigurator, panel.asWidgetContext()));
					}
				}
		);
		
		final EditorGrid<T> editorGrid = elements.editorGrid;
		final Panel formPanel = elements.formPanel;

		final SelectionUpdater<T> selectionUpdater = new SelectionUpdater<T>() {
			@Override
			public void select(T selection) {
				suppressSelectionHandler.set(true);
				editorGrid.select(selection);
				suppressSelectionHandler.set(false);
			}

			@Override
			public void deselectAll() {
				suppressSelectionHandler.set(true);
				editorGrid.deselectAll();
				suppressSelectionHandler.set(false);
			}

			@Override
			public void select(List<T> selection) {
				suppressSelectionHandler.set(true);
				editorGrid.select(selection);
				suppressSelectionHandler.set(false);
			}
		};
		
		final FrameSupplier<T> frameSupplier = new FrameSupplier<T>() {
			
			@Override
			public void open(
					final Callback<FrameContext<T>> result
			) {
				changeSelectionExecutor.get().execute(
						new Action() {
							@Override
							public void perform() {
								final Frame frame = formPanel.frame(null);
								
								final AbstractWidgetContext wc = new AbstractWidgetContext(WidgetContextSupports.from(context, messageInterface, defaultUiMessages)) {

									@Override
									public void bringToFrontInContainer() {
									}

									@Override
									public boolean isOnTopInContainer() {
										return true;
									}
									
								};
								
								changeSelectionExecutor.set(new Executer() {
									@Override
									public void execute(Action approve, Action veto) {
										wc.fireDestroy(approve, veto);
									}
								});
								
								forceClose.set(new Action() {
									@Override
									public void perform() {
										wc.drop();
										formPanel.clear();
										changeSelectionExecutor.set(Executers.APPROVE);
									}
								});
								
								result.onResponse(new FrameContext<T>() {
									
									@Override
									public WidgetContext context() {
										return wc;
									}
									
									@Override
									public void close() {
										formPanel.clear();
									}

									@Override
									public void setHeader(String header) {
										frame.setHeader(header);
									}

									@Override
									public Panel panel() {
										return frame.asPanel();
									}

									@Override
									public void associate(T editing) {
									}
								});
								
							}
						},
						Action.NONE
				);
			}
			
		};
		
		
		final EditorLayout childrenFieldsLayout = formEditorLayout();
		
		
		newButtonAdder.addNewButton(
				editorGrid, 
				frameSupplier, 
				selectionUpdater,
				childrenFieldsLayout
				//changeSelectionExecutor
		);
		
		
		editorGrid.addSelectionChangeHandler(new Action() {
			private List<T> lastSelection;

			@Override
			public void perform() {
				final List<T> savedLastSelection = lastSelection;
				lastSelection = ImmutableList.copyOf(editorGrid.getSelection());
				
				if (!suppressSelectionHandler.get()) {
					
					changeSelectionExecutor.get().execute(
							new Action() {
								@Override
								public void perform() {
									doSelect(lastSelection);
								}
							},
							new Action() {
								@Override
								public void perform() {
									selectionUpdater.select(savedLastSelection);
								}
							}
					);
				}
			}
			
			private void doSelect(List<T> s) {
				boolean single = s.size()==1;
				if (single) {
					final T selection = s.get(0);

					editorStarter.startEditor(selection, frameSupplier, childrenFieldsLayout);
							
				} else {
					formPanel.clear();
					changeSelectionExecutor.set(Executers.APPROVE);
					forceClose.set(Action.NONE);
				}
			}
		});
		return editorGrid;
	}

	public static EditorLayout formEditorLayout() {
		return new EditorLayout() {
			@Override
			public FormFieldsCollecting layout(final Panel panel) {
				final Fields fields = panel.scroll(null).asPanel().margin(new MarginBuilder() {
					@Override
					public void build(Margin o) {
						o.setWidth(10);
					}
				}).asPanel().fields(null);
				
				return new FormFieldsCollecting() {
					
					@Override
					public EditorChildrenArea editorChildrenArea(
							final EditorChildrenAreaBuilder builder) {
						return EditorChildrenAreas.from(fields, builder, panel.asWidgetContext());
					}
					
					@Override
					public LabeledFieldCustomizer addEditorField(
							final String label,
							final boolean notNull,
							final PanelBuilder builder
					) {
						return LabeledFieldCustomizers.editorField(fields, label, notNull, builder);
					}

					@Override
					public Panel addFill(PanelBuilder builder) {
						return fields.fill(builder);
					}
				};
			}
		};
	}

	protected static <T> void doSwap(T oldEditing, T currentEditing) {
		if (oldEditing!=null) {
			((ObservableSwappable)Autobeans.getTag(oldEditing, ObservableObjectWrapper.TAG_NAME)).swap(currentEditing);
		}
	}

	public static <T extends BaseProxy> EditorFlow<T> newObjectEditorFlow(
			final DefaultUiMessages defaultUiMessages,
			final EditingPersistence<T> editingPersistence,
			final Callback<T> onPersist, 
			final Callback<T> onMerge,
			final SelectionUpdater<T> selectionUpdater,
			final EditingPersistenceContext<T> ctx, 
			final T editingObject
	) {
		return new EditorFlow<T>() {
			
			boolean persistent = false;
			T savedEditing = editingObject;
			T currentEditing = editingObject;
			
			EditingPersistenceContext<T> currentContext = ctx;
			Receiver<T> saveReceiver;
			Request<?> saveRequest;
			
			@Override
			public Call<Void> init() {
				selectionUpdater.deselectAll();
				return Calls.ofInstance(null);
			}
			
			@Override
			public String getHeaderName(
					String entityName) {
				return persistent ? defaultUiMessages.editing(entityName) : defaultUiMessages.editingNew(entityName);
			}

			@Override
			public T getEditingObject() {
				return currentEditing;
			}

			@Override
			public void setSaveReceiver(Receiver<T> done) {
				this.saveReceiver = done;
				if (persistent) {
					saveRequest = currentContext.merge(currentEditing, new ForwardReceiver<T>(done) {
						@Override
						protected void process(T response) {
							onMerge.onResponse(response);
							afterSave(response);
						}

					});
				} else {
					saveRequest = currentContext.persist(currentEditing, new ForwardReceiver<T>(done) {
						@Override
						protected void process(T response) {
							onPersist.onResponse(response);
							selectionUpdater.select(response);
							persistent = true;
							afterSave(response);
						}
					});
				}
			}

			@Override
			public ClassDataFactory getClassDataFactory() {
				return currentContext;
			}

			@Override
			public boolean isNew() {
				return !persistent;
			}

			protected void afterSave(T response) {
				currentContext = editingPersistence.newEditingContext();
				setSaveReceiver(saveReceiver);
				savedEditing = currentEditing;
				currentEditing = currentContext.edit(currentEditing);
				doSwap(savedEditing, currentEditing);
			}

			@Override
			public T getSavedObject() {
				return savedEditing;
			}

			@Override
			public void save() {
				saveRequest.fire();
			}
		};
	}
	
	public static <T extends BaseProxy> EditorFlow<T> editExistingFlow(
			final EditingPersistence<T> editingPersistence, 
			final T selection,
			final EntityFetcher<T> entityFetcher,
			final DefaultUiMessages defaultUiMessages,
			final Callback<T> onMerge,
			final boolean isReadOnly
			
	) {
		return new EditorFlow<T>() {
			
			T currentEditing;
			EditingPersistenceContext<T> currentContext;
			T savedEditing;
			
			Receiver<T> saveReceiver;
			Request<?> saveRequest;
			
			@Override
			public Call<Void> init() {
				final Call<Void> call = new Call<Void>();
				entityFetcher.fetch(selection, new Callback<T>() {
					@Override
					public void onResponse(
							T value) {
						savedEditing = value;
						doStartEdit(value);
						call.onResponse(null);
					}
				});
				return call;
			}
			
			@Override
			public String getHeaderName(
					String entityName) {
				return editingLabel(entityName, defaultUiMessages, isReadOnly);
			}
	
			@Override
			public T getEditingObject() {
				return currentEditing;
			}
	
			private void doStartEdit(T response) {
				currentContext = editingPersistence.newEditingContext();
				T oldEditing = currentEditing;
				currentEditing = currentContext.edit(response);
				doSwap(oldEditing, currentEditing);
			}
			
			@Override
			public ClassDataFactory getClassDataFactory() {
				return currentContext;
			}

			@Override
			public boolean isNew() {
				return false;
			}

			@Override
			public T getSavedObject() {
				return savedEditing;
			}

			@Override
			public void setSaveReceiver(Receiver<T> done) {
				this.saveReceiver = done;
				saveRequest = currentContext.merge(currentEditing, new ForwardReceiver<T>(done) {
					@Override
					protected void process(T response) {
						onMerge.onResponse(currentEditing);
						savedEditing = currentEditing;
						doStartEdit(currentEditing);
						setSaveReceiver(saveReceiver);
					}
				});
			}

			@Override
			public void save() {
				saveRequest.fire();
			}
		};
	}

	public static Handlers destroyHandlers(final WidgetContext widgetContext) {
		final Handlers closeHandlers = Handlers.newInstance();
		
		widgetContext.registerListener(new AbstractWidgetListener() {
			@Override
			public void onDestroy(WidgetOperation operation) {
				operation.approve(closeHandlers);
			}
		});
		return closeHandlers;
	}

	public static <T> void safeSetValue(
			Consumer<T> target,
			T value,
			ComplexEditing complexEditing,
			Action undo
	) {	
		try {
			target.set(value);
		} catch (CannotSetValueException e) {
			complexEditing.messageInterface().alert(
					complexEditing.defaultUiMessages().cannotSetValueTitle(),
					complexEditing.defaultUiMessages().cannotSetValueMessage(),
					undo
			);
		}
	}

	public static String editingLabel(
			String entityName,
			final DefaultUiMessages defaultUiMessages, 
			final boolean isReadOnly
	) {
		return isReadOnly ? 
				defaultUiMessages.viewing(entityName)
				:
				defaultUiMessages.editing(entityName);
	}

	public static Action markInvalid(
			EditorForm editorForm, 
			java.util.Set<? extends javax.validation.ConstraintViolation<?>> violations
	) {
		final Set<Validatable> toClear = Sets.newHashSet();
		
		List<ValidationError> errorList = Lists.newArrayList();
		
		for (final ConstraintViolation<?> v : violations) {
			final Validatable validatable = ObservableObjectWrapper.getValidatable(v.getLeafBean(), v.getPropertyPath().toString());
			toClear.add(validatable);
			
			validatable.markInvalid(v.getMessage());
			
			errorList.add(new ValidationError() {
				
				@Override
				public void display() {
					validatable.display();
				}
				
				@Override
				public String getMessage() {
					return v.getMessage();
				}
			});
			
		}
		
		editorForm.showValidationErrors(errorList);
		
		return new Action() {
			@Override
			public void perform() {
				for (Validatable v : toClear) {
					v.clearInvalid();
				}
			}
		};
	}

	
	
}


