package hu.mapro.jpa.model.domain.server;

import java.util.List;

import com.google.common.collect.Lists;

public class Items<T> {
	
	List<? extends T> items = Lists.newArrayList();
	
	void count(int count) {
		if (items.size() != count) {
			throw new RuntimeException("number of items should be: " + count + " was: " + items.size());
		}
	}

	public List<? extends T> getItems() {
		return items;
	}

	public void setItems(List<? extends T> items) {
		this.items = items;
	}

}
