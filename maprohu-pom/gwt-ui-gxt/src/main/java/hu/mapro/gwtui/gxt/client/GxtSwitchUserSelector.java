package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.window.SwitchUserSelector;

import java.util.List;

import javax.inject.Singleton;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ListField;

@Singleton
public class GxtSwitchUserSelector implements SwitchUserSelector {

	final DefaultUiMessages messages;
	
	@Inject
	public GxtSwitchUserSelector(DefaultUiMessages messages) {
		super();
		this.messages = messages;
	}

	@Override
	public void select(List<String> userNames, final Callback<String> callback) {
		
		Dialog dialog = new Dialog();
		
		dialog.setHeadingText(messages.switchUser());
		
		ListStore<String> store = new ListStore<String>(
				ModelKeyProviders.IDENTITY
		);
		ListView<String, String> view = new ListView<String, String>(
				store,
				new IdentityValueProvider<String>()
		);
		final ListField<String, String> field = new ListField<String, String>(
				view
		);
		
		store.addAll(userNames);
		dialog.add(field);
		
		final TextButton okButton = dialog.getButtonById(PredefinedButton.OK.name());

		SelectionHandler<String> handler = new SelectionHandler<String>() {
			@Override
			public void onSelection(SelectionEvent<String> event) {
				okButton.setEnabled(field.getValue()!=null);
			}
		};
		view.getSelectionModel().addSelectionHandler(handler);
		handler.onSelection(null);
		
		okButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				callback.onResponse(field.getValue());
			}
		});
		
		dialog.setHideOnButtonClick(true);

		dialog.show();
		
	}

}
