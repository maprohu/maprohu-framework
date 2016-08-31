package hu.mapro.gwt.earth.client.view;

import hu.mapro.gwt.earth.client.geo.Bounds;

import com.google.gwt.core.client.JavaScriptObject;

public final class Namespace extends JavaScriptObject {

	public static final class SetToBoundsViewOptions extends JavaScriptObject {
		
		protected SetToBoundsViewOptions() {}

		public native void setAspectRatio(double aspectRatio) /*-{
			this.aspectRatio = aspectRatio;
		}-*/;
		
		public native void setDefaultRange(double defaultRange) /*-{
			this.defaultRange = defaultRange;
		}-*/;
		
		public native void setScaleRange(double scaleRange) /*-{
			this.scaleRange = scaleRange;
		}-*/;
		
	}

	protected Namespace() {
	}
	
	public native void setToBoundsView(Bounds bounds, SetToBoundsViewOptions options) /*-{
		this.setToBoundsView(bounds, options);
	}-*/;
	
		
	public static native SetToBoundsViewOptions createSetToBoundsViewOptions(double aspectRatio) /*-{
		return {aspectRatio: aspectRatio};
	}-*/;
		
		
}