package hu.mapro.gwtui.gxt.client.browser;

import com.google.common.base.Supplier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class GridSetPanel<T> extends ResizeComposite {

	interface Binder extends UiBinder<Widget, GridSetPanel<?>> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided=true)
	ColumnModel<T> cm;

	@UiField(provided=true)
	ListStore<T> store;
	
	@UiField
	Grid<T> grid;
	
	@UiField(provided=true)
	ToolButton addButton = new ToolButton(ToolButton.PLUS);
	
	@UiField(provided=true)
	ToolButton removeButton = new ToolButton(ToolButton.MINUS);

	final private GridSelectionModel<T> selectionModel;

	final private Supplier<T> factory;
	
	public GridSetPanel(ColumnModel<T> cm, final ListStore<T> store, Supplier<T> factory) {
		this.cm = cm;
		this.store = store;
		this.factory = factory;
		
		initWidget(binder.createAndBindUi(this));
		
		selectionModel = grid.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		
		selectionModel.addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				T selected = selectionModel.getSelectedItem();
				
				boolean isSelected = selected != null;

				removeButton.setEnabled(isSelected);
			}
		});
	}


	public Grid<T> getGrid() {
		return grid;
	}

	public <V> V getCurrentValue(
			T item,
			ColumnConfig<T, V> cc
	) {
		V value = cc.getValueProvider().getValue(item);
		
		Store<T>.Record r = store.getRecord(item);
		
		if (r!=null) {
			
			Change<T, V> ch = r.getChange(cc.getValueProvider());
			
			if (ch!=null) {
				value = ch.getValue();
			}
			
		}
		
		return value;
		
	}

	@UiHandler("addButton")
	protected void clickAdd(SelectEvent e) {
		store.add(factory.get());
	}
	
	@UiHandler("removeButton")
	protected void clickRemove(SelectEvent e) {
		for (T item : selectionModel.getSelectedItems()) {
			store.remove(item);
		}
	}
	
}
