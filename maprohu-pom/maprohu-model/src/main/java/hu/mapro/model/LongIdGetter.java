package hu.mapro.model;

import com.google.common.base.Function;

public interface LongIdGetter {

	Long getId();
	
	Integer getVersion();
	
	public static final Function<LongIdGetter, Long> KEYPROVIDER = new Function<LongIdGetter, Long>() {
		@Override
		public Long apply(LongIdGetter input) {
			return input.getId();
		} 
	};	
}
