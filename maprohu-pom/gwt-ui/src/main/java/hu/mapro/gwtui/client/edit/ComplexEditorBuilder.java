package hu.mapro.gwtui.client.edit;



public interface ComplexEditorBuilder<T> /*extends FetchPlanNavigator<T>*/ {

	void buildEditor(
			T editigObject,
			ComplexEditorBuilding building
	);
	
}
