package hu.mapro.model;

import com.google.common.base.Function;

public interface LongIdFunctions {

	Function<LongIdGetter, Long> id = new Function<LongIdGetter, Long>() {
		@Override
		public Long apply(LongIdGetter input) {
			if (input==null) return null;
			return input.getId();
		}
	};
	
}
