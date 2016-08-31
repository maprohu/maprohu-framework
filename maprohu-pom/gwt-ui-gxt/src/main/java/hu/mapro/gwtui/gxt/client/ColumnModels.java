package hu.mapro.gwtui.gxt.client;

import java.util.List;

import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class ColumnModels {

	@SuppressWarnings("unchecked")
	public static <T> ColumnModel<T> of(List<? extends ColumnConfig<T, ?>> configs) {
		return new ColumnModel<T>((List<ColumnConfig<T, ?>>) configs);
	}
	
}
