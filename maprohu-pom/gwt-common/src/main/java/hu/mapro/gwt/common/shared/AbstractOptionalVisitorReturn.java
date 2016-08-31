package hu.mapro.gwt.common.shared;

public class AbstractOptionalVisitorReturn<T, R> implements OptionalVisitorReturn<T, R> {

	@Override
	public R present(T value) {
		return null;
	}

	@Override
	public R absent() {
		return null;
	}

}
