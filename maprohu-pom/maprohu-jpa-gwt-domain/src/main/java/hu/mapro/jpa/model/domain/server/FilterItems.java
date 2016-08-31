package hu.mapro.jpa.model.domain.server;

import java.util.List;

public class FilterItems<R> extends Items<FilterBase<R>> {

	public List<? extends FilterBase<R>> getFilters() {
		return getItems();
	}

	public void setFilters(List<? extends FilterBase<R>> items) {
		setItems(items);
	}

	public static <R> FilterItems<R> of() {
		return new FilterItems<R>();
	}
	
}
