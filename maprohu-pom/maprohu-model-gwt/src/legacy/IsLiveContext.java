package hu.mapro.server.model;

import java.util.Deque;
import java.util.LinkedList;


public class IsLiveContext {
	
	private final static Deque<Cemetary> stack = new LinkedList<Cemetary>();
	
	private final static ThreadLocal<Cemetary> threadLocal = new ThreadLocal<Cemetary>();

	public static final void push(Cemetary isLive) {
		stack.push(threadLocal.get());
		threadLocal.set(isLive);
	}
	
	public static final void pop() {
		threadLocal.set(stack.pop());
	}
	
	public static <T> boolean isLive(T object, IsLiveFallback<T> fallback) {
		IsLive o = threadLocal.get();
		
		if (o!=null) {
			return o.isLive(object, fallback);
		} else {
			return fallback.isLive(object);
		}
	}
	
	public static void kill(Object object) {
		Cemetary o = threadLocal.get();
		
		if (o!=null) {
			o.kill(object);
		}
	}

}
