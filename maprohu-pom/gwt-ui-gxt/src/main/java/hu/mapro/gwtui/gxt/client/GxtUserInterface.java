package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.AllVisibility;
import hu.mapro.gwtui.client.OneVisibility;
import hu.mapro.gwtui.client.VisibilityBean;
import hu.mapro.gwtui.client.VisibilityUtils;
import hu.mapro.gwtui.client.app.Menu;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;
import hu.mapro.gwtui.client.app.Subdesktop;
import hu.mapro.gwtui.client.images.GwtUiImages;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.ui.MainWindow;
import hu.mapro.gwtui.client.ui.UserInterface;
import hu.mapro.gwtui.client.uibuilder.Workspace;
import hu.mapro.gwtui.client.window.ApplicationDesktop;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.gwtui.gxt.client.menu.MenuItemWidget;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.DefaultScrollSupport;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;

@Singleton
public class GxtUserInterface implements UserInterface, ApplicationDesktop {


	final private MainWindow mainWindow;
	final MessageInterface messageInterface;
	final DefaultUiMessages defaultUiMessages;
	
	private Viewport viewport;
	private boolean viewportVisible = false;
	

	private HBoxLayoutContainer north;
	private Image logoImage;
	private Label titleLabel;
	
	GxtLoginWindow loginWindow;
	private ContentPanel headerContentPanel;
	private SimpleContainer desktopContainer;
	public String username;

	int desktopCounter = 0;
	
	final private DesktopWidget defaultDesktop;

	public class DesktopWidget implements Menu {
		public ContentPanel menuPanel;
		public VerticalLayoutContainer menuContainer;
		public GxtWorkspace workspace;
		public boolean loadingVisible;
		public BorderLayoutContainer container;
		private TabItemConfig tabItemConfig;
		private TabPanel tabPanel;
		
		final int desktopNumber = desktopCounter++;

		public DesktopWidget() {
			menuPanel = new ContentPanel();
		    menuPanel.setHeadingText("Menu");
		    menuContainer = new VerticalLayoutContainer();
		    menuContainer.addStyleName(Resources.getStyle().whiteBackground());
		    //menuContainer.setScrollMode(ScrollMode.AUTOY);
		    XElement scrollElement = menuContainer.getElement();
	        scrollElement.getStyle().setProperty("overflowY", ScrollMode.ALWAYS.value().toLowerCase());
	        scrollElement.getStyle().setProperty("overflowX", ScrollMode.NONE.value().toLowerCase());
		    menuContainer.setAdjustForScroll(true);
			menuPanel.setWidget(menuContainer);

		    
		    BorderLayoutData westData = new BorderLayoutData(150);
		    westData.setCollapsible(true);
		    westData.setSplit(true);
		    westData.setCollapseMini(true);
		    westData.setMargins(new Margins(5, 5, 5, 5));
		 
		    MarginData centerData = new MarginData(5, 5, 5, 0);
		 
			container = new BorderLayoutContainer();
		    container.setBorders(true);
		    
		    
		    container.setWestWidget(menuPanel, westData);

			
			
			workspace = new GxtWorkspace(messageInterface, defaultUiMessages);
		    container.setCenterWidget(workspace.asWidget(), centerData);
			
		}

		public void clear() {
			workspace.clear();
			menuContainer.clear();
			loadingVisible = true;
			menuContainer.add(new Label("Loading..."), new VerticalLayoutData(1, -1, new Margins(3, 3, 0, 3)));
		}

		@Override
		public MenuGroup addMenuGroup() {
			if (loadingVisible) {
				menuContainer.clear();
				loadingVisible = false;
			}
			
			final ContentPanel cp = new ContentPanel();
			cp.setCollapsible(true);
			menuContainer.add(cp, new VerticalLayoutData(1, -1, new Margins(3, 3, 0, 3)));
			
			
			final VerticalLayoutContainer menuGroupContainer = new VerticalLayoutContainer();
			menuGroupContainer.setLayoutData(new MarginData(3, 3, 3, 3));
			//menuGroupContainer.setScrollMode(ScrollMode.ALWAYS);
//			DefaultScrollSupport ss = new DefaultScrollSupport(menuGroupContainer.getElement());
//			menuGroupContainer.setScrollSupport(ss);
			
//			menuGroupContainer.getElement().getStyle().setProperty("overflowX", "hidden");
			
			
			cp.setWidget(menuGroupContainer);
			
			final AllVisibility groupVisibility = new AllVisibility();
			final VisibilityBean visibilityBean = new VisibilityBean();
			groupVisibility.add(visibilityBean);
			final OneVisibility mgcv = new OneVisibility();
			groupVisibility.add(mgcv);
			VisibilityUtils.addVisibility(groupVisibility, cp);
			
			return new MenuGroup() {
				
				@Override
				public MenuItem addMenuItem() {
					final MenuItemWidget w = new MenuItemWidget();
					menuGroupContainer.add(w, new VerticalLayoutData(1, -1, new Margins(0)));
					
					final VisibilityBean itemVisibilityBean = new VisibilityBean();
					VisibilityUtils.addVisibility(itemVisibilityBean, w);
					mgcv.add(itemVisibilityBean);
					
					return new MenuItem() {
						
						@Override
						public void setText(String text) {
							w.setText(text);
						}
						
						@Override
						public String getText() {
							return w.getText();
						}
						
						@Override
						public void setVisible(boolean visible) {
							itemVisibilityBean.setVisible(visible);
						}
						
						@Override
						public boolean isVisible() {
							return itemVisibilityBean.isVisible();
						}
						
						@Override
						public void setAction(Action action) {
							w.setAction(action);
						}
					};
				}

				@Override
				public boolean isVisible() {
					return visibilityBean.isVisible();
				}

				@Override
				public void setVisible(boolean visible) {
					visibilityBean.setVisible(visible);
				}

				@Override
				public String getText() {
					return cp.getHeader().getText();
				}

				@Override
				public void setText(String text) {
					cp.setHeadingText(text);
				}
			};
		}
		
