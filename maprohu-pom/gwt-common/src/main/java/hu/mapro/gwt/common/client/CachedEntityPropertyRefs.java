package hu.mapro.gwt.common.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class CachedEntityPropertyRefs implements EntityPropertyRefs {
	
	final EntityPropertyRefCollectors entityPropertyRefCollectors;
	
	public CachedEntityPropertyRefs(
			EntityPropertyRefCollectors entityPropertyRefCollectors) {
		super();
		this.entityPropertyRefCollectors = entityPropertyRefCollectors;
	}

	final Supplier<String[]> listPropertyRefs = Suppliers.memoize(new Supplier<String[]>() {
		@Override
		public String[] get() {
			PropertyRefCollector prc = new PropertyRefCollector();
			entityPropertyRefCollectors.getListPropertyRefCollector(prc);
			return prc.getRefs();
		}
	});
	
	final Supplier<String[]> fetchPropertyRefs = Suppliers.memoize(new Supplier<String[]>() {
		@Override
		public String[] get() {
			PropertyRefCollector prc = new PropertyRefCollector();
			entityPropertyRefCollectors.getFetchPropertyRefCollector(prc);
			return prc.getRefs();
		}
	});
	
	@Override
	public String[] getListPropertyRefs() {
		return listPropertyRefs.get();
	}

	@Override
	public String[] getFetchPropertyRefs() {
		return fetchPropertyRefs.get();
	}
	

}
