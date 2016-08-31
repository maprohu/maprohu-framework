package hu.mapro.gwtui.gxt.client.form;

import hu.mapro.gwtui.client.edit.CancelEvent;
import hu.mapro.gwtui.client.edit.CancelEvent.CancelHandler;
import hu.mapro.gwtui.client.edit.SaveEvent;
import hu.mapro.gwtui.client.edit.SaveEvent.SaveHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

@Singleton
public class EditorFormPanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, EditorFormPanel> {
	}

	interface Style extends CssResource {
		String menuItemSelected();
	}
	
	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided=true)
	final SimpleContainer formContentPanel;
	@UiField
	TextButton saveButton;
	@UiField
	TextButton cancelButton;
	
	public EditorFormPanel(SimpleContainer contentPanel) {
		this.formContentPanel = contentPanel;
		
		initWidget(binder.createAndBindUi(this));
		
		new KeyNav(this) {
			@Override
			public void onEnter(NativeEvent evt) {
				super.onEnter(evt);
				if (saveButton.isEnabled()) {
					clickSaveButton(null);
				}
			}
			@Override
			public void onEsc(NativeEvent evt) {
				super.onEsc(evt);
				if (cancelButton.isEnabled()) {
					clickCancelButton(null);
				}
			}
		};
		
//	    XElement scrollElement = formVerticalLayoutContainer.getElement();
//        scrollElement.getStyle().setProperty("overflowY", ScrollMode.ALWAYS.value().toLowerCase());
//        scrollElement.getStyle().setProperty("overflowX", ScrollMode.NONE.value().toLowerCase());
//        formVerticalLayoutContainer.setAdjustForScroll(true);
		
	}

	@UiHandler("saveButton")
	void clickSaveButton(SelectEvent e) {
		fireEvent(new SaveEvent());
	}

	@UiHandler("cancelButton")
	void clickCancelButton(SelectEvent e) {
		fireEvent(new CancelEvent());
	}
	
	public void setMainContent(Widget widget) {
		formContentPanel.setWidget(widget);
	}

	public HandlerRegistration addSaveHandler(
			SaveHandler handler) {
		return addHandler(handler, SaveEvent.type);
	}

	public HandlerRegistration addCancelHandler(
			CancelHandler handler) {
		return addHandler(handler, CancelEvent.type);
	}

	public TextButton getSaveButton() {
		return saveButton;
	}

//	public FieldLabel addEditorField(String label, Widget widget) {
//		FieldLabel labeledField = new FieldLabel(widget, label);
//		formVerticalLayoutContainer.add(labeledField);
//		return labeledField;
//	}

	
}
