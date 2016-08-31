package hu.mapro.gwtui.client.uibuilder;

public class BuildingFactory {

	public static <T> T build(T object, Builder<? super T> builder) {
		if (builder!=null) builder.build(object);
		return object;
	}
	
}
