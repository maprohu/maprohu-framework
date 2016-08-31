package hu.mapro.gwtui.client.edit;

import hu.mapro.gwt.common.client.ClassDataFactory;
import hu.mapro.gwt.common.shared.Flag;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class DefaultComplexEditing implements ComplexEditing {
	private final ClassDataFactory cdf;
	final List<ComplexEditingListener> listeners = Lists.newArrayList();
	final Flag readOnlyFlag;
	final MessageInterface messageInterface;
	final DefaultUiMessages defaultUiMessages;
	
	
//	public DefaultComplexEditing(ClassDataFactory cdf) {
//		this(cdf, Flag.FALSE);
//	}


	public DefaultComplexEditing(ClassDataFactory cdf, Flag readOnlyFlag,
		MessageInterface messageInterface, DefaultUiMessages defaultUiMessages) {
		super();
		this.cdf = cdf;
		this.readOnlyFlag = readOnlyFlag;
		this.messageInterface = messageInterface;
		this.defaultUiMessages = defaultUiMessages;
	}

	@Override
	public <C extends BaseProxy> C create(Class<C> clazz) {
		return cdf.create(clazz);
	}

	final Set<ComplexEditingRegistration> dirties = Sets.newHashSet();
	final Handlers dirtyHandlers = Handlers.newInstance();
	
	public Handlers getDirtyHandlers() {
		return dirtyHandlers;
	}

	boolean isDirty = false;
	
	private void updateDirty() {
		boolean currentDirty = !dirties.isEmpty();
		
		if (isDirty != currentDirty) {
			isDirty = currentDirty;
			
			dirtyHandlers.fire();
		}
	}
	
	public boolean isDirty() {
//		for (ComplexEditingListener listener : listeners) {
//			if (listener.isDirty()) return true;
//		}
		return isDirty;
	}

	
	
	@Override
	public ComplexEditingRegistration register(final ComplexEditingListener listener) {
		listeners.add(listener);
		
		return new ComplexEditingRegistration() {
			@Override
			public void setDirty(boolean dirty) {
				if (dirty) {
					dirties.add(this);
				} else {
					dirties.remove(this);
				}
				
				updateDirty();
			}

			@Override
			public void removeHandler() {
				listeners.remove(listener);
			}
		};
	}
	
	public void flush() {
		for (ComplexEditingListener listener : listeners) {
			listener.onFlush();
		}
	}
	
	public void saved() {
		for (ComplexEditingListener listener : listeners) {
			listener.onSaved();
		}
		
		dirties.clear();
		updateDirty();
	}
	
	public List<String> validate() {
		final List<String> errors = Lists.newArrayList();
		ValidationErrors v = new ValidationErrors() {
			@Override
			public void addError(String message) {
				errors.add(message);
			}
		};
		for (ComplexEditingListener listener : listeners) {
			listener.onValidate(v);
		}
		return errors;
	}
	
	@Override
	public boolean isReadOnly() {
		return readOnlyFlag.isSet();
	}

	@Override
	public MessageInterface messageInterface() {
		return messageInterface;
	}

	@Override
	public DefaultUiMessages defaultUiMessages() {
		return defaultUiMessages;
	}
}