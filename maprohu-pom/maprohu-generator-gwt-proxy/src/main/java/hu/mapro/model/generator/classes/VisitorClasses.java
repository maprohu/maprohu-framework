package hu.mapro.model.generator.classes;

import static com.google.common.base.Preconditions.checkArgument;
import hu.mapro.model.analyzer.HierarchicTypeInfo;
import hu.mapro.model.meta.Type.TypeCategory;

import com.google.common.base.Supplier;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

public class VisitorClasses<V extends VisitorClasses<?>> extends GlobalContext {


	Generator<JDefinedClass> paramReturnVisitor = gen(new InterfaceGeneration("ParamReturnVisitor") {
		
		void init(final JDefinedClass returnVisitor)  {
			final JTypeVar genericParam = returnVisitor.generify("P");
			final JTypeVar genericReturn = returnVisitor.generify("R");
			
			JMethod method = returnVisitor.method(JMod.NONE, genericReturn, "visit");
			method.param(clientClass.get(), "object");
			method.param(genericParam, "param");
			
			hierarchicCreator.new SuperAction<Void>() {

				@Override
				Void present(V superClasses) {
					superClasses.paramReturnVisitor.get()._extends(returnVisitor.narrow(genericParam, genericReturn));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}

			}.process(hierarchicTypeInfo);
			
		}
		
		
	});
	
