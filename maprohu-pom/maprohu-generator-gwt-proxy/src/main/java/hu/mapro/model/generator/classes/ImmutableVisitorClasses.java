package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sun.codemodel.JClass;

public class ImmutableVisitorClasses extends VisitorClasses<ImmutableVisitorClasses> {
	public ImmutableVisitorClasses(
			final GlobalClasses globalClasses,
			HierarchicTypeInfo hierarchicTypeInfo
	) {
		super(
				globalClasses, 
				hierarchicTypeInfo, 
				globalClasses.immutableVisitorClasses, 
				hierarchicTypeInfo.visit(new AbstractTypeInfoVisitor<Supplier<? extends JClass>>() {

					@Override
					public Supplier<? extends JClass> visit(ObjectTypeInfo type) {
						return Suppliers.ofInstance(globalClasses.cm.ref(Object.class));
					}
					
					@Override
					public Supplier<? extends JClass> visit(ComplexTypeInfo type) {
						return globalClasses.typeClasses.get(type).immutable;
					}

				}),
				globalClasses.cm.directClass(hierarchicTypeInfo.getClassFullName()).name()
		);
	}
	
	@Override
	String getTypeName(String name) {
		return this.name + "Immutable" + name;
	}

}