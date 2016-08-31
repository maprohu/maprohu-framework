package hu.mapro.gwtui.gxt.client.edit;

import com.google.inject.Singleton;

@Singleton
public class GxtInlineEditorGridBuilder /*implements InlineEditorGridBuilder*/ {

//	@Override
//	public <T> InlineEditorGrid<T> inlineEditorGrid(
//			Function<? super T, String> modelKeyProvider,
//			InlineEditorGridConfigurator<T> editorGridConfigurator,
//			InlineEditorGridHandler<T> inlineEditorGridHandler
//	) {
//		
//		ListStore<T> store = new ListStore<T>(ModelKeyProviders.from(modelKeyProvider));
//		List<ColumnConfig<T, ?>> configs = Lists.newArrayList();
//		
//		GxtInlineEditorGrid<T> inlineEditorGrid = new GxtInlineEditorGrid<T>(
//				ColumnModels.of(configs),
//				store,
//				inlineEditorGridHandler
//		);
//
//		editorGridConfigurator.configure(new GxtInlineGridConfigurating<T>(configs, store, inlineEditorGrid.getGridEditing()));
//		
//		return inlineEditorGrid;
//	}

}
