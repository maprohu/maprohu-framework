package hu.mapro.gwtui.client.uibuilder;

public class PanelConnectors {

	public static PanelConnector margin() {
		return new PanelConnector() {
			@Override
			public void connect(Panel object, Builder<? super Panel> builder) {
				BuildingFactory.build(object.margin(null).asPanel(), builder);
			}
		};
	}
	
}
