package draft.edit.sample;

import testuidraft.TestAutoBeansDraft.SuperClassDefaultGridConfigurator;
import testuidraft.TestAutoBeansDraft.SuperClassProxy;

import com.google.common.base.Function;
import com.google.inject.Inject;

public class SuperClassLabelProvider implements Function<SuperClassProxy, String> {

	SuperClassDefaultGridConfigurator superClassDefaultGridConfigurator;
	
	
	@Inject
	public SuperClassLabelProvider(
			SuperClassDefaultGridConfigurator superClassDefaultGridConfigurator) {
		super();
		this.superClassDefaultGridConfigurator = superClassDefaultGridConfigurator;
	}

	@Override
	public String apply(SuperClassProxy input) {
		return superClassDefaultGridConfigurator.createLabel(input);
	}

}
