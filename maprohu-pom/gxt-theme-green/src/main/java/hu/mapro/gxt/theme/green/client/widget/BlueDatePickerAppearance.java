/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package hu.mapro.gxt.theme.green.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.widget.DatePickerBaseAppearance;

public class BlueDatePickerAppearance extends DatePickerBaseAppearance {

  public interface BlueDatePickerResources extends DatePickerResources, ClientBundle {
    @Source({"com/sencha/gxt/theme/base/client/widget/DatePicker.css", "DatePickerVisual.css"})
    BlueDatePickerStyle css();
    
    @ImageOptions(preventInlining = true)
    ImageResource downIcon();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource footer();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource header();

    ImageResource leftButton();

    @ImageOptions(preventInlining = true, repeatStyle = RepeatStyle.None)
    ImageResource leftIcon();

    ImageResource rightButton();

    @ImageOptions(preventInlining = true, repeatStyle = RepeatStyle.None)
    ImageResource rightIcon();

  }

  public interface BlueDatePickerStyle extends DatePickerStyle {

  }

  public BlueDatePickerAppearance() {
    this(GWT.<BlueDatePickerResources> create(BlueDatePickerResources.class));
  }

  public BlueDatePickerAppearance(BlueDatePickerResources resources) {
    super(resources);
  }

}
