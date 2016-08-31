package hu.mapro.model.meta;

import com.google.common.base.Function;

public class PathFunctionImpl<A, B> implements PathFunction<A, B> {

	final protected Function<A, B> function;
	final protected String path;
	
	public PathFunctionImpl(Function<A, B> function, String path) {
		super();
		this.function = function;
		this.path = path;
	}

	@Override
	public B apply(A input) {
		return function.apply(input);
	}

	@Override
	public String getPath() {
		return path;
	}

}
