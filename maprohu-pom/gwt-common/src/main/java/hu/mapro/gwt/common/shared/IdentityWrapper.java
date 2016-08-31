package hu.mapro.gwt.common.shared;

public class IdentityWrapper<T> {
	
	final T object;

	public IdentityWrapper(T object) {
		super();
		this.object = object;
	}
	
	public static <T> IdentityWrapper<T> of(T object) {
		return new IdentityWrapper<T>(object);
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(object);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentityWrapper<?> other = (IdentityWrapper<?>) obj;
		return object == other.object;
	}

}
