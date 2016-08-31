package hu.mapro.gwt.common.shared.fetch;

import java.util.Map;

import com.google.common.collect.Maps;

public class NameWalker<T> implements GraphNode<T>, GraphBlocking, Path<T> {
	
	final Map<String, NameWalker<T>> map = Maps.newHashMap();

	final NameWalker<T> blockedWalker;
	
	NameWalker<T> from;
	
	T item;
	
	String name;
	
	boolean blocked = false;
	boolean reached = false;
	
	public NameWalker(T statrtItem, T blockedNode) {
		this(null, null, new NameWalker<T>(null, null, (NameWalker<T>)null) {
			@Override
			NameWalker<T> getCreate(String name) {
				return this;
			}
		});
		blockedWalker.setItem(blockedNode);
		blockedWalker.blocked = true;
		blockedWalker.reached = true;
		
		reached = true;
		setItem(statrtItem);
	}
	
	public NameWalker(NameWalker<T> from, String name, NameWalker<T> blockedNode) {
		super();
		this.from = from;
		this.name = name;
		this.blockedWalker = blockedNode;
	}
	
	public NameWalker<T> walker(String name) {
		return getCreate(name);
	}

	public NameWalker<T> walker(String name, String inverse) {
		NameWalker<T> walker = getCreate(name);
		walker.map.put(inverse, this);
		return walker;
	}
	
	NameWalker<T> getCreate(String name) {
		NameWalker<T> walker = map.get(name);
		if (walker==null) {
			walker = new NameWalker<T>(this, name, blockedWalker);
			map.put(name, walker);
		} 
		return walker;
	}
	
	@Override
	public void _block() {
		blocked = true;
	}
	
	public NameWalker<T> walk(String name, WalkerCallback<T> callback) {
		NameWalker<T> walker = getCreate(name);
		
		if (walker.blocked) {
			walker = blockedWalker;
		}
		
		walker.step(callback);
		
		return walker;
	}
	
	private void step(WalkerCallback<T> callback) {
		if (!reached) {
			reached = true;
			setItem(callback.reached());
		}
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	@Override
	public Path<T> getRoute() {
		return from;
	}

	@Override
	public T getNode() {
		return getItem();
	}

	@Override
	public T _node() {
		return item;
	}
	
}