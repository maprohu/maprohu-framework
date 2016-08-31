package hu.mapro.gwtui.client.window;

public class WindowIdSuppliers {

	public static WindowIdSupplier constant(final Long windowId) {
		return new WindowIdSupplier() {
			@Override
			public Long getWindowId() {
				return windowId;
			}
		};
	}
	
}
