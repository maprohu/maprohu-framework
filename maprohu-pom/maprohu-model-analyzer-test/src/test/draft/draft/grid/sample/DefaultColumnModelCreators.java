package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import hu.mapro.modeltest.AutoBeans.RefClass;
import hu.mapro.modeltest.AutoBeans.SuperClass;
import hu.mapro.modeltest.AutoBeans.TheClass;

import com.google.inject.Singleton;

@Singleton
public class DefaultColumnModelCreators {

	TheClassDefaultColumnModelCreator theClassDefaultColumnModelCreator;
	RefClassDefaultColumnModelCreator refClassDefaultColumnModelCreator;
	
	void theClass(MultiColumn<?, ? extends TheClass> reference) {
		theClassDefaultColumnModelCreator.buildColumnModel(
				new TheClassColumnBuilderWrapperDisplay(reference),
				new TheClassColumnBuilderWrapperReference(reference, this),
				new TheClassColumnBuilderWrapperSuper(reference, this)
		);
	}
	
	<R> void refClass(MultiColumn<R, ? extends RefClass> reference) {
		
	}
	
	<R> void superClass(MultiColumn<R, ? extends SuperClass> reference) {
		
	}
	
}
