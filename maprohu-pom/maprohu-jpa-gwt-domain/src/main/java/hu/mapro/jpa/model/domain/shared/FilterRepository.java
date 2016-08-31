package hu.mapro.jpa.model.domain.shared;

import java.util.Map;

import com.google.common.collect.Maps;

public class FilterRepository {

	int counter = 0;
	
	final Map<Integer, FilterItem<?>> filterMap = Maps.newHashMap();
	
	public class FilterItem<T extends FilterType> {

		final int id;
		final T filterType;
		
		public FilterItem(T filterType) {
			super();
			this.id = counter++;
			this.filterType = filterType;
			
			filterMap.put(id, this);
		}
		
		public int getId() {
			return id;
		}
		
	}
	
	public class FullTextFilterItem extends FilterItem<FullTextFilterType> {

		public FullTextFilterItem() {
			super(FullTextFilterType.INSTANCE);
		}
		
	}
	
	public FilterType getFilter(Integer id) {
		return filterMap.get(id).filterType;
	}
	
}
