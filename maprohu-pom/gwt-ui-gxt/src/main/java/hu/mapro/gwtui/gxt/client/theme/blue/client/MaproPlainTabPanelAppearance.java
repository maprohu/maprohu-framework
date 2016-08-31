package hu.mapro.gwtui.gxt.client.theme.blue.client;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.blue.client.tabs.BluePlainTabPanelAppearance;

public class MaproPlainTabPanelAppearance extends
		BluePlainTabPanelAppearance {

	
	public interface MaproPlainTabPanelResources extends
			BluePlainTabPanelResources {

		@Source({ 
			"com/sencha/gxt/theme/base/client/tabs/TabPanel.css",
			"com/sencha/gxt/theme/blue/client/tabs/BlueTabPanel.css",
			"com/sencha/gxt/theme/base/client/tabs/PlainTabPanel.css",
			"com/sencha/gxt/theme/blue/client/tabs/BluePlainTabPanel.css",
			"MaproTabPanel.css"
		})
		BluePlainTabPanelStyle style();

	}

	public MaproPlainTabPanelAppearance() {
		super(
				GWT.<BluePlainTabPanelResources> create(MaproPlainTabPanelResources.class),
				GWT.<PlainTabPanelTemplates> create(PlainTabPanelTemplates.class),
				GWT.<ItemTemplate> create(ItemTemplate.class));
	}

}
