package hu.mapro.gwtui.gxt.client.theme.blue.client;

import hu.mapro.gwtui.gxt.client.theme.blue.client.MaproPlainTabPanelAppearance.MaproPlainTabPanelResources;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.blue.client.tabs.BluePlainTabPanelAppearance.BluePlainTabPanelResources;
import com.sencha.gxt.theme.blue.client.tabs.BluePlainTabPanelBottomAppearance;

public class MaproPlainTabPanelBottomAppearance extends
		BluePlainTabPanelBottomAppearance {

	public MaproPlainTabPanelBottomAppearance() {
		super(
				GWT.<BluePlainTabPanelResources> create(MaproPlainTabPanelResources.class),
				GWT.<PlainTabPanelBottomTemplates> create(PlainTabPanelBottomTemplates.class),
				GWT.<ItemTemplate> create(ItemTemplate.class));
	}

}
