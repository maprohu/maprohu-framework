package hu.mapro.gwtui.client.browser.grid;

import java.util.List;

import hu.mapro.gwt.common.client.Actions;
import hu.mapro.gwt.common.client.HandlerRegistrations;
import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;
import hu.mapro.gwt.common.shared.ObservableValue;
import hu.mapro.gwt.common.shared.ObservableValues;
import hu.mapro.gwt.common.shared.ValidationHandler;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.HandlerRegistration;

public class GridColumnGraphNode<R, T> implements GridColumnCollector<T> {
	
	public static final Joiner PATH_JOINER = Joiner.on('.').skipNulls();
	private static final Joiner LABEL_JOINER = Joiner.on(" of ").skipNulls();
	
	final GridColumnCollector<R> gridColumnCollector;
	final Function<R, ObservableValue<T>> getter;
	final String path;
	final String label;
	
	public GridColumnGraphNode(GridColumnCollector<R> gridColumnCollector,
			Function<R, ObservableValue<T>> getter, String path, String label
	) {
		super();
		this.gridColumnCollector = gridColumnCollector;
		this.getter = getter;
		this.path = path;
		this.label = label;
	}

	public <P> GridColumnGraphNode<R, P> reference(
			Function<? super T, ObservableValue<P>> getter,
			String path,
			String label
	) {
		return new GridColumnGraphNode<R, P>(
				gridColumnCollector,
				compose(getter, this.getter, gridColumnCollector.closeHandlers()),
				PATH_JOINER.join(this.path, path),
				LABEL_JOINER.join(label, this.label)
		);
	}
	
	public <V> GridColumnCustomizer<V> column(
			Function<? super T, ? extends ObservableValue<V>> getter,
			String path,
			String label
	) {
		return gridColumnCollector.add(
				compose(getter, this.getter, gridColumnCollector.closeHandlers()),
				PATH_JOINER.join(this.path, path),
				LABEL_JOINER.join(label, this.label)
		);
	}

	public static <R> GridColumnGraphNode<R, R> root(GridColumnCollector<R> gridColumnCollector) {
		return new GridColumnGraphNode<R, R>(
				gridColumnCollector, 
				new Function<R, ObservableValue<R>>() {
					@Override
					public ObservableValue<R> apply(R input) {
						return ObservableValues.of(input);
					}
				}, 
				null, 
				null
		);
	}

	
	public static <R, T, P> Function<R, ObservableValue<P>> compose(
			final Function<? super T, ? extends ObservableValue<P>> to,
			final Function<R, ObservableValue<T>> from,
			final Handlers closeHandlers
	) {
		return new Function<R, ObservableValue<P>>() {
			@Override
			public ObservableValue<P> apply(R input) {
				final ObservableValue<T> parent = from.apply(input);
				final Handlers subHandlers = Handlers.newInstance(); 

				//final Handlers subCloseHandlers = Handlers.newInstance();
				//closeHandlers.add(subCloseHandlers);
				
				return new ObservableValue<P>() {
					
					ObservableValue<P> valueObservable;
					HandlerRegistration valueRegistration = HandlerRegistrations.NONE;
					
					{
						closeHandlers.add(Actions.removeHandler(parent.register(new Action() {
							@Override
							public void perform() {
//								subCloseHandlers.fire();
//								subCloseHandlers.clear();
								parentChange();
							}
						})));
						parentChange();
						
					}
					@Override
					public void set(P object) {
						throw new RuntimeException("read only");
					}

					@Override
					public P get() {
						return valueObservable.get();
					}

					
					@Override
					public HandlerRegistration register(Action action) {
						return subHandlers.register(action);
					}

					public void parentChange() {
						T parentValue = parent.get();
						
						valueRegistration.removeHandler();
						
						if (parentValue!=null) {
							valueObservable = to.apply(parentValue);
							valueRegistration = valueObservable.register(subHandlers);
						} else {
							valueObservable = ObservableValues.of(null);
							valueRegistration = HandlerRegistrations.NONE;
						}
						
						subHandlers.fire();
					}

					@Override
					public boolean isReadOnly() {
						return true;
					}

					@Override
					public List<String> getValidationErrors() {
						return ImmutableList.of();
					}

					@Override
					public HandlerRegistration addValidationStatusChangeHandler(
							Action action) {
						return HandlerRegistrations.NONE;
					}

					@Override
					public HandlerRegistration addFocusRequestHandler(
							Action action) {
						return HandlerRegistrations.NONE;
					}
				};
			}
		};
	}

	@Override
	public <V> GridColumnCustomizer<V> add(
			Function<? super T, ? extends ObservableValue<V>> getter, String path,
			String label) {
		return column(getter, path, label);
	}

	@Override
	public Handlers closeHandlers() {
		return gridColumnCollector.closeHandlers();
	}

	public static String extendPath(String base, String extension) {
		return GridColumnGraphNode.PATH_JOINER.join(base, extension);
	}
	
}
