package draft.edit.sample;

import hu.mapro.gwtui.client.edit.field.CachedComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.UncachedComplexFieldCreator;
import hu.mapro.gwtui.client.edit.field.impl.DefaultComplexFieldCreator;
import hu.mapro.modeltest.AutoBeans.SuperClass;

import com.google.inject.Inject;

public class SuperClassComplexEditorFieldCreator extends DefaultComplexFieldCreator<SuperClass> {

	@Inject
	public SuperClassComplexEditorFieldCreator(
			CachedComplexFieldCreator cachedComplexFieldCreator,
			UncachedComplexFieldCreator uncachedComplexFieldCreator,
			SuperClassModelKeyProvider modelKeyProvider,
			SuperClassLabelProvider labelProvider,
			SuperClassUncachedValueProvider valueProvider) {
		super(
				cachedComplexFieldCreator, 
				uncachedComplexFieldCreator, 
				modelKeyProvider,
				labelProvider, 
				valueProvider
		);
	}

	
	
}
