package hu.mapro.model.generator.classes;

import com.google.common.base.Function;

public abstract class HierarchicCreatorBase<A, B, SI extends A, SO extends B, H> extends CreatorBase<A, B, H> {

	public HierarchicCreatorBase(GenerationBase generationBase,
			Function<? super A, H> hashFunction) {
		super(generationBase, hashFunction);
	}

	abstract class SubAction {
		
		void realSubclass(SO subClasses) {
			subclass(subClasses);
		}
		
		abstract void subclass(SO subClasses) ;
		
		@SuppressWarnings("unchecked")
		void process(SI complexType) {
			final SO targetSuperClasses = (SO) get(complexType);
			
			new PostProcessor() {
				
				@Override
				void postProcess(A input, B output) {
					find(input, output, (SO) output);
				}

				protected void find(A input, B output, final SO subClasses) {
					if (output==targetSuperClasses) {
						if (subClasses==targetSuperClasses) {
							subclass(subClasses);
						} else {
							realSubclass(subClasses);
						}
					} else {
						processSuper(input, new SuperAction<Void>() {
							@Override
							Void present(A superInput, SO superOutput) {
								find(superInput, superOutput, subClasses);
								return null;
							}
							
							@Override
							Void absent() {
								return null;
							}
						});
					}
				}
			};
		}
		
	}
	
	abstract class SuperAction<T> {
		
		T present(SO superOutput) {
			return null;
		}
		
		T present(A superInput, SO superOutput) {
			return present(superOutput);
		}
		
		T absent() {
			return null;
		}
		
		T process(A input) {
			return HierarchicCreatorBase.this.processSuper(input, this);
		}
		
	}
	
	public boolean isSuperPresent(A input) {
		return new SuperAction<Boolean>() {
			@Override
			Boolean absent() {
				return false;
			}
			
			Boolean present(A superInput, SO superOutput) {
				return true;
			}
		}.process(input);
	}
	
	abstract <T> T processSuper(A input, SuperAction<T> superAction);
	

}
