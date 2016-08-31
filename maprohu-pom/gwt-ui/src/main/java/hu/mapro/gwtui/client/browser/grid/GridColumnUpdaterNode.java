package hu.mapro.gwtui.client.browser.grid;


public class GridColumnUpdaterNode<T, V> {
	
	private final DefaultGridColumnUpdater<T> updater;
	final String path;
	
	public GridColumnUpdaterNode(DefaultGridColumnUpdater<T> defaultGridColumnUpdater, String path) {
		super();
		updater = defaultGridColumnUpdater;
		this.path = path;
	}

	public <P> GridColumnUpdaterNode<T, P> reference(String path) {
		return new GridColumnUpdaterNode<T, P>(updater, extendPath(path));
	}

	public String extendPath(String path) {
		return GridColumnGraphNode.PATH_JOINER.join(this.path, path);
	}
	
	public <P> DefaultGridColumnUpdater<T>.UpdaterCustomizer<P> display(String path) {
		return updater.new UpdaterCustomizer<P>(extendPath(path));
	}
}