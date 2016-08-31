package hu.mapro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import hu.mapro.model.generator.UpdateCodeWriter;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

public class TestUpdateCodeWriter {

	@Test
	public void test() throws Exception {
		
		JCodeModel cm = new JCodeModel();
		JDefinedClass cl = cm._class("test.Test");
		
		File target = new File("./target/test-output");
		
		FileUtils.deleteDirectory(target);
		target.mkdirs();
		
		File javaFile = new File(target, "test/Test.java");
		
		
		cm.build(new UpdateCodeWriter(target));
		
		Assert.assertTrue(javaFile.exists());
		long ts0 = javaFile.lastModified();
		
		Thread.sleep(500);
		cm.build(new UpdateCodeWriter(target));
		
		long ts1 = javaFile.lastModified();
		assertEquals(ts0, ts1);

		cl.field(JMod.NONE, cm.INT, "x");
		
		Thread.sleep(500);
		cm.build(new UpdateCodeWriter(target));
		
		long ts2 = javaFile.lastModified();
		assertFalse(ts0 == ts2);
		
		
		
		
		
	}
	
}
