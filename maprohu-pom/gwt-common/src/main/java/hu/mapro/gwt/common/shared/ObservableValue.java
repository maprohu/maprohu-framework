package hu.mapro.gwt.common.shared;

import hu.mapro.model.Consumer;

import java.util.List;

import com.google.common.base.Supplier;
import com.google.gwt.event.shared.HandlerRegistration;

public interface ObservableValue<T> extends Consumer<T>, Supplier<T>, Dispatcher {
	
	boolean isReadOnly();
	
	List<String> getValidationErrors();
	
	HandlerRegistration addValidationStatusChangeHandler(Action action);
	
	HandlerRegistration addFocusRequestHandler(Action action);
	
//	HandlerRegistration addFocusRequestHandler(Action action);

}
