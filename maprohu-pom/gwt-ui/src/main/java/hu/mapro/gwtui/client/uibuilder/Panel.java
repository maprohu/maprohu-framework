package hu.mapro.gwtui.client.uibuilder;

import com.google.gwt.user.client.ui.Widget;

public interface Panel extends IsWidgetContext {

	void clear();
	
	void widget(Widget widget);

	void setHeight(Integer height);
	
	Tabs tabs(TabsBuilder tabsBuilder);
	
	Split split(SplitBuilder splitBuilder);
	
	Label label(LabelBuilder builder);
	
	Frame frame(FrameBuilder builder);

	Border border(BorderBuilder builder);
	
	Margin margin(MarginBuilder builder);
	
	Scroll scroll(ScrollBuilder builder);
	
	<T> EditorGrid<T> editorGrid(EditorGridBuilder<T> builder);
	
	<T> InlineEditorGrid<T> inlineEditorGrid(InlineEditorGridBuilder<T> builder);
	
	ToolBarPanel toolBarPanel(ToolBarPanelBuilder builder);
	
	EditorForm editorForm(EditorFormBuilder builder);
	
	Fields fields(FieldsBuilder fieldsBuilder);
	
	StringField stringField(StringFieldBuilder builder);
	TextField textField(TextFieldBuilder builder);
	DoubleField doubleField(DoubleFieldBuilder builder);
	IntegerField integerField(IntegerFieldBuilder builder);
	DateField dateField(DateFieldBuilder builder);
	LongField longField(LongFieldBuilder builder);
	BooleanField booleanField(BooleanFieldBuilder builder);
	
	EnumField enumField(EnumFieldBuilder builder);
	
	CachedComplexField cachedComplexField(CachedComplexFieldBuilder builder);
	CachedComplexSetField cachedComplexSetField(CachedComplexSetFieldBuilder builder);
	CachedComplexListField cachedComplexListField(CachedComplexListFieldBuilder builder);
	UncachedComplexField uncachedComplexField(UncachedComplexFieldBuilder builder);
	UncachedFullTextComplexField uncachedFullTextComplexField(UncachedFullTextComplexFieldBuilder builder);
	
}
