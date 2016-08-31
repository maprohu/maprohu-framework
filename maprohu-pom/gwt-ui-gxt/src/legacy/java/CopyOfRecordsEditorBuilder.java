package hu.mapro.gwtui.gxt.client.recordseditor;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.data.client.PersistenceInterface;
import hu.mapro.gwtui.client.HasPages;
import hu.mapro.gwtui.client.Page;
import hu.mapro.gwtui.client.Visibility;
import hu.mapro.gwtui.client.VisibilityUtils;
import hu.mapro.gwtui.client.action.ActionBuilder;
import hu.mapro.gwtui.client.action.ShowAction;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;
import hu.mapro.gwtui.client.login.LoginService;
import hu.mapro.gwtui.client.login.LoginUtils;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.gwtui.gxt.client.columnmodel.ColumnModelProvider;
import hu.mapro.gwtui.gxt.client.form.EntitiesEditorPanel;
import hu.mapro.gwtui.gxt.client.form.EntitiesEditorPanel.CreateUpdateOperations;
import hu.mapro.gwtui.gxt.client.form.EntitiesEditorPanel.DeleteContext;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Callback;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class CopyOfRecordsEditorBuilder<T, F, I> {

//	LoginService loginService;
//	MessageInterface messageInterface;
//	HasPages hasPages;
//
//	String pageTitle;
//	String gridHeader;
//	String editorHeader;
//	
//	F sortField;
//	
//	private String noneSelectedLabel = "Select an item from the list above...";
//	MenuGroup menuGroup;
//	
//	protected ModelKeyProvider<? super T> modelKeyProvider;
//	
//	EditorProviderInterface<T, I> editorInterface;
//	PersistenceInterface<T, I> persistenceInterface;
//	SortFieldInterface<T, F> sortFieldInterface;
//	
//	ColumnModelProvider<T> columnModelProvider;
//	//hu.mapro.gwtui.gxt.client.fieldseditor.PolymorphProvider<T> fieldsEditorProvider;
//	
//	NewObjectsInterface<T> newObjects;
//	Function<? super T, Void> newObjectinitializer = Functions.constant(null);
//	
//	private Page page;
//	
//	protected  EntitiesEditorPanel<T> entitiesEditorPanel;
//	
//	//ToolBarCustomizer toolBarCustomizer;
//	
//	public void build() {
//		Visibility visibility = LoginUtils.whenLoggedIn(loginService);
//		
//		page = hasPages.addPage();
//		page.setText(pageTitle);
//		VisibilityUtils.addHideHadler(visibility, page);
//		
//		entitiesEditorPanel = new EntitiesEditorPanel<T>(
//				modelKeyProvider, 
//				columnModelProvider.asColumnModel()
//		);
//		
//		entitiesEditorPanel.setDataProxy(new DataProxy<ListLoadConfig, ListLoadResult<T>>() {
//			@Override
//			public void load(ListLoadConfig loadConfig,
//					final Callback<ListLoadResult<T>, Throwable> callback) {
//				persistenceInterface.list(new ListConfigBean(), new Receiver<List<T>>() {
//					@Override
//					public void onSuccess(List<T> response) {
//						callback.onSuccess(new ListLoadResultBean<T>(response));
//					}
//				});
//				
//			}
//		});				
//		
//		if (sortField!=null) {
//			addSortField(entitiesEditorPanel);
//		}
//		
//		entitiesEditorPanel.setGridHeader(gridHeader);
//		entitiesEditorPanel.setNoneSelectedLabel(noneSelectedLabel );
//		
//		final List<CreateObjectOperation<? extends T>> createObjectOperations = buildNewObjects();
//		
//		entitiesEditorPanel.setCreateUpdateOperations(new CreateUpdateOperations<T>() {
//
//			EditorInterface<T> mainInterface;
//			
//			@Override
//			public void persist(Receiver<T> receiver) {
//				persistenceInterface.persist(mainInterface.getEditingObject(), receiver);
//			}
//
//			@Override
//			public boolean isDirty() {
//				return mainInterface.isDirty();
//			}
//
//			@Override
//			public void focus() {
//				mainInterface.focus();
//			}
//
//			@Override
//			public List<EditorError> getErrors() {
//				return mainInterface.getErrors();
//			}
//
//			@Override
//			public boolean hasErrors() {
//				return mainInterface.hasErrors();
//			}
//
//			@Override
//			public void flush() {
//				mainInterface.flush();
//			}
//
//			@Override
//			public void cancel() {
//				persistenceInterface.newEditingContext();
//			}
//
//			@Override
//			public String getHeading() {
//				return editorHeader;
//			}
//
//			@Override
//			public Widget getWidget() {
//				return mainInterface.getWidget();
//			}
//
//			@Override
//			public void edit(T editingObject) {
//				mainInterface = editorInterface.edit(editingObject).initializer(persistenceInterface.getInitializer());
//			}
//
//			@Override
//			public void merge(Receiver<T> receiver) {
//				persistenceInterface.merge(mainInterface.getEditingObject(), receiver);
//			}
//
//			@Override
//			public List<CreateObjectOperation<? extends T>> createObjects() {
//				return createObjectOperations;
//			}
//		});
//		
//		entitiesEditorPanel.addDeleteButton(new DeleteContext<T>() {
//			@Override
//			public void delete(List<T> object, Receiver<Void> receiver) {
//				persistenceInterface.delete(object, receiver);
//			}
//		});
//		
////		if (toolBarCustomizer!=null) {
////			toolBarCustomizer.customize(ee.getToolBar());
////		}
//		
//		entitiesEditorPanel.setMessageInterface(messageInterface);
//
//		persistenceInterface.addChangeHandler(new Action() {
//			@Override
//			public void perform() {
//				entitiesEditorPanel.refresh();
//			}
//		});
//		
//		postEntitiesEditorPanelBuild();
//		
//		page.setWidget(entitiesEditorPanel);
//
//		page.addShowHandler(ActionBuilder.refresh(entitiesEditorPanel).scheduled());
//		page.addHideHandler(new Action() {
//			@Override
//			public void perform() {
//				persistenceInterface.close(new Receiver<Void>() {
//					@Override
//					public void onSuccess(Void response) {
//					}
//				});
//			}
//		});
//		
//		if (menuGroup!=null) {
//			MenuItem menuItem = menuGroup.addMenuItem();
//			menuItem.setText(pageTitle);
//			menuItem.setAction(new ShowAction(page));
//			VisibilityUtils.addVisibility(visibility, menuItem);
//		}
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public List<CreateObjectOperation<? extends T>> buildNewObjects() {
//		List<CreateObjectOperation> result = Lists.newArrayList();
//		
//		for (final NewObjectInterface<? extends T> noi : newObjects.getNewObjects()) {
//			result.add(new CreateObjectOperation() {
//
//				@Override
//				public String getLabel() {
//					return noi.getLabel();
//				}
//
//				@Override
//				public Object createNewObject() {
//					T newObject = persistenceInterface.create(noi.getType());
//					newObjectinitializer.apply(newObject);
//					return newObject;
//				}
//				
//			});
//		}
//		
//		return (List)result;
//	}
//	
//	
//	protected void postEntitiesEditorPanelBuild() {
//	}
//
//	private void addSortField(EntitiesEditorPanel<T> ee) {
//		ee.addSortField(new ValueProvider<T, Object>() {
//
//			@Override
//			public Object getValue(T object) {
//				return sortFieldInterface.getValue(sortField, object);
//			}
//
//			@Override
//			public void setValue(T object, Object value) {
//				throw new RuntimeException("setValue not implemented");
//			}
//
//			@Override
//			public String getPath() {
//				return sortFieldInterface.getPath(sortField);
//			}
//		}, SortDir.ASC);
//	}
//
//
//	public void setLoginService(LoginService loginService) {
//		this.loginService = loginService;
//	}
//
//
//	public void setMessageInterface(MessageInterface messageInterface) {
//		this.messageInterface = messageInterface;
//	}
//
//
//	public void setHasPages(HasPages hasPages) {
//		this.hasPages = hasPages;
//	}
//
//
//	public void setPageTitle(String pageTitle) {
//		this.pageTitle = pageTitle;
//	}
//
//
//	public void setGridHeader(String gridHeader) {
//		this.gridHeader = gridHeader;
//	}
//
//
//	public void setEditorHeader(String editorHeader) {
//		this.editorHeader = editorHeader;
//	}
//
//	public void setSortField(F sortField) {
//		this.sortField = sortField;
//	}
//
//
//	public void setNoneSelectedLabel(String noneSelectedLabel) {
//		this.noneSelectedLabel = noneSelectedLabel;
//	}
//
//
//	public void setMenuGroup(MenuGroup menuGroup) {
//		this.menuGroup = menuGroup;
//	}
//
//	public void setModelKeyProvider(ModelKeyProvider<? super T> modelKeyProvider) {
//		this.modelKeyProvider = modelKeyProvider;
//	}
//
//
//	public void setSortFieldInterface(SortFieldInterface<T, F> sortFieldOperations) {
//		this.sortFieldInterface = sortFieldOperations;
//	}
//
//
//	public void setColumnModel(ColumnModelProvider<T> isColumnModel) {
//		this.columnModelProvider = isColumnModel;
//	}
//
//
////	public void setFieldsEditorProvider(
////			hu.mapro.gwtui.gxt.client.fieldseditor.PolymorphProvider<T> isFieldsEditorComponent) {
////		this.fieldsEditorProvider = isFieldsEditorComponent;
////	}
//
//
//	public Page getPage() {
//		return page;
//	}
//
//	public void setEditorInterface(EditorProviderInterface<T, I> editorInterface) {
//		this.editorInterface = editorInterface;
//	}
//
//	public void setPersistenceInterface(
//			PersistenceInterface<T, I> persistenceInterface) {
//		this.persistenceInterface = persistenceInterface;
//	}
//
//
//	public interface ToolBarCustomizer {
//		void customize(ToolBar toolbar);
//	}
//
//
//	public void setNewObjects(NewObjectsInterface<T> newObjects) {
//		this.newObjects = newObjects;
//	}
//
//	public void setNewObjectinitializer(Function<? super T, Void> newObjectinitializer) {
//		this.newObjectinitializer = newObjectinitializer;
//	}
//
//

}
