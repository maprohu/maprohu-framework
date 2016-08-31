package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.generator.classes.GenerationBase.InterfaceGeneration.DefaultImplementation;
import hu.mapro.model.meta.HierarchicType;

import com.google.common.base.Function;
import com.google.common.base.Suppliers;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

class HierarchicClasses extends VisitorClasses<HierarchicClasses> {

	
//	Generator<JDefinedClass> visitorDelegator = gen(new InterfaceGeneration("VisitorDelegator") {
//		
//		
//		void init(final JDefinedClass returnVisitor)  {
//			
//			JMethod method = returnVisitor.method(JMod.NONE, cm.directClass("R"), "delegate");
//			JTypeVar genericParam = method.generify("P");
//			JTypeVar genericReturn = method.generify("R");
//			
//			
//			method.param(getVisitableClass(), "visitee");
//			method.param(genericParam, "param");
//			method.param(paramReturnVisitor.get().narrow(genericParam, genericReturn), "visitor");
//			
//			globalClasses.hierarchicClasses.new SuperAction<Void>() {
//
//				@Override
//				Void present(HierarchicClasses superClasses) {
//					returnVisitor._extends(superClasses.visitorDelegator.get());
//					return null;
//				}
//
//				@Override
//				Void absent() {
//					return null;
//				}
//
//			}.process(hierarchicTypeInfo);
//			
//		}
//		
//	});
	
	
	
//	Generator<JDefinedClass> visitorDelegatorImpl = gen(new ClassGeneration("VisitorDelegatorImpl") {
//		
//		void init(JDefinedClass visitor)  {
//			visitor._implements(visitorDelegator.get());
//			
//			globalClasses.hierarchicClasses.new SubAction() {
//
//				@Override
//				void subclass(HierarchicClasses subClasses) {
//					JMethod method = subClasses.visitorDelegatorImpl.get().method(JMod.PUBLIC, cm.directClass("R"), "delegate");
//					JTypeVar genericParam = method.generify("P");
//					JTypeVar genericReturn = method.generify("R");
//					
//					
//					JVar visitee = method.param(getVisitableClass(), "visitee");
//					JVar param = method.param(genericParam, "param");
//					JVar delegate = method.param(paramReturnVisitor.get().narrow(genericParam, genericReturn), "visitor");
//
//					method.body()._return(JExpr.invoke(delegate, "visit").arg(JExpr.cast(subClasses.clientClass.get(), visitee)).arg(param));
//				}
//			}.process(hierarchicTypeInfo);
//		}
//		
//	});
	
	
	Generator<JDefinedClass> returnVisitorDelegate = gen(new ClassGeneration("ReturnVisitorDelegate") {
		
		private JTypeVar genericReturn;
		private JVar delegate;
		
		void init(final JDefinedClass returnVisitorDelegate)  {
			genericReturn = returnVisitorDelegate.generify("R");
			
			returnVisitorDelegate._implements(paramReturnVisitor.get().narrow(cm.ref(Void.class), genericReturn));
			
			delegate = builder.mainConstructor().field(returnVisitor.get().narrow(genericReturn));
			
			globalClasses.hierarchicClasses.new SubAction() {
				@Override
				void subclass(HierarchicClasses subClasses) {
					final JMethod method = returnVisitorDelegate.method(JMod.PUBLIC, genericReturn, "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					method.param(cm.ref(Void.class), "param");
					method.body()._return(JExpr.invoke(delegate, "visit").arg(object));
				}
			}.process(hierarchicTypeInfo);
			
		}
		
		
	});
	
	
	
	Generator<JDefinedClass> visitorDelegate = gen(new ClassGeneration("VisitorDelegate") {
		
		private JVar delegate;

		void init(final JDefinedClass visitorDelegate)  {
			visitorDelegate._implements(paramReturnVisitor.get().narrow(cm.ref(Void.class), cm.ref(Void.class)));
			
			delegate = builder.mainConstructor().field(visitor.get());

			globalClasses.hierarchicClasses.new SubAction() {
				@Override
				void subclass(HierarchicClasses subClasses) {
					final JMethod method = visitorDelegate.method(JMod.PUBLIC, cm.ref(Void.class), "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					method.param(cm.ref(Void.class), "param");
					
					method.body().add(JExpr.invoke(delegate, "visit").arg(object));
					method.body()._return(JExpr._null());
				}
			}.process(hierarchicTypeInfo);
			
		}
		
	});
	
	
	
	
	Generator<JDefinedClass> paramVisitorDelegate = gen(new ClassGeneration("ParamVisitorDelegate") {
		
		private JTypeVar genericParam;
		private JVar delegate;
		
		void init(final JDefinedClass returnVisitorDelegate)  {
			genericParam = returnVisitorDelegate.generify("P");
			
			returnVisitorDelegate._implements(paramReturnVisitor.get().narrow(genericParam, cm.ref(Void.class)));
			
			delegate = builder.mainConstructor().field(paramVisitor.get().narrow(genericParam));
			
			globalClasses.hierarchicClasses.new SubAction() {
				@Override
				void subclass(HierarchicClasses subClasses) {
					final JMethod method = returnVisitorDelegate.method(JMod.PUBLIC, cm.ref(Void.class), "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					JVar param = method.param(genericParam, "param");
					method.body().add(JExpr.invoke(delegate, "visit").arg(object).arg(param));
					method.body()._return(JExpr._null());
				}
			}.process(hierarchicTypeInfo);

		}
		
		
	});
	
	public InterfaceGeneration visitable = new InterfaceGeneration("visitable") {
		
		void init(final JDefinedClass proxyVisitable) {
			addVisitorDefinitionMethods(proxyVisitable);
			
			globalClasses.hierarchicClasses.new SuperAction<Void>() {
				Void present(HierarchicClasses superOutput) {
					proxyVisitable._implements(superOutput.visitable.get());
					return null;
				}
			}.process(hierarchicTypeInfo);
			
		}
		
	};
	
	DefaultImplementation defaultVisitable = visitable.new DefaultImplementation() {
		
		void init(JDefinedClass implementation) {
			JFieldVar target = builder.mainConstructor().field(clientClass.get());
			
			implement(HierarchicClasses.this, target);
		}

		private void implement(
				HierarchicClasses complexClasses,
				final JFieldVar target
		) {
			complexClasses.addVisitorImplementationMethods(builder, true, target);

			globalClasses.hierarchicClasses.new SuperAction<Void>() {
				Void present(HierarchicClasses superOutput) {
					implement(superOutput, target);
					return null;
				}
			}.process(complexClasses.hierarchicTypeInfo);			
		}
	};
	
	public ClassGeneration visitableFunction = new ClassGeneration("visitableFunction") {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void init(JDefinedClass proxyVisitableSupplier) {
			Reflector<Function> reflector = builder._implements(Function.class, clientClass.get(), visitable.get());
			reflector.override().apply(null);
			reflector.method().body()._return(
					JExpr._new(defaultVisitable.get()).arg(reflector.param("proxy"))
			);
		}
		
	};
	
	
	HierarchicType<?> hierarchicType;
	final HierarchicTypeInfo hierarchicTypeInfo;

	HierarchicClasses(
			final GlobalClasses globalClasses, 
			final HierarchicTypeInfo hierarchicTypeInfo,
			String name,
			JClass clientClass 
	)  {
		super(
				globalClasses,
				hierarchicTypeInfo,
				globalClasses.hierarchicClasses,
				Suppliers.ofInstance(clientClass),
				name
		);
		this.hierarchicTypeInfo = hierarchicTypeInfo;
		this.hierarchicType = hierarchicTypeInfo;
	}

	
	JClass getVisitableClass() {
		return hierarchicTypeInfo.visit(new AbstractTypeInfoVisitor<JClass>() {
			@Override
			public JClass visit(ComplexTypeInfo type) {
				return clientClass.get();
//				if (type.generateServer()) {
//					return globalClasses.serverClasses.get(type).immutable.get();
//				} else {
//					return clientClass.get();
//				}
			}
			
			@Override
			public JClass visit(ObjectTypeInfo type) {
				return cm.ref(Object.class);
			}
		});
		
	}
	
}