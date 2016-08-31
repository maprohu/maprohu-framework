package hu.mapro.gwtui.client.edit;

public interface LabeledFieldCustomizer {

	void setLabel(String text);
	
	void setNotNull(boolean notNull);
	
	void setFill(boolean fill);
	
	public static final LabeledFieldCustomizer FAKE = new LabeledFieldCustomizer() {
		@Override
		public void setLabel(String text) {
		}

		@Override
		public void setNotNull(boolean notNull) {
		}

		@Override
		public void setFill(boolean fill) {
		}
	};
	
}
