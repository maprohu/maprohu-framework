package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.FieldInfo;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ProxyFactory {

	public static Iterable<FieldInfo> getClientFields(ComplexTypeInfo complexTypeInfo) {
		return Iterables.filter(complexTypeInfo.getFields(), new Predicate<FieldInfo>() {
			@Override
			public boolean apply(FieldInfo input) {
				return !input.isServer();
			}
		});
	}
	
	
}
