package hu.mapro.gwtui.client.impl;

import hu.mapro.gwtui.client.ui.MainWindow;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class LoadingMainWindow implements MainWindow {

	@Override
	public void display(Widget application) {
		Element loading = Document.get().getElementById("loading");
		loading.getParentElement().removeChild(loading);
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		rootPanel.add(application);
	}

}
