package hu.mapro.gwtui.gxt.client;

import java.util.List;

import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.BooleanField;

import com.google.common.collect.ImmutableList;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

public class GxtBooleanField extends GxtAbstractValueField<Boolean, CheckBox> implements BooleanField {
	
	final DefaultUiMessages defaultUiMessages;

	public GxtBooleanField(DefaultUiMessages defaultUiMessages, WidgetContext widgetContext) {
		super(new CheckBox(), widgetContext);
		this.defaultUiMessages = defaultUiMessages;
		field.setBoxLabel("");
	}

	@Override
	public void setNotNull(boolean notNull) {
		field.addValidator(new AbstractValidator<Boolean>() {
			@Override
			public List<EditorError> validate(Editor<Boolean> editor,
					Boolean value) {
				return createError(editor, defaultUiMessages.notNullMessage(), value);
			}
		});
	}

}
