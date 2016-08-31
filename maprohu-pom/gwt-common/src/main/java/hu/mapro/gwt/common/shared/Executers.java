package hu.mapro.gwt.common.shared;


public class Executers {

	public static final Executer APPROVE = new Executer() {
		@Override
		public void execute(Action approve, Action veto) {
			approve.perform();
		}
	};
	
	
}
