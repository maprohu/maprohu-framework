package hu.mapro.gwtui.gxt.client.edit.field;

import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwtui.client.edit.ComplexEditing;
import hu.mapro.gwtui.client.edit.ComplexEditingListener;
import hu.mapro.gwtui.client.edit.FocusableManagedWidget;
import hu.mapro.gwtui.client.edit.ValidationErrors;
import hu.mapro.gwtui.client.edit.field.EditorFieldCreator;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.gxt.client.ColumnModels;
import hu.mapro.gwtui.gxt.client.GxtFactory;
import hu.mapro.gwtui.gxt.client.ModelKeyProviders;
import hu.mapro.gwtui.gxt.client.browser.GridSetPanel;
import hu.mapro.model.Wrapper;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiHandler;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;


// TODO this is all wrong. observablevalue<set> does not exist

public class GxtGridSetEditor<V> /*implements EditorFieldCreator<Set<V>>*/ {

//	final Function<? super V, String> modelKeyProvider;
//	final Supplier<V> factory;
//	final Customizer<V> customizer;
//	
//	public GxtGridSetEditor(Function<? super V, String> modelKeyProvider,
//			Supplier<V> factory, Customizer<V> customizer) {
//		super();
//		this.modelKeyProvider = modelKeyProvider;
//		this.factory = factory;
//		this.customizer = customizer;
//	}
//
//	@Override
//	public HandlerRegistration createField(
//			ObservableValue<Set<V>> value,
//			ComplexEditing editing, 
//			Panel panel
//	) {
//		final Wrapper<Boolean> dirty = Wrapper.of(false);
//		
//		final ListStore<V> store = new ListStore<V>(ModelKeyProviders.from(modelKeyProvider));
//		store.setAutoCommit(false);
//		
//		store.replaceAll(Lists.newArrayList(value.get()));
//
//		final List<ColumnConfig<V, ?>> ccs = Lists.newArrayList();
//		
//		GridSetPanel<V> gridSetPanel = new GridSetPanel<V>(ColumnModels.of(ccs), store, factory) {
//			@Override
//			@UiHandler("addButton")
//			protected void clickAdd(SelectEvent e) {
//				super.clickAdd(e);
//				dirty.set(true);
//			}
//			
//			@Override
//			@UiHandler("removeButton")
//			protected void clickRemove(SelectEvent e) {
//				super.clickRemove(e);
//				dirty.set(true);
//			}
//		};
//		
//		final GridInlineEditing<V> inlineEditing = new GridInlineEditing<V>(gridSetPanel.getGrid());
//		
//		
//		
//		customizer.customize(new Factory<V>() {
//
//			@Override
//			public <P> void readOnly(ColumnConfig<V, P> cc) {
//				ccs.add(cc);
//			}
//
//			@Override
//			public <P> void readWrite(ColumnConfig<V, P> cc, Field<P> field) {
//				readOnly(cc);
//				inlineEditing.addEditor(cc, field);
//			}
//
//			@Override
//			public void sort(StoreSortInfo<V> cc) {
//				store.addSortInfo(cc);
//			}
//
//			
//		});
//		
//		gridSetPanel.getGrid().setHeight(200);
//
//		return FocusableManagedWidgets.from(gridSetPanel, editing.register(new ComplexEditingListener() {
//			
//			@Override
//			public void onValidate(ValidationErrors errors) {
//				// TODO do some validation on subeditors
//			}
//			
//			@Override
//			public void onFlush() {
//				store.commitChanges();
//				value.set(ImmutableSet.copyOf(store.getAll()));
//			}
//			
//			@Override
//			public boolean isDirty() {
//				return dirty.get() || !store.getModifiedRecords().isEmpty();
//			}
//		}));
//	}
//	
////	abstract protected ManagedWidget createRecordEditor();
//
//	public interface Factory<T> {
//		
//		<P> void readOnly(ColumnConfig<T, P> cc);
//		<P> void readWrite(ColumnConfig<T, P> cc, Field<P> field);
//		void sort(StoreSortInfo<T> cc);
//		
//	}
//	
//	public interface Customizer<T> {
//		
//		void customize(Factory<T> factory);
//		
//	}
	
}
