package hu.mapro.gwtui.client.records;

import hu.mapro.gwt.common.client.AbstractReceiver;
import hu.mapro.gwt.common.client.InstanceFactory;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Callbacks;
import hu.mapro.gwt.common.shared.Executers;
import hu.mapro.gwt.data.client.ClientStore;
import hu.mapro.gwt.data.client.EditingPersistence;
import hu.mapro.gwt.data.client.EditingPersistenceContext;
import hu.mapro.gwt.data.client.EditingPersistences;
import hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer;
import hu.mapro.gwtui.client.edit.ComplexEditorPageAccessControl;
import hu.mapro.gwtui.client.edit.field.FieldConstructor;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurating;
import hu.mapro.gwtui.client.grid.InlineEditorGridConfigurator;
import hu.mapro.gwtui.client.grid.InlineEditorGridHandler;
import hu.mapro.gwtui.client.grid.InstanceEditingHandler;
import hu.mapro.gwtui.client.grid.InstanceSavingHandler;
import hu.mapro.gwtui.client.iface.WidgetContext;
import hu.mapro.gwtui.client.iface.WidgetEmbedder;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.menu.Button;
import hu.mapro.gwtui.client.records.RecordsFactory.EditorGridBinder;
import hu.mapro.gwtui.client.records.RecordsFactory.EditorGridCreator;
import hu.mapro.gwtui.client.uibuilder.EditorGrid;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGrid;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilder;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGridBuilding;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.workspace.MessageInterface;
import hu.mapro.model.Setter;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Suppliers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

public class RecordsInline<T extends BaseProxy> implements WidgetEmbedder {

	final public ProvidesKey<? super T> modelKeyProvider;
	final public ClientStore<T> clientStore;
	final public ComplexEditorPageAccessControl<T> complexEditorAccessControl;
	final public MessageInterface messageInterface;
	final public DefaultUiMessages defaultUiMessages;
	final public InlineEditorGridConfigurator<T> editorGridConfigurator;
	final public InstanceFactory<T> instanceFactory;

	
	
	public RecordsInline(ProvidesKey<? super T> modelKeyProvider,
			ClientStore<T> clientStore,
			ComplexEditorPageAccessControl<T> complexEditorAccessControl,
			MessageInterface messageInterface,
			DefaultUiMessages defaultUiMessages,
			InlineEditorGridConfigurator<T> editorGridConfigurator,
			InstanceFactory<T> instanceFactory) {
		super();
		this.modelKeyProvider = modelKeyProvider;
		this.clientStore = clientStore;
		this.complexEditorAccessControl = complexEditorAccessControl;
		this.messageInterface = messageInterface;
		this.defaultUiMessages = defaultUiMessages;
		this.editorGridConfigurator = editorGridConfigurator;
		this.instanceFactory = instanceFactory;
	}

	@Override
	public void widget(
			final Panel panel, 
			final WidgetContext context
	) {
		RecordsFactory.inline(
				panel, 
				context, 
				modelKeyProvider, 
				clientStore, 
				editorGridConfigurator, 
				complexEditorAccessControl, 
				instanceFactory, 
				messageInterface, 
				defaultUiMessages
		);
	}

}
