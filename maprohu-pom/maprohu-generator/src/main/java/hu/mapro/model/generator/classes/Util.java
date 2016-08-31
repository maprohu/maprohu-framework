package hu.mapro.model.generator.classes;

import hu.mapro.model.analyzer.FieldInfo;
import hu.mapro.model.impl.Cardinality;
import hu.mapro.model.meta.Field;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JMethod;

public class Util {

	public static void override(JMethod method) {
		method.annotate(Override.class);
	}

	public static String getReadableSymbolName(final String camelCase) {
	    final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
	    final Matcher m = p.matcher(StringUtils.capitalize(camelCase));
	    final StringBuilder builder = new StringBuilder();
	    while (m.find()) {
	        builder.append(m.group()).append(" ");
	    }
	    return builder.toString().trim();
	}
	
	public static String getMutatorMethodName(
	        final String fieldName) {
	    return "set"
	            + StringUtils.capitalize(fieldName);
	}
	
}
