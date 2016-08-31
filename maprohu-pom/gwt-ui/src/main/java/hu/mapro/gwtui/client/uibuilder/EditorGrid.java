package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.grid.FullTextFiltering;
import hu.mapro.gwtui.client.grid.Paging;
import hu.mapro.gwtui.client.grid.PagingControl;
import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

public interface EditorGrid<T> {

	
	void replaceValues(List<T> values);
	void addValue(T value);
	void removeValue(T value);
	void updateValue(T value);
	
	Button button();
	
	MultiButton multiButton();

	HandlerRegistration addSelectionChangeHandler(Action action);
	HandlerRegistration addDoubleClickHandler(Action action);

	List<T> getSelection();

	void deselectAll();
	
	void select(T object);
	
	void select(List<T> objects);
	
	void showLoadMask();
	
	void hideLoadMask();
	
	PagingControl setPaging(Paging<T> paging);
	
	void setFullTextFiltering(FullTextFiltering filtering);
	
	void redraw();
	
}
