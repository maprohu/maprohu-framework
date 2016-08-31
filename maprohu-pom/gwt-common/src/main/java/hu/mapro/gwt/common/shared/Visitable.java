package hu.mapro.gwt.common.shared;

import com.google.common.base.Optional;

public class Visitable<T> {
	
	final Optional<T> value;

	public Visitable(Optional<T> value) {
		super();
		this.value = value;
	}
	
	public static <T> Visitable<T> of(Optional<T> value) {
		return new Visitable<T>(value);
	}

	public static <T> Visitable<T> absent() {
		return of(Optional.<T>absent());
	}
	
	public static <T> Visitable<T> of(T instance) {
		return of(Optional.<T>of(instance));
	}
	
	public void accept(OptionalVisitor<T> visitor) {
		if (value.isPresent()) {
			visitor.present(value.get());
		} else {
			visitor.absent();
		}
	}
	
	public <R> R accept(OptionalVisitorReturn<T, R> visitor) {
		if (value.isPresent()) {
			return visitor.present(value.get());
		} else {
			return visitor.absent();
		}
	}

	public Optional<T> getValue() {
		return value;
	}

	public abstract class VisitorReturn<R> implements OptionalVisitorReturn<T, R> {
		
	}
	
	public class Visitor extends AbstractOptionalVisitor<T> {
		public Visitor() {
			accept(this);
		}
	}
	
}
