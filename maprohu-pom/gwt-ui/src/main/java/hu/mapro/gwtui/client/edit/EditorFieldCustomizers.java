package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.field.EntityAwareEditorFieldCreator;
import hu.mapro.gwtui.client.uibuilder.Builders;
import hu.mapro.gwtui.client.uibuilder.Connector;
import hu.mapro.gwtui.client.uibuilder.Connectors;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenArea;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreaBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreaBuilding;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.PanelConnectors;

import com.google.gwt.user.client.ui.IsWidget;


public class EditorFieldCustomizers {

	public static <V> EditorFieldCustomizer<V> from(
			final LabeledFieldCustomizer labeledFieldCustomizer
	) {
		return new EditorFieldCustomizer<V>() {
			@Override
			public void setLabel(String text) {
				labeledFieldCustomizer.setLabel(text);
			}

			@Override
			public void setNotNull(boolean notNull) {
				labeledFieldCustomizer.setNotNull(notNull);
			}

			@Override
			public void setFill(boolean fill) {
				labeledFieldCustomizer.setFill(fill);
			}
		};
	}

	public static <T, V> EditorFieldCustomizer<V> from(
			String label,
			boolean notNull,
			final EditorFieldsCollecting collecting,
			final EntityAwareEditorFieldCreator<T, V> editorFieldCreator,
			final T editingEntity,
			final ObservableValue<V> observableValue
	) {
		return from(collecting.addEditorField(
				label, 
				notNull,
				new PanelBuilder() {
					@Override
					public void build(Panel o) {
						Builders.removeOnDestroy(o.asWidgetContext(), editorFieldCreator.createField(editingEntity, observableValue, collecting, o));
					}
				}
		));
	}

	public static <T, V> EditorFieldCustomizer<V> tab(
			String label,
			boolean notNull,
			final EditorFieldsCollecting collecting,
			final EntityAwareEditorFieldCreator<T, V> editorFieldCreator,
			final T editingEntity,
			final ObservableValue<V> observableValue
	) {
		return tab(collecting, label, new PanelBuilder() {
			@Override
			public void build(Panel o) {
				editorFieldCreator.createField(editingEntity, observableValue, collecting, o);
			}
		}, PanelConnectors.margin(), Connectors.<Panel>direct());
	}
	
	
	private static final EditorFieldCustomizer<Object> FAKE = new EditorFieldCustomizer<Object>() {
		@Override
		public void setLabel(String text) {
		}

		@Override
		public void setNotNull(boolean notNull) {
		}

		@Override
		public void setFill(boolean fill) {
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <V> EditorFieldCustomizer<V> fake() {
		return (EditorFieldCustomizer<V>) FAKE;
	}


	public static <V> EditorFieldCustomizer<V> tab(
			EditorFieldsCollecting editorFieldsCollecting,
			String header,
			final IsWidget widget
	) {
		return tab(editorFieldsCollecting, header, new PanelBuilder() {
			@Override
			public void build(Panel o) {
				o.widget(widget.asWidget());
			}
		}); 
		
	}

	public static <V> EditorFieldCustomizer<V> tab(
			EditorFieldsCollecting editorFieldsCollecting,
			final String header,
			final PanelBuilder panelBuilder
	) {
		return tab(editorFieldsCollecting, header, panelBuilder, Connectors.<Panel>direct(), Connectors.<Panel>direct()); 
	}
	
	public static <V> EditorFieldCustomizer<V> tab(
			EditorFieldsCollecting editorFieldsCollecting,
			final String header,
			final PanelBuilder panelBuilder,
			final Connector<Panel> framed,
			final Connector<Panel> unframed
	) {
        final EditorChildrenArea tab = editorFieldsCollecting.editorChildrenArea(new EditorChildrenAreaBuilder() {
			@Override
			public void build(EditorChildrenAreaBuilding o) {
				o.getTarget().setHeader(header);
				
				(o.isFramed() ? framed : unframed).connect(o.getTarget().asPanel(), panelBuilder);
			}
		});
		
		return new EditorFieldCustomizer<V>() {
			@Override
			public void setLabel(String text) {
				tab.setHeader(text);
			}

			@Override
			public void setNotNull(boolean notNull) {
			}

			@Override
			public void setFill(boolean fill) {
				tab.setFill(fill);
			}
		};
	}
	
	
}
