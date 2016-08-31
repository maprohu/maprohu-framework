package hu.mapro.gwtui.shared.access;

import hu.mapro.model.meta.Rebindable;

import com.google.inject.ImplementedBy;

@ImplementedBy(GrantedAdminAccess.class)
@Rebindable
public interface AdminAccess extends AccessControl {

}
