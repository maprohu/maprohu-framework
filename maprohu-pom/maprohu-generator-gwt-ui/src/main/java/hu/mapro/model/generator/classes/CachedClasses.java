package hu.mapro.model.generator.classes;

import hu.mapro.gwt.data.client.CachedClientStore;
import hu.mapro.gwt.data.client.DefaultCachedClientStore;
import hu.mapro.gwt.data.client.DefaultSubCachedClientStore;
import hu.mapro.gwt.data.client.MapObjectStore;
import hu.mapro.gwt.data.client.ObjectStore;
import hu.mapro.model.analyzer.AbstractTypeInfoVisitor;
import hu.mapro.model.analyzer.FieldInfo;

import com.sun.codemodel.JDefinedClass;

public class CachedClasses extends ComplexUiExtension {

	Generator<JDefinedClass> objectStore = new InterfaceGeneration("ObjectStore") {
		public void init(JDefinedClass objectStore) {
			objectStore._implements(cm.ref(ObjectStore.class).narrow(clientClass.get()));
	
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				Void absent() {
					final FieldInfo key = complexUiClasses.keyProvider.get();
					if (key!=null) {
						new DefaultImplementation() {
							void init(JDefinedClass object) {
								object._extends(cm.ref(MapObjectStore.class).narrow(clientClass.get(), globalClasses.getClientPropertyType(key)));
								builder.inject().superArg(complexUiClasses.fieldClasses.get(key).staticRef());
							}
						};
					}
					
					return null;
				}
			}.process(complexTypeInfo);

			complexTypeInfo.visit(new AbstractTypeInfoVisitor<Void>() {
				protected Void defaultVisit(hu.mapro.model.analyzer.TypeInfo type) {
					return null;
				}
				
				public Void visit(hu.mapro.model.analyzer.EntityTypeInfo type) {
					new DefaultImplementation() {
						void init(JDefinedClass implementation) {
							implementation._extends(cm.ref(hu.mapro.gwt.data.client.LongIdObjectStore.class).narrow(clientClass.get()));
						}
					};

					return null;
				}
			});
			
		};
	};
	
	
	Generator<JDefinedClass> clientStore = new InterfaceGeneration("ClientStore") {
		public void init(JDefinedClass cachedClientStore) {
			cachedClientStore._implements(cm.ref(CachedClientStore.class).narrow(clientClass.get()));
			
			globalUiClasses.typeClasses.new SuperAction<Void>() {
				Void absent() {
					new DefaultImplementation() {
						void init(JDefinedClass object) {
							object._extends(cm.ref(DefaultCachedClientStore.class).narrow(clientClass.get()));
							
							builder.singleton();
							builder.injectSuper(complexUiClasses.editingPersistenceContextFactory.get(), objectStore.get());
						}
					};
					
					return null;
				}
				
				Void present(final ComplexUiClasses superOutput) {
					new DefaultImplementation() {
						void init(JDefinedClass object) {
							object._extends(cm.ref(DefaultSubCachedClientStore.class).narrow(superOutput.clientClass.get(), clientClass.get()));
							
							builder.singleton();
							builder.injectSuper(
									superOutput.cached().clientStore.get(),
									complexClasses.instanceOfPredicate.get()
							);
						}
					};
					return null;
				}
				
			}.process(complexTypeInfo);
			
		}
	};

	public CachedClasses(ComplexUiClasses complexUiClasses) {
		super(complexUiClasses);
	}

	
	
}
