package hu.mapro.model;

public interface MetaRoot<T, V> extends MetaRootReadOnly<T, V>, Setter<T, V> {

	
	@SuppressWarnings("rawtypes")
	public static final MetaRoot META_IDENTITY = new MetaRoot() {
		@Override
		public Object get(Object object) {
			return object;
		}

		@Override
		public void set(Object object, Object value) {
			throw new UnsupportedOperationException("Trying to call setter method on identity root!");
		}
		
	};
	
}
