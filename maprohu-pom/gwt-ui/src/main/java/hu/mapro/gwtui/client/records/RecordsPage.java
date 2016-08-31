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
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.Tabs;
import hu.mapro.gwtui.client.workspace.MessageInterface;

import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class RecordsPage<T extends BaseProxy> implements WidgetEmbedder {

	final public ProvidesKey<? super T> modelKeyProvider;
	final public ClientStore<T> clientStore;
	final public ComplexEditorPageAccessControl<T> complexEditorAccessControl;
	final public MessageInterface messageInterface;
	final public DefaultUiMessages defaultUiMessages;
	final public EditorGridConfigurator<T> editorGridConfigurator;
	final public ComplexEditorConfigurator<T> complexEditorConfigurator;
	final public EntityFetcher<T> entityFetcher;
	final public ComplexEditorBuilder<T> complexEditorBuilder;
	final public Tabs tabs;
	
	public RecordsPage(
			ProvidesKey<? super T> modelKeyProvider,
			ClientStore<T> clientStore,
			ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages,
			EditorGridConfigurator<T> editorGridConfigurator,
			ComplexEditorConfigurator<T> complexEditorConfigurator,
			EntityFetcher<T> entityFetcher,
			ComplexEditorBuilder<T> complexEditorBuilder, 
			Tabs tabs
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
		this.tabs = tabs;
	}

	@Override
	public void widget(
			final Panel panel, 
			final WidgetContext context
	) {
		RecordsFactory.page(
				panel, 
				context, 
				modelKeyProvider, 
				clientStore, 
				complexEditorAccessControl, 
				messageInterface, 
				defaultUiMessages, 
				editorGridConfigurator, 
				complexEditorConfigurator, 
				entityFetcher, 
				complexEditorBuilder, 
				tabs,
				new Callback<EditorGrid<T>>() {
					public void onResponse(hu.mapro.gwtui.client.uibuilder.EditorGrid<T> value) {
						customize(value);
					}
				}
		);
	}

	protected void customize(EditorGrid<T> editorGrid) {
		
	}

	
	
}
