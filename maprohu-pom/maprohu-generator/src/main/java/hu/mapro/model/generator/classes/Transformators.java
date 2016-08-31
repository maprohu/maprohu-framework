package hu.mapro.model.generator.classes;

import com.google.common.base.Function;

public class Transformators  {
	
	public static <F, T, H> Transformator<F, T> hashed(Transformation<F, T> generation, Function<? super F, H> hash) {
		return new HashedTransformator<F, T, H>(generation, hash);
	}
	
}
