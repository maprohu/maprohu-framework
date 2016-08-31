package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.gxt.client.GxtFactory;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class PageableToolBar extends PagingToolBar {

	final WidgetContext widgetContext;
	
	private int insertIndex = 0;
	
	final Collection<Widget> pagingWidgets;
	
	Widget fillToolItem = new FillToolItem();
	
	public PageableToolBar(WidgetContext widgetContext, int pageSize) {
		super(pageSize);
		this.widgetContext = widgetContext;
		
		pagingWidgets = ImmutableList.copyOf(getChildren().iterator());
		
		remove(11);
		insert(fillToolItem, 0);
		
		setPagingVisible(false);
	}
	
	public void setPagingVisible(boolean visible) {
		for(Widget pw : pagingWidgets) {
			pw.setVisible(visible);
		}
	}
	
	public void addItem(Widget item) {
		insert(item, insertIndex++);
	}
	
	@Override
	public void forceLayout() {
		GxtFactory.layoutWhenVisible(
				widgetContext,
				new Action() {
					@Override
					public void perform() {
						PageableToolBar.super.forceLayout();
					}
				}
		);
	}
	
	public void setFillToolItem(Widget item) {
	    BoxLayoutData data = new BoxLayoutData();
	    data.setFlex(1.0);
	    item.setLayoutData(data);
		
		int idx = getChildren().indexOf(fillToolItem);
		remove(idx);
		insert(item, idx);
		this.fillToolItem = item;
	}

}
