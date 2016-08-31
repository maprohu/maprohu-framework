package hu.mapro.gwtui.shared;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class Transforms {

	public static <T, O> Collection<T> apply(O object, Function<? super O, T>... functions) {
		return apply(object, Arrays.asList(functions));
	}

	public static <T, O> Collection<T> apply(O object, Iterable<? extends Function<? super O, T>> functions) {
		List<T> result = Lists.newArrayList();
		
		for (Function<? super O, T> f : functions) {
			result.add(f.apply(object));
		}
		
		return result;
	}
	
	public static <A, B, C> Collection<Function<A, C>> before(final Function<A, ? extends B> before, Function<B, C>... functions) {
		return before(before, Arrays.asList(functions));
	}
	public static <A, B, C> Collection<Function<A, C>> before(final Function<A, ? extends B> before, Collection<? extends Function<B, C>> functions) {
		return Collections2.transform(functions, new Function<Function<B, C>, Function<A, C>>() {
			@Override
			public Function<A, C> apply(Function<B, C> input) {
				return Functions.compose(input, before);
			}
		});
	}
	
	public static <A, B, C> Collection<Function<A, C>> after(final Function<B, C> after, Function<A, ? extends B>... functions) {
		return after(after, Arrays.asList(functions));
	}
	
	public static <A, B, C> Collection<Function<A, C>> after(final Function<B, C> after, Collection<? extends Function<A, ? extends B>> functions) {
		return Collections2.transform(functions, new Function<Function<A, ? extends B>, Function<A, C>>() {
			@Override
			public Function<A, C> apply(Function<A, ? extends B> input) {
				return Functions.compose(after, input);
			}
		});
	}
	
}
