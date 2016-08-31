package hu.mapro.gwt.common.shared;

public class AbstractOptionalVisitor<T> implements OptionalVisitor<T> {

	@Override
	public void present(T value) {
	}

	@Override
	public void absent() {
	}

}
