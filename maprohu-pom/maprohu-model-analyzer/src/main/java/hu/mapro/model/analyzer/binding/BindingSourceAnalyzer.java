package hu.mapro.model.analyzer.binding;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.sun.codemodel.JCodeModel;

public class BindingSourceAnalyzer {

	public static final Collection<SingleBinding>  analyze(
			File packageDirectory, 
			final String[] definitionPackages,
			final JCodeModel cm
	) throws Exception {
		List<SingleBinding> bindings = Lists.newArrayList();
		analyze(packageDirectory, bindings, definitionPackages, cm);
		return bindings;
	}
	
	
	public static void analyze(
			File packageDirectory, 
			final Collection<SingleBinding> bindings, 
			final String[] definitionPackages,
			final JCodeModel cm
	) throws Exception {
		Collection<File> files = FileUtils.listFiles(packageDirectory, new String[] {"java"}, true);
		
		for (File f : files) {
			CompilationUnit j = JavaParser.parse(f);
			
			for (TypeDeclaration td : j.getTypes()) {
				td.accept(new VoidVisitorAdapter<Void>() {
					
					@Override
					public void visit(ClassOrInterfaceDeclaration n, Void arg) {
						process(n, n.getExtends());
						process(n, n.getImplements());
						
					}

					private void process(ClassOrInterfaceDeclaration n, List<ClassOrInterfaceType> types) {
						if (types==null) return;
						
						for (ClassOrInterfaceType sup : types) {
							if (isDefinition(sup)) {
								bindings.add(new SingleBindingBean(cm.directClass(sup.getName()), cm.directClass(n.getName())));
							}
						}
						
					}

					private boolean isDefinition(ClassOrInterfaceType sup) {
						if (sup.getScope()!=null) return isDefinition(sup.getScope());
						
						for (String p: definitionPackages) {
							if (sup.getName().startsWith(p)) return true;
						}
						
						return false;
					}
					
				}, null);
			}
		}
		
	}
	
}
