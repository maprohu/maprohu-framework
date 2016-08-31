package hu.mapro.model;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class VisitorHierarchy<P> {

	VisitableType<Object> objectRoot;
	
	Map<Class<?>, VisitableType<?>> typeMap = Maps.newHashMap(); 
	
	boolean closed = false;
	
	InstanceTester defaultInstanceTester;
	
	public VisitorHierarchy() {
		objectRoot = new VisitableType<Object>();
		objectRoot.visitableClass = Object.class;
		objectRoot.instanceTester = new InstanceTester() {
			@Override
			public boolean isInstanceOf(Class<?> visitableClass, Object object) {
				return true;
			}
		};
		typeMap.put(Object.class, objectRoot);
	}
	
	public interface InstanceTester {
		
		boolean isInstanceOf(Class<?> visitableClass, Object object);
		
	}
	
	public class VisitableType<T> {
		
		P payload;
		
		InstanceTester instanceTester;
		
		Class<T> visitableClass;
		
		VisitableType<? super T> superClass;
		Set<VisitableType<? extends T>> directSubclasses = Sets.newHashSet();
		
		Map<Class<?>, VisitableType<?>> cache = Maps.newHashMap();
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private <S extends T> VisitableType<? super S> find(S object) {
			
			if (!instanceTester.isInstanceOf(visitableClass, object)) {
				return null;
			}

			Class<? extends Object> objectClass = object.getClass();
			
			if (visitableClass==objectClass) {
				return this;
			}
			
			VisitableType<? super S> found = (VisitableType<? super S>) cache.get(objectClass);
			
			if (found==null) {
				
				for (VisitableType sub : directSubclasses) {

					found = sub.find(object);
					
					if (found!=null) {
						break;
					}
				}

				if (found==null) {
					found = this;
//					throw new RuntimeException("class not found in visitor hierarchy: " + objectClass);
				}

//				if (found!=null) {
					cache.put(objectClass, found);
//				}
				
				
			}
			
			return found;
		}
		
	}
	
	public <T> void root(Class<T> clazz) {
		link(clazz, Object.class);
	}
	
	public <T> void link(Class<T> clazz, Class<? super T> superClass) {
		if (closed) {
			throw new IllegalStateException("Visitor hierarchy already closed!");
		}
		
		VisitableType<T> type = getOrCreate(clazz);
		VisitableType<? super T> superType = getOrCreate(superClass);
		
		superType.directSubclasses.add(type);
		type.superClass = superType;
	}

	public <T> void setPayload(Class<T> clazz, P payload) {
		if (closed) {
			throw new IllegalStateException("Visitor hierarchy already closed!");
		}
		
		getOrCreate(clazz).payload = payload;
	}
	public <T> void setInstanceTester(Class<T> clazz, InstanceTester instanceTester) {
		if (closed) {
			throw new IllegalStateException("Visitor hierarchy already closed!");
		}
		
		getOrCreate(clazz).instanceTester = instanceTester;
	}
	
	@SuppressWarnings("unchecked")
	private <T> VisitableType<T> getOrCreate(Class<T> clazz) {
		VisitableType<T> type = (VisitableType<T>) typeMap.get(clazz);
		
		if (type==null) {
			type = new VisitableType<T>();
			type.instanceTester = defaultInstanceTester;
			type.visitableClass = clazz;
			typeMap.put(clazz, type);
		}
		
		return type;
	}
	
	//@SuppressWarnings("unchecked")
	public <T> VisitableType<? super T> find(T object) {
		//Class<? extends T> type = DataTypeUtils.getType(object);
		VisitableType<? super T> find = (VisitableType<? super T>) objectRoot.find(object);
		
		if (find==null) {
			throw new RuntimeException("class not found in visitor hierarchy: " + object);
		}
		
		return find;
	}
	
	public <T> P getPayload(T object) {
		return find(object).payload;
	}

	public void setDefaultInstanceTester(InstanceTester defaultInstanceTester) {
		this.defaultInstanceTester = defaultInstanceTester;
	}
	
	
	
}
