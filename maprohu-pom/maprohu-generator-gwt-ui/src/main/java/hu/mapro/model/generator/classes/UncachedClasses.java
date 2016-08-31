package hu.mapro.model.generator.classes;

import hu.mapro.gwt.data.client.DefaultUncachedClientStore;

import com.sun.codemodel.JDefinedClass;

public class UncachedClasses extends ComplexUiExtension {

	Generator<JDefinedClass> clientStore = new ClassGeneration("ClientStore") {
		public void init(JDefinedClass uncachedClientStore) {
			uncachedClientStore._extends(cm.ref(DefaultUncachedClientStore.class).narrow(clientClass.get()));
			
			builder.singleton();
			builder.injectSuper(complexUiClasses.editingPersistenceContextFactory.get());

			complexUiClasses.fullTextVisitable.new Visitor() {
				public void present(FullTextClasses value) {
					builder.inject().superArg(value.fullTextFilterItemRef(builder.injectParam(complexUiClasses.filterRepository.get())));
				}
			};
		}
	};
	
	
	public UncachedClasses(ComplexUiClasses complexUiClasses) {
		super(complexUiClasses);
	}

}
