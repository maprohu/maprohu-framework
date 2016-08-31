package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.VetoException;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.iface.AbstractWidgetContext;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.uibuilder.AbstractTabHandler;
import hu.mapro.gwtui.client.uibuilder.Builder;
import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.Connector;
import hu.mapro.gwtui.client.uibuilder.Connectors;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Tab;
import hu.mapro.gwtui.client.uibuilder.TabHandler;
import hu.mapro.gwtui.client.uibuilder.Tabs;
import hu.mapro.gwtui.client.uibuilder.TabsBuilder;
import hu.mapro.gwtui.client.uibuilder.TabsBuilding;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;
import hu.mapro.gwtui.gxt.client.theme.blue.client.MaproPlainTabPanelBottomAppearance;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelAppearance;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent.BeforeCloseHandler;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;

@Singleton
public class GxtTabs implements Tabs, IsWidget, TabsBuilding {

	final WidgetContextSupport widgetContextSupport;
	
	SimpleContainer tabsContainer = new SimpleContainer()/* {
		private void superForceLayout() {
			super.forceLayout();
		}
		public void forceLayout() {
			GxtFactory.layoutWhenVisible(
					widgetContextSupport,
					new Action() {
						@Override
						public void perform() {
							superForceLayout();
						}
					}
			);
		}
	}*/;
	
	
	TabPanel tabPanel;
	
	Map<Widget, GxtTab> tabMap = Maps.newHashMap();
	
	boolean initializing = true;
	
	private boolean singleTabVisible = true;
	
	
	final class GxtTab implements Tab {
		
		boolean initializing = true;
		
		TabItemConfig tabItemConfig;
		
		List<TabHandler> handlers = Lists.newArrayList();
		
		SimpleContainer tabContainer = new SimpleContainer() {
			{
				cacheSizes = false;
			}
		};
		
		/* {
			private void superForceLayout() {
				super.forceLayout();
			}
			public void forceLayout() {
				GxtFactory.layoutWhenVisible(
						widgetContext,
						new Action() {
							@Override
							public void perform() {
								superForceLayout();
							}
						}
				);
			}
		}*/;
		
		AbstractWidgetContext widgetContext = new AbstractWidgetContext(widgetContextSupport) {
			@Override
			public void bringToFrontInContainer() {
				show();
			}

			public boolean isOnTopInContainer() {
				return tabPanel.getActiveWidget() == tabContainer;
			}
		};

		final GxtPanel panel = new GxtPanel(widgetContext);
		
		boolean activateOnShow = false;
		
		GxtTab(Builder<Tab> tabBuilder) {
			this.tabItemConfig = new TabItemConfig("", true);
			
			BuildingFactory.build(this, tabBuilder);
			
			initializing = false;
			
			tabContainer.setWidget(panel.container);
			tabPanel.add(tabContainer, tabItemConfig);
			if (activateOnShow) {
				tabPanel.setActiveWidget(tabContainer);
			}
			addToTabMap();
			
			handlers.add(new AbstractTabHandler() {
				@Override
				public void onClosing() throws VetoException {
					widgetContext.fireDestroy(new Action() {
						@Override
						public void perform() {
							doRemove();
							doClose();
						}
					}, Action.NONE);
					
					throw new VetoException();
				}
				
				@Override
				public void onShow() {
					// TODO elaborate - implement objection
					widgetContext.fireShow(Action.NONE, Action.NONE);
				}
				
				@Override
				public void onHide() {
					// TODO elaborate - implement objection
					widgetContext.fireHide(Action.NONE, Action.NONE);
				}
				
			});
		}

		private void addToTabMap() {
			tabMap.put(tabContainer, this);
			updateTabPanelVisibility();
		}

		@Override
		public void show() {
			if (initializing) {
				activateOnShow = true;
			} else {
				tabPanel.setActiveWidget(tabContainer);
			}
		}

		@Override
		public void setHeader(String text) {
			tabItemConfig.setText(text);
			updateTabHeader();
		}

		private void updateTabHeader() {
			if (!initializing) tabPanel.update(tabContainer, tabItemConfig);
		}

		@Override
		public void remove() {
			try {
				onBeforeClose();
				onClose();
				doRemove();
			} catch (VetoException e) {
			}
		}

		public void doRemove() {
			tabPanel.remove(tabContainer);
		}

		public HandlerRegistration addTabHandler(
				final TabHandler handler
		) {
			handlers.add(handler);
			
			return new HandlerRegistration() {
				@Override
				public void removeHandler() {
					handlers.remove(handler);
				}
			};
		}

		void onClose() {
			for (TabHandler wsh : handlers) {
				wsh.onClose();
			}
			
			doClose();
		}

		private void doClose() {
			tabMap.remove(tabContainer);
			updateTabPanelVisibility();
		}

		
		void onBeforeClose() throws VetoException {
			for (TabHandler wsh : handlers) {
				wsh.onClosing();
			}
		}

		void onBeforeHide() {
			for (TabHandler wsh : handlers) {
				wsh.onHiding();
			}
		}

