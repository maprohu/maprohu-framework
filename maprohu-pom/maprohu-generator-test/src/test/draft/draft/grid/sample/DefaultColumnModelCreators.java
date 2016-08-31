package draft.grid.sample;

import hu.mapro.gwtui.client.grid.MultiColumn;
import testproxydraft.TestAutoBeansDraft.RefClassProxy;
import testproxydraft.TestAutoBeansDraft.SuperClassProxy;
import testproxydraft.TestAutoBeansDraft.TheClassProxy;

import com.google.inject.Singleton;

@Singleton
public class DefaultColumnModelCreators {

	TheClassDefaultColumnModelCreator theClassDefaultColumnModelCreator;
	RefClassDefaultColumnModelCreator refClassDefaultColumnModelCreator;
	
	void theClass(MultiColumn<?, ? extends TheClassProxy> reference) {
		theClassDefaultColumnModelCreator.buildColumnModel(
				new TheClassColumnBuilderWrapperDisplay(reference),
				new TheClassColumnBuilderWrapperReference(reference, this),
				new TheClassColumnBuilderWrapperSuper(reference, this)
		);
	}
	
	<R> void refClass(MultiColumn<R, ? extends RefClassProxy> reference) {
		
	}
	
	<R> void superClass(MultiColumn<R, ? extends SuperClassProxy> reference) {
		
	}
	
}
