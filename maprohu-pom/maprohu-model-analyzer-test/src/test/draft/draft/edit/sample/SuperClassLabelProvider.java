package draft.edit.sample;

import hu.mapro.modeltest.AutoBeans.SuperClass;
import hu.mapro.modeltest.AutoBeans.SuperClassDefaultGridConfigurator;

import com.google.common.base.Function;
import com.google.inject.Inject;

public class SuperClassLabelProvider implements Function<SuperClass, String> {

	SuperClassDefaultGridConfigurator superClassDefaultGridConfigurator;
	
	
	@Inject
	public SuperClassLabelProvider(
			SuperClassDefaultGridConfigurator superClassDefaultGridConfigurator) {
		super();
		this.superClassDefaultGridConfigurator = superClassDefaultGridConfigurator;
	}

	@Override
	public String apply(SuperClass input) {
		return superClassDefaultGridConfigurator.createLabel(input);
	}

}
