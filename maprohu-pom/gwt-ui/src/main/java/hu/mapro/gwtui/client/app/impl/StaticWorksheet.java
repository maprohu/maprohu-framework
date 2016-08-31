package hu.mapro.gwtui.client.app.impl;

import hu.mapro.gwtui.client.uibuilder.Tab;
import hu.mapro.gwtui.client.uibuilder.TabBuilder;
import hu.mapro.gwtui.client.uibuilder.Workspace;

import com.google.gwt.user.client.ui.Widget;

public class StaticWorksheet extends SingletonWorksheet {

	final Workspace workspace;
	final String title;

	final Widget widget;
	
	@Override
	protected Tab createWorksheet() {
		return workspace.tab(new TabBuilder() {
			@Override
			public void build(Tab o) {
				o.setHeader(title);
				o.asPanel().widget(widget);
			}
		});
	}

	public Tab getWorksheet() {
		return current.get();
	}

	public StaticWorksheet(Workspace workspace, String title, Widget widget) {
		super();
		this.workspace = workspace;
		this.title = title;
		this.widget = widget;
	}
	
}
