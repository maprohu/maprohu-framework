package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.AllVisibility;
import hu.mapro.gwtui.client.LoginWindow;
import hu.mapro.gwtui.client.OneVisibility;
import hu.mapro.gwtui.client.Page;
import hu.mapro.gwtui.client.VisibilityBean;
import hu.mapro.gwtui.client.VisibilityUtils;
import hu.mapro.gwtui.client.app.MenuGroup;
import hu.mapro.gwtui.client.app.MenuItem;
import hu.mapro.gwtui.client.browser.Browser;
import hu.mapro.gwtui.client.images.GwtUiImages;
import hu.mapro.gwtui.client.upload.UploadWindow;
import hu.mapro.gwtui.client.widget.Button;
import hu.mapro.gwtui.client.widget.CenterLayout;
import hu.mapro.gwtui.client.widget.Command;
import hu.mapro.gwtui.client.widget.FormField;
import hu.mapro.gwtui.client.widget.FormPanel;
import hu.mapro.gwtui.client.widget.WidgetBuilder;
import hu.mapro.gwtui.gxt.client.browser.GxtBrowserWidget;
import hu.mapro.gwtui.gxt.client.menu.MenuItemWidget;
import hu.mapro.gwtui.gxt.client.upload.GxtUploadWindow;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.theme.blue.client.tabs.BlueTabPanelAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelAppearance;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent.BeforeCloseHandler;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

@Singleton
public class CopyOfGxtUserInterface  {

	Viewport viewport;
	private TabPanel tabPanel;
	private ContentPanel menuPanel;
	private VerticalLayoutContainer menuContainer;

	Map<Widget, Action> closeAction = Maps.newHashMap();
	private HBoxLayoutContainer north;
	private Image logoImage;
	private Label titleLabel;

	GxtLoginWindow loginWindow;
	private ContentPanel headerContentPanel;
	
	public static class MyBlueTabPanelAppearance extends BlueTabPanelAppearance {

		public interface MyBlueTabPanelResources extends BlueTabPanelResources {
			
		    @Source({"com/sencha/gxt/theme/base/client/tabs/TabPanel.css", "com/sencha/gxt/theme/blue/client/tabs/BlueTabPanel.css", "MyBlueTabPanel.css"})
		    BlueTabPanelStyle style();
			
		    
		}
		
		public MyBlueTabPanelAppearance() {
		    super(
		    		GWT.<BlueTabPanelResources> create(MyBlueTabPanelResources.class), 
		    		GWT.<Template> create(Template.class),
		            GWT.<ItemTemplate> create(ItemTemplate.class)
            );
		}
		
	}
	
