package hu.mapro.gwtui.client.app;

import hu.mapro.gwtui.client.app.impl.LogoutUserMenuBuilder;

import com.google.inject.ImplementedBy;

@ImplementedBy(LogoutUserMenuBuilder.class)
public interface UserMenuBuilder extends MenuGroupBuilder {

}
