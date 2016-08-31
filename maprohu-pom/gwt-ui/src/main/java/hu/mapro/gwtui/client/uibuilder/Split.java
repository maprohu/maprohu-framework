package hu.mapro.gwtui.client.uibuilder;

public interface Split {

	ResizablePanel north(ResizablePanelBuilder builder);
	ResizablePanel south(ResizablePanelBuilder builder);
	ResizablePanel west(ResizablePanelBuilder builder);
	ResizablePanel east(ResizablePanelBuilder builder);
	Panel center(PanelBuilder builder);
	
}