	public CopyOfGxtUserInterface() {
		viewport = new Viewport();

		final BorderLayoutContainer con = new BorderLayoutContainer();
	    con.setBorders(true);
	    
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
	    
	    menuPanel = new ContentPanel();
	    menuPanel.setHeadingText("Menu");
	    menuContainer = new VerticalLayoutContainer();
	    //menuContainer.setScrollMode(ScrollMode.AUTOY);
	    XElement scrollElement = menuContainer.getElement();
        scrollElement.getStyle().setProperty("overflowY", ScrollMode.ALWAYS.value().toLowerCase());
        scrollElement.getStyle().setProperty("overflowX", ScrollMode.NONE.value().toLowerCase());
	    menuContainer.setAdjustForScroll(true);
		menuPanel.setWidget(menuContainer);

		
		
		tabPanel = new TabPanel(GWT.<TabPanelAppearance>create(MyBlueTabPanelAppearance.class));
	    //tabPanel.getElement().getStyle().setBackgroundColor("rgb(223, 232, 246)");
	    tabPanel.setBorders(false);

	    tabPanel.addBeforeCloseHandler(new BeforeCloseHandler<Widget>() {
			@Override
			public void onBeforeClose(BeforeCloseEvent<Widget> event) {
			}
		});
	    
	    
	    tabPanel.addCloseHandler(new CloseHandler<Widget>() {
			@Override
			public void onClose(CloseEvent<Widget> event) {
				Action action = closeAction.get(event.getItem());
				if (action!=null) {
					action.perform();
				}
			}
		});

	    
	    tabPanel.addCloseHandler(new CloseHandler<Widget>() {
			@Override
			public void onClose(CloseEvent<Widget> event) {
				Action action = closeAction.get(event.getItem());
				if (action!=null) {
					action.perform();
				}
			}
		});

	    north.setLayoutData(new MarginData(5, 5, 5, 5));
	    
	    BorderLayoutData westData = new BorderLayoutData(150);
	    westData.setCollapsible(true);
	    westData.setSplit(true);
	    westData.setCollapseMini(true);
	    westData.setMargins(new Margins(5, 5, 5, 5));
	 
	    MarginData centerData = new MarginData(5, 5, 5, 0);
	 
	    con.setWestWidget(menuPanel, westData);
	    con.setCenterWidget(tabPanel, centerData);

	    final VerticalLayoutContainer vlc = new VerticalLayoutContainer();

//	    ToolBar toolbar = new ToolBar();
//	    toolbar.setPack(BoxLayoutPack.END);
//	    
//	    final ToolButton hideLogoButton = new ToolButton(ToolButton.DOUBLEUP);
//	    final ToolButton showLogoButton = new ToolButton(ToolButton.DOUBLEDOWN);
//	    showLogoButton.setVisible(false);
//	    
//	    
//		toolbar.add(hideLogoButton);
//		toolbar.add(showLogoButton);
//		SelectHandler logoHandler = new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				boolean visible = event.getSource() == showLogoButton;
//				
//				north.setVisible(visible);
//				hideLogoButton.setVisible(visible);
//				showLogoButton.setVisible(!visible);
//				vlc.forceLayout();
//			}
//		};
//		hideLogoButton.addSelectHandler(logoHandler);
//		showLogoButton.addSelectHandler(logoHandler);
//	    vlc.add(toolbar, new VerticalLayoutData(1.0, -1, new Margins(0, 0, 0, 0)));
//	    vlc.add(north, new VerticalLayoutData(1.0, -1, new Margins(0, 0, 0, 0)));
		
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

		
		vlc.add(con, new VerticalLayoutData(1.0, 1.0, new Margins(0, 0, 0, 0)));
		viewport.setWidget(vlc);

		clearLoginName();
	}

	
	@Override
	public MenuGroup addMenuGroup() {
		final ContentPanel cp = new ContentPanel();
		cp.setCollapsible(true);
		menuContainer.add(cp, new VerticalLayoutData(1, -1, new Margins(3, 3, 0, 3)));
		
		
		final VerticalLayoutContainer menuGroupContainer = new VerticalLayoutContainer();
		menuGroupContainer.setLayoutData(new MarginData(3, 3, 3, 3));
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

	@Override
	public Page addPage() {
		return new Page() {
			
			Handlers attachHandlers = Handlers.newInstance();
			Handlers showHandlers = Handlers.newInstance();
			Handlers hideHandlers = Handlers.newInstance();
			
			boolean added = false;
			TabItemConfig tabItemConfig = new TabItemConfig(null, true);
			Widget widget;
			
			@Override
			public void show() {
				if (!added) {
					tabPanel.add(widget, tabItemConfig);

					closeAction.put(widget, new Action() {
						@Override
						public void perform() {
							onClose();
						}
					});
					
					added = true;
					tabPanel.setActiveWidget(widget);
					
					attachHandlers.fire();
					showHandlers.fire();
				} else {
					if (tabPanel.getActiveWidget()!=widget) {
						tabPanel.setActiveWidget(widget);
						
						showHandlers.fire();
					}
				}
			}
			
			@Override
			public void setWidget(Widget widget) {
				if (!added) {
					this.widget = widget;
				} else {
					int index = tabPanel.getWidgetIndex(widget);
					tabPanel.remove(index);
					this.widget = widget;
					tabPanel.insert(widget, index, tabItemConfig);
				}
			}
			
			@Override
			public void hide() {
				if (added) {
					tabPanel.remove(widget);
					onClose();
					hideHandlers.fire();
				}
			}
			
			private void onClose() {
				closeAction.remove(widget);
				added = false;
			}

			@Override
			public void addAttachHandler(Action action) {
				attachHandlers.add(action);
			}

			@Override
			public String getText() {
				return tabItemConfig.getText();
			}

			@Override
			public void setText(String text) {
				tabItemConfig.setText(text);
				if (added) {
					tabPanel.update(widget, tabItemConfig);
				}
			}

			@Override
			public void addShowHandler(Action action) {
				showHandlers.add(action);
			}

			@Override
			public void addHideHandler(Action action) {
				hideHandlers.add(action);
			}

		};
		
	}

	@Override
	public UploadWindow createUploadWindow() {
		return new GxtUploadWindow();
	}


	@Override
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	@Override
	public LoginWindow getLoginWindow() {
		if (loginWindow==null) {
			loginWindow = new GxtLoginWindow(); 
		}
		return loginWindow;
	}


	@Override
	public void addHeaderItem(Widget item) {
		north.add(item, new BoxLayoutData());
	}

	@Override
	public void setLoginName(String loginName) {
		headerContentPanel.setHeadingHtml("Logged in as <i>"+SafeHtmlUtils.htmlEscape(loginName)+"</i>");
	}

	@Override
	public void clearLoginName() {
		headerContentPanel.setHeadingText("Not logged in");
	}
	
	@Override
	public WidgetBuilder getWidgetBuilder() {
		return new WidgetBuilder() {
			@Override
			public FormPanel formPanel() {
				final VerticalLayoutContainer formPanelContainer = new VerticalLayoutContainer();
				final VerticalLayoutContainer fieldsContainer = new VerticalLayoutContainer();
				formPanelContainer.add(fieldsContainer, new VerticalLayoutData(1, -1, new Margins(5)));
				final ButtonBar buttons = new ButtonBar();
				buttons.setPack(BoxLayoutPack.CENTER);
				formPanelContainer.add(buttons, new VerticalLayoutData(1, -1, new Margins(5)));
				formPanelContainer.setBorders(false);			
				return new FormPanel() {
					
					@Override
					public Widget getWidget() {
						return formPanelContainer;
					}
					
					@Override
					public FormField addField() {
						final FieldLabel field = new FieldLabel();
						fieldsContainer.add(field, new VerticalLayoutData(1, -1, new Margins(5)));
						return new FormField() {
							
							@Override
							public void setWidget(Widget widget) {
								field.setWidget(widget);
							}
							
							@Override
							public void setLabel(String label) {
								field.setText(label);
							}
						};
					}
					
					@Override
					public Command addButton() {
						final TextButton button = new TextButton("<no label>");
						buttons.add(button);
						return GxtUiUtils.createCommand(button);
					}
				};
			}
			
			@Override
			public CenterLayout centerLayout() {
				final CenterLayoutContainer widget = new CenterLayoutContainer();
				return new CenterLayout() {
					
					@Override
					public void setWidget(Widget toCenter) {
						widget.setWidget(toCenter);
					}
					
					@Override
					public Widget getWidget() {
						return widget;
					}
				};
			}
			
			@Override
			public Button button() {
				final TextButton button = new TextButton("<no label>");
				
				class MyButton extends hu.mapro.gwtui.gxt.client.GxtUiUtils.ButtonCommand implements Button {

					public MyButton() {
						super(button);
					}

					@Override
					public Widget getWidget() {
						return button;
					}
					
				}

				return new MyButton();
			}

			@Override
			public <T> Browser<T> browser() {
				return new GxtBrowserWidget<T>();
			}
		};
	}

	@Override
	public void clearMenuGroups() {
		menuContainer.clear();
	}

	@Override
	public void clearPages() {
		for (Widget w : closeAction.keySet()) {
			tabPanel.remove(w);
		}
		closeAction.clear();
	}

//	public void alert(String title, String message, Action after) {
//		messageInterface.alert(title, message, after);
//	}
//
//	public void info(String title, String message, Action after) {
//		messageInterface.info(title, message, after);
//	}
//
//	public void confirm(String title, String message, Action confirmed,
//			Action cancelled) {
//		messageInterface.confirm(title, message, confirmed, cancelled);
//	}
	
}
