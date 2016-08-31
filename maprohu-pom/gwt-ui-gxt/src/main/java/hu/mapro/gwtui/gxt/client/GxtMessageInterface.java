package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.workspace.DialogWindow;
import hu.mapro.gwtui.client.workspace.DialogWindowType;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GxtMessageInterface implements MessageInterface {

	@Override
	public void alert(String title, String message, final Action after) {
		final AlertMessageBox alertMessageBox = new AlertMessageBox(title, message);
		
		showMessageBox(alertMessageBox, after);
	}

	@Override
	public void info(String title, String message, final Action after) {
		final MessageBox messageBox = new MessageBox(title, message);
		messageBox.setIcon(MessageBox.ICONS.info());
		
		showMessageBox(messageBox, after);
	}
	
	@Override
	public void confirm(String title, String message, final Action confirmed, final Action cancelled) {
		final MessageBox messageBox = new ConfirmMessageBox(title, message) {
			@Override
			protected void onButtonPressed(TextButton button) {
				super.onButtonPressed(button);
				
				if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
					if (confirmed!=null) confirmed.perform();
				} else {
					if (cancelled!=null) cancelled.perform();
				}
			}
		};
		showMessageBox(messageBox, null);
	}
	
	private void showMessageBox(final MessageBox messageBox,
			final Action after) {
		new KeyNav(messageBox) {
			@Override
			public void onEnter(NativeEvent evt) {
				messageBox.hide();
			}
			@Override
			public void onEsc(NativeEvent evt) {
				messageBox.hide();
			}
		};
		
		messageBox.addHideHandler(new HideHandler() {
			
			@Override
			public void onHide(HideEvent event) {
				if (after!=null) {
					after.perform();
				}
			}
		});

		
		messageBox.show();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				messageBox.setFocusWidget(null);
				messageBox.focus();
			}
		});

	}

	@Override
	public DialogWindow custom(
			final DialogWindowType type, 
			final String title,
			final String message, 
			final boolean closable
	) {
		return new DialogWindow() {
			
			List<Button> buttons = Lists.newArrayList();
			
			Integer width;
			
			@Override
			public void show() {
				MessageBox w = new MessageBox(title, message) {
					@Override
					protected void createButtons() {
					    getButtonBar().clear();
					    setFocusWidget(null);

					    boolean first = true;
					    
						for (final Button b : buttons) {
							TextButton tb = new TextButton(b.label);
							tb.addSelectHandler(new SelectHandler() {
								@Override
								public void onSelect(SelectEvent event) {
									b.action.perform();
									hide();
								}
							});
							if (first) {
								setFocusWidget(tb);
								first = false;
							}
							addButton(tb);
						}
					}
				};
				
				switch (type) {
				case ERROR:
					w.setIcon(MessageBox.ICONS.error());
					break;
				case INFO:
					w.setIcon(MessageBox.ICONS.info());
					break;
				case QUESTION:
					w.setIcon(MessageBox.ICONS.question());
					break;
				case WARNING:
					w.setIcon(MessageBox.ICONS.warning());
					break;
				}
				
				w.setClosable(closable);
				//w.setBodyBorder(true);
				if (width!=null) {
					w.setWidth(width);
				}
				
				w.show();
			}
			
			@Override
			public void addButton(String label, Action action) {
				buttons.add(new Button(label, action));
			}
			
			class Button {
				String label;
				Action action;
				public Button(String label, Action action) {
					super();
					this.label = label;
					this.action = action;
				}
			}

			@Override
			public void setWidth(int px) {
				width = px;
			}
			
		};
	}
	
}
