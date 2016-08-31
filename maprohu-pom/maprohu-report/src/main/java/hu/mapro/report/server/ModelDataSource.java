package hu.mapro.report.server;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import hu.mapro.model.meta.PathFunction;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class ModelDataSource<T> implements JRDataSource {

	final Map<String, Function<? super T, ?>> properties = Maps.newHashMap();
	final List<TextColumnBuilder<?>> columnBuilders = Lists.newArrayList();
	
	final Collection<T> items;
	Iterator<T> iterator;
	T current;
	
	public ModelDataSource(Collection<T> items) {
		super();
		this.items = items;
		
		iterator = items.iterator();
	}

	public <V> TextColumnBuilder<V> add(
			String label, 
			PathFunction<? super T, V> pathFunction, 
			DRIDataType<? super V, V> columnType
	) {
		properties.put(pathFunction.getPath(), pathFunction);
		
		TextColumnBuilder<V> columnBuilder = col.column(label, pathFunction.getPath(), columnType);
		columnBuilders.add(columnBuilder);
		return columnBuilder;
	}
	
	@SuppressWarnings("rawtypes")
	public ColumnBuilder[] getColumnBuilders() {
		return columnBuilders.toArray(new ColumnBuilder[0]);
	}
	
	@Override
	public boolean next() throws JRException {
		if (iterator.hasNext()) {
			current = iterator.next();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		return properties.get(jrField.getName()).apply(current);
	}

	public static <T> ModelDataSource<T> of(Collection<T> items) {
		return new ModelDataSource<T>(items);
	}
}
