package hu.mapro.gwt.client.widget;


public class UiConstants {
	
	static IUiConstants constants = new DefaultUiConstants();

	public static IUiConstants getConstants() {
		return constants;
	}
	

	public static String headerTitleHeight() {
		return constants.headerTitleHeight()+"px";
	}

	public static String panelMargin() {
		return constants.panelMargin()+"px";
	}

	public static String frameWidth() {
		return constants.frameWidth()+"px";
	}

	public static String borderWidth() {
		return constants.borderWidth()+"px";
	}

	public static String minusBorderWidth() {
		return -constants.borderWidth()+"px";
	}

	public static String frameColor() {
		return constants.frameColor();
	}

	public static String borderColor() {
		return constants.borderColor();
	}

	public static String backgroundColor() {
		return constants.backgroundColor();
	}
	
	
	
	
	
}
