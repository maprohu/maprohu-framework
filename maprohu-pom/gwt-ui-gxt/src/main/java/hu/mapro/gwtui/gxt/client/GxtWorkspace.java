package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.TabsBuilder;
import hu.mapro.gwtui.client.uibuilder.TabsBuilding;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupports;
import hu.mapro.gwtui.client.uibuilder.Workspace;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.inject.Singleton;

@Singleton
public class GxtWorkspace extends GxtTabs implements Workspace {

	public GxtWorkspace(MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages) {
		super(WidgetContextSupports.from(messageInterface, defaultUiMessages), new TabsBuilder() {
			
			@Override
			public void build(TabsBuilding o) {
				o.setSingleTabVisible(true);
				o.setTabPosition(TabPosition.TOP);
			}
		});
	}

}
