package hu.mapro.gwtui.client.uibuilder;

public interface TabsBuilding extends Tabs {

	void setSingleTabVisible(boolean singleTabVisible);
	void setSingleConnector(Connector<Panel> connector);
	void setTabbedConnector(Connector<Panel> connector);
	
	void setTabPosition(TabPosition tabPosition);
	
	public static enum TabPosition {
		
		TOP,
		BOTTOM
		
	}
	
}
