package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwtui.client.iface.WidgetContext;

public class EditorChildrenAreas {
	
	public static EditorChildrenArea from(
			Fields fields,
			final EditorChildrenAreaBuilder builder,
			final WidgetContext widgetContext
	) {
		final Field field = fields.field(null);
		final Panel fieldPanel = field;
		fieldPanel.setHeight(150);
		
		return new EditorChildrenArea() {
			
			{
				final EditorChildrenArea thisFinal = this;
				
				BuildingFactory.build(new EditorChildrenAreaBuilding() {
					
					@Override
					public boolean isFramed() {
						return false;
					}
					
					@Override
					public EditorChildrenArea getTarget() {
						return thisFinal;
					}
				}, builder);
			}
			
			@Override
			public WidgetContext asWidgetContext() {
				return widgetContext;
			}
			
			@Override
			public Panel asPanel() {
				return fieldPanel;
			}
			
			@Override
			public void setHeader(String header) {
				field.setHeader(header);
			}

			@Override
			public void setFill(boolean fill) {
				field.setFill(fill);
			}
		};
	}

}
