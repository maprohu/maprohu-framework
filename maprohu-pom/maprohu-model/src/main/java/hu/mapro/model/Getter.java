package hu.mapro.model;

public interface Getter<T, V> {

	V get(T object);
	
	@SuppressWarnings("rawtypes")
	public static final Getter IDENTITY = new Getter() {
		@Override
		public Object get(Object object) {
			return object;
		}
		
	};
	
}
