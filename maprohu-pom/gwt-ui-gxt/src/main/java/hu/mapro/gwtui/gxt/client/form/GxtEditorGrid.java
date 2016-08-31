package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.grid.FullTextFiltering;
import hu.mapro.gwtui.client.grid.Paging;
import hu.mapro.gwtui.client.grid.PagingControl;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.GxtFactory;
import hu.mapro.gwtui.gxt.client.PagingLoaders;
import hu.mapro.gwtui.gxt.client.theme.blue.client.MaproPlainTabPanelBottomAppearance;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelAppearance;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class GxtEditorGrid<T> implements EditorGrid<T>, IsWidget {


	@UiField(provided=true)
	protected Grid<T> grid;
	
	@UiField(provided=true)
	PageableToolBar toolBar;
	
	final DefaultUiMessages defaultUiMessages;
	
	public  GxtEditorGrid(
			WidgetContextSupport widgetContext,
			ColumnModel<T> cm,
			ListStore<T> listStore
	) {
		this.defaultUiMessages = widgetContext.getDefaultUiMessages();
		
		grid = new Grid<T>(listStore, cm);

		grid.getView().setForceFit(true);
		grid.setLoadMask(true);

		grid.getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<T>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<T> event) {
						fireSelectionChangedActions();
					}
				}
		);
		
		toolBar = new PageableToolBar(widgetContext, 50);
		
		vlc = new VerticalLayoutContainer();
		vlc.add(toolBar, new VerticalLayoutData(1.0, -1.0));
		vlc.add(grid, new VerticalLayoutData(1.0, 1.0));
	}

	Handlers selectionHandlers = Handlers.newInstance();

	private VerticalLayoutContainer vlc;
	
	private void fireSelectionChangedActions() {
		selectionHandlers.fire();
	}

	public Grid<T> getGrid() {
		return grid;
	}

	public ListStore<T> getStore() {
		return grid.getStore();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addSortField(ValueProvider<T, ?> valueProvider, SortDir sortDir) {
		assert valueProvider!=null;
		getStore().addSortInfo(new StoreSortInfo<T>((ValueProvider)valueProvider, sortDir));
	}
	
	public Handlers getSelectionHandlers() {
		return selectionHandlers;
	}

	@Override
	public void replaceValues(List<T> values) {
		getStore().replaceAll(Lists.newArrayList(values));
	}

	@Override
	public Button button() {
		GxtSelectButton button = GxtFactory.button();
		toolBar.addItem(button.asWidget());
		return button;
	}

	@Override
	public MultiButton multiButton() {
		GxtMultiSelectButton button = GxtFactory.multiButton();
		toolBar.addItem(button.asWidget());
		return button;
	}

	@Override
	public com.google.gwt.event.shared.HandlerRegistration addSelectionChangeHandler(Action action) {
		return selectionHandlers.add(action);
	}

	@Override
	public List<T> getSelection() {
		return grid.getSelectionModel().getSelectedItems();
	}

	@Override
	public void deselectAll() {
		grid.getSelectionModel().deselectAll();
	}
	

	@Override
	public void select(T object) {
		grid.getSelectionModel().select(object, false);
		grid.getView().focusRow(getStore().indexOf(object));
	}

	@UiFactory
	TabPanel createTabPanel() {
		return new TabPanel(GWT.<TabPanelAppearance>create(MaproPlainTabPanelBottomAppearance.class)) {
			{
				cacheSizes = false;
			}
		};
	}

	@Override
	public void showLoadMask() {
		grid.mask(DefaultMessages.getMessages().loadMask_msg());
	}

	@Override
	public void hideLoadMask() {
		grid.unmask();
	}

	@Override
	public PagingControl setPaging(final Paging<T> paging) {
		toolBar.setPagingVisible(true);
		
		PagingLoader<PagingLoadConfig, PagingLoadResult<T>> loader = PagingLoaders.from(paging);
		
		for (StoreSortInfo<T> si : grid.getStore().getSortInfo()) {
			loader.addSortInfo(new SortInfoBean(
					si.getPath(),
					si.getDirection()
			));
		}
		
		toolBar.bind(loader);
		
		grid.getStore().clearSortInfo();
		grid.setLoader(loader);
		
		redraw();
		
		return new PagingControl() {
			
			@Override
			public void refresh() {
				toolBar.refresh();
			}
		};
		
	}

	@Override
	public void redraw() {
		grid.getView().refresh(true);
	}

	@Override
	public void select(List<T> objects) {
		grid.getSelectionModel().select(objects, false);
		if (!objects.isEmpty()) {
			grid.getView().focusRow(getStore().indexOf(objects.get(0)));
		}
	}

	@Override
	public void addValue(T value) {
		grid.getStore().add(value);
	}

	@Override
	public void removeValue(T value) {
		grid.getStore().remove(value);
	}

	@Override
	public void updateValue(T value) {
		grid.getStore().update(value);
	}

	@Override
	public com.google.gwt.event.shared.HandlerRegistration addDoubleClickHandler(
			final Action action) {
		return grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				action.perform();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}

	@Override
	public void setFullTextFiltering(final FullTextFiltering filtering) {
//		final TextField textField = new TextField();
		
		
		final ComboBox<String> filterField = new ComboBox<String>(new ListStore<String>(new ModelKeyProvider<String>() {
			@Override
			public String getKey(String item) {
				return item;
			}
		}), new LabelProvider<String>() {
			@Override
			public String getLabel(String item) {
				return item;
			}
		}) {
			boolean restoreFocus = false;
			
			@Override
			protected void onDisable() {
				restoreFocus = hasFocus;
				super.onDisable();
			}
			
			@Override
			protected void onEnable() {
				super.onEnable();

				if (restoreFocus) {
					focus();
					getCell().setCursorPos(getElement(), Strings.nullToEmpty(getCell().getText(getElement())).length());
					setEmptyText(defaultUiMessages.typeFilterHere());
				}
			}
		};
		filterField.setEmptyText(defaultUiMessages.typeFilterHere());
		filterField.setHideTrigger(true);
		filterField.setForceSelection(false);
		filterField.setQueryDelay(500);
		
	    toolBar.setFillToolItem(filterField);

	    filterField.addBeforeQueryHandler(new BeforeQueryHandler<String>() {

	    	String lastQuery = "";
	    	
			@Override
			public void onBeforeQuery(BeforeQueryEvent<String> event) {
				String query = event.getQuery();
				
				if (!lastQuery.equals(query)) {
					lastQuery = query;
					filterField.setValue(query);
					filtering.filter(query);
				}
			}
		});
	    
	    
	}
	
}
