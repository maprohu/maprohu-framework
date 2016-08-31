package hu.mapro.model.generator.classes;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class HashedTransformator<F, T, H> implements Transformator<F, T> {
	
	Transformation<F, T> generation;
	Function<? super F, H> hash;
	LoadingCache<Wrapper, T> cache;
	
	class Wrapper {
		F from;

		@Override
		public int hashCode() {
			return Objects.hashCode(hash.apply(this.from));
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj==null) return false;
			return Objects.equal(hash.apply(this.from), hash.apply(((Wrapper)obj).from));
		}

		public Wrapper(F from) {
			this.from = from;
		}
		
	}
	
	HashedTransformator(
			final Transformation<F, T> generation,
			Function<? super F, H> hash
	) {
		super();
		this.generation = generation;
		this.hash = hash;
		
		cache = CacheBuilder.newBuilder().build(new CacheLoader<Wrapper, T>() {
			@Override
			public T load(Wrapper key)  {
				return generation.create(key.from);
			}
		});
	}

	@Override
	public T get(F from) {
		return cache.getUnchecked(new Wrapper(from)); 
	}
	
	@Override
	public T init(F from)  {
		T to = get(from);
		generation.init(to, from);
		return to;
	}
	
}