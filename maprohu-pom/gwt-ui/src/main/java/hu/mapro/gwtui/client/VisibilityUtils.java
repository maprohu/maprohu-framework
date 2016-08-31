package hu.mapro.gwtui.client;

import hu.mapro.gwt.common.shared.Action;

import com.google.gwt.user.client.ui.HasVisibility;

public class VisibilityUtils {

	public static void addVisibility(final Visibility visibility,
			final ShowHidable object) {
		Action handler = new Action() {

			@Override
			public void perform() {
				if (visibility.isVisible()) {
					object.show();
				} else {
					object.hide();
				}
			}
		};
		visibility.addChangeHandler(handler);
		handler.perform();
	}

	public static void addVisibility(Visibility visibility, HasVisibility widget) {
		addVisibility(visibility, createShowHideable(widget));
	}

	public static ShowHidable createShowHideable(final HasVisibility widget) {
		return new ShowHidable() {

			@Override
			public void hide() {
				widget.setVisible(false);
			}

			@Override
			public void show() {
				widget.setVisible(true);
			}
		};
	}

	public static void addHideHadler(final Visibility v, final Hidable h) {
		v.addChangeHandler(new Action() {
			@Override
			public void perform() {
				if (!v.isVisible()) {
					h.hide();
				}
			}
		});
	}

}
