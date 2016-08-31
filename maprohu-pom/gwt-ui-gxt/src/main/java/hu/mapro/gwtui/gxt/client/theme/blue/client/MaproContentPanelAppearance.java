package hu.mapro.gwtui.gxt.client.theme.blue.client;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.blue.client.panel.BlueContentPanelAppearance;

public class MaproContentPanelAppearance extends BlueContentPanelAppearance {

  public interface MaproContentPanelResources extends BlueContentPanelResources {

	    @Source({
	    	"com/sencha/gxt/theme/base/client/panel/ContentPanel.css", 
	    	"com/sencha/gxt/theme/blue/client/panel/BlueContentPanel.css",
	    	"MaproContentPanel.css"
    	})
	    @Override
	    BlueContentPanelStyle style();

  }

  public MaproContentPanelAppearance() {
    super(GWT.<BlueContentPanelResources> create(MaproContentPanelResources.class));
  }

}
