package hu.mapro.gwt.common.shared;

import java.util.List;

import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.model.Setter;
import hu.mapro.model.meta.HasPath;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.TakesValue;

public class FieldObserver<V> implements TakesValue<V>, ObservableValue<V> {
	
	final TakesValue<V> takesValue;
	final Flag readOnlyFlag;
	
	public FieldObserver(TakesValue<V> takesValue) {
		this(takesValue, Flag.FALSE);
	}

	public FieldObserver(TakesValue<V> takesValue, Flag readOnlyFlag) {
		super();
		this.takesValue = takesValue;
		this.readOnlyFlag = readOnlyFlag;
	}

	public <T> FieldObserver(
			T object,
			Function<? super T, V> getter,
			Setter<? super T, V> setter
	) {
		this(TakesValues.from(object, getter, setter));
	}
	
	public <T> FieldObserver(
			T object,
			Function<? super T, V> getter,
			Setter<? super T, V> setter,
			Flag readOnlyFlag
	) {
		this(TakesValues.from(object, getter, setter), readOnlyFlag);
	}
	
	
	final HandlerSupport<FieldChangeHandler<V>> handlers = HandlerSupports.of();
 
	public void setValue(final V value) {
		takesValue.setValue(value);
		handlers.fire(new Callback<FieldChangeHandler<V>>() {
			@Override
			public void onResponse(FieldChangeHandler<V> handler) {
				handler.onChange(value);
			}
		});
	}
	
	public HandlerRegistration addHandler(FieldChangeHandler<V> handler) {
		return handlers.addHandler(handler);
	}
	
	public V getValue() {
		return takesValue.getValue();
	}
	
	public static <T, V> Supplier<FieldObserver<V>> supplier(
			final T object,
			final Function<? super T, V> getter,
			final Setter<? super T, V> setter
	) {
		return new Supplier<FieldObserver<V>>() {
			@Override
			public FieldObserver<V> get() {
				return new FieldObserver<V>(object, getter, setter);
			}
		};
	}
	
	public static <T, V, F extends HasPath&Function<? super T, V>&Setter<? super T, V>> FieldObserver<V> of(
			T object,
			F field
	) {
		return Autobeans.getOrCreateTag(object, FieldObserver.class.getName()+field.getPath(), supplier(object, field, field));
	}

	@Override
	public void set(V object) {
		setValue(object);
	}

	@Override
	public V get() {
		return getValue();
	}

	@Override
	public HandlerRegistration register(final Action action) {
		return addHandler(new FieldChangeHandler<V>() {
			@Override
			public void onChange(V value) {
				action.perform();
			}
		});
	}

	@Override
	public boolean isReadOnly() {
		return readOnlyFlag.isSet();
	}

	//final HandlerSupport<ValidationHandler> validationHandlers = HandlerSupports.of();
//	final Handlers validationStatusChangeHandlers = Handlers.newInstance();
//	final Handlers focusRequestHandlers = Handlers.newInstance();
	
//	@Override
//	public HandlerRegistration addValidationHandler(
//			ValidationHandler validationHandler) {
//		return validationHandlers.addHandler(validationHandler);
//	}
//	
//	public void markInvalid(final String message) {
//		validationHandlers.fire(new Callback<ValidationHandler>() {
//			@Override
//			public void onResponse(ValidationHandler value) {
//				value.markInvalid(message);
//			}
//		});
//	}
//
//	public void display() {
//		validationHandlers.fire(new Callback<ValidationHandler>() {
//			@Override
//			public void onResponse(ValidationHandler value) {
//				value.display();
//			}
//		});
//	}

	@Override
	public List<String> getValidationErrors() {
		return ImmutableList.of();
	}

	@Override
	public HandlerRegistration addValidationStatusChangeHandler(Action action) {
		return HandlerRegistrations.NONE;
	}

	@Override
	public HandlerRegistration addFocusRequestHandler(Action action) {
		return HandlerRegistrations.NONE;
	}
	
}
