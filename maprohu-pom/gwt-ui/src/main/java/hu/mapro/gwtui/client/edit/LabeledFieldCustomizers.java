package hu.mapro.gwtui.client.edit;

import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.Field;
import hu.mapro.gwtui.client.uibuilder.FieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Fields;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;

public class LabeledFieldCustomizers {

	public static LabeledFieldCustomizer editorField(
			final Fields fields,
			final String label,
			final boolean notNull, final PanelBuilder builder) {
		final Field field = fields.field(new FieldBuilder() {
			@Override
			public void build(Field o) {
				o.setHeader(label);
				o.setBold(notNull);
				BuildingFactory.build(o, builder);
			}
		});
		
		return new LabeledFieldCustomizer() {
			
			@Override
			public void setNotNull(boolean notNull) {
				field.setBold(notNull);
			}
			
			@Override
			public void setLabel(String text) {
				field.setHeader(text);
			}
	
			@Override
			public void setFill(boolean fill) {
				field.setFill(fill);
			}
		};
	}

}
