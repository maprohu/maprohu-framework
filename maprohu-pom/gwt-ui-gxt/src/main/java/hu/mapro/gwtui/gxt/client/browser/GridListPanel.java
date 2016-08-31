package hu.mapro.gwtui.gxt.client.browser;

import com.google.common.base.Supplier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
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

public class GridListPanel<T> extends ResizeComposite {

	interface Binder extends UiBinder<Widget, GridListPanel<?>> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided=true)
	ColumnModel<T> cm;

	@UiField(provided=true)
	ListStore<T> store;
	
	@UiField
	Grid<T> grid;
	
	@UiField(provided=true)
	ToolButton upButton = new ToolButton(ToolButton.UP);
	
	@UiField(provided=true)
	ToolButton topButton = new ToolButton(ToolButton.DOUBLEUP);
	
	@UiField(provided=true)
	ToolButton downButton = new ToolButton(ToolButton.DOWN);
	
	@UiField(provided=true)
	ToolButton bottomButton = new ToolButton(ToolButton.DOUBLEDOWN);
	
	@UiField(provided=true)
	ToolButton addButton = new ToolButton(ToolButton.PLUS);
	
	@UiField(provided=true)
	ToolButton removeButton = new ToolButton(ToolButton.MINUS);

	private GridSelectionModel<T> selectionModel;
	
	
	public GridListPanel(ColumnModel<T> cm, final ListStore<T> store, Supplier<T> factory) {
		this.cm = cm;
		this.store = store;
		
		initWidget(binder.createAndBindUi(this));
		
		selectionModel = grid.getSelectionModel();
		selectionModel.setSelectionMode(SelectionMode.SINGLE);
		
		selectionModel.addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				T selected = selectionModel.getSelectedItem();
				int selectedIndex = store.indexOf(selected);
				
				
				
				boolean isSelected = selected != null;
				boolean isFirst = selectedIndex == 0;
				boolean isLast = selectedIndex == store.size()-1;

				upButton.setEnabled(isSelected && !isFirst);
				downButton.setEnabled(isSelected && !isLast);
				topButton.setEnabled(isSelected && !isFirst);
				bottomButton.setEnabled(isSelected && !isLast);
				
				removeButton.setEnabled(isSelected);
			}
		});
	}


	public Grid<T> getGrid() {
		return grid;
	}

	@UiHandler("upButton")
	void clickUp(SelectEvent e) {
		moveRelative(-1);
	}


	
	@UiHandler("downButton")
	void clickDown(SelectEvent e) {
		moveRelative(1);
	}
	
	@UiHandler("topButton")
	void clickTop(SelectEvent e) {
		moveAbsolute(0);
	}
	
	@UiHandler("bottomButton")
	void clickBottom(SelectEvent e) {
		moveAbsolute(store.size()-1);
	}

	private void moveRelative(int relative) {
		T item = selectionModel.getSelectedItem();
		int fromIndex = store.indexOf(item);
		int toIndex = fromIndex+relative;
		
		moveAbsolute(fromIndex, toIndex);
	}

	private void moveAbsolute(int toIndex) {
		T item = selectionModel.getSelectedItem();
		int fromIndex = store.indexOf(item);
		
		moveAbsolute(fromIndex, toIndex);
	}
	

	@SuppressWarnings("unchecked")
	private  void moveAbsolute(int fromIndex, int toIndex) {
		
		T movedItem = store.get(fromIndex);
		ListStore<T>.Record record = store.getRecord(movedItem);
		store.remove(fromIndex);
		store.add(toIndex, movedItem);
		selectionModel.select(toIndex, false);
		ListStore<T>.Record newRecord = store.getRecord(movedItem);

		for (ColumnConfig<T, ?> cc : cm.getColumns()) {
			ValueProvider<? super T, Object> vp = (ValueProvider<? super T, Object>) cc.getValueProvider();
			
			Change<T, Object> ch = record.getChange(vp);
			
			if (ch!=null) {
				newRecord.addChange(vp, ch.getValue());
			}
		}
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
	
}
