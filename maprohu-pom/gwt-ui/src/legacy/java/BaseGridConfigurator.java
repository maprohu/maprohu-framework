package hu.mapro.gwtui.client.grid;

import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizers;
import hu.mapro.jpa.model.domain.client.FetchPlanFollower;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import hu.mapro.model.meta.ComplexType;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;


public class BaseGridConfigurator<T> implements EditorGridConfigurator<T>, FetchPlanNavigator<T> {
	
	private final ComplexType<T> complexType;
	private final MultiColumnDelegator<T> initializer;
	
	public BaseGridConfigurator(ComplexType<T> complexType,
			MultiColumnDelegator<T> initializer) {
		super();
		this.complexType = complexType;
		this.initializer = initializer;
	}

	@Override
	public void configure(final GridConfigurating<T> grid) {
		
		initializer.delegate(ColumnRoot.create(complexType, new ColumnCollector<T>() {
			@Override
			public <V> GridColumnCustomizer<V> add(
					DisplayColumn<T, V> columnConfig) {
				GridColumnCustomizer<V> cc = grid.addColumn(columnConfig.getter, columnConfig.path);
				cc.setLabel(columnConfig.label);
				return cc;
			}
		}));
	}
	
	public String createLabel(final T object) {
		if (object==null) return "<none>";
		
		final List<Object> values = Lists.newArrayList();

		initializer.delegate(ColumnRoot.create(complexType, new ColumnCollector<T>() {
			@Override
			public <V> GridColumnCustomizer<V> add(
					DisplayColumn<T, V> columnConfig) {
				values.add(columnConfig.getGetter().apply(object));
				
				return GridColumnCustomizers.fake();
			}
		}));
		
		return Joiner.on(", ").useForNull("<empty>").join(values);
	}



	@Override
	public void navigate(final FetchPlanFollower<T> path) {
		initializer.delegate(ColumnRoot.create(complexType, new ColumnCollector<T>() {
			@Override
			public <V> GridColumnCustomizer<V> add(
					DisplayColumn<T, V> columnConfig) {
				columnConfig.navigate(path);
				return GridColumnCustomizers.fake();
			}
		}));
	}

}
