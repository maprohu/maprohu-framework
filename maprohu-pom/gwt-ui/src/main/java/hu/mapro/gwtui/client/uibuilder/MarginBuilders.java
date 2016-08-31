package hu.mapro.gwtui.client.uibuilder;

public class MarginBuilders {

	public static MarginBuilder width(final int width) {
		return new MarginBuilder() {
			@Override
			public void build(Margin o) {
				o.setWidth(width);
			}
		};
	}
	
}
