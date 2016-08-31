package hu.mapro.model.generator.classes;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

public class AutoBeanGenerationReferencedTypes {

	JCodeModel cm;

	public AutoBeanGenerationReferencedTypes(JCodeModel cm) {
		super();
		this.cm = cm;
	}

	public JClass multiColumn(JClass r, JClass t) {
		return cm.directClass("hu.mapro.gwtui.client.grid.MultiColumn").narrow(r, t);
	}

	public JClass gridColumnCustomizer(JClass v) {
		return cm.directClass("hu.mapro.gwtui.client.browser.grid.GridColumnCustomizer").narrow(v);
	}

	public JClass multiColumnDelegator(JClass t) {
		return cm.directClass("hu.mapro.gwtui.client.grid.MultiColumnDelegator").narrow(t);
	}

	public JClass baseGridColumnCustomBuilder(JClass t) {
		return cm.directClass("hu.mapro.gwtui.client.grid.BaseGridColumnCustomBuilder").narrow(t);
	}

	public JClass gridColumnCustomBuilder(JClass t) {
		return cm.directClass("hu.mapro.gwtui.client.browser.grid.GridColumnCustomBuilder").narrow(t);
	}

	public JClass field() {
		return cm.directClass("hu.mapro.model.meta.Field");
	}

	public JClass longIdProxy() {
		return cm.directClass("hu.mapro.model.client.LongIdProxy");
	}

	public JClass longIdProxyType() {
		return cm.directClass("hu.mapro.model.client.LongIdProxyType");
	}

	public JClass menuGroup() {
		return cm.directClass("hu.mapro.gwtui.client.app.MenuGroup");
	}

	public JClass messageInterface() {
		return cm.directClass("hu.mapro.gwtui.client.workspace.MessageInterface");
	}

	public JType workspace() {
		return cm.directClass("hu.mapro.gwtui.client.app.Workspace");
	}

	public JClass delegatedMenuGroup() {
		return cm.directClass("hu.mapro.gwtui.client.impl.DelegatedMenuGroup");
	}

	public JClass defaultComplexEditor(JClass clientClass) {
		return cm.directClass("hu.mapro.gwtui.client.edit.impl.DefaultComplexEditor").narrow(clientClass);
	}

	public JType complexEditorPageBuilder() {
		return cm.directClass("hu.mapro.gwtui.client.edit.ComplexEditorPageBuilder");
	}

	public JType defaultUiMessages() {
		return cm.directClass("hu.mapro.gwtui.client.impl.DefaultUiMessages");
	}

	public JClass complexEditorConfigurator(JClass clientClass) {
		return cm.directClass("hu.mapro.gwtui.client.edit.ComplexEditorConfigurator").narrow(clientClass);
	}

	public JType complexEditorConfigurating(JClass clientClass) {
		return cm.directClass("hu.mapro.gwtui.client.edit.ComplexEditorConfigurating").narrow(clientClass);
	}

	public JType visitorHierarchyType() {
		return cm.directClass("hu.mapro.model.VisitorHierarchy").narrow(Object.class);
	}

	public JClass instanceTester() {
		return cm.directClass("hu.mapro.model.VisitorHierarchy.InstanceTester");
	}

	public JClass longIdModelKeyFunction(JClass clientClass) {
		return cm.directClass("hu.mapro.gwt.data.client.LongIdModelKeyFunction").narrow(clientClass);
	}
	
	public JClass fetchPlanBuilding(JClass clientClass) {
		return cm.directClass("hu.mapro.jpa.model.domain.client.FetchPlanBuilding").narrow(clientClass);
	}

	public JClass fetchPlanNavigator(JClass clientClass) {
		return cm.directClass("hu.mapro.jpa.model.domain.client.FetchPlanNavigator").narrow(clientClass);
	}

	public JType fetchPlanFollower(JClass clientClass) {
		return cm.directClass("hu.mapro.jpa.model.domain.client.FetchPlanFollower").narrow(clientClass);
	}

	public JClass editorFieldCreator(JClass clientPropertyType) {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.EditorFieldCreator").narrow(clientPropertyType);
	}

	public JType stringFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.StringFieldCreator");
	}

	public JType integerFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.IntegerFieldCreator");
	}
	
	public JType dateFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.DateFieldCreator");
	}
	
	public JType longFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.LongFieldCreator");
	}
	
	public JType booleanFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.BooleanFieldCreator");
	}
	
	public JType doubleFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.DoubleFieldCreator");
	}
	
	public JType focusableManagedWidget() {
		return cm.directClass("hu.mapro.gwtui.client.edit.FocusableManagedWidget");
	}

	public JType complexEditing() {
		return cm.directClass("hu.mapro.gwtui.client.edit.ComplexEditing");
	}

	public JClass editorFieldCustomizers() {
		return cm.directClass("hu.mapro.gwtui.client.edit.EditorFieldCustomizers");
	}

	public JClass takesValues() {
		return cm.directClass("hu.mapro.model.client.TakesValues");
	}

	public JClass menuItems() {
		return cm.directClass("hu.mapro.gwtui.client.app.MenuItems");
	}

	public JClass defaultComplexFieldCreator(JClass clientClass) {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.impl.DefaultComplexFieldCreator").narrow(clientClass);
	}

	public JType cachedComplexFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.CachedComplexFieldCreator");
	}

	public JType uncachedComplexFieldCreator() {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.UncachedComplexFieldCreator");
	}

	public JClass defaultUncachedValueProvider(JClass clientClass) {
		return cm.directClass("hu.mapro.gwtui.client.edit.field.impl.DefaultUncachedValueProvider").narrow(clientClass);
	}
	
	
	
}

