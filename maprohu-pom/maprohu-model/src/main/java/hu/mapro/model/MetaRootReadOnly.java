package hu.mapro.model;

public interface MetaRootReadOnly<T, V> extends Getter<T, V> {

	
	@SuppressWarnings("rawtypes")
	public static final MetaRootReadOnly META_IDENTITY = new MetaRootReadOnly() {
		@Override
		public Object get(Object object) {
			return object;
		}
		
	};
	
}
