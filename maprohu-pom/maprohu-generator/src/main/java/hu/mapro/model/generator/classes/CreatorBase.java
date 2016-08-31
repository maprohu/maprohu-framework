package hu.mapro.model.generator.classes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class CreatorBase<A, B, H> extends GenerationBase {

	final Map<H, B> map = Maps.newHashMap();
	
	final Function<? super A, H> hashFunction;
	
	public CreatorBase(
			GenerationBase generationBase, 
			Function<? super A, H> hashFunction,
			Iterable<A> items
	) {
		this(generationBase, hashFunction);
		init(items);
	}
	
	public CreatorBase(GenerationBase generationBase, Function<? super A, H> hashFunction) {
		super(generationBase);
		this.hashFunction = hashFunction;
		
		inits.init(new Init() {
			@Override
			public void init() {
				for (A item : getInitItems()) {
					get(item);
				}
			}
		});
	}

	public B get(A input) {
		H hash = hashFunction.apply(input);
		
		B output = map.get(hash);
		
		if (!map.containsKey(hash)) {
			output = create(input);
			postProcess(input, output);
			map.put(hash, output);
		}
		
		return output;
		
	}

	abstract protected B create(final A input);

	class Creation {
		final A input;
		final B output;
		public Creation(A input, B output) {
			super();
			this.input = input;
			this.output = output;
		}
	}
	
	final List<Creation> creations = Lists.newArrayList();
	final List<PostProcessor> postProcessors = Lists.newArrayList();
	
	abstract class PostProcessor {
		
		public PostProcessor() {
			 
			postProcessors.add(this);
			
			if (!creations.isEmpty()) {
				final ImmutableList<Creation> p = ImmutableList.copyOf(creations);
				
				inits.init(new Init() {
					@Override
					public void init()  {
						for (Creation c : p) {
							doPostProcess(c.input, c.output);
						}
					}
					
				});
			}
		}
		
		abstract void postProcess(A input, B output);
	
		void doPostProcess(A input, B output) {
			if (isActive()) {
				postProcess(input, output);
			}
		}
		
		boolean isActive() {
			return true;
		}
		
	}

	private void postProcess(final A input, final B output) {
		creations.add(new Creation(input, output));
		
		for (final PostProcessor pp : postProcessors) {
			inits.init(new Init() {
				@Override
				public void init() {
					pp.doPostProcess(input, output);
				}
			});
		}
		
	}

	void init(final Iterable<A> items) {
		inits.init(new Init() {
			@Override
			public void init() {
				for (A item : items) {
					get(item);
				}
			}
		});
	}
	
	Iterable<A> getInitItems() {
		return ImmutableList.of();
	}
	
}
