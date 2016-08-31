package hu.mapro.model.analyzer.meta.test.run;

import hu.mapro.model.analyzer.meta.MetaModelAnalyzer;
import hu.mapro.model.analyzer.meta.test.test1.entity.BaseEntity;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class Test1 {

	@Test
	public void run() {
		ImmutableList<Class<BaseEntity>> entities = ImmutableList.of(
				BaseEntity.class
		);
		
		new MetaModelAnalyzer(
				entities, 
				ImmutableList.<Class<?>>of(),
				ImmutableList.<Class<?>>of()
		).getDefinedTypes();
	}
	
}
