package hu.mapro.gwtui.client.uibuilder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.gwt.event.shared.HandlerRegistration;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetListener;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

public class WidgetContextSupports {

	public static WidgetContextSupport from(
			final MessageInterface messageInterface, 
			final DefaultUiMessages defaultUiMessages
	) {
		return from(WidgetContext.NONE, messageInterface, defaultUiMessages);
	}
	
	public static WidgetContextSupport from(
			final WidgetContext parentContext,
			final ComplexEditing complexEditing
	) {
		return from(parentContext, complexEditing.messageInterface(), complexEditing.defaultUiMessages());
	}
	
	public static WidgetContextSupport from(
			final WidgetContext parentContext,
			final MessageInterface messageInterface, 
			final DefaultUiMessages defaultUiMessages
	) {
		checkNotNull(defaultUiMessages);
		
		return new WidgetContextSupport() {
			
			@Override
			public HandlerRegistration registerListener(WidgetListener listener) {
				return parentContext.registerListener(listener);
			}
			
			@Override
			public void bringToFront() {
				parentContext.bringToFront();
			}
			
			@Override
			public void object(Iterable<String> messages, Action perform) {
				messageInterface.alert(
						defaultUiMessages.operationNotPermitted(), 
						Joiner.on("; ").join(messages), 
						perform
				);
			}

			@Override
			public void confirm(List<String> confirmationMessages,
					Action confirmed, Action cancelled) {
				messageInterface.confirm(
						defaultUiMessages.confirmationRequired(), 
						Joiner.on("; ").join(confirmationMessages),
						confirmed,
						cancelled
				);
			}

			@Override
			public boolean isOnTop() {
				return parentContext.isOnTop();
			}

			@Override
			public DefaultUiMessages getDefaultUiMessages() {
				return defaultUiMessages;
			}
		};
		
		
	}

	public static WidgetContextSupport from(
			final WidgetContext widgetContext,
			final WidgetContextSupport widgetContextSupport
	) {
		return new WidgetContextSupport() {
			
			@Override
			public HandlerRegistration registerListener(WidgetListener listener) {
				return widgetContext.registerListener(listener);
			}
			
			@Override
			public boolean isOnTop() {
				return widgetContext.isOnTop();
			}
			
			@Override
			public void bringToFront() {
				widgetContext.bringToFront();
			}
			
			@Override
			public void object(Iterable<String> messages, Action perform) {
				widgetContextSupport.object(messages, perform);
			}
			
			@Override
			public void confirm(List<String> confirmationMessages, Action confirmed,
					Action cancelled) {
				widgetContextSupport.confirm(confirmationMessages, confirmed, cancelled);
			}

			@Override
			public DefaultUiMessages getDefaultUiMessages() {
				return widgetContextSupport.getDefaultUiMessages();
			}
		};
	}
	
}
