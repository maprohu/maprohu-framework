package hu.mapro.gwt.client.widget;

import hu.mapro.gwtui.client.MenuGroup;
import hu.mapro.gwtui.client.MenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuGroupWidget extends Composite implements MenuGroup {

	interface Binder extends UiBinder<Widget, MenuGroupWidget> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	VerticalPanel groupPanel;

	@UiField
	IUiConstants constants;

	@UiField
	DisclosurePanel panel;
	
	public MenuGroupWidget() {
		initWidget(binder.createAndBindUi(this));
	}
	
	@UiFactory
	IUiConstants createUiConstants() {
		return UiConstants.getConstants();
	}

	@Override
	public MenuItem addMenuItem() {
		MenuItemWidget item = new MenuItemWidget();
		groupPanel.add(item);
		return item;
	}
	
	public void open() {
		panel.setAnimationEnabled(false);
		panel.setOpen(true);
		panel.setAnimationEnabled(true);		
	}
	
	public void close() {
		panel.setAnimationEnabled(false);
		panel.setOpen(false);
		panel.setAnimationEnabled(true);		
	}
	
	@UiFactory
	DisclosurePanel createDisclosurePanel() {
		return new DisclosurePanel("<group>");
	}

	@Override
	public String getText() {
		return panel.getHeaderTextAccessor().getText();
	}

	@Override
	public void setText(String text) {
		panel.getHeaderTextAccessor().setText(text);
	}
	
}
