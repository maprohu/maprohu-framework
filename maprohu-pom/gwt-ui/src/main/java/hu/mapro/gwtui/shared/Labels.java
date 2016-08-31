package hu.mapro.gwtui.shared;

import java.util.Arrays;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;

public class Labels {

	public static <T> Function<T, String> join(final String separator, final Function<? super T, String>... elements) {
		return join(separator, Arrays.asList(elements));
	}
	
	public static <T> Function<T, String> join(final String separator, final Iterable<? extends Function<? super T, String>> elements) {
		return new Function<T, String>() {
			@Override
			public String apply(T input) {
				return Joiner.on(separator).join(Transforms.apply(input, elements));
			}
		};
	}

	public static final Function<String, String> PARANTHESIS = new Function<String, String>() {
		@Override
		public String apply(String input) {
			return "("+input+")";
		}
	};
	
	public static <T> Function<T, String> paranthesis(Function<T, String> label) {
		return Functions.compose(PARANTHESIS, label);
	}
	
}
