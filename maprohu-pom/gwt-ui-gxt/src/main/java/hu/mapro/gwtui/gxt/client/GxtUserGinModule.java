package hu.mapro.gwtui.gxt.client;

import hu.mapro.gwtui.client.GwtUserGinModule;
import hu.mapro.gwtui.client.app.impl.LogoutUserMenuBuilder;
import hu.mapro.gwtui.client.app.impl.WindowUserMenuBuilder;
import hu.mapro.gwtui.client.window.SwitchUserSelector;
import hu.mapro.gwtui.client.workspace.MessageInterface;


public class GxtUserGinModule extends GwtUserGinModule {
	
	protected void configure() {
		bind(MessageInterface.class).to(GxtMessageInterface.class);
		bind(SwitchUserSelector.class).to(GxtSwitchUserSelector.class);
		
//		bind(BooleanFieldCreator.class).to(GxtBooleanFieldCreator.class);
//		bind(DateFieldCreator.class).to(GxtDateFieldCreator.class);
//		bind(IntegerFieldCreator.class).to(GxtIntegerFieldCreator.class);
//		bind(LongFieldCreator.class).to(GxtLongFieldCreator.class);
//		bind(DoubleFieldCreator.class).to(GxtDoubleFieldCreator.class);
//		bind(EnumFieldCreator.class).to(GxtEnumFieldCreator.class);
//		
//		bind(CachedComplexFieldCreator.class).to(GxtCachedComplexFieldCreator.class);
//		bind(UncachedComplexFieldCreator.class).to(GxtUncachedComplexFieldCreator.class);
//		bind(CachedComplexListFieldCreator.class).to(GxtCachedComplexListFieldCreator.class);
//		bind(CachedComplexSetFieldCreator.class).to(GxtCachedComplexSetFieldCreator.class);
//		bind(UncachedComplexListFieldCreator.class).to(GxtUncachedComplexListFieldCreator.class);
//		bind(UncachedComplexSetFieldCreator.class).to(GxtUncachedComplexSetFieldCreator.class);
//		bind(UncachedComplexFullTextFieldCreator.class).to(GxtUncachedComplexFullTextFieldCreator.class);
		
		bind(LogoutUserMenuBuilder.class).to(WindowUserMenuBuilder.class);
	}
	
}