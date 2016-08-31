package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenArea;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreaBuilder;
import hu.mapro.gwtui.client.uibuilder.EditorChildrenAreas;
import hu.mapro.gwtui.client.uibuilder.Fields;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Tabs;

public class DefaultEditorFieldsCollecting extends ForwardingComplexEditing implements EditorFieldsCollecting {

	final Fields fields;
	final WidgetContext widgetContext;
	
	public DefaultEditorFieldsCollecting(ComplexEditing delegate,
			Fields fields, WidgetContext widgetContext) {
		super(delegate);
		this.fields = fields;
		this.widgetContext = widgetContext;
	}

	@Override
	public LabeledFieldCustomizer addEditorField(final String label, final boolean notNull,
			final PanelBuilder builder) {
		return LabeledFieldCustomizers.editorField(fields, label, notNull, builder);
	}

	@Override
	public Panel addFill(PanelBuilder builder) {
		return fields.fill(builder);
	}

	@Override
	public EditorChildrenArea editorChildrenArea(
			EditorChildrenAreaBuilder builder) {
		return EditorChildrenAreas.from(fields, builder, widgetContext);
	}

	@Override
	public Tabs entityTabs() {
		throw new RuntimeException("not implemented");
	}

}
