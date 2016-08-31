package hu.mapro.gwtui.shared.access;

public class GrantedAccess implements AccessControl {

	@Override
	public boolean granted() {
		return true;
	}

}
