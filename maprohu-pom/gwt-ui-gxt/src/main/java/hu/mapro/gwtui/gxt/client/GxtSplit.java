package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.ResizablePanel;
import hu.mapro.gwtui.client.uibuilder.ResizablePanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Split;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;

public class GxtSplit extends GxtPanel implements Split, IsWidget {

	final BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
	
	public GxtSplit(WidgetContextSupport widgetContextSupport) {
		super(widgetContextSupport);
		borderLayoutContainer.setCenterWidget(container);
	}
	
	
	final GxtSplitPanel east = new GxtSplitPanel(LayoutRegion.EAST) {
		@Override
		void set(Widget widget) {
			borderLayoutContainer.setEastWidget(widget);
		}
	};
	final GxtSplitPanel west = new GxtSplitPanel(LayoutRegion.WEST) {
		@Override
		void set(Widget widget) {
			borderLayoutContainer.setWestWidget(widget);
		}
	};
	final GxtSplitPanel north = new GxtSplitPanel(LayoutRegion.NORTH) {
		@Override
		void set(Widget widget) {
			borderLayoutContainer.setNorthWidget(widget);
		}
	};
	final GxtSplitPanel south = new GxtSplitPanel(LayoutRegion.SOUTH) {
		@Override
		void set(Widget widget) {
			borderLayoutContainer.setSouthWidget(widget);
		}
	};
	
	@Override
	public Widget asWidget() {
		return borderLayoutContainer;
	}

	abstract class GxtSplitPanel implements ResizablePanel {

		final LayoutRegion layoutRegion;
		final BorderLayoutData borderLayoutData = new BorderLayoutData(300);
		final GxtPanel panel = new GxtPanel(widgetContextSupport);
		final ContentPanel contentPanel = new ContentPanel();
		
		public GxtSplitPanel(LayoutRegion layoutRegion) {
			super();
			this.layoutRegion = layoutRegion;
			
			borderLayoutData.setSplit(true);
			borderLayoutData.setCollapsible(false);
			borderLayoutData.setCollapseMini(false);
			
			switch (layoutRegion) {
			case SOUTH:
				borderLayoutData.setMargins(new Margins(5, 0, 0, 0));
				break;
			case NORTH:
				borderLayoutData.setMargins(new Margins(0, 0, 5, 0));
				break;
			case WEST:
				borderLayoutData.setMargins(new Margins(0, 5, 0, 0));
				break;
			case EAST:
				borderLayoutData.setMargins(new Margins(0, 0, 0, 5));
				break;
			}
			
			contentPanel.setWidget(panel.container);
			contentPanel.setHeaderVisible(false);
			contentPanel.setBodyBorder(false);
			contentPanel.setBorders(false);
			
			
		}

		boolean added = false;
		Widget regionWidget = contentPanel;
		
		@Override
		public Panel asPanel() {
			return panel;
		}

		@Override
		public void setSize(int size) {
			borderLayoutData.setSize(size);
			if (added) update();
		}
		
		abstract void set(Widget widget);

		public GxtSplitPanel update() {
			added = true;
			regionWidget.setLayoutData(borderLayoutData);
			set(regionWidget);
			return this;
		}
		
	}
	
	private ResizablePanel build(
			GxtSplitPanel region,
			ResizablePanelBuilder builder
	) {
		return BuildingFactory.build(region, builder).update();
	}
	
	@Override
	public ResizablePanel north(ResizablePanelBuilder builder) {
		return build(north, builder);
	}

	@Override
	public ResizablePanel south(ResizablePanelBuilder builder) {
		return build(south, builder);
	}

	@Override
	public ResizablePanel west(ResizablePanelBuilder builder) {
		return build(west, builder);
	}

	@Override
	public ResizablePanel east(ResizablePanelBuilder builder) {
		return build(east, builder);
	}

	@Override
	public Panel center(PanelBuilder builder) {
		return BuildingFactory.build(this, builder);
	}

}