	Generator<JDefinedClass> paramReturnVisitorBase = gen(new ClassGeneration("ParamReturnVisitorBase") {
		
		void init(final JDefinedClass paramReturnVisitorBase)  {
			final JTypeVar genericParam = paramReturnVisitorBase.generify("P");
			final JTypeVar genericReturn = paramReturnVisitorBase.generify("R");

			paramReturnVisitorBase._implements(paramReturnVisitor.get().narrow(genericParam, genericReturn));
			
			hierarchicCreator.new SubAction() {

				@Override
				void subclass(V subClasses) {
					
					final JMethod method = paramReturnVisitorBase.method(JMod.PUBLIC, genericReturn, "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					final JVar param = method.param(genericParam, "param");
					
					if (VisitorClasses.this==subClasses) {
						method.body()._return(JExpr._null());
						return;
					}
					
					hierarchicCreator.new SuperAction<Void>() {

						@Override
						Void present(V superClasses) {
							method.body()._return(JExpr.invoke("visit").arg(JExpr.cast(superClasses.clientClass.get(), object)).arg(param));
							return null;
						}

						@Override
						Void absent() {
							method.body()._return(JExpr._null());
							return null;
						}

					}.process(subClasses.hierarchicTypeInfo);
					
					
				}
			}.process(hierarchicTypeInfo);
			
			
		}
		
	});

	Generator<JDefinedClass> returnVisitor = gen(new InterfaceGeneration("ReturnVisitor") {
		
		void init(final JDefinedClass returnVisitor)  {
			final JTypeVar genericReturn = returnVisitor.generify("R");
			
			JMethod method = returnVisitor.method(JMod.NONE, genericReturn, "visit");
			method.param(clientClass.get(), "object");
			
			hierarchicCreator.new SuperAction<Void>() {

				@Override
				Void present(V superClasses) {
					superClasses.returnVisitor.get()._extends(returnVisitor.narrow(genericReturn));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}

			}.process(hierarchicTypeInfo);

		}
		
	});
	
	Generator<JDefinedClass> returnVisitorBase = gen(new ClassGeneration("ReturnVisitorBase") {
		
		void init(final JDefinedClass returnVisitorBase)  {
			final JTypeVar genericReturn = returnVisitorBase.generify("R");

			returnVisitorBase._implements(returnVisitor.get().narrow(genericReturn));
			
			hierarchicCreator.new SubAction() {
				@Override
				void subclass(V subClasses) {
					final JMethod method = returnVisitorBase.method(JMod.PUBLIC, genericReturn, "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					
					if (VisitorClasses.this==subClasses) {
						method.body()._return(JExpr._null());
						return;
					}
					
					hierarchicCreator.new SuperAction<Void>() {

						@Override
						Void present(V superClasses) {
							method.body()._return(JExpr.invoke("visit").arg(JExpr.cast(superClasses.clientClass.get(), object)));
							return null;
						}

						@Override
						Void absent() {
							method.body()._return(JExpr._null());
							return null;
						}

					}.process(subClasses.hierarchicTypeInfo);

				}
			}.process(hierarchicTypeInfo);
		}
		
	});

	
	Generator<JDefinedClass> visitor = gen(new InterfaceGeneration("Visitor") {
		
		
		void init(final JDefinedClass visitor)  {
			JMethod method = visitor.method(JMod.NONE, cm.VOID, "visit");
			method.param(clientClass.get(), "visitee");
			
			hierarchicCreator.new SuperAction<Void>() {

				@Override
				Void present(V superClasses) {
					superClasses.visitor.get()._extends(visitor);
					return null;
				}

				@Override
				Void absent() {
					return null;
				}

			}.process(hierarchicTypeInfo);

		}
		
		
	});
	
	Generator<JDefinedClass> visitorBase = gen(new ClassGeneration("VisitorBase") {
		
		void init(final JDefinedClass visitorBase)  {
			visitorBase._implements(visitor.get());
			
			hierarchicCreator.new SubAction() {
				@Override
				void subclass(V subClasses) {
					final JMethod method = visitorBase.method(JMod.PUBLIC, cm.VOID, "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					
					if (VisitorClasses.this==subClasses) return;
					
					hierarchicCreator.new SuperAction<Void>() {

						@Override
						Void present(V superClasses) {
							method.body().add(JExpr.invoke("visit").arg(JExpr.cast(superClasses.clientClass.get(), object)));
							return null;
						}

						@Override
						Void absent() {
							return null;
						}

					}.process(subClasses.hierarchicTypeInfo);

				}
			}.process(hierarchicTypeInfo);

		}
		
	});
	
	Generator<JDefinedClass> paramVisitor = gen(new InterfaceGeneration("ParamVisitor") {
		
		private JTypeVar generic;

		void init(final JDefinedClass paramVisitor)  {
			generic = paramVisitor.generify("T");
			JMethod method = paramVisitor.method(JMod.NONE, cm.VOID, "visit");
			method.param(clientClass.get(), "object");
			method.param(generic, "param");
			
			hierarchicCreator.new SuperAction<Void>() {

				@Override
				Void present(V superClasses) {
					superClasses.paramVisitor.get()._implements(paramVisitor.narrow(generic));
					return null;
				}

				@Override
				Void absent() {
					return null;
				}

			}.process(hierarchicTypeInfo);

		}
		
	});
	
	Generator<JDefinedClass> paramVisitorBase = gen(new ClassGeneration("ParamVisitorBase") {
		
		private JTypeVar generic;

		void init(final JDefinedClass paramVisitorBase)  {
			generic = paramVisitorBase.generify("T");
			paramVisitorBase._implements(paramVisitor.get().narrow(generic));
			
			hierarchicCreator.new SubAction() {
				@Override
				void subclass(V subClasses) {
					final JMethod method = paramVisitorBase.method(JMod.PUBLIC, cm.VOID, "visit");
					final JVar object = method.param(subClasses.clientClass.get(), "object");
					final JVar param = method.param(generic, "param");
					
					if (VisitorClasses.this==subClasses) return;
					
					hierarchicCreator.new SuperAction<Void>() {

						@Override
						Void present(V superClasses) {
							method.body().add(JExpr.invoke("visit").arg(JExpr.cast(superClasses.clientClass.get(), object)).arg(param));
							return null;
						}

						@Override
						Void absent() {
							return null;
						}

					}.process(subClasses.hierarchicTypeInfo);
					
				}
			}.process(hierarchicTypeInfo);
			
		}

		
	});
	

	VisitorClasses(
			GlobalClasses context,
			HierarchicTypeInfo hierarchicTypeInfo,
			HierarchicCreatorBase<HierarchicTypeInfo, V, HierarchicTypeInfo, V, String> hierarchicCreator,
			Supplier<? extends JClass> clientClass,
			String name
	) {
		super(context);
		
		checkArgument(hierarchicTypeInfo.getTypeCategory()!=TypeCategory.OBJECT);
		
		this.hierarchicTypeInfo = hierarchicTypeInfo;
		this.hierarchicCreator = hierarchicCreator;
		this.clientClass = clientClass;
		this.name = name;
	}

	final HierarchicTypeInfo hierarchicTypeInfo;
	final HierarchicCreatorBase<HierarchicTypeInfo, V, HierarchicTypeInfo, V, String> hierarchicCreator;
	final Supplier<? extends JClass> clientClass;
	final String name;

	@Override
	String getTypeName(String name) {
		return this.name + super.getTypeName(name);
	}	

	void addVisitorDefinitionMethods(JDefinedClass target) {

		JMethod accept = target.method(JMod.NONE, cm.VOID, "accept");
		accept.param(visitor.get(), "visitor");
		
		JMethod acceptParam = target.method(JMod.NONE, cm.VOID, "accept");
		final JTypeVar acceptParamGeneric = acceptParam.generify("T");
		acceptParam.param(paramVisitor.get().narrow(acceptParamGeneric), "visitor");
		acceptParam.param(acceptParamGeneric, "param");
		
		JMethod acceptReturn = target.method(JMod.NONE, cm.directClass("R"), "accept");
		JTypeVar acceptReturnGeneric = acceptReturn.generify("R");
		acceptReturn.param(returnVisitor.get().narrow(acceptReturnGeneric), "visitor");
		
		JMethod acceptParamReturn = target.method(JMod.NONE, cm.directClass("R"), "accept");
		final JTypeVar acceptParamReturnGenericP = acceptParamReturn.generify("P");
		final JTypeVar acceptParamReturnGenericR = acceptParamReturn.generify("R");
		acceptParamReturn.param(paramReturnVisitor.get().narrow(acceptParamReturnGenericP, acceptParamReturnGenericR), "visitor");
		acceptParamReturn.param(acceptParamReturnGenericP, "param");
		
	}
	
	
	void addVisitorImplementationMethods(DefinedClassBuilder builder) {
		addVisitorImplementationMethods(builder, true, JExpr._this());
	}
	
	void addVisitorImplementationMethods(DefinedClassBuilder builder, boolean override, JExpression target) {
		DefinedMethod accept = builder.method(cm.VOID, "accept");
		if  (override) accept.override();
		JVar acceptV = accept.param(visitor.get(), "visitor");
		accept.method.body().add(acceptV.invoke("visit").arg(target));
		
		DefinedMethod acceptParam = builder.method(cm.VOID, "accept");
		if  (override) acceptParam.override();
		final JTypeVar acceptParamGeneric = acceptParam.method.generify("T");
		JVar acceptParamV = acceptParam.param(paramVisitor.get().narrow(acceptParamGeneric), "visitor");
		JVar acceptParamP = acceptParam.param(acceptParamGeneric, "param");
		acceptParam.method.body().add(acceptParamV.invoke("visit").arg(target).arg(acceptParamP));
		
		DefinedMethod acceptReturn = builder.method(cm.directClass("R"), "accept");
		if  (override) acceptReturn.override();
		JTypeVar acceptReturnGeneric = acceptReturn.method.generify("R");
		JVar acceptReturnV = acceptReturn.param(returnVisitor.get().narrow(acceptReturnGeneric), "visitor");
		acceptReturn.method.body()._return(acceptReturnV.invoke("visit").arg(target));
		
		DefinedMethod acceptParamReturn = builder.method(cm.directClass("R"), "accept");
		if  (override) acceptParamReturn.override();
		final JTypeVar acceptParamReturnGenericP = acceptParamReturn.method.generify("P");
		final JTypeVar acceptParamReturnGenericR = acceptParamReturn.method.generify("R");
		JVar acceptParamReturnV = acceptParamReturn.param(paramReturnVisitor.get().narrow(acceptParamReturnGenericP, acceptParamReturnGenericR), "visitor");
		JVar acceptParamReturnP = acceptParamReturn.param(acceptParamReturnGenericP, "param");
		acceptParamReturn.method.body()._return(acceptParamReturnV.invoke("visit").arg(target).arg(acceptParamReturnP));
	}
	
}
