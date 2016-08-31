package hu.mapro.gwtui.gxt.client.modelkeyprovider;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.sencha.gxt.data.shared.ModelKeyProvider;

public class DefaultModelKeyAdapter<T> implements ModelKeyProvider<T> {

	Function<T, ?> keyFunction = Functions.identity();
	
	int nextId = 0;
	
	Map<Object, String> idMap = new HashMap<Object, String>();
	
	public DefaultModelKeyAdapter(Function<T, ?> keyFunction) {
		super();
		this.keyFunction = keyFunction;
	}

	public DefaultModelKeyAdapter() {
		this(Functions.<T>identity());
	}

	@Override
	public String getKey(T item) {
		Object key = keyFunction.apply(item);
		String id = idMap.get(key);
		
		if (id==null) {
			id = Integer.toString(nextId++);
			idMap.put(key, id);
		}
		
		return id;
	}

}
