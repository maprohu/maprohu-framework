package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.uibuilder.BuildingFactory;
import hu.mapro.gwtui.client.uibuilder.Field;
import hu.mapro.gwtui.client.uibuilder.FieldBuilder;
import hu.mapro.gwtui.client.uibuilder.Fields;
import hu.mapro.gwtui.client.uibuilder.Panel;
import hu.mapro.gwtui.client.uibuilder.PanelBuilder;
import hu.mapro.gwtui.client.uibuilder.WidgetContextSupport;
import hu.mapro.gwtui.gxt.client.data.GxtUtil;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class GxtFields implements Fields, IsWidget {

	final WidgetContextSupport widgetContextSupport;

	SimpleContainer container = new SimpleContainer() {
		{
			cacheSizes = false;
		}
	};
	
	interface Layout {
		
		void insert(Widget widget);
		
		void forceLayout();
		
		void update();
		
	}
	
	class NonFillLayout implements Layout {
		final private VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		final private VerticalLayoutData layoutData = new VerticalLayoutContainer.VerticalLayoutData(1, -1);
		
		@Override
		public void insert(Widget widget) {
			vlc.add(widget, layoutData);
		}

		@Override
		public void forceLayout() {
			GxtUtil.forceLayout(vlc);
		}

		@Override
		public void update() {
			if (!fillers.isEmpty()) {
				fillLayout.takeOver();
			}
		}
		
		void takeOver() {
			layout = this;
			
			for (Widget w : widgets) {
				insert(w);
			}
			
			container.setWidget(vlc);
			forceLayout();
		}
		
	}
	
	class FillLayout implements Layout {
		final private VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		final private BoxLayoutData nonFillLayoutData = new BoxLayoutData(new Margins(0, 0, 5, 0));
		final private BoxLayoutData fillLayoutData = new BoxLayoutData(new Margins(0, 0, 5, 0));

		@Override
		public void insert(Widget widget) {
			vlc.add(widget, fillers.contains(widget) ? fillLayoutData : nonFillLayoutData);
		}
		
		{
			nonFillLayoutData.setFlex(0);
			fillLayoutData.setFlex(1);
		}
		
		void setFill(Widget widget, boolean fill) {
			widget.setLayoutData(fill ? fillLayoutData : nonFillLayoutData);
		}

		@Override
		public void forceLayout() {
			GxtUtil.forceLayout(vlc);
		}
		
		@Override
		public void update() {
			if (fillers.isEmpty()) {
				nonFillLayout.takeOver();
			}
		}
		
		void takeOver() {
			layout = this;
			
			for (Widget w : widgets) {
				insert(w);
			}

			container.setWidget(vlc);
			forceLayout();
			
		}
	}
	
	
	final NonFillLayout nonFillLayout = new NonFillLayout();
	final FillLayout fillLayout = new FillLayout();
	Layout layout;

	final List<Widget> widgets = Lists.newArrayList();

	@Override
	public Widget asWidget() {
		return container;
	}
	
	public GxtFields(
			WidgetContextSupport widgetContextSupport
	) {
		super();
		this.widgetContextSupport = widgetContextSupport;
		nonFillLayout.takeOver();
	}

	@Override
	public Field field(FieldBuilder builder) {
		GxtField field = BuildingFactory.build(new GxtField(widgetContextSupport), builder);
		FieldLabel w = field.labeledField;
		widgets.add(w);
		layout.insert(w);
		return field;
	}
	
	final Set<Widget> fillers = Sets.newHashSet();
	
	void updateFilling() {
		layout.update();
	}
	
	class GxtField extends GxtPanel implements Field {
		
		public GxtField(WidgetContextSupport widgetContextSupport) {
			super(widgetContextSupport);
		}

		final FieldLabel labeledField = new FieldLabel(container, "") {
			{
				cacheSizes = false;
			}
		};

		boolean bold;
		String header;
		boolean fill;
		
		@Override
		public void setHeader(String header) {
			this.header = header;
			update();
		}

		protected void update() {
			if (bold) {
				labeledField.setHTML("<b>"+SafeHtmlUtils.htmlEscape(header)+"</b>");
			} else {
				labeledField.setText(header);
			}
		}

		@Override
		public void setBold(boolean bold) {
			this.bold = bold;
			update();
		}

		@Override
		public void setFill(boolean fill) {
			if (this.fill == fill) return;
			
			this.fill = fill;
			
			if (fill) {
				fillers.add(labeledField);
			} else {
				fillers.remove(labeledField);
			}
			
			updateFilling();
			
			fillLayout.setFill(labeledField, fill);
			layout.forceLayout();
		}
		
	}

	@Override
	public Panel fill(PanelBuilder builder) {
		GxtPanel panel = BuildingFactory.build(new GxtPanel(widgetContextSupport), builder);
		SimpleContainer w = panel.getContainer();
		widgets.add(w);
		fillers.add(w);
		layout.insert(w);
		updateFilling();
		return panel;
	}

}
