package hu.mapro.modeltest;

import hu.mapro.modeltest.imdate.AutoBeans;
import hu.mapro.modeltest.imdate.ref.BoundingBox;
import hu.mapro.modeltest.imdate.ref.Composite;
import hu.mapro.modeltest.imdate.ref.SqlFilter;
import hu.mapro.modeltest.imdate.ref.UserArea;
import hu.mapro.server.model.Wrapper;
import junit.framework.Assert;

import org.junit.Test;

public class TestWrapper {

	@Test
	public void test() {
		
		Wrapper wrapper = Wrapper.create(AutoBeans.class);
		
		UserArea userArea = new UserArea();
		userArea.setUsername("theuser");
		
		SqlFilter sqlFilter = new SqlFilter();
		sqlFilter.setSql("sql");
		
		userArea.setFilter(sqlFilter);
		
		hu.mapro.modeltest.imdate.AutoBeans.UserArea wrappedUserArea = wrapper.wrap(userArea);
		
		Assert.assertEquals("theuser", wrappedUserArea.getUsername());
		Assert.assertEquals("sql", ((hu.mapro.modeltest.imdate.AutoBeans.SqlFilter)wrappedUserArea.getFilter()).getSql());
		
	}

	@Test
	public void test2() {
		
		Wrapper wrapper = Wrapper.create(AutoBeans.class);
		
		Composite composite = new Composite();
		BoundingBox boundingBox = new BoundingBox();
		composite.getAreas().add(boundingBox);
		boundingBox.setLatitude1(5.0);
		
		hu.mapro.modeltest.imdate.AutoBeans.Composite wrappedComposite = wrapper.wrap(composite);
		
		Assert.assertEquals(5.0, ((hu.mapro.modeltest.imdate.AutoBeans.BoundingBox)wrappedComposite.getAreas().get(0)).getLatitude1());
		
	}
	
}
