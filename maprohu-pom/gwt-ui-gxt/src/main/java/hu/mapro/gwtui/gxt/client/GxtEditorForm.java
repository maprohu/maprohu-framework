package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.edit.CancelEvent;
import hu.mapro.gwtui.client.edit.CancelEvent.CancelHandler;
import hu.mapro.gwtui.client.edit.SaveEvent;
import hu.mapro.gwtui.client.edit.SaveEvent.SaveHandler;
import hu.mapro.gwtui.client.edit.ValidationError;
import hu.mapro.gwtui.client.uibuilder.EditorForm;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.data.ValueProviders;
import hu.mapro.gwtui.gxt.client.modelkeyprovider.DefaultModelKeyAdapter;

import java.util.List;

import com.google.common.base.Function;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class GxtEditorForm implements EditorForm, IsWidget {
	
	final private GxtPanel panel;
	final private TextButton saveButton;
	final private TextButton cancelButton;
	final private VerticalLayoutContainer vlc;
	final private ListView<ValidationError, String> errorsView;
	final private BorderLayoutContainer splitPanel;
	final private ListStore<ValidationError> errorStore;
	final private BorderLayoutData errorsLayoutData;

	public GxtEditorForm(WidgetContextSupport widgetContextSupport) {
		panel = new GxtPanel(widgetContextSupport);
		
		
		ToolBar toolBar = new ToolBar();
		saveButton = new TextButton();
		saveButton.setText("Save");
		toolBar.add(saveButton);
		
		cancelButton = new TextButton();
		cancelButton.setText("Cancel");
		toolBar.add(cancelButton);
		
		DefaultModelKeyAdapter<ValidationError> keyProvider = new DefaultModelKeyAdapter<ValidationError>();
		errorStore = new ListStore<ValidationError>(keyProvider);
		
		errorsView = new ListView<ValidationError, String>(errorStore, ValueProviders.from(new Function<ValidationError, String>() {
			@Override
			public String apply(ValidationError input) {
				return input.getMessage();
			}
		}, "message"));
		
		errorsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		errorsView.getSelectionModel().addSelectionHandler(new SelectionHandler<ValidationError>() {
			@Override
			public void onSelection(SelectionEvent<ValidationError> event) {
				event.getSelectedItem().display();
			}
		});
		errorsView.setBorders(false);
		
		SimpleContainer errorsMarginPanel = new SimpleContainer();
		
		ContentPanel errorsPanel = new ContentPanel();
		errorsPanel.setHeadingText(widgetContextSupport.getDefaultUiMessages().validationErrors());
		errorsPanel.setWidget(errorsView);
		errorsPanel.setBorders(false);
		//errorsPanel.setBodyBorder(false);
		//errorsPanel.setCollapsible(true);
		
		errorsPanel.setLayoutData(new MarginData(5, 5, 0, 5));
		errorsMarginPanel.setWidget(errorsPanel);
		
		errorsLayoutData = new BorderLayoutData();
		errorsLayoutData.setHidden(true);
		errorsLayoutData.setSize(150);
		errorsLayoutData.setSplit(true);
		//errorsLayoutData.setCollapseMini(true);
		//errorsLayoutData.setCollapsible(true);
		
		splitPanel = new BorderLayoutContainer();
		splitPanel.setNorthWidget(errorsMarginPanel, errorsLayoutData);
		splitPanel.setCenterWidget(panel.container);
		
		vlc = new VerticalLayoutContainer();
		vlc.add(toolBar, new VerticalLayoutData(1.0, -1.0));
		vlc.add(splitPanel, new VerticalLayoutData(1.0, 1.0));
	}
	
	@Override
	public Panel asPanel() {
		return panel;
	}

	@Override
	public HandlerRegistration addSaveHandler(final SaveHandler handler) {
		return saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				handler.onSave(new SaveEvent());
			}
		});
	}

	@Override
	public HandlerRegistration addCancelHandler(final CancelHandler handler) {
		return cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				handler.onCancel(new CancelEvent());
			}
		});
	}

	boolean savingState = false;
	boolean invalidState = false;
	boolean dirtyState = false;
	
	@Override
	public void setInvalid(boolean invalid) {
		invalidState = invalid;
		updateButtons();
	}

	private void updateButtons() {
		saveButton.setEnabled(!invalidState && !savingState && dirtyState);
		cancelButton.setEnabled(!savingState);
		
		if (dirtyState) {
			cancelButton.setText(panel.getWidgetContextSupport().getDefaultUiMessages().cancel());
		} else {
			cancelButton.setText(panel.getWidgetContextSupport().getDefaultUiMessages().close());
		}
		
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}

	@Override
	public void setDirty(boolean dirty) {
		dirtyState = dirty;
		updateButtons();
		
	}

	@Override
	public void setSaving(boolean saving) {
		savingState = saving;
		updateButtons();
	}

	@Override
	public void showValidationErrors(List<ValidationError> errors) {
		errorStore.replaceAll(errors);
		splitPanel.show(LayoutRegion.NORTH);
		errorsLayoutData.setCollapsed(false);
	}

	@Override
	public void hideValidationErrors() {
		splitPanel.hide(LayoutRegion.NORTH);
	}

}
