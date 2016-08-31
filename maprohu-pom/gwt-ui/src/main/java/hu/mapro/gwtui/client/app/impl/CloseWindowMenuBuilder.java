package hu.mapro.gwtui.client.app.impl;

import com.google.inject.ImplementedBy;

import hu.mapro.gwtui.client.app.MenuGroupBuilder;

@ImplementedBy(DefaultCloseWindowMenuBuilder.class)
public interface CloseWindowMenuBuilder extends MenuGroupBuilder {

}
