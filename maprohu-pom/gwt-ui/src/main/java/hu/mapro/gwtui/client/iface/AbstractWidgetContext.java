package hu.mapro.gwtui.client.iface;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractWidgetContext implements WidgetContextSupport {

	final protected WidgetContextSupport widgetContextSupport;
	final HandlerRegistration registration;

	@Override
	public void bringToFront() {
		widgetContextSupport.bringToFront();
		bringToFrontInContainer();
	}
	
	abstract public void bringToFrontInContainer();
	
	@Override
	public boolean isOnTop() {
		return widgetContextSupport.isOnTop() && isOnTopInContainer();
	}
	
	abstract public boolean isOnTopInContainer();

	public AbstractWidgetContext(
			WidgetContextSupport widgetContextSupport
	) {
		this.widgetContextSupport = widgetContextSupport;
		
		this.registration = widgetContextSupport.registerListener(new AbstractWidgetListener() {
			@Override
			public void onDestroy(WidgetOperation operation) {
				fireDestroy(operation);
			}
			
			@Override
			public void onHide(WidgetOperation operation) {
				if (isOnTopInContainer()) {
					fireHide(operation);
				}
			}
			
			@Override
			public void onShow(WidgetOperation operation) {
				if (isOnTopInContainer()) {
					fireShow(operation);
				}
			}
			
		});
	}

	final List<WidgetListener> listeners = Lists.newArrayList();
	
	@Override
	public HandlerRegistration registerListener(WidgetListener listener) {
		return HandlerRegistrations.addItem(listeners, listener);
	}
	
	public void fireDestroy(Action success, Action failure) {
		fireOperation(new Callback<WidgetOperation>() {
			@Override
			public void onResponse(WidgetOperation value) {
				fireDestroy(value);
			}
		}, success, failure);
	}

	public void fireHide(Action success, Action failure) {
		fireOperation(new Callback<WidgetOperation>() {
			@Override
			public void onResponse(WidgetOperation value) {
				fireHide(value);
			}
		}, success, failure);
	}
	
	public void fireShow(Action success, Action failure) {
		fireOperation(new Callback<WidgetOperation>() {
			@Override
			public void onResponse(WidgetOperation value) {
				fireShow(value);
			}
		}, success, failure);
	}
	
	
	public void fireOperation(Callback<WidgetOperation> fire, Action success, Action failure) {
		final List<String> objections = Lists.newArrayList();
		final List<String> confirmationMessages = Lists.newArrayList();
		final Handlers actions = Handlers.newInstance();
		
		WidgetOperation operation = new WidgetOperation() {
			
			@Override
			public void object(String message) {
				objections.add(message);
			}
			
			@Override
			public void confirm(String message, Action perform) {
				confirmationMessages.add(message);
				actions.add(perform);
			}
			
			@Override
			public void approve(Action perform) {
				actions.add(perform);
			}
		};
		fire.onResponse(operation);
		
		actions.add(Actions.removeHandler(registration));
		actions.add(success);
		
		if (!objections.isEmpty()) {
			widgetContextSupport.object(objections, failure);
		} else if (!confirmationMessages.isEmpty()) {
			widgetContextSupport.confirm(
					confirmationMessages,
					actions,
					failure
			);
		} else {
			actions.fire();
		}
	}
	
	private void fireDestroy(WidgetOperation operation) {
		for (WidgetListener listener : listeners) {
			listener.onDestroy(operation);
		}
	}
	
	private void fireHide(WidgetOperation operation) {
		for (WidgetListener listener : listeners) {
			listener.onHide(operation);
		}
	}
	
	private void fireShow(WidgetOperation operation) {
		for (WidgetListener listener : listeners) {
			listener.onShow(operation);
		}
	}
	
	public void drop() {
		registration.removeHandler();
	}
	
	public void onShowInContainer(Action success, Action failure) {
		if (widgetContextSupport.isOnTop()) {
			fireShow(success, failure);
		} else {
			success.perform();
		}
	}

	public void hideInContainer(Action success, Action failure) {
		if (widgetContextSupport.isOnTop()) {
			fireHide(success, failure);
		} else {
			success.perform();
		}
	}

	public void object(Iterable<String> messages, Action perform) {
		widgetContextSupport.object(messages, perform);
	}

	public void confirm(List<String> confirmationMessages, Action confirmed,
			Action cancelled) {
		widgetContextSupport
				.confirm(confirmationMessages, confirmed, cancelled);
	}
	
	@Override
	public DefaultUiMessages getDefaultUiMessages() {
		return widgetContextSupport.getDefaultUiMessages();
	}
	
}
