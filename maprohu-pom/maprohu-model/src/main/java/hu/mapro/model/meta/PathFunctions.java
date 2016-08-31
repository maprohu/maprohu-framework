package hu.mapro.model.meta;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class PathFunctions {

	public static <A, B> PathFunction<A, B> of(Function<A, B> function, String path) {
		return new PathFunctionImpl<A, B>(function, path);
	}

	public static <A, B, C> PathFunction<A, C> compose(PathFunction<A, B> function, Function<? super B, C> extra, String functionName) {
		return new PathFunctionImpl<A, C>(
				Functions.compose(extra, function), 
				functionName+"("+function.getPath()+")"
		);
	}
	
}
