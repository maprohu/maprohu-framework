package hu.mapro.model.generator.classes;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Inits {

	final private List<Init> inits = Lists.newArrayList();

	public <F> void init(final F from, final Transformator<F, ?> generator) {
		inits.add(new Init() {
			@Override
			public void init()  {
				generator.init(from);
			}
		});
	}
	
	public void doInits()  {
		while (!inits.isEmpty()) {
			List<Init> copy = ImmutableList.copyOf(inits);
			inits.clear();
			
			for (Init init : copy) {
				init.init();
			}
		}
	}

	public void init(Init init) {
		inits.add(init);
	}

}
