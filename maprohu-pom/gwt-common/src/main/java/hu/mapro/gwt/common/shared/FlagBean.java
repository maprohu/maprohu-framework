package hu.mapro.gwt.common.shared;

public class FlagBean implements Flag {

	boolean flag;

	public FlagBean() {
		this(false);
	}
	
	public FlagBean(boolean flag) {
		super();
		this.flag = flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public boolean isSet() {
		return flag;
	}
	
	public void set() {
		setFlag(true);
	}
	
	public void clear() {
		setFlag(false);
	}

}
