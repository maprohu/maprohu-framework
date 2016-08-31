package hu.mapro.gwtui.client.edit.impl;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.CachedClientStoreHandler;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.ClientStoreReader;
import hu.mapro.gwt.data.client.EditingPersistence;
import hu.mapro.gwt.data.client.EditingPersistenceContext;
import hu.mapro.gwt.data.client.ListResult;
import hu.mapro.gwt.data.client.MoreSuppliers;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.gwtui.client.app.ManagedWidget;
import hu.mapro.gwtui.client.app.WidgetBuilder;
import hu.mapro.gwtui.client.edit.CancelEvent;
import hu.mapro.gwtui.client.edit.CancelEvent.CancelHandler;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.ComplexEditingRegistration;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilding;
import hu.mapro.gwtui.client.edit.ComplexEditorConfigurating;
import hu.mapro.gwtui.client.edit.ComplexEditorConfigurator;
import hu.mapro.gwtui.client.edit.ComplexEditorPageAccessControl;
import hu.mapro.gwtui.client.edit.FormEditorGridBuilder;
import hu.mapro.gwtui.client.edit.EditorForm;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.LabeledFieldCustomizer;
import hu.mapro.gwtui.client.edit.ManagedComplexEditorPage;
import hu.mapro.gwtui.client.edit.SaveEvent;
import hu.mapro.gwtui.client.edit.SaveEvent.SaveHandler;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.grid.EditorGrid;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.gwtui.client.grid.Paging;
import hu.mapro.gwtui.client.grid.PagingControl;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.menu.SubButton;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.jpa.model.domain.client.AutoBeans.Factory;
import hu.mapro.jpa.model.domain.client.AutoBeans.ListConfigProxy;
import hu.mapro.jpa.model.domain.client.ListConfigBuilder;
import hu.mapro.model.Wrapper;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class DefaultComplexEditor<T> implements WidgetBuilder {

	final protected String noneSelectedLabel = "";
	
	protected final MessageInterface messageInterface;
	protected final DefaultUiMessages defaultUiMessages;
	protected final ClientStore<T> clientStore;
	protected final ComplexEditorPageAccessControl<T> complexEditorAccessControl; 
	protected final Function<? super T, String> modelKeyProvider;
	protected final String gridHeader;
	protected final FormEditorGridBuilder complexEditorWidgetBuilder;
	protected final ComplexEditorBuilder<T> complexEditorBuilder;
	protected final EditorGridConfigurator<T> editorGridConfigurator;
	protected final ComplexEditorConfigurator<T> complexEditorConfigurator;
	
	public DefaultComplexEditor(
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages, ClientStore<T> clientStore,
			ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			Function<? super T, String> modelKeyProvider, String gridHeader,
			FormEditorGridBuilder complexEditorWidgetBuilder,
			ComplexEditorBuilder<T> complexEditorBuilder,
			EditorGridConfigurator<T> editorGridConfigurator,
			ComplexEditorConfigurator<T> complexEditorConfigurator) {
		super();
		this.messageInterface = messageInterface;
		this.defaultUiMessages = defaultUiMessages;
		this.clientStore = clientStore;
		this.complexEditorAccessControl = complexEditorAccessControl;
		this.modelKeyProvider = modelKeyProvider;
		this.gridHeader = gridHeader;
		this.complexEditorWidgetBuilder = complexEditorWidgetBuilder;
		this.complexEditorBuilder = complexEditorBuilder;
		this.editorGridConfigurator = editorGridConfigurator;
		this.complexEditorConfigurator = complexEditorConfigurator;
	}

	protected void postEntitiesEditorPanelBuild() {
	}

	protected class WidgetImpl {

		public ManagedComplexEditorPage<T> complexEditorPage;
		public Optional<EditingContext> currentContext = Optional.absent();
		public EditorGrid<T> entitiesEditorPanel;
		
		StoreImpl storeImpl;
		
		public abstract class StoreImpl implements ClassDataFactory {
			
			final EditingPersistence<T> editingPersistence;
			Optional<EditingPersistenceContext<T>> editingPersistenceContext = Optional.absent();
			
			public StoreImpl(EditingPersistence<T> editingPersistence) {
				super();
				this.editingPersistence = editingPersistence;
			}
			
			private EditingPersistenceContext<T> context() {
				return editingPersistenceContext.get();
			}
			
			private EditingPersistenceContext<T> closing() {
				EditingPersistenceContext<T> result = context();
				closeContext();
				return result;
			}
			
			public void openContext() {
				editingPersistenceContext = Optional.of(editingPersistence.newEditingContext());
			}

			private void closeContext() {
				editingPersistenceContext = Optional.absent();
			}
			
			public T edit(T editingObject) {
				return context().edit(editingObject);
			}
			
			public void delete(List<T> selectedItems, Receiver<Void> receiver) {
				closing().delete(selectedItems, receiver);
			}
			
			public void cancelEditing() {
				closeContext();
			}
			
			public void save(T editingObject, Receiver<T> receiver) {
				closing().persist(editingObject, receiver);
			}
			
			public void merge(T editingObject, Receiver<T> receiver) {
				closing().merge(editingObject, receiver);
			}
			
			@Override
			public <C extends BaseProxy> C create(Class<C> clazz) {
				return editingPersistenceContext.get().create(clazz);
			}
			
		}
		
		public class CachedStoreImpl extends StoreImpl {
			//private boolean loaded = false;
			
			Optional<List<T>> savedSelection = Optional.absent();

			public void replaceAll(Iterable<T> values) {
				// TODO check if is okay to abort any current editing
				entitiesEditorPanel.replaceValues(Lists.newArrayList(values));
				
		//		loaded = true;
				entitiesEditorPanel.hideLoadMask();
				
				if (savedSelection.isPresent()) {
					entitiesEditorPanel.select(savedSelection.get());
					savedSelection = Optional.absent();
				}
			}
			
			
			public CachedStoreImpl(final CachedClientStore<T> cachedClientStore) {
				super(cachedClientStore);
				
				entitiesEditorPanel.showLoadMask();
				
				cachedClientStore.load(new Callback<Iterable<T>>() {

					@Override
					public void onResponse(Iterable<T> value) {
						replaceAll(value);
						
						HandlerRegistration registration = cachedClientStore.addCachedClientStoreHandler(new CachedClientStoreHandler<T>() {

							@Override
							public void onStartLoading() {
								entitiesEditorPanel.showLoadMask();
							}

							@Override
							public void onLoad(Iterable<T> objects) {
								replaceAll(objects);
							}

							@Override
							public void onAdd(T object) {
								entitiesEditorPanel.addValue(object);
							}

							@Override
							public void onRemove(T object) {
								entitiesEditorPanel.removeValue(object);
							}

							@Override
							public void onUpdate(T object) {
								entitiesEditorPanel.updateValue(object);
							}
						});
						
						complexEditorPage.addCloseHandler(hu.mapro.gwt.common.client.Actions.removeHandler(registration));
						
					}
					
				});
				
				
				
				Button refreshButton = entitiesEditorPanel.button();
				refreshButton.setLabel(defaultUiMessages.refresh());
				refreshButton.addListener(new Action() {
					@Override
					public void perform() {
						savedSelection = Optional.of(entitiesEditorPanel.getSelection());
						
						stopConfirmed(new Action() {
							@Override
							public void perform() {
								cachedClientStore.reload();
							}
						});
					}
				});
			}
			
		}

		public class UncachedStoreImpl extends StoreImpl {

			private PagingControl pagingControl;
			Optional<T> lastSaved = Optional.absent();

			public UncachedStoreImpl(final UncachedClientStore<T> uncachedClientStore) {
				super(uncachedClientStore);
				
				pagingControl = entitiesEditorPanel.setPaging(new Paging<T>() {
					@Override
					public void load(final int offset, final int limit,
							final Receiver<ListResult<T>> receiver) {
						
						entitiesEditorPanel.showLoadMask();
						
						uncachedClientStore.list(new ListConfigBuilder() {
							@Override
							public void buildListConfig(ListConfigProxy listConfigProxy, Factory factory) {
								listConfigProxy.setFirstResult(offset);
								listConfigProxy.setMaxResults(limit);
							}
						}, new AbstractReceiver<ListResult<T>>() {
							@Override
							public void onSuccess(ListResult<T> response) {
								entitiesEditorPanel.replaceValues(response.getList());
								entitiesEditorPanel.hideLoadMask();
								
								if (lastSaved.isPresent()) {
									entitiesEditorPanel.select(lastSaved.get());
									lastSaved = Optional.absent();
								}
								
								receiver.onSuccess(response);
							}
						});
						
					}
				});
				
				pagingControl.refresh();
			}
			
			@Override
			public void delete(List<T> selectedItems, final Receiver<Void> receiver) {
				super.delete(selectedItems, new AbstractReceiver<Void>() {
					@Override
					public void onSuccess(Void response) {
						receiver.onSuccess(response);
						pagingControl.refresh();
					}
				});
			}

			public void merge(T editingObject, final com.google.web.bindery.requestfactory.shared.Receiver<T> receiver) {
				super.merge(editingObject, new AbstractReceiver<T>() {
					@Override
					public void onSuccess(T response) {
						receiver.onSuccess(response);
						pagingControl.refresh();
					}
				});
			}
			
			public void save(T editingObject, final com.google.web.bindery.requestfactory.shared.Receiver<T> receiver) {
				super.save(editingObject, new AbstractReceiver<T>() {
					@Override
					public void onSuccess(T response) {
						receiver.onSuccess(response);
						lastSaved = Optional.of(response);
						pagingControl.refresh();
					}
				});
			}
			
		}
		
		public WidgetImpl() {
			complexEditorPage = complexEditorWidgetBuilder.formEditorGrid(
					modelKeyProvider,
					editorGridConfigurator
			);
			
			entitiesEditorPanel = complexEditorPage.getEditorGrid();
			
			complexEditorPage.setNoneSelectedLabel(noneSelectedLabel );
			
			clientStore.register(new ClientStoreReader<T>() {

				@Override
				public void cached(CachedClientStore<T> store) {
					storeImpl = new CachedStoreImpl(store);
				}

				@Override
				public void uncached(UncachedClientStore<T> store) {
					storeImpl = new UncachedStoreImpl(store);
				}
			});
			
			if (complexEditorAccessControl.newButton()) {
				final MultiButton newButton = entitiesEditorPanel.multiButton();
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

								stopConfirmed(
										new Action() {
											@Override
											public void perform() {
												entitiesEditorPanel.deselectAll();
												
												startEdit(new EditingContextCreate(MoreSuppliers.from(instanceFactory, storeImpl)));
											}
										}
								);
								
								
							}
						});
						
					}
				});
				
			}
			
			
			entitiesEditorPanel.addSelectionChangeHandler(new Action() {
				@Override
				public void perform() {
					fireSelectionChangedActions();
				}
			});
			
			if (complexEditorAccessControl.deleteButton()) {
				final Button deleteButton = entitiesEditorPanel.button();
				deleteButton.setLabel(defaultUiMessages.delete());
				enableWhenSelectionNotEmpty(deleteButton);
				deleteButton.addListener(new Action() {
					
					@Override
					public void perform() {
						final List<T> selectedItems = entitiesEditorPanel.getSelection();
						
						Action deleteAction = new Action() {

							@Override
							public void perform() {
								storeImpl.delete(selectedItems, new AbstractReceiver<Void>() {
									@Override
									public void onSuccess(Void response) {
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
				});

			}

			postEntitiesEditorPanelBuild();
			
			fireSelectionChangedActions();
			
			
			
		
			
		}


		public void enableWhenSingleSelection(final Button button) {
			Action selectionListener = new Action() {
				@Override
				public void perform() {
					List<T> selection = entitiesEditorPanel.getSelection();
					button.setEnabled(selection.size()==1);
				}
			};
			addAndPerformSelectionChangeHandler(selectionListener);
		}
		
		public void enableWhenSelectionNotEmpty(final Button button) {
			Action selectionListener = new Action() {
				@Override
				public void perform() {
					List<T> selection = entitiesEditorPanel.getSelection();
					button.setEnabled(!selection.isEmpty());
				}
			};
			addAndPerformSelectionChangeHandler(selectionListener);
		}

		public T getSingleSelection() {
			return Iterables.getOnlyElement(entitiesEditorPanel.getSelection());
		}


		public void addAndPerformSelectionChangeHandler(
				Action selectionListener) {
			entitiesEditorPanel.addSelectionChangeHandler(selectionListener);
			selectionListener.perform();
		}
		
		
		private void startEdit(
				final EditingContext ctx
		) {
			stopConfirmed(
				new Action() {
					@Override
					public void perform() {
						ctx.doStartEdit();
					}
				}
			);
			
		}

		abstract class EditingContext implements ComplexEditing {
			T editingObject;
			EditorForm editorForm;
			String entityName;
			Action focusAction = Action.NONE;
			
			private EditingContext(
					Supplier<? extends T> editingObject
			) {
				super();
				storeImpl.openContext();
				
				this.editingObject = editingObject.get();
			}

			public void stopConfirmed(final Action action) {
				Action doStopAction = new Action() {
					
					@Override
					public void perform() {
						doStop();
						action.perform();
					}
				};
				
				if (isDirty()) {
					messageInterface.confirm(
							"Discard Changes", 
							"You have unsaved changes. Are you sure you want to discard them?",
							doStopAction,
							new Action() {
								@Override
								public void perform() {
									suppressSelectionHandler = true;
									entitiesEditorPanel.select(editingObject);
									suppressSelectionHandler = false;
								}
							}
					);
				} else {
					doStopAction.perform();
				}
			}
			
			void doStop() {
				editorForm.closeForm();
				currentContext = Optional.absent();
			}

			private void doStartEdit() {
				editorForm = complexEditorPage.editorForm();
				
				T editingCopy = storeImpl.edit(editingObject);
				
				complexEditorBuilder.buildEditor(
						editingCopy,
						new ComplexEditorBuilding() {

							boolean first = true;
							
							@Override
							public ComplexEditingRegistration register(
									ComplexEditingListener listener) {
								return EditingContext.this.register(listener);
							}


							@Override
							public void setEntityName(String name) {
								entityName = name;
							}

							@Override
							public LabeledFieldCustomizer addEditorField(
									String label, final FocusableManagedWidget widget) {
								if (first) focusAction = new Action() {
									@Override
									public void perform() {
										widget.focus();
									}
								};
								first = false;
								
								return editorForm.addEditorField(label, widget.asWidget());
							}


							@Override
							public <C extends BaseProxy> C create(Class<C> clazz) {
								return storeImpl.editingPersistenceContext.get().create(clazz);
							}
						}
				);
				
				editorForm.addSaveHandler(new SaveHandler() {
					@Override
					public void onSave(SaveEvent event) {
						save();
					}
				});
				editorForm.addCancelHandler(new CancelHandler() {
					@Override
					public void onCancel(CancelEvent event) {
						cancel();
					}
				});
				
				editorForm.setHeadingText(getEditorHeadingText());
				
				
				currentContext = Optional.of(this);
				
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						focusAction.perform();
					}
				});
			}

			abstract String getEditorHeadingText();

			public void save() {
				flush();
				
				
				List<String> errorMessages = validate();
				if (!errorMessages.isEmpty()) {
					messageInterface.alert(
							"Validation Failed", 
							Joiner.on("\n").join(errorMessages), null
					);
					
					return;
				}
				
				editorForm.setSaveEnabled(false);
				save(new AbstractReceiver<T>() {
					@Override
					public void onSuccess(T response) {
						currentContext = Optional.absent();
						updateListAfterEdit(response);
						editorForm.setSaveEnabled(true);
					}
					
					@Override
					public void onFailure(ServerFailure error) {
						if (messageInterface==null) {
							super.onFailure(error);
						} else {
							messageInterface.alert("Save Error", error.getMessage(), null);
						}
						editorForm.setSaveEnabled(true);
					}
				});
				
				
			}

			
			private boolean isDirty() {
				for (ComplexEditingListener listener : listeners) {
					if (listener.isDirty()) return true;
				}
				return false;
			}
			
			private List<String> validate() {
				final List<String> errors = Lists.newArrayList();
				ValidationErrors v = new ValidationErrors() {
					
					@Override
					public void addError(String message) {
						errors.add(message);
					}
				};
				for (ComplexEditingListener listener : listeners) {
					listener.onValidate(v);
				}
				
				return errors;
			}


			private void flush() {
				for (ComplexEditingListener listener : listeners) {
					listener.onFlush();
				}
			}

			private void cancel() {
				storeImpl.cancelEditing();
				doStop();
				afterCancel();
			}
			
			abstract void afterCancel();

			abstract void save(Receiver<T> receiver);

			void updateListAfterEdit(final T response) {
				entitiesEditorPanel.select(response);
			}
			
			List<ComplexEditingListener> listeners = Lists.newArrayList();
			
			@Override
			public ComplexEditingRegistration register(final ComplexEditingListener listener) {
				listeners.add(listener);
				
				return new ComplexEditingRegistration() {
					@Override
					public void setDirty(boolean dirty) {
						// TODO do something with setdirty notifications
					}

					@Override
					public void removeHandler() {
						listeners.remove(listener);
					}
				};
			}

			@Override
			public <C extends BaseProxy> C create(Class<C> clazz) {
				return storeImpl.editingPersistenceContext.get().create(clazz);
			}
			
		}
		
		class EditingContextCreate extends EditingContext {

			public EditingContextCreate(Supplier<? extends T> object) {
				super(object);
			}
			
			@Override
			void save(final Receiver<T> receiver) {
				storeImpl.save(editingObject, receiver);
			}

			@Override
			void afterCancel() {
			}

			@Override
			String getEditorHeadingText() {
				return defaultUiMessages.editingNew(entityName);
			}
			
		}
		
		class EditingContextUpdate extends EditingContext {

			public EditingContextUpdate(T editingObject) {
				super(com.google.common.base.Suppliers.ofInstance(editingObject));
			}
			
			@Override
			void save(Receiver<T> receiver) {
				storeImpl.merge(editingObject, receiver);
			}
			
			@Override
			void afterCancel() {
				entitiesEditorPanel.deselectAll();
			}

			@Override
			String getEditorHeadingText() {
				return defaultUiMessages.editing(entityName);
			}
		}

		private boolean suppressSelectionHandler = false;
		
		
		private void fireSelectionChangedActions() {
			if (suppressSelectionHandler) return;
			
			final List<T> selection = entitiesEditorPanel.getSelection();
			
			if (selection.size()==1) {
				T editingObject = selection.get(0);
				
				if (complexEditorAccessControl.edit(Wrapper.of(editingObject))) {
					startEdit(new EditingContextUpdate(
							editingObject
					));
					
					return;
				}
			}
				
			stopConfirmed(Action.NONE);
		}
		
		private void stopConfirmed(Action action) {
			if (currentContext.isPresent()) {
				currentContext.get().stopConfirmed(action);
			} else {
				action.perform();
			}
		}
		
	}

	
	
	@Override
	public ManagedWidget widget() {
		return createWidgetImpl().complexEditorPage;
	}

	protected WidgetImpl createWidgetImpl() {
		return new WidgetImpl();
	}



}
