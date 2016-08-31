package hu.mapro.gwtui.client.records;

import hu.mapro.gwt.common.client.EntityFetcher;
import hu.mapro.gwt.common.shared.Callback;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwtui.client.edit.ComplexEditorBuilder;
import hu.mapro.gwtui.client.edit.ComplexEditorConfigurator;
import hu.mapro.gwtui.client.edit.ComplexEditorPageAccessControl;
import hu.mapro.gwtui.client.grid.EditorGridConfigurator;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetEmbedder;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.records.RecordsFactory.SplitFormElements;
import hu.mapro.gwtui.client.records.RecordsFactory.SplitFormLayout;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.EditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.ResizablePanel;
import hu.mapro.gwtui.client.uibuilder.ResizablePanelBuilder;
import hu.mapro.gwtui.client.uibuilder.Split;
import hu.mapro.gwtui.client.uibuilder.SplitBuilder;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class RecordsForm<T extends BaseProxy> implements WidgetEmbedder {

	final public ProvidesKey<? super T> modelKeyProvider;
	final public ClientStore<T> clientStore;
	final public ComplexEditorPageAccessControl<T> complexEditorAccessControl;
	final public MessageInterface messageInterface;
	final public DefaultUiMessages defaultUiMessages;
	final public EditorGridConfigurator<T> editorGridConfigurator;
	final public ComplexEditorConfigurator<T> complexEditorConfigurator;
	final public EntityFetcher<T> entityFetcher;
	final public ComplexEditorBuilder<T> complexEditorBuilder;
	
	public RecordsForm(
			ProvidesKey<? super T> modelKeyProvider,
			ClientStore<T> clientStore,
			ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages,
			EditorGridConfigurator<T> editorGridConfigurator,
			ComplexEditorConfigurator<T> complexEditorConfigurator,
			EntityFetcher<T> entityFetcher,
			ComplexEditorBuilder<T> complexEditorBuilder
	) {
		super();
		this.modelKeyProvider = modelKeyProvider;
		this.clientStore = clientStore;
		this.complexEditorAccessControl = complexEditorAccessControl;
		this.messageInterface = messageInterface;
		this.defaultUiMessages = defaultUiMessages;
		this.editorGridConfigurator = editorGridConfigurator;
		this.complexEditorConfigurator = complexEditorConfigurator;
		this.entityFetcher = entityFetcher;
		this.complexEditorBuilder = complexEditorBuilder;
	}

	@Override
	public void widget(final Panel panel, final WidgetContext context) {
		RecordsFactory.form(
				panel, context, modelKeyProvider, clientStore, complexEditorAccessControl, messageInterface, defaultUiMessages, editorGridConfigurator, complexEditorConfigurator, entityFetcher, complexEditorBuilder, 
				new Callback<EditorGrid<T>>() {
					@Override
					public void onResponse(EditorGrid<T> value) {
						customize(value, context);
					}
				},
				new SplitFormLayout() {
					@Override
					public <Q> SplitFormElements<Q> layout(Panel panel,
							final EditorGridBuilder<Q> editorGridBuilder) {
						final SplitFormElements<Q> result = new SplitFormElements<Q>();
						
						panel.split(new SplitBuilder() {
							
							@Override
							public void build(Split split) {
								
								result.formPanel = split.center(null);

								split.north(new ResizablePanelBuilder() {
									@Override
									public void build(ResizablePanel resizablePanel) {
										resizablePanel.setSize(300);
										result.editorGrid = resizablePanel.asPanel().border(null).asPanel().editorGrid(editorGridBuilder); 
									}
								}).asPanel();
								
//														split.center(new PanelBuilder() {
//															@Override
//															public void build(Panel o) {
//																editorGrid = RecordsFactory.createEditorGrid(o, modelKeyProvider, editorGridConfigurator);
//															}
//														});
//														
//														formPanel = split.south(new ResizablePanelBuilder() {
//															@Override
//															public void build(ResizablePanel resizablePanel) {
//																resizablePanel.setSize(400);
//															}
//														}).asPanel();
								
							}
						});
						
						return result;
					}
				}
		);
		

	}

	protected void customize(EditorGrid<T> editorGrid) {
	}

	protected void customize(EditorGrid<T> editorGrid, WidgetContext widgetContext) {
		customize(editorGrid);
	}
	
}
