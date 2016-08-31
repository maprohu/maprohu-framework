package hu.mapro.model.generator.classes;

import hu.mapro.gwt.common.shared.NullSafeFunction;
import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.analyzer.TypeInfo;
import hu.mapro.model.generator.classes.DefinedClassBuilder.Reflector;
import hu.mapro.model.meta.Field;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;

public class FieldUiContext extends FieldUiGenerationContext {

	
	final Field<?, ?> fieldObject;
	final String name;
	final String propertyName;
	final JClass clientPropertyType;
	final JClass clientElementType;
	final JClass serverPropertyType;
	final TypeInfo valueType;
	final FieldInfo fieldInfo;
	
	
	final FieldClasses fieldClasses;
	
	public FieldUiContext(ComplexUiClasses complexClasses, FieldInfo fieldInfo) {
		super(complexClasses, new FieldHelper(complexClasses.globalClasses, fieldInfo));
		
		fieldClasses = complexClasses.complexClasses.getClasses(fieldInfo);
		this.fieldObject = fieldInfo;
		
		propertyName = fieldClasses.propertyName;
		name = fieldClasses.name;
		this.clientPropertyType = fieldClasses.clientPropertyType;
		this.clientElementType = globalClasses.getScalarClientPropertyType(fieldInfo);
		valueType = fieldInfo.getValueType();
		
		serverPropertyType = globalClasses.getServerPropertyType(fieldInfo);
		this.fieldInfo  = fieldInfo;
		
	}

	public boolean isEditable() {
		return 
				!propertyName.equals("id")&&!propertyName.equals("version")&&
				fieldObject.isWritable() && fieldObject.isReadable();
	}

	public JExpression staticRef() {
		return fieldClasses.staticRef();
	}
	
	public static void initFunction(
			DefinedClassBuilder builder,
			JClass input,
			JClass output,
			String readMethod
	) {
		if (readMethod!=null) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Reflector<NullSafeFunction<?, ?>> reflector = (Reflector)builder._extends(NullSafeFunction.class, input, output);
			
			reflector.override().applyNonNull(null);
			JMethod fieldApply = reflector.method();
			JVar fieldApplyObject = reflector.param("object");
			fieldApply.body()._return(fieldApplyObject.invoke(readMethod));
		}
	}	

	
	
}