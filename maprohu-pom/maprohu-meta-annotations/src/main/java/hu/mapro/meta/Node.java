package hu.mapro.meta;

import java.util.List;

import com.google.common.collect.Lists;

abstract public class Node<R, T> {
	
	public T base;
	public List<T> nodes = Lists.newArrayList();
	
	public R root;
	
	public void base(T base) {
		this.base = base;
	}
	
	public void node(T node) {
		nodes.add(node);
	}

	abstract public void build();
	
}
