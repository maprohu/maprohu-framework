package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.edit.field.CheckboxFieldControl;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;
import hu.mapro.gwtui.client.edit.field.FieldControl;
import hu.mapro.gwtui.client.edit.field.FieldFactory;
import hu.mapro.gwtui.client.edit.field.TextFieldControl;
import hu.mapro.gwtui.client.grid.InlineEditorGridHandler;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetOperation;
import hu.mapro.gwtui.client.uibuilder.BooleanField;
import hu.mapro.gwtui.client.uibuilder.BooleanFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Border;
import hu.mapro.gwtui.client.uibuilder.BorderBuilder;
import hu.mapro.gwtui.client.uibuilder.Builder;
import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.CachedComplexField;
import hu.mapro.gwtui.client.uibuilder.CachedComplexFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.CachedComplexListField;
import hu.mapro.gwtui.client.uibuilder.CachedComplexListFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.CachedComplexSetField;
import hu.mapro.gwtui.client.uibuilder.CachedComplexSetFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.DateField;
import hu.mapro.gwtui.client.uibuilder.DateFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.DoubleField;
import hu.mapro.gwtui.client.uibuilder.DoubleFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorForm;
import hu.mapro.gwtui.client.uibuilder.EditorFormBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilding;
import hu.mapro.gwtui.client.uibuilder.EnumField;
import hu.mapro.gwtui.client.uibuilder.EnumFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Fields;
import hu.mapro.gwtui.client.uibuilder.FieldsBuilder;
import hu.mapro.gwtui.client.uibuilder.FrameBuilder;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGrid;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilding;
import hu.mapro.gwtui.client.uibuilder.IntegerField;
import hu.mapro.gwtui.client.uibuilder.IntegerFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.IsPanel;
import hu.mapro.gwtui.client.uibuilder.Label;
import hu.mapro.gwtui.client.uibuilder.LabelBuilder;
import hu.mapro.gwtui.client.uibuilder.LongField;
import hu.mapro.gwtui.client.uibuilder.LongFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Margin;
import hu.mapro.gwtui.client.uibuilder.MarginBuilder;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.Scroll;
import hu.mapro.gwtui.client.uibuilder.ScrollBuilder;
import hu.mapro.gwtui.client.uibuilder.Split;
import hu.mapro.gwtui.client.uibuilder.SplitBuilder;
import hu.mapro.gwtui.client.uibuilder.StringField;
import hu.mapro.gwtui.client.uibuilder.StringFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Tabs;
import hu.mapro.gwtui.client.uibuilder.TabsBuilder;
import hu.mapro.gwtui.client.uibuilder.TextField;
import hu.mapro.gwtui.client.uibuilder.TextFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.ToolBarPanel;
import hu.mapro.gwtui.client.uibuilder.ToolBarPanelBuilder;
import hu.mapro.gwtui.client.uibuilder.UncachedComplexField;
import hu.mapro.gwtui.client.uibuilder.UncachedComplexFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.UncachedFullTextComplexField;
import hu.mapro.gwtui.client.uibuilder.UncachedFullTextComplexFieldBuilder;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;
import hu.mapro.gwtui.gxt.client.edit.GxtCheckboxFieldControl;
import hu.mapro.gwtui.gxt.client.edit.GxtFakeFieldControl;
import hu.mapro.gwtui.gxt.client.edit.GxtFieldControl;
import hu.mapro.gwtui.gxt.client.edit.GxtGridConfigurating;
import hu.mapro.gwtui.gxt.client.edit.GxtInlineEditorGrid;
import hu.mapro.gwtui.gxt.client.edit.GxtTextFieldControl;
import hu.mapro.gwtui.gxt.client.form.GxtEditorGrid;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;

public class GxtPanel implements Panel, IsPanel {


	final protected WidgetContextSupport widgetContextSupport;

	final protected SimpleContainer container;
	
	public GxtPanel(
			WidgetContextSupport widgetContextSupport,
			SimpleContainer container
	) {
		super();
		this.widgetContextSupport = widgetContextSupport;
		this.container = container;
	}

	public GxtPanel(WidgetContextSupport widgetContextSupport) {
		this(widgetContextSupport, new SimpleContainer() {
			{
				cacheSizes = false;
			}
		});
	}
	
	@Override
	public void widget(Widget widget) {
		container.clear();
		container.setWidget(widget);
		GxtUtil.forceLayout(container);
	}

	private <T extends IsWidget> T widget(T widget) {
		widget(widget.asWidget());
		return widget;
	}
	
	@Override
	public Tabs tabs(TabsBuilder tabsBuilder) {
		return widget(new GxtTabs(widgetContextSupport, tabsBuilder));
	}

	@Override
	public Fields fields(FieldsBuilder builder) {
		return widget(new GxtFields(widgetContextSupport), builder);
	}

	@Override
	public Split split(SplitBuilder builder) {
		return widget(new GxtSplit(widgetContextSupport), builder);
	}

	@Override
	public void clear() {
		container.clear();
	}

	@Override
	public EditorForm editorForm(EditorFormBuilder builder) {
		return widget(new GxtEditorForm(widgetContextSupport), builder);
	}

