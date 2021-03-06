/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package hu.mapro.gxt.theme.green.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

public class BlueHeaderFramedAppearance extends BlueHeaderAppearance {

  public interface BlueHeaderFramedStyle extends HeaderStyle {

  }

  public interface BlueFramedHeaderResources extends HeaderResources {

    @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "BlueHeader.css", "BlueFramedHeader.css"})
    BlueHeaderFramedStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource headerBackground();
  }

  public BlueHeaderFramedAppearance() {
    this(GWT.<BlueFramedHeaderResources> create(BlueFramedHeaderResources.class), GWT.<Template> create(Template.class));
  }

  public BlueHeaderFramedAppearance(BlueFramedHeaderResources resources, Template template) {
    super(resources, template);
  }
}
