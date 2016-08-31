package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.menu.MultiButton;
import hu.mapro.gwtui.client.menu.ToggleButton;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.ToolBarPanel;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.form.GxtMultiSelectButton;
import hu.mapro.gwtui.gxt.client.form.GxtSelectButton;
import hu.mapro.gwtui.gxt.client.form.GxtToggleButton;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class GxtToolBarPanel implements ToolBarPanel, IsWidget {
	
	private GxtPanel panel;
	final private VerticalLayoutContainer vlc;
	private ToolBar toolBar;

	public GxtToolBarPanel(WidgetContextSupport widgetContextSupport) {
		panel = new GxtPanel(widgetContextSupport);
		
		
		toolBar = new ToolBar();
		
		vlc = new VerticalLayoutContainer();
		vlc.add(toolBar, new VerticalLayoutData(1.0, -1.0));
		vlc.add(panel.container, new VerticalLayoutData(1.0, 1.0));
	}
	
	@Override
	public Panel asPanel() {
		return panel;
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}
	
	@Override
	public Button button() {
		GxtSelectButton button = GxtFactory.button();
		toolBar.add(button.asWidget());
		return button;
	}

	@Override
	public MultiButton multiButton() {
		GxtMultiSelectButton button = GxtFactory.multiButton();
		toolBar.add(button.asWidget());
		return button;
	}

	@Override
	public ToggleButton toggleButton() {
		GxtToggleButton button = GxtFactory.toggleButton();
		toolBar.add(button.asWidget());
		return button;
	}

	@Override
	public Panel custom() {
		GxtPanel custom = new GxtPanel(panel.widgetContextSupport);
		toolBar.add(custom.getContainer());
		return custom;
	}
	

}