	@Override
	public Label label(LabelBuilder builder) {
		return widget(new GxtLabel(), builder);
	}

	public SimpleContainer getContainer() {
		return container;
	}

	@Override
	public GxtFrame frame(FrameBuilder builder) {
		return widget(new GxtFrame(widgetContextSupport), builder);
	}

	public <T extends IsWidget> T widget(T object, Builder<? super T> builder) {
		return widget(BuildingFactory.build(object, builder));
	}

	@Override
	public Panel asPanel() {
		return this;
	}
	
	@Override
	public Border border(BorderBuilder builder) {
		return widget(new GxtBorder(widgetContextSupport), builder);
	}

	@Override
	public Margin margin(MarginBuilder builder) {
		return widget(new GxtMargin(widgetContextSupport), builder);
	}

	@Override
	public Scroll scroll(ScrollBuilder builder) {
		return widget(new GxtScroll(widgetContextSupport), builder);
	}

	@Override
	public <T> EditorGrid<T> editorGrid(EditorGridBuilder<T> builder) {
		GxtEditorGridBuilding<T> built = new GxtEditorGridBuilding<T>();
		builder.build(built);
		built.handlers.fire();
		GxtEditorGrid<T> w = new GxtEditorGrid<T>(widgetContextSupport, ColumnModels.of(built.columnConfigs), built.listStore);
		built.registerClose(widgetContextSupport);
		widget(w);
		return w;
	}

	public static class GxtEditorGridBuilding<T> implements EditorGridBuilding<T> {
		
		final List<ColumnConfig<T, ?>> columnConfigs = Lists.newArrayList();
		ListStore<T> listStore;

		final Handlers handlers = Handlers.newInstance(); 
		final Handlers closeHandlers = Handlers.newInstance(); 
		
		void registerClose(WidgetContext widgetContext) {
			widgetContext.registerListener(new AbstractWidgetListener() {
				@Override
				public void onDestroy(WidgetOperation operation) {
					operation.approve(closeHandlers);
				}
			});
		}
		
		@Override
		public <V> GridColumnCustomizer<V> addColumn(
				Function<? super T, ? extends ObservableValue<V>> getter,
				String path
		) {
			return GxtGridConfigurating.createColumn(listStore, columnConfigs, closeHandlers, getter, path);
		}

//		protected <V> GridColumnCustomizer<V> createColumn(
//				final ValueProvider<T, V> valueProvider
//		) {
//			final ColumnConfig<T, V> cc = createColumnConfig(valueProvider);
//			return addColumnConfig(valueProvider, cc);
//		}
//
//		protected <V> GridColumnCustomizer<V> addColumnConfig(
//				final ValueProvider<T, V> valueProvider, 
//				final ColumnConfig<T, V> cc
//		) {
//			columnConfigs.add(cc);
//			return new GridColumnCustomizer<V>() {
//
//				@Override
//				public GridColumnCustomizer<V> setWidth(double value, TableWidthUnit unit) {
//					switch (unit) {
//					case PX:
//						cc.setWidth((int) value);
//						break;
//
//					default:
//						throw new RuntimeException("unimplemented width unit: " + unit);
//					}
//					return this;
//				}
//
//				@Override
//				public GridColumnCustomizer<V> setCell(Cell<V> cell) {
//					cc.setCell(cell);
//					return this;
//				}
//
//				@SuppressWarnings({ "unchecked", "rawtypes" })
//				@Override
//				public GridColumnCustomizer<V> sort(final SortingDirection sortingDirection) {
////					handlers.add(new Action() {
////						@Override
////						public void perform() {
//							listStore.addSortInfo(
//									new StoreSortInfo(
//											valueProvider, 
//											sortingDirection==SortingDirection.DESCENDING 
//												? SortDir.DESC
//												: SortDir.ASC
//									)
//							);
////						}
////					});
//					return this;
//				}
//
//				@Override
//				public GridColumnCustomizer<V> setLabel(String label) {
//					cc.setHeader(label);
//					return this;
//				}
//
//				@Override
//				public GridColumnCustomizer<V> setVisible(boolean visible) {
//					cc.setHidden(!visible);
//					return null;
//				}
//			};
//		}
//
//		protected <V> ColumnConfig<T, V> createColumnConfig(
//				final ValueProvider<T, V> valueProvider) {
//			final ColumnConfig<T, V> cc = new ColumnConfig<T, V>(valueProvider);
//			return cc;
//		}

		@Override
		public void setModelKeyProvider(
				ProvidesKey<? super T> modelKeyProvider) {
			listStore = new ListStore<T>(ModelKeyProviders.from(modelKeyProvider));
		}
	}	
	
