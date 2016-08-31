/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hu.mapro.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ScrollableTabLayoutPanel extends TabLayoutPanel {

	interface CSS extends CssResource {
		String leftEnabled();

		String rightEnabled();

		String leftDisabled();

		String rightDisabled();
	}

	interface Bundle extends ClientBundle {
		@Source("forward_enabled_16x16.gif")
		@ImageOptions()
		ImageResource tabNext();

		@Source("forward_disabled_16x16.gif")
		ImageResource tabNextDisabled();

		@Source("backward_enabled_16x16.gif")
		ImageResource tabPrevious();

		@Source("backward_disabled_16x16.gif")
		ImageResource tabPreviousDisabled();

		@Source("ScrollableTabLayoutPanel.css")
		CSS css();
	}

	static final Bundle bundle = GWT.create(Bundle.class);
	private FlowPanel tabBar;
	boolean leftButtonEnabled = false;
	boolean rightButtonEnabled = false;
	private FlowPanel leftButton;
	private FlowPanel rightButton;
	private Style tabBarStyle;
	private Element tabWindow;
	int tabBarLeft = 0;

	static {
		bundle.css().ensureInjected();
	}

	public ScrollableTabLayoutPanel(double barHeight, Unit barUnit) {
		super(barHeight, barUnit);
		LayoutPanel panel = (LayoutPanel) getWidget();

		tabBar = (FlowPanel) panel.getWidget(0);

		tabWindow = tabBar.getElement().getParentElement();
		tabBarStyle = tabBar.getElement().getStyle();
		tabBarStyle.setLeft(tabBarLeft, Unit.PX);

		panel.setWidgetLeftRight(tabBar, barHeight, barUnit, barHeight, barUnit);
		panel.setWidgetTopHeight(tabBar, 0, Unit.PX, barHeight, barUnit);

		leftButton = new FlowPanel();
		panel.add(leftButton);
		panel.setWidgetLeftWidth(leftButton, 0, Unit.PX, barHeight, barUnit);
		panel.setWidgetTopHeight(leftButton, 0, Unit.PX, barHeight, barUnit);
		leftButton.addStyleName("gwt-TabLayoutPanelLeft");
		leftButton.addStyleName(bundle.css().leftDisabled());

		rightButton = new FlowPanel();
		panel.add(rightButton);
		panel.setWidgetRightWidth(rightButton, 0, Unit.PX, barHeight, barUnit);
		panel.setWidgetTopHeight(rightButton, 0, Unit.PX, barHeight, barUnit);
		rightButton.addStyleName("gwt-TabLayoutPanelRight");
		rightButton.addStyleName(bundle.css().rightDisabled());

		addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateButtons();
				com.google.gwt.core.client.Scheduler.get().scheduleDeferred(
						new ScheduledCommand() {
							@Override
							public void execute() {
								ensureSelectedVisible();
							}
						});
			}
		});
		
//		addAttachHandler(new Handler() {
//			@Override
//			public void onAttachOrDetach(AttachEvent event) {
//				updateButtons();
//			}
//		});

		leftButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getSelectedIndex() > 0) {
					selectTab(getSelectedIndex() - 1);
				}
			}
		}, ClickEvent.getType());

		rightButton.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getSelectedIndex() < getWidgetCount() - 1) {
					selectTab(getSelectedIndex() + 1);
				}
			}
		}, ClickEvent.getType());

		updateButtons();
	}

	protected void updateButtons() {
		int selected = getSelectedIndex();

		boolean left = false;
		boolean right = false;

		if (selected != -1) {
			if (selected > 0)
				left = true;

			if (selected < getWidgetCount() - 1)
				right = true;
		}

		if (left != leftButtonEnabled) {
			leftButtonEnabled = left;
			changeStyle(leftButton, bundle.css().leftEnabled(), bundle.css()
					.leftDisabled(), leftButtonEnabled);
		}
		if (right != rightButtonEnabled) {
			rightButtonEnabled = right;
			changeStyle(rightButton, bundle.css().rightEnabled(), bundle.css()
					.rightDisabled(), rightButtonEnabled);
		}
	}

	private void ensureSelectedVisible() {
		int selected = getSelectedIndex();

		if (selected == -1)
			return;

		Widget tabWidget = getTabWidget(selected);

		Element tabOuter = tabWidget.getElement().getParentElement()
				.getParentElement();

		int tabRight = tabOuter.getAbsoluteRight();
		int barRight = tabWindow.getAbsoluteRight();

		if (tabRight > barRight) {
			int diff = tabRight - barRight;
			tabBarLeft -= diff;
			tabBarStyle.setLeft(tabBarLeft, Unit.PX);
		}

		int tabLeft = tabOuter.getAbsoluteLeft();
		int barLeft = tabWindow.getAbsoluteLeft();

		if (tabLeft < barLeft) {
			int diff = barLeft - tabLeft;
			tabBarLeft += diff;
			tabBarStyle.setLeft(tabBarLeft, Unit.PX);
		}

	}

	private void changeStyle(UIObject widget, String first, String second,
			boolean addFirst) {
		if (!addFirst) {
			changeStyle(widget, first, second);
		} else {
			changeStyle(widget, second, first);
		}
	}

	private void changeStyle(UIObject widget, String remove, String add) {
		widget.removeStyleName(remove);
		widget.addStyleName(add);
	}

	private void scheduledLayout() {
		if (isAttached() && !layoutScheduled) {
			layoutScheduled = true;
			Scheduler.get().scheduleDeferred(layoutCmd);
		}
	}

	public void onResize() {
		super.onResize();
		scheduledLayout();
	}

	private boolean layoutScheduled = false;

	private final ScheduledCommand layoutCmd = new ScheduledCommand() {
		public void execute() {
			layoutScheduled = false;
			ensureSelectedVisible();
		}
	};
	
	@Override
	public boolean remove(int index) {
		if (super.remove(index)) {
			updateButtons();
			return true;
		}
		return false;
	};
}
