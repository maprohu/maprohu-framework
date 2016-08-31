package hu.mapro.gwtui.client.app;

public class MenuGroups {

	public static MenuGroup create(Menu menu, String title) {
		MenuGroup group = menu.addMenuGroup();
		group.setText(title);
		return group;
	}
	
}
