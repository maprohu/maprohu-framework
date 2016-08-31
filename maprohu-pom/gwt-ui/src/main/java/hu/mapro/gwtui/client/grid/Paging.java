package hu.mapro.gwtui.client.grid;

import hu.mapro.gwt.data.client.ListResult;
import hu.mapro.jpa.model.domain.client.Sorting;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;

public interface Paging<T> {
	
	void load(int offset, int limit, List<Sorting<T>> sorting, Receiver<ListResult<T>> receiver);
	
}
