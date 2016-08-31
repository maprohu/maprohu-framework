package hu.mapro.jpa.model.domain.shared;

import hu.mapro.jpa.model.domain.client.AutoBeans.FilterParameterImmutable;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterValueImmutable;
import hu.mapro.jpa.model.domain.client.AutoBeans.FilterValueImmutableReturnVisitorBase;
import hu.mapro.jpa.model.domain.client.AutoBeans.StringFilterValueImmutable;

public class SharedFilterUtil {

	public static String string(FilterParameterImmutable param) {
		return param.getValue().accept(new FilterValueImmutableReturnVisitorBase<String>() {
			@Override
			public String visit(FilterValueImmutable object) {
				throw new RuntimeException("string type expected");
			}
			
			@Override
			public String visit(StringFilterValueImmutable object) {
				return object.getValue();
			}
		});
	}
	
}
