package hu.mapro.gwtui.gxt.client.edit;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwtui.client.grid.InlineEditorGridHandler;
import hu.mapro.gwtui.client.grid.InstanceEditingHandler;
import hu.mapro.gwtui.client.grid.InstanceSavingHandler;
import hu.mapro.gwtui.client.impl.DefaultUiMessages;
import hu.mapro.gwtui.client.uibuilder.InlineEditorGrid;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.form.GxtEditorGrid;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class GxtInlineEditorGrid<T> extends GxtEditorGrid<T> implements InlineEditorGrid<T> {

	final private GridRowEditing<T> gridEditing;
	final DefaultUiMessages defaultUiMessages;

	
	public GxtInlineEditorGrid(
			WidgetContextSupport widgetContext,
			ColumnModel<T> cm, 
			ListStore<T> listStore,
			final InlineEditorGridHandler<T> inlineEditorGridHandler,
			final DefaultUiMessages defaultUiMessages
	) {
		super(widgetContext, cm, listStore);
		this.defaultUiMessages = defaultUiMessages;
		
		//grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		grid.getStore().setAutoCommit(true);
		gridEditing = new GridRowEditing<T>(grid);
		
		gridEditing.addBeforeStartEditHandler(new BeforeStartEditHandler<T>() {
			@Override
			public void onBeforeStartEdit(BeforeStartEditEvent<T> event) {
				if (
						grid.getSelectionModel().getSelectedItems().size()>1
						||
						(
								grid.getSelectionModel().getSelectedItems().size()==1
								&&
								!inlineEditorGridHandler.canEdit(grid.getSelectionModel().getSelectedItems().get(0))
						)
				) {
					gridEditing.cancelEditing();
					event.setCancelled(true);
				}
			}
		});
		
		gridEditing.addStartEditHandler(new StartEditHandler<T>() {
			@Override
			public void onStartEdit(StartEditEvent<T> event) {
				if (!currentHandler.isPresent()) {
					final T originalObject = grid.getStore().get(
							event.getEditCell().getRow()
					);
					final InstanceEditingHandler<T> handler = inlineEditorGridHandler.startEdit(
							originalObject
					);
					
					grid.getStore().update(handler.getEditingObject());
					
					currentHandler = Optional.of(
							new InstanceSavingHandler() {
								
								@Override
								public void onSave() {
									handler.onSave();
									grid.getStore().applySort(false);
								}
								
								@Override
								public void onCancel() {
									grid.getStore().update(originalObject);
									grid.getSelectionModel().refresh();
									handler.onCancel();
								}
							}
					);
				}
			}
		});
		
		gridEditing.addCompleteEditHandler(new CompleteEditHandler<T>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<T> event) {
				currentHandler.get().onSave();
				currentHandler = Optional.absent();
			}
		});
		
		gridEditing.addCancelEditHandler(new CancelEditHandler<T>() {
			@Override
			public void onCancelEdit(CancelEditEvent<T> event) {
				currentHandler.get().onCancel();
				currentHandler = Optional.absent();
			}
		});
		
	}

	public GridRowEditing<T> getGridEditing() {
		return gridEditing;
	}

	Optional<? extends InstanceSavingHandler> currentHandler = Optional.absent();
	
	@Override
	public void editNew(final T object, final InstanceSavingHandler handler) {
		final ImmutableList<StoreSortInfo<T>> savedSortInfo = ImmutableList.copyOf(grid.getStore().getSortInfo());
		
		currentHandler = Optional.of(new InstanceSavingHandler() {
			void remove() {
				grid.getStore().remove(object);
				grid.getStore().getSortInfo().clear();
				grid.getStore().getSortInfo().addAll(savedSortInfo);
				grid.getStore().applySort(true);
			}
			
			@Override
			public void onSave() {
				remove();
				handler.onSave();
			}
			
			@Override
			public void onCancel() {
				remove();
				handler.onCancel();
			}

		});
		
		grid.getStore().clearSortInfo();
		grid.getStore().removeFilters();
		grid.getSelectionModel().deselectAll();
		
		grid.getStore().add(0, object);
		gridEditing.startEditing(new GridCell(0, 0));
		
	}

	@Override
	public void stopEditing(Action action) {
		gridEditing.cancelEditing();
		action.perform();
	}

	@Override
	public void setIsTop(boolean isTop) {
		if (isTop) {
			gridEditing.setMessages(gridEditing.new DefaultRowEditorMessages());
		} else {
			gridEditing.setMessages(gridEditing.new DefaultRowEditorMessages() {
				@Override
				public String saveText() {
					return defaultUiMessages.done();
				}
			});
		}
	}
	

}