	@Override
	public <T> InlineEditorGrid<T> inlineEditorGrid(
			InlineEditorGridBuilder<T> builder) {
		GxtInlineEditorGridBuilding<T> built = new GxtInlineEditorGridBuilding<T>();
		builder.build(built);
		final GxtInlineEditorGrid<T> editorGrid = new GxtInlineEditorGrid<T>(widgetContextSupport, ColumnModels.of(built.columnConfigs), built.listStore, built.inlineEditorGridHandler, widgetContextSupport.getDefaultUiMessages());
		built.gridEditing = editorGrid.getGridEditing();
		built.handlers.fire();
		built.registerClose(widgetContextSupport);
		widget(editorGrid);
//		com.google.gwt.core.client.Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//			@Override
//			public void execute() {
//				editorGrid.redraw();
//			}
//		});
		return editorGrid;
	}

	public class GxtInlineEditorGridBuilding<T> extends GxtEditorGridBuilding<T> implements InlineEditorGridBuilding<T> {

		GridEditing<T> gridEditing;
		InlineEditorGridHandler<T> inlineEditorGridHandler;
		
		@SuppressWarnings("unchecked")
		@Override
		public <V> GridColumnCustomizer<V> addEditableColumn(
				Function<? super T, ? extends ObservableValue<V>> value, 
				final FieldConstructor<V> fieldConstructor, 
				String path
		) {
			ValueProvider<T, V> valueProvider = GxtGridConfigurating.readWriteValueProvider(value, path);
			final ColumnConfig<T, V> cc = GxtGridConfigurating.createColumnConfig(valueProvider);
			
			handlers.add(new Action() {
				
				@Override
				public void perform() {
					
					FieldControl<V> constructedField = fieldConstructor.constructField(new FieldFactory() {
						
						@Override
						public TextFieldControl text() {
							return new GxtTextFieldControl();
						}
						
						@Override
						public CheckboxFieldControl checkbox() {
							return new GxtCheckboxFieldControl();
						}

						@Override
						public <X> FieldControl<X> fake() {
							return new GxtFakeFieldControl<X>();
						}
						
					});
					
					Field<V> gxtField = ((GxtFieldControl<V, Field<V>>)constructedField).getField();
					if (gxtField!=null) {
						gridEditing.addEditor(cc, gxtField);
					}
				}
			});
			
			GxtGridConfigurating.registerObservers(listStore, closeHandlers, value);
			return GxtGridConfigurating.addColumnConfig(listStore, columnConfigs, valueProvider, cc);
		}

		@Override
		public void setInlineEditorGridHandler(
				InlineEditorGridHandler<T> inlineEditorGridHandler) {
			this.inlineEditorGridHandler = inlineEditorGridHandler;
		}
		

	}

	@Override
	public WidgetContext asWidgetContext() {
		return widgetContextSupport;
	}

	@Override
	public void setHeight(Integer height) {
		container.setHeight(height);
	}

	@Override
	public StringField stringField(StringFieldBuilder builder) {
		return widget(new GxtStringField(widgetContextSupport), builder);
	}

	@Override
	public TextField textField(TextFieldBuilder builder) {
		return widget(new GxtTextField(widgetContextSupport), builder);
	}
	
	@Override
	public DoubleField doubleField(DoubleFieldBuilder builder) {
		return widget(new GxtDoubleField(widgetContextSupport), builder);
	}

	@Override
	public IntegerField integerField(IntegerFieldBuilder builder) {
		return widget(new GxtIntegerField(widgetContextSupport), builder);
	}

	@Override
	public DateField dateField(DateFieldBuilder builder) {
		return widget(new GxtDateField(widgetContextSupport), builder);
	}

	@Override
	public LongField longField(LongFieldBuilder builder) {
		return widget(new GxtLongField(widgetContextSupport), builder);
	}

	@Override
	public BooleanField booleanField(BooleanFieldBuilder builder) {
		return widget(new GxtBooleanField(widgetContextSupport.getDefaultUiMessages(), widgetContextSupport), builder);
	}

	@Override
	public EnumField enumField(EnumFieldBuilder builder) {
		return widget(new GxtEnumField(), builder);
	}

	@Override
	public CachedComplexField cachedComplexField(
			CachedComplexFieldBuilder builder) {
		return widget(new GxtCachedComplexField(widgetContextSupport), builder);
	}

	@Override
	public CachedComplexSetField cachedComplexSetField(
			CachedComplexSetFieldBuilder builder) {
		return widget(new GxtCachedComplexSetField(), builder);
	}
	
	@Override
	public CachedComplexListField cachedComplexListField(
			CachedComplexListFieldBuilder builder) {
		return widget(new GxtCachedComplexListField(), builder);
	}
	
	@Override
	public UncachedComplexField uncachedComplexField(
			UncachedComplexFieldBuilder builder) {
		return widget(new GxtUncachedComplexField(widgetContextSupport), builder);
	}

	@Override
	public UncachedFullTextComplexField uncachedFullTextComplexField(
			UncachedFullTextComplexFieldBuilder builder) {
		return widget(new GxtUncachedFullTextComplexField(widgetContextSupport.getDefaultUiMessages(), widgetContextSupport), builder);
	}

	@Override
	public ToolBarPanel toolBarPanel(ToolBarPanelBuilder builder) {
		return widget(new GxtToolBarPanel(widgetContextSupport), builder);
	}

	public WidgetContextSupport getWidgetContextSupport() {
		return widgetContextSupport;
	}
	
	
}