		void onShow() {
			GxtUtil.forceLayout(tabPanel);
//			GxtUtil.forceLayoutParent(tabContainer);
			for (TabHandler wsh : handlers) {
				wsh.onShow();
			}
		}

		@Override
		public void setClosable(boolean closable) {
			tabItemConfig.setClosable(closable);
			updateTabHeader();
		}
		
//		@Override
//		public void widget(Widget widget) {
//			super.widget(widget);
//			GxtUtil.forceLayoutParent(tabPanel);
//		}

		@Override
		public Panel asPanel() {
			return panel;
		}

		@Override
		public WidgetContext asWidgetContext() {
			return widgetContext;
		}

		
		
	}


	
	public GxtTabs(
			WidgetContextSupport widgetContextSupport,
			TabsBuilder tabsBuilder
	) {
		this.widgetContextSupport = widgetContextSupport;
		
		if (tabsBuilder!=null) {
			tabsBuilder.build(this);
		}
		
		initializing = false;
		
		tabPanel = new TabPanel(tabPosition == TabPosition.TOP ? GWT.<TabPanelAppearance> create(TabPanelAppearance.class) : GWT.<TabPanelAppearance>create(MaproPlainTabPanelBottomAppearance.class)) {
			{
				cacheSizes = false;
			}
		};
		
		tabsContainer.setWidget(tabPanel);
				
	    tabPanel.setBorders(false);
//	    tabPanel.setBodyBorder(false);
	    
	    tabPanel.addBeforeCloseHandler(new BeforeCloseHandler<Widget>() {
			@Override
			public void onBeforeClose(BeforeCloseEvent<Widget> event) {
				GxtTab ws = tabMap.get(event.getItem());
				if (ws!=null) {
					try {
						ws.onBeforeClose();
					} catch (VetoException e) {
						event.setCancelled(true);
					}
				}
			}
		});
		
	    tabPanel.addBeforeSelectionHandler(new BeforeSelectionHandler<Widget>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Widget> event) {
				GxtTab ws = tabMap.get(tabPanel.getActiveWidget());
				if (ws!=null) {
					try {
						ws.onBeforeHide();
					} catch (VetoException e) {
						event.cancel();
					}
				}
			}
		});
	    
	    tabPanel.addCloseHandler(new CloseHandler<Widget>() {
			@Override
			public void onClose(CloseEvent<Widget> event) {
				GxtTab ws = tabMap.get(event.getItem());
				if (ws!=null) {
					ws.onClose();
				}
			}
		});
	    
	    tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				GxtTab ws = tabMap.get(event.getSelectedItem());
				if (ws!=null) {
					ws.onShow();
				}
			}
		});
	    
	   
	    updateTabPanelVisibility();
	    
	}
	
	public void clear() {
		for (Widget w : tabMap.keySet()) {
			tabPanel.remove(w);
		}
		tabMap.clear();
		updateTabPanelVisibility();
	}
	
	Optional<GxtTab> singleTab = Optional.absent();
	
	private void updateTabPanelVisibility() {
		if (!singleTabVisible) {
			if (tabMap.size()==1 && !singleTab.isPresent()) {
				singleTab = Optional.of(tabMap.values().iterator().next());
				
				singleConnector.connect(
						new GxtPanel(widgetContextSupport, tabsContainer),
						new PanelBuilder() {
							@Override
							public void build(Panel panel) {
								panel.widget(singleTab.get().panel.container);
							}
						}
				);
				
				tabsContainer.setWidget(singleTab.get().panel.container);
				
				for (GxtTab tab : tabMap.values()) {
					GxtUtil.forceLayout(tab.tabContainer);
				}
			} else if (tabMap.size()!=1 && singleTab.isPresent()) {
				singleTab.get().tabContainer.setWidget(singleTab.get().panel.container);
				
				tabbedConnector.connect(
						new GxtPanel(widgetContextSupport, tabsContainer),
						new PanelBuilder() {
							@Override
							public void build(Panel panel) {
								panel.widget(tabPanel);
							}
						}
				);
				singleTab = Optional.absent();
				
				for (GxtTab tab : tabMap.values()) {
					GxtUtil.forceLayout(tab.tabContainer);
				}
			}
			
		}
	}

	
	@Override
	public GxtTab tab(Builder<Tab> builder) {
		return new GxtTab(builder);
	}

	@Override
	public Widget asWidget() {
		return tabsContainer;
	}

	@Override
	public void setSingleTabVisible(boolean singleTabVisible) {
		this.singleTabVisible  = singleTabVisible;
	}
	
	Connector<Panel> singleConnector = Connectors.direct();
	Connector<Panel> tabbedConnector = Connectors.direct();
	private TabPosition tabPosition = TabPosition.TOP;

	public void setSingleConnector(Connector<Panel> singleConnector) {
		this.singleConnector = singleConnector;
	}

	public void setTabbedConnector(Connector<Panel> tabbedConnector) {
		this.tabbedConnector = tabbedConnector;
	}

	@Override
	public void setTabPosition(TabPosition tabPosition) {
		this.tabPosition  = tabPosition;
	}
	
}
