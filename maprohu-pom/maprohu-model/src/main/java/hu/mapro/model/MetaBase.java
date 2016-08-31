package hu.mapro.model;

abstract public class MetaBase<T, V> {

	protected MetaRoot<T, V> root;
	
	public MetaRoot<T, V> getRoot() {
		return root;
	}

	public MetaBase() {
		this(ModelUtil.<T,V>metaIdentity());
	}

	public MetaBase(MetaRoot<T, V> root) {
		this.root = root;
	};

}
