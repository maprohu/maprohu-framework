package hu.mapro.gwtui.client.uibuilder;

import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.menu.ToggleButton;

public interface ToolBarPanel extends IsPanel {
	
	Button button();
	
	ToggleButton toggleButton();
	
	MultiButton multiButton();
	
	Panel custom();

}
