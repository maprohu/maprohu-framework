package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.client.widget.CloseTabEvent.CloseTabHandler;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.Display;
import hu.mapro.gwtui.client.HasPages;
import hu.mapro.gwtui.client.LoginInterface;
import hu.mapro.gwtui.client.LoginWindow;
import hu.mapro.gwtui.client.Menu;
import hu.mapro.gwtui.client.MenuGroup;
import hu.mapro.gwtui.client.Page;
import hu.mapro.gwtui.client.Window;
import hu.mapro.gwtui.client.WindowDecoration;
import hu.mapro.gwtui.client.impl.TabPage;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class MainWindow extends ResizeComposite implements Menu, HasPages, Window, LoginInterface, WindowDecoration {

	interface Binder extends UiBinder<Widget, MainWindow> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	
	interface Style extends CssResource {
		String tabPanel();
	}
	
	public interface MyTemplates extends SafeHtmlTemplates {
		@Template("{0}")
		SafeHtml notLoggedIn(String notLoggedIn);
		@Template("{0} <b>{1}</b>")
		SafeHtml loggedInAs(String loggedInAs, String userName);
	}

	private static final MyTemplates TEMPLATES = GWT.create(MyTemplates.class);
	private static final UiLabels LABELS = GWT.create(UiLabels.class);
	
	@UiField
	Style style;
	
	@UiField
	Panel northPanel;

	@UiField
	Image logo;

	@UiField
	SplitLayoutPanel splitPanel;
	
	@UiField
	Panel menuPanel;
	
	@UiField(provided=true)
	IUiConstants constants = UiConstants.getConstants();
	
	@UiField
	SimplePanel loginLabel;
	
	@UiField
	Label titleLabel;
	
	//@UiField(provided=true)
	ScrollableTabLayoutPanel tabPanel = new ScrollableTabLayoutPanel(constants.tabHeight(), Unit.PX);
	
	
	List<MenuGroupWidget> menuGroupWidgets = Lists.newArrayList();
	
	public MainWindow() {
		
		DockLayoutPanel panel = (DockLayoutPanel) binder.createAndBindUi(this);
		
		panel.setWidgetSize(
				northPanel, 
				logo.getHeight()+
				constants.panelMargin()*2+
				constants.borderWidth()*2
		);

		initWidget(panel);
		
		tabPanel.addStyleName(style.tabPanel());
		splitPanel.add(tabPanel);
	}
	
	@UiFactory
	SplitLayoutPanel createSplitLayoutPanel() {
		return new SplitLayoutPanel(constants.frameWidth());
	}
	
	@UiHandler("menuCollapseButton")
	void handMenuCollapseClick(ClickEvent e) {
		for (MenuGroupWidget dp : menuGroupWidgets) {
			dp.close();
		}
	}
	
	@UiHandler("menuExpandButton")
	void handMenuExpandClick(ClickEvent e) {
		for (MenuGroupWidget dp : menuGroupWidgets) {
			dp.open();
		}
	}
	
	public MenuGroup addMenuGroup() {
		MenuGroupWidget group = new MenuGroupWidget();
		menuPanel.add(group);
		menuGroupWidgets.add(group);
		return group;
	}

	@Override
	public Page addPage() {
		final CloseableTab tab = new CloseableTab();
		
		final TabPage tabPage = new TabPage(tab) {
			
			@Override
			protected void removeTab() {
				tabPanel.remove(widget);
			}
			
			@Override
			protected void addTab() {
				tabPanel.add(widget, tab);
			}
			
			@Override
			protected void activateTab() {
				tabPanel.selectTab(widget);
			}
			
			
		};
		
		tab.addCloseTabHandler(new CloseTabHandler() {
			@Override
			public void onCloseTab(CloseTabEvent event) {
				tabPage.hide();
			}
		});
		return tabPage;
	}

	@Override
	public void display(Display display) {
		display.display(this);
	}

	LoginWindowDialog loginDialog = new LoginWindowDialog();

	@Override
	public LoginWindow getLoginWindow() {
		return loginDialog;
	}

	@Override
	public void setLoginName(String loginName) {
		loginLabel.setWidget(new HTML(TEMPLATES.loggedInAs(LABELS.loggedInAs(), loginName)));
	}

	@Override
	public void clearLoginName() {
		loginLabel.setWidget(new HTML(TEMPLATES.notLoggedIn(LABELS.notLoggedIn())));
	}

	@Override
	public void setLogo(ImageResource logo) {
		this.logo.setResource(logo);
	}

	@Override
	public void addHeaderItem(Widget item) {
		northPanel.add(item);
	}
	
	@Override
	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	@Override
	public void clearMenuGroups() {
		menuPanel.clear();
		menuGroupWidgets.clear();
	}

	@Override
	public void clearPages() {
		tabPanel.clear();
	}
	
}
