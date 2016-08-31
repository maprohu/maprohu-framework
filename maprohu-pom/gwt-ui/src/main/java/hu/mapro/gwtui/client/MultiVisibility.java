package hu.mapro.gwtui.client;

import hu.mapro.gwt.common.shared.Action;
import hu.mapro.gwt.common.shared.Handlers;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class MultiVisibility implements Visibility {

	List<Visibility> list = Lists.newArrayList();
	Handlers h = Handlers.newInstance();
	
	boolean visible = false;
	
	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void addChangeHandler(Action action) {
		h.add(action);
	}
	
	public void add(final Visibility v) {
		list.add(v);

		Action a = new Action() {
			@Override
			public void perform() {
				changed(v.isVisible());
			}
		};
		v.addChangeHandler(a);
		
		boolean newVisibility = v.isVisible();
		added(newVisibility);
	}

	
	void changed(boolean newVisibility) {
		if (newVisibility) {
			showed();
		} else {
			hidden();
		}
	}

	abstract protected void showed();
	
	abstract protected void hidden();
	
	abstract protected void added(boolean newVisibility);

	@Override
	public void removeChangeHandler(Action action) {
		h.remove(action);
	}
	

}
