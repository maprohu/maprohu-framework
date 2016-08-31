package hu.mapro.gwtui.client.uibuilder;

public class Connectors {
	
	public static <T> Connector<T> direct() {
		return new Connector<T>() {
			@Override
			public void connect(T object, Builder<? super T> builder) {
				BuildingFactory.build(object, builder);
			}
		};
	}

}
