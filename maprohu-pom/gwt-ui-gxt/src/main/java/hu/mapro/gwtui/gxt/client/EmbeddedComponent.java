package hu.mapro.gwtui.gxt.client;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class EmbeddedComponent<C extends Component> extends SimpleContainer {
	
	//final Component embedder;
	final C component;
	
	boolean componentEnabled = true;
	
	public EmbeddedComponent(/*Component embedder, */C component) {
		super();
		//this.embedder = embedder;
		this.component = component;
		
		setWidget(component);
	}
	
	@Override
	public void enable() {
		component.setEnabled(componentEnabled);
	}
	
	@Override
	public void disable() {
		component.disable();
	}

	public boolean isComponentEnabled() {
		return componentEnabled;
	}

	protected Component getEmbedder() {
		return (Component) getParent();
	}
	
	public void setComponentEnabled(boolean componentEnabled) {
		this.componentEnabled = componentEnabled;
		component.setEnabled(getEmbedder().isEnabled() && componentEnabled);
	}

	public C getComponent() {
		return component;
	}

	public static <C extends Component> EmbeddedComponent<C> of(/*Component embedder, */C component) {
		return new EmbeddedComponent<C>(/*embedder, */component);
	}
	
}
