package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.ComplexTypeInfo;
import hu.mapro.model.analyzer.DefinedTypeInfo;
import hu.mapro.model.analyzer.EntityTypeInfo;
import hu.mapro.model.analyzer.EnumTypeInfo;
import hu.mapro.model.analyzer.ObjectTypeInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.analyzer.ValueTypeInfo;

import com.google.common.base.Function;

class TypeGenerator<
//	TYPE,
//	BUILTIN extends TYPE,
//	OBJECT extends TYPE,
//	DEFINED extends TYPE,
	DEFINED,
	ENUM extends DEFINED,
	COMPLEX extends DEFINED,
	VALUE extends COMPLEX,
	ENTITY extends COMPLEX
	
> extends HierarchicCreatorBase<DefinedTypeInfo, DEFINED, ComplexTypeInfo, COMPLEX, String> {

	public TypeGenerator(GenerationBase generationBase) {
		super(generationBase, new Function<TypeInfo, String>() {
			@Override
			public String apply(TypeInfo input) {
				return input.getClassFullName();
			}
		});
	}
	
	@Override
	protected DEFINED create(DefinedTypeInfo input) {
		return input.visit(new AbstractTypeInfoVisitor<DEFINED>() {

			@Override
			public DEFINED visit(final EnumTypeInfo type) {
				final ENUM created = createEnum(type);
				inits.init(new Init() {
					@Override
					public void init() {
						initEnum(created, type);
					}
				});
				return created;
			}

			@Override
			public DEFINED visit(final ValueTypeInfo type) {
				final VALUE created = createValue(type);
				inits.init(new Init() {
					@Override
					public void init() {
						initValue(created, type);
					}
				});
				return created;
			}

			@Override
			public DEFINED visit(final EntityTypeInfo type) {
				final ENTITY created = createEntity(type);
				inits.init(new Init() {
					@Override
					public void init() {
						initEntity(created, type);
					}
				});
				return created;
			}
			

		});
	}
	
	public DEFINED get(DefinedTypeInfo input) {
		return (DEFINED) super.get(input);
	}
	
	@SuppressWarnings("unchecked")
	public COMPLEX get(ComplexTypeInfo input) {
		return (COMPLEX) super.get(input);
	}
	
	@SuppressWarnings("unchecked")
	public ENTITY get(EntityTypeInfo input) {
		return (ENTITY) super.get(input);
	}
	
	@SuppressWarnings("unchecked")
	public VALUE get(ValueTypeInfo input) {
		return (VALUE) super.get(input);
	}
	
	@SuppressWarnings("unchecked")
	public ENUM get(EnumTypeInfo input) {
		return (ENUM) super.get(input);
	}
	
	protected DEFINED createDefined(DefinedTypeInfo type) {
		throw new RuntimeException("is not implemented");
	}
	
	protected void initDefined(DEFINED created, DefinedTypeInfo type) {
	}
	
	@SuppressWarnings("unchecked")
	protected ENUM createEnum(EnumTypeInfo type) {
		return (ENUM) createDefined(type);
	}

	protected void initEnum(ENUM created, EnumTypeInfo type) {
		initDefined(created, type);
	}
	
	
	@SuppressWarnings("unchecked")
	protected COMPLEX createComplex(ComplexTypeInfo type) {
		return (COMPLEX) createDefined(type);
	}
	
	protected void initComplex(COMPLEX created, ComplexTypeInfo type) {
		initDefined(created, type);
	}
	
	@SuppressWarnings("unchecked")
	protected VALUE createValue(ValueTypeInfo type) {
		return (VALUE) createComplex(type);
	}
	
	protected void initValue(VALUE created, ValueTypeInfo type) {
		initComplex(created, type);
	}
	
	@SuppressWarnings("unchecked")
	protected ENTITY createEntity(EntityTypeInfo type) {
		return (ENTITY) createComplex(type);
	}
	
	protected void initEntity(ENTITY created, EntityTypeInfo type) {
		initComplex(created, type);
	}
	
	public <T> T visit(DefinedTypeInfo typeInfo, final Visitor<T> visitor) {
		return typeInfo.visit(new AbstractTypeInfoVisitor<T>() {

			@Override
			public T visit(EnumTypeInfo type) {
				return visitor.visitEnum(get(type));
			}

			@Override
			public T visit(ValueTypeInfo type) {
				return visitor.visitValue(get(type));
			}

			@Override
			public T visit(EntityTypeInfo type) {
				return visitor.visitEntity(get(type));
			}
		});
	}
	
	class Visitor<T> {
		
		T visitDefined(DEFINED classes)  {
			throw new RuntimeException("not implemented");
		}
		
		T visitComplex(COMPLEX classes)  {
			return visitDefined(classes);
		}
		
		T visitEnum(ENUM classes)  {
			return visitDefined(classes);
		}
		
		T visitValue(VALUE classes)  {
			return visitComplex(classes);
		}
		
		T visitEntity(ENTITY classes)  {
			return visitComplex(classes);
		}
		
		T accept(DefinedTypeInfo definedTypeInfo) {
			return visit(definedTypeInfo, this);
		}
		
		void process() {
			new PostProcessor() {
				@Override
				void postProcess(DefinedTypeInfo input, DEFINED output) {
					accept(input);
				}
			};
		}
		
	}
	
	class AbstractVisitor<T> extends Visitor<T> {
		T visitDefined(DEFINED classes) {
			return null;
		}
	}
	
	class VoidVisitor extends AbstractVisitor<Void> {
		public VoidVisitor() {
			process();
		}
		
		final Void visitDefined(DEFINED classes) {
			visitDefinedVoid(classes);
			return null;	
		}

		void visitDefinedVoid(DEFINED classes) {
		}
		
		final Void visitComplex(COMPLEX classes) {
			visitComplexVoid(classes);
			return null;	
		}

		void visitComplexVoid(COMPLEX classes) {
			visitDefinedVoid(classes);
		}

		final Void visitEnum(ENUM classes) {
			visitEnumVoid(classes);
			return null;	
		}

		void visitEnumVoid(ENUM classes) {
			visitDefinedVoid(classes);
		}

		final Void visitValue(VALUE classes) {
			visitValueVoid(classes);
			return null;	
		}

		void visitValueVoid(VALUE classes) {
			visitComplexVoid(classes);
		}

		final Void visitEntity(ENTITY classes) {
			visitEntityVoid(classes);
			return null;	
		}
		
		void visitEntityVoid(ENTITY classes) {
			visitComplexVoid(classes);
		}
		

	}
	
	abstract class Processor<T> {
		
		void processDefined(DEFINED classes, T object)  {
		}
		
		void processComplex(COMPLEX classes, T object)  {
			processDefined(classes, object);
		}
		
		void processEnum(ENUM classes, T object)  {
			processDefined(classes, object);
		}

		void processEntity(ENTITY classes, T object)  {
			processComplex(classes, object);
		}

		void processValue(VALUE classes, T object)  {
			processComplex(classes, object);
		}

	}

	@Override
	<T> T processSuper(DefinedTypeInfo input, final SuperAction<T> superAction) {
		return input.visit(new AbstractTypeInfoVisitor<T>() {
			@Override
			public T visit(TypeInfo type) {
				return null;
			}
			
			@Override
			public T visit(ComplexTypeInfo complexType) {
				return complexType.getSuperType().visit(new AbstractTypeInfoVisitor<T>() {
					@Override
					public T visit(ObjectTypeInfo type) {
						return superAction.absent(); 
					}
					
					@Override
					public T visit(ComplexTypeInfo type) {
						return superAction.present(type, get(type));
					}
				});
			}
			
		});
	}
	
	
}


