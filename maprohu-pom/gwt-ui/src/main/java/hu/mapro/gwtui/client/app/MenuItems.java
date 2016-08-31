package hu.mapro.gwtui.client.app;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.app.impl.SingletonWorksheet;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetEmbedder;
import hu.mapro.gwtui.client.iface.WidgetOperation;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.Tab;
import hu.mapro.gwtui.client.uibuilder.TabBuilder;
import hu.mapro.gwtui.client.uibuilder.Workspace;

import com.google.gwt.user.client.ui.Widget;

public class MenuItems {

	public static MenuItem from(
			MenuGroup menuGroup,
			String title,
			Action action
	) {
		MenuItem item = menuGroup.addMenuItem();
		item.setText(title);
		item.setAction(action);
		return item;
	}
	
	public static void staticPage(
			Workspace workspace,
			MenuGroup group,
			String title,
			Widget widget
	) {
		singletonPage(
				workspace, 
				group, 
				title, 
				WidgetBuilders.staticWidget(widget)
		);
	}
	
	public static SingletonWorksheet singletonPage(
			final Workspace workspace,
			MenuGroup group,
			final String title,
			final WidgetEmbedder widget
	) {
		SingletonWorksheet sws = new SingletonWorksheet() {
			@Override
			protected Tab createWorksheet() {
				return workspace.tab(new TabBuilder() {
					@Override
					public void build(Tab o) {
						o.setHeader(title);
						o.show();
						widget.widget(o.asPanel(), o.asWidgetContext());
					}
				});
			}
		};
		MenuItem item = group.addMenuItem();
		item.setText(title);
		item.setAction(sws.showAction());
		return sws;
		
	}
	
	public static void singletonPage(
			final Workspace workspace,
			MenuGroup group,
			final String title,
			final WidgetBuilder widget
	) {
		singletonPage(
				workspace, 
				group, 
				title, 
				new WidgetEmbedder() {
					@Override
					public void widget(Panel panel, WidgetContext context) {
						final ManagedWidget w = widget.widget();
						panel.widget(w.asWidget());
						final Handlers handlers = Handlers.newInstance();
						handlers.add(Actions.removeHandler(context.registerListener(new AbstractWidgetListener() {
							@Override
							public void onDestroy(WidgetOperation opertation) {
								opertation.approve(new Action() {
									@Override
									public void perform() {
										w.close();
										handlers.fire();
									}
								});
							}
						})));
						
					}
				} 
		);
	}
	
}
