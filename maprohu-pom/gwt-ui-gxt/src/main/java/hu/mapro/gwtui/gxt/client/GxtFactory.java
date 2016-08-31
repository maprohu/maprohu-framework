package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.ComplexEditingRegistration;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.iface.AbstractWidgetListener;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetOperation;
import hu.mapro.gwtui.client.records.RecordsFactory;
import hu.mapro.gwtui.gxt.client.form.GxtMultiSelectButton;
import hu.mapro.gwtui.gxt.client.form.GxtSelectButton;
import hu.mapro.gwtui.gxt.client.form.GxtToggleButton;
import hu.mapro.model.Wrapper;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

public class GxtFactory {

	public static void layoutWhenVisible(
			WidgetContext widgetContext,
			final Action action
	) {
//		final Action deferred = action;
//		final Action deferred = new Action() {
//			@Override
//			public void perform() {
//				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//					@Override
//					public void execute() {
//						action.perform();
//					}
//				});
//			}
//		};
		
		if (widgetContext.isOnTop()) {
			action.perform();
		} else {
			final Handlers handlers = Handlers.newInstance();
			handlers.add(Actions.removeHandler(widgetContext.registerListener(new AbstractWidgetListener() {
				@Override
				public void onShow(WidgetOperation operation) {
					operation.approve(Actions.of(action, handlers));
				}
			})));
		}
	}

	public static <T> HandlerRegistration registerFieldValue(
			final ComplexEditing editing,
			final Field<T> valueBaseField,
			final ObservableValue<T> observableValue,
			final Supplier<T> editorValueSupplier
	) {
		valueBaseField.setValue(observableValue.get());
		valueBaseField.setReadOnly(editing.isReadOnly() || observableValue.isReadOnly());
		
		HandlerRegistration valueRegistration = observableValue.register(new Action() {
			@Override
			public void perform() {
				T newValue = observableValue.get();
				T currentValue = editorValueSupplier.get();
				
				if (!Objects.equal(newValue, currentValue)) {
					valueBaseField.setValue(newValue);
				}
				
				valueBaseField.setReadOnly(editing.isReadOnly() || observableValue.isReadOnly());
			}
		});
		
		HandlerRegistration fieldRegistration = valueBaseField.addValueChangeHandler(new ValueChangeHandler<T>() {
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				T newValue = valueBaseField.getValue();
				fireFieldValueChange(editing, valueBaseField, observableValue,
						newValue);
			}
		});
		
		HandlerRegistration registrations = HandlerRegistrations.of(
				valueRegistration,
				fieldRegistration
		);
		
		return registrations;
	}

	public static <T> HandlerRegistration registerFieldValueBase(
			final ComplexEditing editing,
			final ValueBaseField<T> valueBaseField,
			final ObservableValue<T> observableValue
	) {
		return valueBaseField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				T newValue = valueBaseField.getCurrentValue();
				fireFieldValueChange(editing, valueBaseField, observableValue,
						newValue);
			}
		});
	}

	public static <T> HandlerRegistration registerFieldHasSelectionHandlers(
			final ComplexEditing editing,
			final Field<T> valueBaseField,
			final HasSelectionHandlers<T> hasHandlers,
			final ObservableValue<T> observableValue
	) {
		return hasHandlers.addSelectionHandler(new SelectionHandler<T>() {
			@Override
			public void onSelection(SelectionEvent<T> event) {
				T newValue = event.getSelectedItem();
				fireFieldValueChange(editing, valueBaseField, observableValue,
						newValue);
			}
		});
		
	}
	
	public static <T> HandlerRegistration registerEditing(
			ComplexEditing editing, 
			final Field<T> valueBaseField,
			final Supplier<T> fieldValueSupplier, 
			final ObservableValue<T> value
	) {
		final Wrapper<T> originalValue = Wrapper.of(value.get());
		
		final ComplexEditingRegistration registration = editing.register(new ComplexEditingListener() {
			
			@Override
			public void onValidate(final ValidationErrors errors) {
				HandlerRegistration handler = valueBaseField.addInvalidHandler(new InvalidHandler() {
					@Override
					public void onInvalid(InvalidEvent event) {
						for (EditorError e : event.getErrors()) {
							errors.addError(e.getMessage());
						}
					}
				});
	
				valueBaseField.validate();
				
				handler.removeHandler();
			}
			
			@Override
			public void onFlush() {
				T fieldValue = fieldValueSupplier.get();
				if (!Objects.equal(fieldValue, value.get())) {
					value.set(fieldValue);
				}
			}
			
			@Override
			public boolean isValid() {
				return !valueBaseField.isValid();
			}

			@Override
			public void focus(Action nextFocus) {
				if (valueBaseField.isReadOnly()) {
					nextFocus.perform();
				} else {
					valueBaseField.focus();
				}
			}

			@Override
			public void onSaved() {
				originalValue.set(value.get());
			}

//			@Override
//			public void clearInvalidMarks() {
//				valueBaseField.clearInvalid();
//			}
//
//			@Override
//			public void addInvalidMark(String message) {
//				valueBaseField.markInvalid(message);
//			}
		});
		
		
		Action checkDirtyAction = new Action() {
			@Override
			public void perform() {
				registration.setDirty(
						!Objects.equal(originalValue.get(), value.get())
				);
			}
		};
		HandlerRegistration dirtyRegistration = value.register(checkDirtyAction);
		
		return HandlerRegistrations.of(
				registration,
				dirtyRegistration
		);
	}