		void addTab(TabPanel tp, String label) {
			this.tabPanel = tp;
			container.setBorders(false);
			tabItemConfig = new TabItemConfig(label, false);
			tp.add(container, tabItemConfig);
			tp.setActiveWidget(container);
		}
		
		void removeTab() {
			tabPanel.remove(container);
			container.setBorders(true);
			tabPanel = null;
			tabItemConfig = null;
		}

		public void setTabLabel(String value) {
			tabItemConfig.setText(value + " ("+desktopNumber+")");
			tabPanel.update(container, tabItemConfig);
		}
	}

	
	@Inject
	public GxtUserInterface(MainWindow mainWindow,
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages) {
		super();
		this.mainWindow = mainWindow;
		this.messageInterface = messageInterface;
		this.defaultUiMessages = defaultUiMessages;
		
		this.defaultDesktop = new DesktopWidget();
		
		viewport = new Viewport();
	    
	    north = new HBoxLayoutContainer();
	    north.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCHMAX);
	    ImageResource logo = GwtUiImages.INSTANCE.gwtLogo();
	    logoImage = new Image(logo);
	    north.add(logoImage, new BoxLayoutData());
	    CenterLayoutContainer tc = new CenterLayoutContainer();
	    titleLabel = new Label("<Title of Application>");
	    titleLabel.setStyleDependentName("applicationTitle", true);
	    tc.setWidget(titleLabel);
		BoxLayoutData titleLD = new BoxLayoutData();
		titleLD.setFlex(1);
		north.add(tc, titleLD);
		north.setLayoutData(new MarginData(5, 5, 5, 5));
	    
	    final VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		
	    headerContentPanel = new ContentPanel();
	    headerContentPanel.setCollapsible(true);
	    headerContentPanel.setWidget(north);
	    headerContentPanel.setBorders(false);
	    headerContentPanel.setBodyBorder(false);
	    headerContentPanel.setAnimCollapse(false);
	    headerContentPanel.addBeforeExpandHandler(new BeforeExpandHandler() {
			@Override
			public void onBeforeExpand(BeforeExpandEvent event) {
				headerContentPanel.setWidget(north);
			}
		});
	    headerContentPanel.addExpandHandler(new ExpandHandler() {
			@Override
			public void onExpand(ExpandEvent event) {
				vlc.forceLayout();
			}
		});
	    
	    headerContentPanel.addCollapseHandler(new CollapseHandler() {
			@Override
			public void onCollapse(CollapseEvent event) {
				headerContentPanel.remove(north);
				vlc.forceLayout();
			}
		});
		vlc.add(headerContentPanel, new VerticalLayoutData(1.0, -1, new Margins(0, 0, 0, 0)));

		desktopContainer = new SimpleContainer();
		vlc.add(desktopContainer, new VerticalLayoutData(1.0, 1.0, new Margins(0, 0, 0, 0)));
		
		desktopContainer.setWidget(defaultDesktop.container);
		
		
		viewport.setWidget(vlc);

	}

	
	
	@Override
	public void setTitle(String title) {
		titleLabel.setText(title);
	}


	
	@Override
	public ApplicationDesktop desktop() {
		if (!viewportVisible) {
			viewportVisible = true;
			mainWindow.display(viewport);
		}
		
		defaultDesktop.clear();
		

		return this;
	}

	@Override
	public Menu menu() {
		return defaultDesktop;
	}

	@Override
	public Workspace workspace() {
		return defaultDesktop.workspace;
	}

	@Override
	public void setUserName(String username) {
		this.username = username;
		headerContentPanel.setHeadingHtml("Logged in as <i>"+SafeHtmlUtils.htmlEscape(username)+"</i>");
	}

	@Override
	public void clearUserName() {
		headerContentPanel.setHeadingText("Not logged in");
	}

	@Override
	public void setLogo(ImageResource logo) {
		logoImage.setResource(logo);
	}
	
	@Override
	public void addHeaderItem(Widget item) {
		north.add(item, new BoxLayoutData());
	}

	TabPanel desktopTabPanel;
	
	@Override
	public Subdesktop newTab() {
		if (desktopTabPanel == null) {
			desktopTabPanel = new TabPanel() {
				{
					cacheSizes = false;
				}
			};
			defaultDesktop.addTab(desktopTabPanel, username);
			desktopContainer.setWidget(desktopTabPanel);
			desktopContainer.forceLayout();
		}

		final DesktopWidget newTab = new DesktopWidget();
		
		newTab.addTab(desktopTabPanel, "");
		
		return new Subdesktop() {
			
			@Override
			public Workspace workspace() {
				return newTab.workspace;
			}
			
			@Override
			public Menu menu() {
				return newTab;
			}
			
			@Override
			public void setTitle(String value) {
				newTab.setTabLabel(value);
			}
			
			@Override
			public void close() {
				newTab.removeTab();
				
				if (desktopTabPanel.getWidgetCount()==1) {
					defaultDesktop.removeTab();
					desktopContainer.setWidget(defaultDesktop.container);
					desktopTabPanel = null;
					desktopContainer.forceLayout();
				}
			}
		};
	}

	@Override
	public Subdesktop newWindow() {
		throw new RuntimeException("not yet");
	}
	
	
}
