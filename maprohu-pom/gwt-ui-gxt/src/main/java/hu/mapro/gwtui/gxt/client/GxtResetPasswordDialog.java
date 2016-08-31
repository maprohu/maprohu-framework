package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.common.base.Objects;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.web.bindery.requestfactory.shared.Request;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.PasswordField;

public abstract class GxtResetPasswordDialog extends Dialog {
	
	final DefaultUiMessages messages;
	final MessageInterface messageInterface;
	
	
	private final VerticalLayoutContainer vlc;
	private final PasswordField tf1;
	private final PasswordField tf2;
	
	public GxtResetPasswordDialog(DefaultUiMessages messages,
			MessageInterface messageInterface) {
		super();
		this.messages = messages;
		this.messageInterface = messageInterface;
		
	    vlc = new VerticalLayoutContainer();
	    
	    tf1 = new PasswordField();
	    tf1.setAllowBlank(false);
		FieldLabel fl1 = new FieldLabel(tf1, messages.newPassword());
	    fl1.setLabelWidth(150);
		vlc.add(fl1, new VerticalLayoutData(1, -1));
		tf2 = new PasswordField();
		tf2.setAllowBlank(false);
	    FieldLabel fl2 = new FieldLabel(tf2, messages.repeatNewPassword());
	    fl2.setLabelWidth(150);
	    vlc.add(fl2, new VerticalLayoutData(1, -1));

	    setBodyBorder(true);
	    setHeadingText(messages.resetPassword());
	    setWidth(400);
	    setHideOnButtonClick(true);			
	    setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
	    setClosable(true);
	    setResizable(false);

	    add(vlc, new MarginData(5));
	    
		new KeyNav(this) {
			@Override
			public void onEnter(NativeEvent evt) {
				super.onEnter(evt);
				onButtonPressed((TextButton) getButtonBar().getItemByItemId(PredefinedButton.OK.name()));
			}
			@Override
			public void onEsc(NativeEvent evt) {
				super.onEsc(evt);
				onButtonPressed((TextButton) getButtonBar().getItemByItemId(PredefinedButton.CANCEL.name()));
			}
		};
	    
	}

	@Override
	protected void onButtonPressed(TextButton button) {
		if (button == getButtonBar().getItemByItemId(PredefinedButton.OK.name())) {
			
			tf1.clearInvalid();
			tf2.clearInvalid();
			
			if (FormPanelHelper.isValid(vlc)) {
				
				String pw1 = tf1.getValue();
				String pw2 = tf2.getValue();
	
				if (!Objects.equal(pw1, pw2)) {
					tf1.forceInvalid(messages.passwordsDoNotMatch());
					tf2.forceInvalid(messages.passwordsDoNotMatch());
				} else {
					createChangePasswordRequest(pw1).fire(new AbstractReceiver<Void>() {
						@Override
						public void onSuccess(Void response) {
							messageInterface.info(messages.resetPassword(), messages.resetPasswordSuccessful(), null);
						}
						
						public void onFailure(com.google.web.bindery.requestfactory.shared.ServerFailure error) {
							messageInterface.alert(messages.resetPassword(), messages.resetPasswordUnsuccessful(error.getMessage()), null);
						}
					});
					
					hide();
				}
				
			}
			
		} else {
			hide();
		}
	}

	abstract protected Request<Void> createChangePasswordRequest(String password);

	@Override
	protected void notifyShow() {
		super.notifyShow();
		
	    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				tf1.focus();
			}
		});
	}
}