//	public static <T> Supplier<T> fieldValueSupplier(
//			final Field<T> valueBaseField) {
//		return new Supplier<T>() {
//			@Override
//			public T get() {
//				return valueBaseField.getValue();
//			}
//		};
//	}

	public static <V, F extends Field<V>> HandlerRegistration registerAll(
			F field,
			Supplier<V> editorValueSupplier,
			ObservableValue<V> value,
			ComplexEditing editing,
			WidgetContext widgetContext
	) {
		return HandlerRegistrations.of(
				registerFieldValue(editing, field, value, editorValueSupplier),
				registerEditing(editing, field, editorValueSupplier, value),
				registerInvalid(value, field, widgetContext, editing)
		);
	}

	public static GxtSelectButton button() {
		
		final TextButton button = new TextButton();

		EmbeddedComponent<TextButton> ec = EmbeddedComponent.of(button);
		
		return new GxtSelectButton(ec);
	}
	
	public static GxtToggleButton toggleButton() {
		
		final ToggleButton button = new ToggleButton();

		EmbeddedComponent<ToggleButton> ec = EmbeddedComponent.of(button);
		
		return new GxtToggleButton(ec);
	}
	
	public static GxtMultiSelectButton multiButton() {
		final SplitButton createButton = new SplitButton();
		
		EmbeddedComponent<SplitButton> ec = EmbeddedComponent.of(createButton);
		
		return new GxtMultiSelectButton(ec);
	}
	
	public static <T> Function<T, String> function(final ModelKeyProvider<T> key) {
		return new Function<T, String>() {
			@Override
			public String apply(T input) {
				return key.getKey(input);
			}
		};
	}

	public static <T> void fireFieldValueChange(final ComplexEditing editing,
			final Field<T> valueBaseField,
			final ObservableValue<T> observableValue, T newValue) {
		final T currentValue = observableValue.get();
		
		if (!Objects.equal(newValue, currentValue)) {
			RecordsFactory.safeSetValue(
					observableValue,
					newValue,
					editing,
					new Action() {
						@Override
						public void perform() {
							valueBaseField.setValue(currentValue);
						}
					}
			);
		}
	}

	
	public static <T> Supplier<T> fieldSupplier(final Field<T> field) {
		return new Supplier<T>() {
			@Override
			public T get() {
				return field.getValue();
			}
		};
	}
	
	public static <T> Supplier<T> valueBaseFieldSupplier(final ValueBaseField<T> field) {
		return new Supplier<T>() {
			@Override
			public T get() {
				return field.getCurrentValue();
			}
		};
	}

	public static <T> HandlerRegistration registerInvalid(
			final ObservableValue<T> value,
			final Field<T> field,
			final WidgetContext widgetContext,
			final ComplexEditing editing
	) {
		final Action mark = new Action() {
			
			@Override
			public void perform() {
				if (!value.getValidationErrors().isEmpty()) {
					field.markInvalid(
							Joiner.on("\n").join(value.getValidationErrors())
					);
				} else {
					field.clearInvalid();
				}
			}
		};
		
		mark.perform();
		
		return HandlerRegistrations.of(
				value.addValidationStatusChangeHandler(mark),
				value.addFocusRequestHandler(new Action() {
					@Override
					public void perform() {
						widgetContext.bringToFront();
						
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							
							@Override
							public void execute() {
								field.focus();
							}
						});
					}
				})
		);
		
	}
	
}
