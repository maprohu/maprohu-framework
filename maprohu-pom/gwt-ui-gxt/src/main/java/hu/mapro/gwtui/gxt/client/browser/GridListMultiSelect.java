package hu.mapro.gwtui.gxt.client.browser;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class GridListMultiSelect<T> extends AdapterField<List<T>> {

	ListStore<T> store;
	Grid<T> grid;
	
	@SuppressWarnings("unchecked")
	public GridListMultiSelect(
			ColumnModel<T> cm,
			ModelKeyProvider<? super T> modelKeyProvider
	) {
		super(createWidget(cm, modelKeyProvider));

		grid = (Grid<T>) getWidget();
		store = grid.getStore();

	}
	
	private static <T> Widget createWidget(
			ColumnModel<T> cm, 
			ModelKeyProvider<? super T> modelKeyProvider
	) {
		ListStore<T> store = new ListStore<T>(modelKeyProvider);
		CheckBoxSelectionModel<T> selectionModel = new CheckBoxSelectionModel<T>(new IdentityValueProvider<T>());
		List<ColumnConfig<T, ?>> cms = Lists.newArrayList(); 
		cms.add(selectionModel.getColumn());
		cms.addAll(cm.getColumns());
		Grid<T> grid = new Grid<T>(store, new ColumnModel<T>(cms));
		grid.setSelectionModel(selectionModel);
		
		grid.setBorders(true);
		grid.getView().setForceFit(true);
		return grid;
	}

	@Override
	public void setValue(final List<T> value) {
		grid.getSelectionModel().setSelection(value);
//		loadSourceValues(new Callback<List<T>>() {
//			@Override
//			public void onResponse(final List<T> response) {
//				store.clear();
//				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//					@Override
//					public void execute() {
//						setSourceList(response);
//						grid.getSelectionModel().setSelection(value);
//					}
//				});
//			}
//		});
	}

	@Override
	public List<T> getValue() {
		return grid.getSelectionModel().getSelectedItems();
	}

	public void setSourceList(List<T> list) {
		store.replaceAll(list);
	}
	
//	protected void loadSourceValues(Callback<List<T>> response) {
//		response.onResponse(store.getAll());
//	}
	
}
